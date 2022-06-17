package vue_controleur;

import modele.Images;
import modele.Jeu;
import server.Client;
import server.ClientManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MultiplayerMenu extends JFrame implements Observer {

    private ArrayList<String> roomsList = new ArrayList<String>();
    private JPanel roomUI = new JPanel();

    private ClientManager clientManager;
    private Images images = new Images();

    public MultiplayerMenu(ClientManager clientManager) {
        this.clientManager = clientManager;
        this.setTitle("Multiplayer Menu");
        this.setSize(500, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setBackground(images.getBackgroundColor());
        try {
            clientManager.askForRooms();
        } catch (IOException e) {
            e.printStackTrace();
        }
        buildMenu();
        Toolkit toolkit = Toolkit.getDefaultToolkit();  
        Dimension screenSize = toolkit.getScreenSize();  
        int x = (screenSize.width - this.getWidth()) / 2;  
        int y = (screenSize.height - this.getHeight()) / 2; 
        this.setLocation(x, y);  
        rafraichir();
        clientManager.setRefreshRooms(true);
        clientManager.refreshRooms();
    }

    private void buildMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem item = new JMenuItem("Retour au menu principal");
        roomUI.setBackground(images.getBackgroundColor());
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MultiplayerMenu.this.setVisible(false);
                MultiplayerMenu.this.dispose();
                MainMenu menu = new MainMenu();
                menu.setVisible(true);
            }
        });
        menu.add(item);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);


        BorderLayout grid = new BorderLayout();
        this.setLayout(grid);
        
        JLabel title = new JLabel("Rooms list");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setOpaque(true);
        title.setBackground(images.getBackgroundColor());
        title.setForeground(new Color(255,255,255));
        title.setBorder(BorderFactory.createLineBorder(images.getBackgroundColor(), 20));
        title.setFont(new Font("Arial", Font.BOLD, 30));
        
        JButton button = new JButton("Create room");
        button.setFont(new Font("Arial", Font.PLAIN, 25));
        button.setBackground(images.getAccentColor());
        button.setForeground(new Color(255,255,255));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(images.getAccentColor(), 10));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    clientManager.createRoom();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        this.add(BorderLayout.NORTH, title);
        this.add(BorderLayout.CENTER, this.roomUI);
        this.add(BorderLayout.SOUTH, button);        
    }

    @Override
    public void update(Observable o, Object arg) {
        rafraichir();
    }

    private void rafraichir(){
        SwingUtilities.invokeLater(new Runnable() { // demande au processus graphique de réaliser le traitement
            @Override
            public void run() {
                roomsList = clientManager.getRooms();
                roomUI.removeAll();
                if(roomsList.size() == 0){
                    JLabel label = new JLabel("No room available");
                    label.setHorizontalAlignment(JLabel.CENTER);
                    label.setForeground(new Color(255,255,255));
                    label.setFont(new Font("Arial", Font.PLAIN, 20));
                    roomUI.add(label);
                }
                for(int i=0; i<roomsList.size(); i++){
                    JButton button = new JButton(roomsList.get(i));
                    button.setFont(new Font("Arial", Font.PLAIN, 20));
                    button.setBackground(images.getAccentColor());
                    button.setForeground(new Color(255,255,255));
                    button.setFocusPainted(false);
                    button.setBorder(BorderFactory.createLineBorder(images.getAccentColor(), 10));
                    roomUI.add(button);

                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String result = (String)JOptionPane.showInputDialog(
                            null,
                            "Choose a username", 
                            "Username selection",            
                            JOptionPane.PLAIN_MESSAGE,
                            null,            
                            null, 
                            ""
                            );
                            String pseudo = "Guest";
                            if(result != null && result.length() > 0){
                                pseudo = result;
                            }
                            try {
                                clientManager.joinRoom(1, pseudo);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            clientManager.setRefreshRooms(false);
                            MultiplayerMenu.this.setVisible(false);
                            MultiplayerInterface menu = new MultiplayerInterface(clientManager);
                            clientManager.addObserver(menu);
                            menu.setVisible(true);
                        }
                    });

                    roomUI.updateUI();
                }
            }
        });
    }
}
