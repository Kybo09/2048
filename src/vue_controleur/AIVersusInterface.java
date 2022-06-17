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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.awt.image.BufferedImage;

public class AIVersusInterface extends JFrame implements Observer {

    private JLabel[][] tabJoueur;
    private JLabel scoreJoueur;
    private JLabel[][] tabIA;
    private JLabel scoreIA;
    private int PIXEL_PER_SQUARE = 100;

    private BehaviorTree2048 bt = new BehaviorTree2048();

    private JLabel[] tabScore = new JLabel[2];

    private ArrayList<JLabel[][]> grids = new ArrayList<JLabel[][]>();
    private Jeu[] jeux = new Jeu[2];

    private Jeu jeuJoueur;
    private Jeu jeuIA;
    private Images images = new Images();

    public AIVersusInterface(Jeu jeuJoueur, Jeu jeuIA) {
        SoundManager s = new SoundManager("start.wav", false, 1);
        s.start();
        this.jeuJoueur = jeuJoueur;
        this.jeuIA = jeuIA;
        
        this.jeuJoueur.soloMode = true;

        jeux[0] = jeuJoueur;
        jeux[1] = jeuIA;

        this.tabJoueur = new JLabel[jeuJoueur.getSize()][jeuJoueur.getSize()];
        this.tabIA = new JLabel[jeuIA.getSize()][jeuIA.getSize()];

        grids.add(tabJoueur);
        grids.add(tabIA);

        this.setTitle("Versus AI Mode");
        this.setSize(1100, 680);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setBackground(images.getBackgroundColor());
        buildWindow();
        Toolkit toolkit = Toolkit.getDefaultToolkit();  
        Dimension screenSize = toolkit.getScreenSize();  
        int x = (screenSize.width - this.getWidth()) / 2;  
        int y = (screenSize.height - this.getHeight()) / 2; 
        this.setLocation(x, y);  
        ajouterEcouteurClavier();
        refresh();
    }

    private void buildWindow() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem item = new JMenuItem("Retour au menu principal");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AIVersusInterface.this.setVisible(false);
                AIVersusInterface.this.dispose();
                MainMenu menu = new MainMenu();
                menu.setVisible(true);
            }
        });

        menu.add(item);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
        GridLayout grid = new GridLayout(1, 2);
        grid.setVgap(40);

        JPanel panel1 = genGrille(jeuJoueur, tabJoueur, "You", true);
        JPanel panel2 = genGrille(jeuIA, tabIA, "Robert (AI)", false);
        panel1.setBackground(images.getBackgroundColor());
        panel2.setBackground(images.getBackgroundColor());

        this.add(panel1);
        this.add(panel2);

        this.setLayout(grid);
        this.setBackground(images.getBackgroundColor());

    }

    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }

    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
    
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
    
        return resizedImg;
    }

    public void refresh() {
        SwingUtilities.invokeLater(new Runnable() { // demande au processus graphique de réaliser le traitement
            @Override
            public void run() {
                int it=0;
                boolean gameOver = false;
                for(JLabel[][] grid : grids) {
                    if(!gameOver) {
                        if (jeux[it].gameOver) {
                            String winner = "Victory ! You win !";
                            if (it == 0) winner = "Game Over, the AI won !";
                            JOptionPane.showMessageDialog(null, winner);
                            jeux[0].restart();
                            jeux[1].restart();
                            gameOver = true;
                        }
                    }
                    tabScore[it].setText("Score : " + jeux[it].score);
                    for (int i = 0; i < jeux[it].getSize(); i++) {
                        for (int j = 0; j < jeux[it].getSize(); j++) {
                            Case c = jeux[it].getCase(i, j);
                            if (c == null) {
                                grid[i][j].setText("");
                                grid[i][j].setBackground(new Color(43, 17, 59));
                                grid[i][j].setIcon(null);
                            } else {
                                //grid[i][j].setText(c.getValeur() + "");
                                grid[i][j].setText("");
                                ImageIcon icon;
                                Image img;
                                int size = PIXEL_PER_SQUARE + 15;
                                switch(c.getValeur()){
                                    case 2: 
                                    icon = new ImageIcon(images.getImage(2));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    grid[i][j].setIcon(icon);
                                    break;
                                case 4:  
                                    icon = new ImageIcon(images.getImage(4));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    grid[i][j].setIcon(icon);
                                    break;
                                case 8:   
                                    icon = new ImageIcon(images.getImage(8));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    grid[i][j].setIcon(icon);
                                    break;
                                case 16:    
                                    icon = new ImageIcon(images.getImage(16));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    grid[i][j].setIcon(icon);
                                    break;
                                case 32:    
                                    icon = new ImageIcon(images.getImage(32));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    grid[i][j].setIcon(icon);
                                    break;
                                case 64:    
                                    icon = new ImageIcon(images.getImage(64));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    grid[i][j].setIcon(icon);
                                    break;
                                case 128:    
                                    icon = new ImageIcon(images.getImage(128));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    grid[i][j].setIcon(icon);
                                    break;
                                case 256:    
                                    icon = new ImageIcon(images.getImage(256));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    grid[i][j].setIcon(icon);
                                    break;
                                case 512:    
                                    icon = new ImageIcon(images.getImage(512));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    grid[i][j].setIcon(icon);
                                    break;
                                case 1024:    
                                    icon = new ImageIcon(images.getImage(1024));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    grid[i][j].setIcon(icon);
                                    break;
                                case 2048:    
                                    icon = new ImageIcon(images.getImage(2048));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    grid[i][j].setIcon(icon);
                                    break;
                                case 4096:   
                                    icon = new ImageIcon(images.getImage(4096));
                                    img = getScaledImage(icon.getImage(), size, size);
                                    icon.setImage(img);
                                    grid[i][j].setIcon(icon);
                                    break;
                                }

                            }
                        }
                    }
                    it++;
                }
            }
        });
    }

    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : playTurn(Direction.gauche); break;
                    case KeyEvent.VK_RIGHT : playTurn(Direction.droite); break;
                    case KeyEvent.VK_DOWN : playTurn(Direction.bas); break;
                    case KeyEvent.VK_UP : playTurn(Direction.haut); break;
                }
            }
        });
    }

    private void playTurn(Direction d){
        // On fait jouer le joueur
        jeuJoueur.move(d);
        // On fait jouer l"IA
        if(jeuJoueur.moved){
            jeuIA.move(bt.getBestDestination(jeuIA));
        }
        
    }

    private JPanel genGrille(Jeu jeu, JLabel[][] tab, String name, boolean isPlayer) {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createLineBorder(images.getBackgroundColor(), 25));
        JPanel gamePane = new JPanel(new GridLayout(jeu.getSize(), jeu.getSize()));
        gamePane.setBackground(images.getBackgroundColor());
        for (int i = 0; i < jeu.getSize(); i++) {
            for (int j = 0; j < jeu.getSize(); j++) {
                Border border = BorderFactory.createLineBorder(images.getBackgroundColor(), 2);
                tab[i][j] = new JLabel();
                tab[i][j].setFont(new Font("Arial", Font.BOLD, 50));
                tab[i][j].setOpaque(false);
                //tab[i][j].setBorder(border);
                tab[i][j].setForeground(new Color(255,255,255));
                tab[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                gamePane.add(tab[i][j]);
            }
        }

        JPanel scorePane = new JPanel(new GridLayout(2,1));
        scorePane.setBackground(images.getBackgroundColor());

        JLabel pseudo = new JLabel(name);
        pseudo.setFont(new Font("Arial", Font.BOLD, 30));
        pseudo.setForeground(new Color(255,255,255));
        pseudo.setVerticalAlignment(JLabel.TOP);
        pseudo.setHorizontalAlignment(JLabel.CENTER);

        JLabel score = new JLabel("Score : 0");
        score.setFont(new Font("Arial", Font.ITALIC, 20));
        score.setVerticalAlignment(JLabel.TOP);
        score.setForeground(new Color(255,255,255));
        score.setHorizontalAlignment(JLabel.CENTER);
        if(!isPlayer){
            gamePane.setBorder(BorderFactory.createLineBorder(new Color(45, 23, 54), 20));
            this.scoreIA = score;
            tabScore[1] = score;
        }else{
            gamePane.setBorder(BorderFactory.createLineBorder(images.getAccentColor(), 20));
            pseudo.setForeground(images.getAccentColor());
            this.scoreJoueur = score;
            tabScore[0] = score;
        }

        contentPane.add(BorderLayout.CENTER, gamePane);
        scorePane.add(pseudo);
        scorePane.add(score);
        contentPane.add(BorderLayout.SOUTH, scorePane);
        return contentPane;
    }
}
