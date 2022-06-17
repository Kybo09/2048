package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Observable;

import modele.Case;
import modele.SoundManager;
import vue_controleur.MainMenu;

public class Client {

    private static ClientManager clientManager;
    
    public static void main(String[] args) throws SocketException, UnknownHostException{
        ClientManager c = new ClientManager();
        clientManager = c;
        mainMenu();
        startClient();
    }

    public static void mainMenu() {
        MainMenu main = new MainMenu();
        main.setVisible(true);
    }

    public static ClientManager getClientManager(){
        return clientManager;
    }

    private static void startClient() throws SocketException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    clientManager.receivePacket();
                }
            }
        }).start();        
    }
}
