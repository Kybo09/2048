package vue_controleur;

import modele.Case;
import modele.Direction;
import modele.Images;
import modele.Jeu;
import modele.SoundManager;
import server.ClientManager;
import server.User;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
public class MultiplayerInterface extends javax.swing.JFrame implements Observer {

    private JLabel[][] tabC;
    private int playerNumber = 2;
    private ClientManager clientManager;
    private JPanel mainPanel;
    private Images images = new Images();

    public MultiplayerInterface(ClientManager clientManager) {
        SoundManager s = new SoundManager("start.wav", false, 1);
        s.start();
        this.clientManager = clientManager;
        this.mainPanel = new JPanel();
        this.setTitle("Multiplayer Mode");
        this.setSize(800, 830);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        buildWindow();
        Toolkit toolkit = Toolkit.getDefaultToolkit();  
        Dimension screenSize = toolkit.getScreenSize();  
        int x = (screenSize.width - this.getWidth()) / 2;  
        int y = (screenSize.height - this.getHeight()) / 2; 
        this.setLocation(x, y);  
    }

    private void buildWindow() {
        GridLayout grid = new GridLayout(2, 2);

        mainPanel.setLayout(grid);
        this.add(mainPanel);
        mainPanel.setBackground(images.getBackgroundColor());
        ajouterEcouteurClavier();
        rafraichir();
    }

    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
    
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
    
        return resizedImg;
    }

    private JPanel genGrille(Jeu jeu, String name, boolean isPlayer, boolean playerIsGameOver) {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createLineBorder(images.getBackgroundColor(), 25));
        JPanel gamePane = new JPanel(new GridLayout(jeu.getSize(), jeu.getSize()));
        gamePane.setBackground(images.getBackgroundColor());
        tabC = new JLabel[jeu.getSize()][jeu.getSize()];
        for (int i = 0; i < jeu.getSize(); i++) {
            for (int j = 0; j < jeu.getSize(); j++) {
                Border border = BorderFactory.createLineBorder(images.getBackgroundColor(), 2);
                tabC[i][j] = new JLabel();
                tabC[i][j].setFont(new Font("Arial", Font.BOLD, 50));
                tabC[i][j].setOpaque(false);
                //tab[i][j].setBorder(border);
                tabC[i][j].setForeground(new Color(255,255,255));
                tabC[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                gamePane.add(tabC[i][j]);
            }
        }

        for (int i = 0; i < jeu.getSize(); i++) {
            for (int j = 0; j < jeu.getSize(); j++) {
                Case c = jeu.getCase(i, j);
                if (c == null) {
                    tabC[i][j].setText("");
                    tabC[i][j].setBackground(new Color(43, 17, 59));
                    tabC[i][j].setIcon(null);
                } else {
                    //tabC[i][j].setText(c.getValeur() + "");
                    tabC[i][j].setText("");
                    ImageIcon icon;
                    Image img;
                    int size = 70;
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

        JPanel scorePane = new JPanel(new GridLayout(2,1));
        scorePane.setBackground(images.getBackgroundColor());

        JLabel pseudo = new JLabel(name);
        pseudo.setFont(new Font("Arial", Font.BOLD, 30));
        pseudo.setForeground(new Color(255,255,255));
        pseudo.setVerticalAlignment(JLabel.TOP);
        pseudo.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel score = new JLabel("Score : " + jeu.score);
        score.setFont(new Font("Arial", Font.ITALIC, 20));
        score.setVerticalAlignment(JLabel.TOP);
        score.setForeground(new Color(255,255,255));
        score.setHorizontalAlignment(JLabel.CENTER);
        if(!isPlayer){
            gamePane.setBorder(BorderFactory.createLineBorder(new Color(45, 23, 54), 20));
        }else{
            gamePane.setBorder(BorderFactory.createLineBorder(images.getAccentColor(), 20));
            pseudo.setForeground(images.getAccentColor());
        }
        if(playerIsGameOver){
            gamePane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 20));
            pseudo.setText("<html><strike>" + name + "</strike></html>");
            pseudo.setForeground(Color.GRAY);
        }

        contentPane.add(BorderLayout.CENTER, gamePane);
        scorePane.add(pseudo);
        scorePane.add(score);
        contentPane.add(BorderLayout.SOUTH, scorePane);
        return contentPane;
    }

    @Override
    public void update(Observable o, Object arg) {
        rafraichir();
    }

    private void rafraichir(){
        SwingUtilities.invokeLater(new Runnable() { // demande au processus graphique de réaliser le traitement
            @Override
            public void run() {
                ArrayList<User> users = clientManager.getUsersInRoom();
                mainPanel.removeAll();
                int compteur = 0;
                for(User u: users){
                    Jeu jeu = new Jeu(4);
                    if(u.getPseudo() != "none"){
                        jeu.score = u.getScore();
                        if(clientManager.getPseudoPlayer().equals(u.getPseudo())){
                            mainPanel.add(genGrille(clientManager.getJeu(), u.getPseudo(), true, false));
                        }else{
                            mainPanel.add(genGrille(u.getJeu(), u.getPseudo(), false, u.isGameOver()));
                        }
                        compteur++;
                        
                    }
                }
                while(compteur < 4){
                    mainPanel.add(new JLabel(""));
                    compteur++;
                }
                mainPanel.updateUI();
            }
        });
    }   


    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : treatMovement(Direction.gauche); break;
                    case KeyEvent.VK_RIGHT : treatMovement(Direction.droite); break;
                    case KeyEvent.VK_DOWN : treatMovement(Direction.bas); break;
                    case KeyEvent.VK_UP : treatMovement(Direction.haut); break;
                }
            }
        });
    }

    private void treatMovement(Direction d){
        if(clientManager.getJeu().gameOver){
            clientManager.sendGameOver();
            return;
        }
        clientManager.moveJeu(d);
        clientManager.sendScore();
        clientManager.sendGrid();
        clientManager.updateScore();
        clientManager.updateGrids();
    }
}
