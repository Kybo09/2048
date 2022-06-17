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

import modele.Jeu;

public class AIAutoInterface extends JFrame implements Observer {

    private static final int PIXEL_PER_SQUARE = 200;
    private JLabel[][] tabC;
    private Jeu jeu;
    private Images images = new Images();

    public AIAutoInterface(Jeu jeu){
        SoundManager s = new SoundManager("start.wav", false, 1);
        s.start();
        this.jeu = jeu;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(images.getBackgroundColor());
        setSize(jeu.getSize() * PIXEL_PER_SQUARE, jeu.getSize() * PIXEL_PER_SQUARE);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem item = new JMenuItem("Retour au menu principal");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AIAutoInterface.this.setVisible(false);
                AIAutoInterface.this.dispose();
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
        Toolkit toolkit = Toolkit.getDefaultToolkit();  
        Dimension screenSize = toolkit.getScreenSize();  
        int x = (screenSize.width - this.getWidth()) / 2;  
        int y = (screenSize.height - this.getHeight()) / 2; 
        this.setLocation(x, y);  
        setContentPane(contentPane);
        rafraichir();
        jeu.startAiAuto();
    }

    @Override
    public void update(Observable o, Object arg) {
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

    public void rafraichir(){
        SwingUtilities.invokeLater(new Runnable() { // demande au processus graphique de réaliser le traitement
            @Override
            public void run() {
                if(jeu.gameOver){
                    //JOptionPane.showMessageDialog(null, "Game Over");
                    jeu.restart();
                }
                for (int i = 0; i < jeu.getSize(); i++) {
                    for (int j = 0; j < jeu.getSize(); j++) {
                        Case c = jeu.getCase(i, j);
                        if (c == null) {
                            tabC[i][j].setText("");
                            tabC[i][j].setBackground(images.getBackgroundColor());
                            tabC[i][j].setIcon(null);
                        } else {
                            //tabC[i][j].setText(c.getValeur() + "");
                            tabC[i][j].setText("");
                            ImageIcon icon;
                            Image img;
                            int size = PIXEL_PER_SQUARE - 10;
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
            }
        });
        
    }
    
}
