package server;

import java.net.InetAddress;

import modele.Case;
import modele.Jeu;

public class User {
    private InetAddress address;
    private int port;
    private String pseudo;
    private String grid = "0.0.0.0|0.0.0.0|0.0.0.0|0.0.0.0";
    private Jeu jeu = new Jeu(4);
    private boolean gameOver = false;
    private int score = 0;

    public User(InetAddress address, int port, String pseudo) {
        this.address = address;
        this.port = port;
        this.pseudo = pseudo;
    }

    public InetAddress getAddress() {
        return address;
    }

    public Jeu getJeu(){
        return this.jeu;
    }

    public void setJeu(Jeu jeu){
        this.jeu = jeu;
    }	

    public int getPort() {
        return port;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setGrid(String tab){
        this.grid = tab;
    }

    public String getGrid(){
        return this.grid;
    }

    public void setGameOver(boolean b){
        this.gameOver = b;
    }

    public boolean isGameOver(){
        return this.gameOver;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}
