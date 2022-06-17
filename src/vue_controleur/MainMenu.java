package vue_controleur;

import modele.Images;
import modele.Jeu;
import modele.SoundManager;
import server.Client;
import server.ClientManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class MainMenu extends JFrame {

    private Images images = new Images();

    public MainMenu(){
        this.setTitle("2048");
        this.setSize(800, 450);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        Toolkit toolkit = Toolkit.getDefaultToolkit();  
        Dimension screenSize = toolkit.getScreenSize();  
        int x = (screenSize.width - this.getWidth()) / 2;  
        int y = (screenSize.height - this.getHeight()) / 2; 
        this.setLocation(x, y);  
        buildMenu();
    }

    private void buildMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem item = new JMenuItem("Retour au menu principal");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu.this.setVisible(true);
            }
        });

        menu.add(item);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);

        JPanel buttons = new JPanel();
        GridLayout grid = new GridLayout(2,2);
        grid.setHgap(20);
        grid.setVgap(20);

        ImageIcon text_icon = new ImageIcon(images.getButton("mainmenu"));
        Image text_img = getScaledImage(text_icon.getImage(), 600, 50);
        text_icon.setImage(text_img);

        JLabel textField = new JLabel();
        textField.setHorizontalAlignment(JLabel.CENTER);
        textField.setBorder(BorderFactory.createLineBorder(images.getBackgroundColor(), 25));
        textField.setOpaque(true);
        textField.setBackground(images.getBackgroundColor());
        textField.setIcon(text_icon);
        
        this.add(textField);

        JButton[] buttonsGroup = new JButton[5];
        String[] buttonsLabel = {"btn-solo", "btn-multi", "btn-vsia", "btn-iaauto", "btn-settings"};

        JButton button = new JButton();
        buttons.add(button);
        buttonsGroup[0] = button;

        JButton button3 = new JButton();
        buttons.add(button3);
        buttonsGroup[1] = button3;

        JButton button2 = new JButton();
        buttons.add(button2);
        buttonsGroup[2] = button2;

        JButton button4 = new JButton();
        
        buttons.add(button4);
        buttonsGroup[3] = button4;

        JButton settings = new JButton();
        buttonsGroup[4] = settings;

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

        
        settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu.this.setVisible(false);
                Settings settings = new Settings();
                settings.setVisible(true);
            }
        });

        buttons.setLayout(grid);
        buttons.setBackground(images.getBackgroundColor());
        buttons.setBorder(BorderFactory.createLineBorder(images.getBackgroundColor(), 25));

        JSlider soundLevel = new JSlider(JSlider.HORIZONTAL, 0, 100, 20);
        soundLevel.setBorder(BorderFactory.createLineBorder(images.getBackgroundColor(), 25));

        soundLevel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                float volume = (float)source.getValue()/100;
                ClientManager c = Client.getClientManager();
                c.toggleVolumeSounds(volume, 0);
            }
        });
    
        this.setLayout(new BorderLayout());
        this.add(BorderLayout.NORTH, textField);
        this.add(BorderLayout.CENTER, buttons);
        this.add(BorderLayout.SOUTH, settings);

        // Listener du mode solo
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Swing2048 vue;
                Jeu jeu = new Jeu(4);
                int result = JOptionPane.showConfirmDialog(menu,"Do you want to enable AI Assisted Mode ?", "Choose AI",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if(result == JOptionPane.YES_OPTION){
                    vue =new Swing2048(jeu, true);
                }else if (result == JOptionPane.NO_OPTION){
                    vue =new Swing2048(jeu, false);
                }else {
                    vue =new Swing2048(jeu, false);
                }
                MainMenu.this.setVisible(false);

                jeu.addObserver(vue);
                vue.setVisible(true);
            }
        });

        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Jeu jeu = new Jeu(4);
                AIAutoInterface vue = new AIAutoInterface(jeu);
                MainMenu.this.setVisible(false);

                jeu.addObserver(vue);
                vue.setVisible(true);
            }
        });

        // Listener du mode AI
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu.this.setVisible(false);
                Jeu jeuJoueur = new Jeu(4);
                AIVersusInterface menu = new AIVersusInterface(jeuJoueur, new Jeu(4));
                jeuJoueur.addObserver(menu);
                menu.setVisible(true);
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu.this.setVisible(false);
                ClientManager c = Client.getClientManager();
                MultiplayerMenu menu = new MultiplayerMenu(c);
                c.addObserver(menu);
                menu.setVisible(true);
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


}
