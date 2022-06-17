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
import modele.Direction;
import modele.Jeu;
import modele.SoundManager;

public class ClientManager extends Observable {

    private InetAddress address;
    private int port = 300;
    private DatagramSocket socket;
    private boolean refreshRooms = false;
    private ArrayList<String> roomsList = new ArrayList<String>();
    private ArrayList<User> usersInRoom = new ArrayList<User>();
    private String pseudoPlayer;
    private Jeu jeuMulti = new Jeu(4);
    private ArrayList<SoundManager> sounds = new ArrayList<SoundManager>();
    
    public ClientManager() throws SocketException, UnknownHostException{
        socket = new DatagramSocket();
        address = InetAddress.getByName("127.0.0.1");
        updateUI();
        generateBlankUser();
        SoundManager s = new SoundManager("mainmusic.wav", true, 0);
        s.start();
        sounds.add(s);
        this.jeuMulti.soloMode = true;
    }

    public Jeu getJeu(){
        return this.jeuMulti;
    }

    public void moveJeu(Direction d){
        jeuMulti.move(d);
        updateUI();
    }

    public void setJeu(Jeu jeu){
        this.jeuMulti = jeu;
    }

    private void generateBlankUser() {
        for(int i=0; i<4;i++){
            User u = new User(null, 0, "none");
            usersInRoom.add(u);
        }
    }

    public ArrayList<String> getRooms(){
        return roomsList;
    }

    public ArrayList<User> getUsersInRoom(){
        return usersInRoom;
    }

    public void setRefreshRooms(boolean b){
        this.refreshRooms = b;
    }

    public void updateUI(){
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers();
    }

    public void toggleVolumeSounds(float volume, int type){
        for(SoundManager s: sounds){
            s.setVolume(volume, type);
        }
    }

    public void receivePacket(){
        byte[] byteBuffer1 = new byte[1024];
        DatagramPacket packetReceive = new DatagramPacket(byteBuffer1, 1024);
        try {
            socket.receive(packetReceive);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String data = new String(packetReceive.getData(), 0, packetReceive.getLength());
        treatPacket(data);
        this.address = packetReceive.getAddress();
        this.port = packetReceive.getPort();
        System.out.println("New packet received : " + data);
    }

    public void refreshRooms(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(refreshRooms){
                    try {
                        askForRooms();
                        updateUI();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void clearUsers(){
        for(int i=0; i<usersInRoom.size();i++){
            usersInRoom.get(i).setPseudo("none");
        }
    }

    private void treatPacket(String packet){
        if(packet.contains("roomslist")){
            String[] rooms = packet.split(",");
            roomsList.clear();
            if(!packet.contains("None")){
                for(int i = 1; i < rooms.length; i++){
                    roomsList.add("Room " + rooms[i]);
                }
            }
        }
        if(packet.contains("newPlayer")){
            updatePseudoRoom();
        }
        if(packet.contains("pseudoList")){
            String[] msg = packet.split(":");
            String[] pseudoList = msg[1].split(",");
            clearUsers();
            for(int i = 0; i < pseudoList.length; i++){
                usersInRoom.get(i).setPseudo(pseudoList[i]);
            }
            updateUI();
        }
        if(packet.contains("scoreList")){
            String[] msg = packet.split(":");
            String[] pseudoList = msg[1].split(",");
            for(int i = 0; i < pseudoList.length; i++){
                usersInRoom.get(i).getJeu().score = Integer.parseInt(pseudoList[i]);
            }
            updateUI();
        }
        if(packet.contains("gridList")){
            String[] msg = packet.split(":");
            String[] gridList = msg[1].split(",");
            for(int i = 0; i < gridList.length; i++){
                Case[][] grid = gridToArray(gridList[i]);
                usersInRoom.get(i).getJeu().setGrid(grid);
            }
            updateUI();
        }
        if(packet.contains("welcome")){
            sendGrid();
            sendScore();
            updateUI();
        }
        if(packet.contains("gameOver")){
            String[] msg = packet.split(":");
            for(int i = 0; i < usersInRoom.size(); i++){
                if(usersInRoom.get(i).getPseudo().equals(msg[1])){
                    usersInRoom.get(i).setGameOver(true);
                }
                System.out.println(usersInRoom.get(i).isGameOver());
            }
            updateUI();
        }
    }

    public void updatePseudoRoom(){
        String data = "pseudoList";
        byte[] buf = data.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendGrid();
        sendScore();
        updateUI();
    }

    public void sendGameOver(){
        String data = "gameOver" + ":" + this.pseudoPlayer;
        byte[] buf = data.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateGrids(){
        String data = "gridList";
        byte[] buf = data.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createRoom() throws IOException{
        String messageClavier = "createroom";
        byte[] byteBuffer = messageClavier.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(byteBuffer, byteBuffer.length, address, port);
        socket.send(packet);
        updateUI();
    }

    public void askForRooms() throws IOException {
        String messageClavier = "roomslist";
        byte[] byteBuffer = messageClavier.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(byteBuffer, byteBuffer.length, address, port);
        socket.send(packet);
    }

    public String getPseudoPlayer(){
        return this.pseudoPlayer;
    }

    public void joinRoom(int roomId, String pseudo) throws IOException {
        String messageClavier = "joinroom:" + roomId + ":" + pseudo;
        byte[] byteBuffer = messageClavier.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(byteBuffer, byteBuffer.length, address, port);
        socket.send(packet);
        this.pseudoPlayer = pseudo;
    }

    public void sendScore(){
        String messageClavier = "sendScore:" + this.pseudoPlayer + "," + jeuMulti.score;
        byte[] byteBuffer = messageClavier.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(byteBuffer, byteBuffer.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateScore() {
        String data = "scoreList";
        byte[] buf = data.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendGrid() {
        String data = "sendGrid:" + this.pseudoPlayer + ":";
        data += gridToString(jeuMulti.getTabCases());
        byte[] buf = data.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String gridToString(Case[][] grid){
        String gridString = "";
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++){
                if(grid[i][j] == null){
                    gridString += "0";
                }
                else{
                    gridString += grid[i][j].getValeur();
                }
                gridString += ".";
            }
            gridString = gridString.substring(0, gridString.length()-1);
            gridString += "|";
        }
        return gridString.substring(0, gridString.length() - 1);
    }

    private Case[][] gridToArray(String gridString){
        String[] gridTab = gridString.split("\\|");
        int length = 4;
        Jeu dummy = new Jeu(length);
        Case[][] grid = new Case[length][length];
        for(int i = 0; i < gridTab.length; i++){
            String[] line = gridTab[i].split("\\.");
            for(int j = 0; j < line.length; j++){
                if(line[j].equals("0")){
                    grid[i][j] = null;
                }
                else{
                    grid[i][j] = new Case(dummy, Integer.parseInt(line[j]));
                }
            }
        }
        return grid;
    }

    public void display(Case[][] c){
        Case[][] tabCases = c;
        System.out.println("--------");
            for (int i = 0; i < tabCases.length; i++) {
                for (int j = 0; j < tabCases.length; j++) {
                    if(tabCases[i][j] != null){
                        System.out.print(tabCases[i][j].getValeur() + " ");
                    }
                    else{
                        System.out.print("  ");
                    }
                }
                System.out.println("");
            }
        System.out.println("--------");
    }
}
