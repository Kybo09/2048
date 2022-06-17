package vue_controleur;

import ai.BehaviorTree2048;
import modele.Case;
import modele.Direction;
import modele.Images;
import modele.Jeu;
import modele.SoundManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;
import java.awt.image.BufferedImage;

public class Swing2048 extends JFrame implements Observer {
    private static final int PIXEL_PER_SQUARE = 150;
    // tableau de cases : i, j -> case graphique
    private JLabel[][] tabC;

    private JLabel[] aiSuggestionTab = new JLabel[4];
    private Jeu jeu;

    private BehaviorTree2048 bt = new BehaviorTree2048();
    private boolean aiAssist = false;
    private Images images = new Images();
    
    private JPanel global;

    public Swing2048(Jeu _jeu, boolean aiAssist) {
        SoundManager s = new SoundManager("start.wav", false, 1);
        s.start();
        jeu = _jeu;
        jeu.soloMode = true;
        this.aiAssist = aiAssist;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setBackground(images.getBackgroundColor());
        if(aiAssist){
            this.setSize(1200, 675);
        }else{
            setSize(jeu.getSize() * PIXEL_PER_SQUARE, jeu.getSize() * PIXEL_PER_SQUARE);
        }
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem item = new JMenuItem("Retour au menu principal");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Swing2048.this.setVisible(false);
                Swing2048.this.dispose();
                MainMenu menu = new MainMenu();
                menu.setVisible(true);
            }
        });

        menu.add(item);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);



        tabC = new JLabel[jeu.getSize()][jeu.getSize()];


        JPanel contentPane = new JPanel(new GridLayout(jeu.getSize(), jeu.getSize()));

        for (int i = 0; i < jeu.getSize(); i++) {
            for (int j = 0; j < jeu.getSize(); j++) {
                Border border = BorderFactory.createLineBorder(images.getBackgroundColor(), 2);
                tabC[i][j] = new JLabel();
                tabC[i][j].setFont(new Font("Arial", Font.BOLD, 50));
                tabC[i][j].setOpaque(true);
                //tabC[i][j].setBorder(border);
                tabC[i][j].setForeground(new Color(255,255,255));
                tabC[i][j].setHorizontalAlignment(SwingConstants.CENTER);

                contentPane.add(tabC[i][j]);

            }
        }
        if(aiAssist) {
            JPanel mainPanel = new JPanel(new GridLayout(1,2));
            mainPanel.add(contentPane);
            mainPanel.setBackground(images.getBackgroundColor());

            JPanel aiPanel = new JPanel(new BorderLayout());

            JLabel title = new JLabel("AI Assisted Mode");
            title.setForeground(new Color(255,255,255));
            title.setFont(new Font("Arial", Font.BOLD, 30));
            title.setVerticalAlignment(JLabel.TOP);
            title.setOpaque(true);
            title.setBackground(images.getBackgroundColor());
            title.setHorizontalAlignment(JLabel.CENTER);

            aiPanel.add(title, BorderLayout.NORTH);

            JPanel aiSuggestion = new JPanel(new GridLayout(4,1));
            JLabel up = new JLabel("Up : 100%");
            up.setFont(new Font("Arial", Font.PLAIN, 20));
            up.setOpaque(true);
            up.setBackground(images.getBackgroundColor());
            JLabel down = new JLabel("Down : 100%");
            down.setFont(new Font("Arial", Font.PLAIN, 20));
            down.setOpaque(true);
            down.setBackground(images.getBackgroundColor());
            JLabel left = new JLabel("Left: 100%");
            left.setFont(new Font("Arial", Font.PLAIN, 20));
            left.setOpaque(true);
            left.setBackground(images.getBackgroundColor());
            JLabel right = new JLabel("Right: 100%;");
            right.setFont(new Font("Arial", Font.PLAIN, 20));
            right.setOpaque(true);
            right.setBackground(images.getBackgroundColor());
            aiSuggestion.add(up);
            aiSuggestion.add(down);
            aiSuggestion.add(left);
            aiSuggestion.add(right);
            aiSuggestionTab[0] = up;
            aiSuggestionTab[1] = down;
            aiSuggestionTab[2] = left;
            aiSuggestionTab[3] = right;

            aiSuggestion.setBorder(BorderFactory.createLineBorder(images.getBackgroundColor(), 20));

            aiPanel.add(aiSuggestion, BorderLayout.CENTER);

            aiPanel.setBorder(BorderFactory.createLineBorder(images.getBackgroundColor(), 20));

            mainPanel.add(aiPanel);
            global = mainPanel;
        }else{
            global = contentPane;
            
        }
        setContentPane(global);
        Toolkit toolkit = Toolkit.getDefaultToolkit();  
        Dimension screenSize = toolkit.getScreenSize();  
        int x = (screenSize.width - this.getWidth()) / 2;  
        int y = (screenSize.height - this.getHeight()) / 2; 
        this.setLocation(x, y);  
        ajouterEcouteurClavier();
        rafraichir();

    }




    /**
     * Correspond à la fonctionnalité de Vue : affiche les données du modèle
     */
    private void rafraichir()  {

        SwingUtilities.invokeLater(new Runnable() { // demande au processus graphique de réaliser le traitement
            @Override
            public void run() {
                if(jeu.gameOver){
                    gameOver();
                }
                for (int i = 0; i < jeu.getSize(); i++) {
                    for (int j = 0; j < jeu.getSize(); j++) {
                        Case c = jeu.getCase(i, j);
                        if (c == null) {
                            tabC[i][j].setText("");
                            tabC[i][j].setBackground(images.getBackgroundColor());
                            tabC[i][j].setIcon(null);
                        } else {
                            tabC[i][j].setText("");
                            ImageIcon icon;
                            Image img;
                            int size;
                            String theme = images.getTheme();
                            if(theme.equals("/futur/")){
                                size = PIXEL_PER_SQUARE;
                            }else{
                                size = PIXEL_PER_SQUARE - 10;
                            }
                            switch(c.getValeur()){
                                case 2: 
                                    icon = new ImageIcon(images.getImage(2));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    tabC[i][j].setIcon(icon);
                                    break;
                                case 4:  
                                    icon = new ImageIcon(images.getImage(4));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    tabC[i][j].setIcon(icon);
                                    break;
                                case 8:   
                                    icon = new ImageIcon(images.getImage(8));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    tabC[i][j].setIcon(icon);
                                    break;
                                case 16:    
                                    icon = new ImageIcon(images.getImage(16));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    tabC[i][j].setIcon(icon);
                                    break;
                                case 32:    
                                    icon = new ImageIcon(images.getImage(32));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    tabC[i][j].setIcon(icon);
                                    break;
                                case 64:    
                                    icon = new ImageIcon(images.getImage(64));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    tabC[i][j].setIcon(icon);
                                    break;
                                case 128:    
                                    icon = new ImageIcon(images.getImage(128));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    tabC[i][j].setIcon(icon);
                                    break;
                                case 256:    
                                    icon = new ImageIcon(images.getImage(256));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    tabC[i][j].setIcon(icon);
                                    break;
                                case 512:    
                                    icon = new ImageIcon(images.getImage(512));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    tabC[i][j].setIcon(icon);
                                    break;
                                case 1024:    
                                    icon = new ImageIcon(images.getImage(1024));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    tabC[i][j].setIcon(icon);
                                    break;
                                case 2048:    
                                    icon = new ImageIcon(images.getImage(2048));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    tabC[i][j].setIcon(icon);
                                    break;
                                case 4096:   
                                    icon = new ImageIcon(images.getImage(4096));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    tabC[i][j].setIcon(icon);
                                    break;
                            }

                        }


                    }
                }
                if(aiAssist){
                    bt.getBestDestination(jeu);
                    int[] scores = bt.getScores();
                    int bestScore = 1;
                    for (int i = 0; i < 4; i++){
                        if(scores[i] > bestScore){ bestScore = scores[i]; }
                    }
                    int index = 0;
                    for(JLabel l : aiSuggestionTab){
                        if(scores[index] == bestScore){
                            l.setForeground(new Color(255,0,0));
                            l.setFont(new Font("Arial", Font.BOLD, 20));
                        }
                        else{
                            l.setForeground(new Color(255,255,255));
                            l.setFont(new Font("Arial", Font.PLAIN, 20));
                        }
                        l.setText(Direction.values()[index].toString() + " : " + scores[index]*100/bestScore + "%");
                        index++;
                    }
                }
            }
        });


    }

    private void gameOver(){
        global.removeAll();
        JPanel gameOverPanel = new JPanel(new GridLayout(2,1));
        JLabel score = new JLabel("Score : " + jeu.score);
        score.setFont(new Font("Arial", Font.BOLD, 30));
        score.setForeground(Color.WHITE);
        score.setHorizontalAlignment(JLabel.CENTER);
        score.setOpaque(true);
        score.setBackground(images.getBackgroundColor());
        
        SoundManager s = new SoundManager("gameover.wav", false, 1);
        s.start();

        ImageIcon text_icon = new ImageIcon(images.getButton("gameover"));
        Image text_img;
        if(aiAssist){
            text_img = getScaledImage(text_icon.getImage(), 720, 100);
        }else{
            text_img = getScaledImage(text_icon.getImage(), 500, 65);
        }
        text_icon.setImage(text_img);

        JLabel textField = new JLabel();
        textField.setHorizontalAlignment(JLabel.CENTER);
        textField.setBorder(BorderFactory.createLineBorder(images.getBackgroundColor(), 25));
        textField.setOpaque(true);
        textField.setBackground(images.getBackgroundColor());
        textField.setIcon(text_icon);

        gameOverPanel.add(textField);
        gameOverPanel.add(score);

        GridLayout grid = new GridLayout(2,1);
        JPanel buttons;
        if(aiAssist){
            buttons = new JPanel(new GridLayout(1,2));
        }else{
            buttons = new JPanel(new GridLayout(2,1));
        } 

        JButton[] buttonsGroup = new JButton[2];
        String[] buttonsLabel = {"btn-tryagain", "btn-menu"};

        JButton button = new JButton();
        buttons.add(button);
        buttonsGroup[0] = button;

        JButton button2 = new JButton();
        buttons.add(button2);
        buttonsGroup[1] = button2;

        int count = 0;

        for(JButton btn : buttonsGroup){
            btn.setBackground(images.getBackgroundColor());
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(images.getBackgroundColor(), 1));
            ImageIcon icon = new ImageIcon(images.getButton(buttonsLabel[count]));
            Image img = getScaledImage(icon.getImage(), 350, 80);
            icon.setImage(img);
            btn.setIcon(icon);
            int countcopy = count;
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    ImageIcon icon = new ImageIcon(images.getButtonHover(buttonsLabel[countcopy]));
                    Image img = getScaledImage(icon.getImage(), 350, 80);
                    icon.setImage(img);
                    btn.setIcon(icon);
                    SoundManager s = new SoundManager("btnclick.wav", false, 1);
                    s.start();
                }
            
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    ImageIcon icon = new ImageIcon(images.getButton(buttonsLabel[countcopy]));
                    Image img = getScaledImage(icon.getImage(), 350, 80);
                    icon.setImage(img);
                    btn.setIcon(icon);
                }
            });
            count++;
        }

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Jeu jeu = new Jeu(4);
                Swing2048 vue = new Swing2048(jeu, aiAssist);
                Swing2048.this.setVisible(false);
                jeu.addObserver(vue);
                vue.setVisible(true);
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu vue = new MainMenu();
                Swing2048.this.setVisible(false);
                vue.setVisible(true);
            }
        });

        global.setBorder(BorderFactory.createLineBorder(images.getBackgroundColor(), 1));
        global.add(gameOverPanel);
        global.add(buttons);
        global.setLayout(grid);
        global.updateUI();
    }

    /**
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements, et déclenche des traitements sur le modèle
     */
    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : jeu.move(Direction.gauche); break;
                    case KeyEvent.VK_RIGHT : jeu.move(Direction.droite); break;
                    case KeyEvent.VK_DOWN : jeu.move(Direction.bas); break;
                    case KeyEvent.VK_UP : jeu.move(Direction.haut); break;
                }
            }
        });
    }

    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
    
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
    
        return resizedImg;
    }

    @Override
    public void update(Observable o, Object arg) {
        rafraichir();
    }
}