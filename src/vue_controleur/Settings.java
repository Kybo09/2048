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

public class Settings extends JFrame {

    private Images images = new Images();

    public Settings(){
        this.setTitle("2048 - Settings");
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
                Settings.this.setVisible(false);
                Settings.this.dispose();
                MainMenu menu = new MainMenu();
                menu.setVisible(true);
            }
        });

        menu.add(item);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);

        JPanel buttons = new JPanel();
        GridLayout grid = new GridLayout(1,2);
        grid.setHgap(10);
        grid.setVgap(20);

        ImageIcon text_icon = new ImageIcon(images.getButton("settings"));
        Image text_img = getScaledImage(text_icon.getImage(), 500, 50);
        text_icon.setImage(text_img);

        JLabel textField = new JLabel();
        textField.setHorizontalAlignment(JLabel.CENTER);
        textField.setBorder(BorderFactory.createLineBorder(images.getBackgroundColor(), 25));
        textField.setOpaque(true);
        textField.setBackground(images.getBackgroundColor());
        textField.setIcon(text_icon);
        
        this.add(textField);

        JButton[] buttonsGroup = new JButton[2];
        String[] buttonsLabel = {"theme-futur", "theme-cartoon"};

        JPanel mainSettings = new JPanel(new GridLayout(2,1));

        JButton button = new JButton();
        buttons.add(button);
        buttonsGroup[0] = button;

        JButton button3 = new JButton();
        buttons.add(button3);
        buttonsGroup[1] = button3;

        int count = 0;
        for(JButton btn : buttonsGroup){
            btn.setBackground(images.getBackgroundColor());
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(images.getBackgroundColor(), 1));
            ImageIcon icon = new ImageIcon(images.getButton(buttonsLabel[count]));
            Image img = getScaledImage(icon.getImage(), 362, 147);
            icon.setImage(img);
            btn.setIcon(icon);
            int countcopy = count;
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for(JButton b: buttonsGroup){
                        b.setBorder(BorderFactory.createLineBorder(images.getBackgroundColor(), 1));
                    }
                    btn.setBorder(BorderFactory.createLineBorder(new Color(204, 108, 240), 5));
                    images.changeTheme();
                }
            });
            count++;
        }

        if(images.getTheme().equals("/futur/")){
            buttonsGroup[0].setBorder(BorderFactory.createLineBorder(new Color(204, 108, 240), 5));
            buttonsGroup[1].setBorder(BorderFactory.createLineBorder(images.getBackgroundColor(), 1));
        }
        else{
            buttonsGroup[0].setBorder(BorderFactory.createLineBorder(images.getBackgroundColor(), 1));
            buttonsGroup[1].setBorder(BorderFactory.createLineBorder(new Color(204, 108, 240), 5));
        }

        buttons.setLayout(grid);
        buttons.setBackground(images.getBackgroundColor());
        buttons.setBorder(BorderFactory.createLineBorder(images.getBackgroundColor(), 25));

        JPanel soundControl = new JPanel(new GridLayout(2,1));
        JLabel soundLabel = new JLabel("Volume de la musique :");
        soundLabel.setForeground(Color.WHITE);
        soundLabel.setHorizontalAlignment(JLabel.CENTER);
        soundLabel.setOpaque(true);
        soundLabel.setBackground(images.getBackgroundColor());
        soundLabel.setFont(new Font("Arial", Font.BOLD, 30));


        JSlider soundLevel = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
        soundLevel.setBackground(images.getBackgroundColor());

        soundLevel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                float volume = (float)source.getValue()/100;
                ClientManager c = Client.getClientManager();
                c.toggleVolumeSounds(volume, 0);
            }
        });

        soundControl.add(soundLabel);
        soundControl.add(soundLevel);

        mainSettings.add(buttons);
        mainSettings.add(soundControl);
    
        this.setLayout(new BorderLayout());
        this.add(BorderLayout.NORTH, textField);
        this.add(BorderLayout.CENTER, mainSettings);
        this.add(BorderLayout.SOUTH, new JLabel(""));
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
