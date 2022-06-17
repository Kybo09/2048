package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import modele.Case;

public class Room extends Thread {
    private int id;
    private static int number = 1;
    private ArrayList<User> users = new ArrayList<User>();
    public boolean canJoin = true;
    private DatagramSocket ds;
    private int nbPlayer = 0;

    public Room() {
        this.id = number;
        number++;
        try {
            this.ds = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public boolean canJoin() {
        return canJoin;
    }

    public int getNum() {
        return this.id;
    }

    public int getNbPlayer(){
        return nbPlayer;
    }

    public void run() {
        byte[] buf = new byte[1024];
        while(users.size() > 0) {
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
            try {
                ds.receive(datagramPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String data = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
            System.out.println("Room " + this.id + " receive : " + data);
            if(data.contains("pseudoList")){
                String answer = "pseudoList:";
                for(User u: users){
                    answer += u.getPseudo() + ",";
                }
                for(User u: users){
                    byte[] ucast = answer.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket packetSend = new DatagramPacket(ucast, ucast.length, u.getAddress(), u.getPort());
                    try {
                        ds.send(packetSend);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(data.contains("scoreList")){
                String answer = "scoreList:";
                for(User u: users){
                    answer += u.getScore() + ",";
                }
                for(User u: users){
                    byte[] ucast = answer.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket packetSend = new DatagramPacket(ucast, ucast.length, u.getAddress(), u.getPort());
                    try {
                        ds.send(packetSend);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(data.contains("gridList")){
                String answer = "gridList:";
                for(User u: users){
                    answer += u.getGrid() + ",";
                }
                for(User u: users){
                    byte[] ucast = answer.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket packetSend = new DatagramPacket(ucast, ucast.length, u.getAddress(), u.getPort());
                    try {
                        ds.send(packetSend);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(data.contains("sendScore")){
                String[] msg = data.split(":");
                String[] scoreList = msg[1].split(",");
                for(User u: users){
                    if(u.getPseudo().equals(scoreList[0])){
                        u.setScore(Integer.parseInt(scoreList[1]));
                    }
                }
            }
            if(data.contains("sendGrid")){
                String[] msg = data.split(":");
                for(User u: users){
                    if(u.getPseudo().equals(msg[1])){
                        u.setGrid(msg[2]);
                    }
                }
            }
            if(data.contains("gameOver")){
                String[] msg = data.split(":");
                for(User u: users){
                    byte[] ucast = data.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket packetSend = new DatagramPacket(ucast, ucast.length, u.getAddress(), u.getPort());
                    try {
                        ds.send(packetSend);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        this.canJoin = false;
        ds.close();
    }

    public void addPlayer(InetAddress address, int port, String pseudo) {
        User u = new User(address, port, pseudo);
        users.add(u);
        nbPlayer++;
        if(nbPlayer == 4){
            canJoin = false;
        }
        welcomeNewPlayer(u);
        notifyNewPlayer();
    }

    private void welcomeNewPlayer(User u) {
        String answer = "welcome:" + u.getPseudo();
        byte[] ucast = answer.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packetSend = new DatagramPacket(ucast, ucast.length, u.getAddress(), u.getPort());
        try {
            ds.send(packetSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void notifyNewPlayer(){
        for(User u : users){
            String data = "newPlayer";
            byte[] buf = data.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, u.getAddress(), u.getPort());
            try {
                ds.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}