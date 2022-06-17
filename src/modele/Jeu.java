package modele;

import ai.BehaviorTree2048;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Random;


public class Jeu extends Observable implements Cloneable {

    private Case[][] tabCases;
    private HashMap<Case, Point> hmap;
    public boolean gameOver = false;
    public int score;
    public boolean moved;   
    public boolean soloMode = false;
    public static final Random rand = new Random();

    public Jeu clone(){
        Jeu clone = new Jeu(getSize(), true);
        for (int i = 0; i < tabCases.length; i++) {
            for (int j = 0; j < tabCases.length; j++) {
                if(tabCases[i][j] != null)
                    clone.addCase(new Case(clone, tabCases[i][j].getValeur()), new Point(i,j));
            }
        }
        clone.score = score;
        clone.gameOver = gameOver;
        return clone;
    }

    public Jeu(int size) {
        tabCases = new Case[size][size];
        hmap = new HashMap<Case, Point>();
        score = 0;
        spawnTiles();
    }

    public Jeu(int size, boolean copy){
        tabCases = new Case[size][size];
        hmap = new HashMap<Case, Point>();
        score = 0;
    }

    public Case[][] getTabCases() {
        return tabCases;
    }

    public void restart(){
        for (int i = 0; i < tabCases.length; i++) {
            for (int j = 0; j < tabCases.length; j++) {
                tabCases[i][j] = null;
            }
        }
        hmap.clear();
        gameOver = false;
        score = 0;

        spawnTiles();
        setChanged();
        notifyObservers();
    }

    public int getSize() {
        return tabCases.length;
    }

    public HashMap getHmap(){ return this.hmap; }

    public Case getCase(int i, int j) {
        return tabCases[i][j];
    }


    public void move(Direction d){
        moved = false;
        for (int i = 0; i < tabCases.length; i++) {
            for (int j = 0; j < tabCases.length; j++) {
                if(tabCases[j][i] != null){
                    tabCases[j][i].hasFusionned = false;
                }
            }
        }
        /*
        for (Map.Entry<Case, Point> c : hmap.entrySet()) {
            if(c.getKey().j != this){
                hmap.remove(c.getKey());
            }
            
        }*/
        switch(d){
            case haut:
                for (int i = 0; i < tabCases.length; i++) {
                    for (int j = 0; j < tabCases.length; j++) {
                        if(tabCases[j][i] != null){
                            if(tabCases[j][i].move(d)){ moved = true; }
                        }
                    }
                }
                break;
            case bas:
                for (int i = tabCases.length-1; i >= 0; i--) {
                    for (int j = tabCases.length-1; j >= 0; j--) {
                        if(tabCases[j][i] != null){
                            if(tabCases[j][i].move(d)){ moved = true; }
                        }
                    }
                }
                break;
            case droite:
                for (int i = 0; i < tabCases.length; i++) {
                    for (int j = tabCases.length-1; j >= 0; j--) {
                        if(tabCases[i][j] != null){
                            if(tabCases[i][j].move(d)){ moved = true; }
                        }
                    }
                }
                break;
            case gauche:
                for (int i = 0; i < tabCases.length; i++) {
                    for (int j = 0; j < tabCases.length; j++) {
                        if(tabCases[i][j] != null){
                            if(tabCases[i][j].move(d)){ moved = true; }
                        }
                    }
                }
                break;
        }
        //System.out.println("hmap size : " + hmap.size());
        if(moved &&!gameOver && hmap.size() < (tabCases.length*tabCases.length)){
            spawnTiles();
        }

        boolean findNull = false;
        boolean findCombinaison = false;

        for (int i = 0; i < tabCases.length; i++) {
            for (int j = 0; j < tabCases.length; j++) {
                if(tabCases[i][j] == null){
                    findNull = true;
                }
            }
        }
        if(!findNull){
            for (int i = 0; i < tabCases.length; i++) {
                for (int j = 0; j < tabCases.length; j++) {
                    for (Direction dir : Direction.values()) {
                        Case n = getNeighbor(dir, tabCases[i][j]);
                        if(n != null && n.getValeur() == tabCases[i][j].getValeur()){
                            findCombinaison = true;
                        }
                    }
                }
            }
            if(!findCombinaison){
                gameOver = true;
            }


        }

        setChanged();
        notifyObservers();
    }

    public void spawnTiles(){
        int x = rand.nextInt(4);
        int y = rand.nextInt(4);
        int value = rand.nextInt(2)+1;

        while(tabCases[x][y] != null){
            x = rand.nextInt(4);
            y = rand.nextInt(4);
        }
        addCase(new Case(this, value*2), new Point(x, y));
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public Case getNeighbor(Direction d, Case c){
        Point coords = hmap.get(c);
        switch(d){
            case haut:
                if(coords.x == 0){
                    return null;
                }
                if(tabCases[coords.x-1][coords.y] == null){
                    return new Case(this, -1);
                }
                return tabCases[coords.x-1][coords.y];

            case bas:
                if(coords.x == tabCases.length-1){
                    return null;
                }
                if(tabCases[coords.x+1][coords.y] == null){
                    return new Case(this, -1);
                }
                return tabCases[coords.x+1][coords.y];

            case gauche:
                if(coords.y == 0){
                    return null;
                }
                if(tabCases[coords.x][coords.y-1] == null){
                    return new Case(this, -1);
                }
                return tabCases[coords.x][coords.y-1];

            case droite:
                if(coords.y == tabCases.length-1){
                    return null;
                }
                if(tabCases[coords.x][coords.y+1] == null){
                    return new Case(this, -1);
                }
                return tabCases[coords.x][coords.y+1];
        }
        return null;
    }

    private void addCase(Case c, Point p){
        tabCases[p.x][p.y] = c;
        hmap.put(c, p);
    }

    private void delCase(Case c, Point p){
        tabCases[p.x][p.y] = null;
        hmap.remove(c);
    }

    public void moveCase(Direction d, Case c){
        Point coords = hmap.get(c);
        delCase(c, coords);
        switch (d) {
            case haut:
                addCase(c, new Point(coords.x - 1, coords.y));
                break;
            case bas:
                addCase(c, new Point(coords.x + 1, coords.y));
                break;
            case gauche:
                addCase(c, new Point(coords.x, coords.y - 1));
                break;
            case droite:
                addCase(c, new Point(coords.x, coords.y + 1));
                break;
        }
    }

    public void fusionCase(Case origine, Case destination){
        if(this.soloMode){
            SoundManager s = new SoundManager("fusion.wav", false, 1);
            s.start();
        }
        destination.setValeur(destination.getValeur()*2);
        score += destination.getValeur();
        delCase(origine, hmap.get(origine));
    }

    public void setHmap(HashMap<Case, Point> hmapold) {
        this.hmap = hmapold;
    }

    public void startAiAuto() {
        Jeu jeu = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!jeu.gameOver){
                    BehaviorTree2048 bt = new BehaviorTree2048();
                    Direction bestDirection = bt.getBestDestination(jeu);
                    jeu.move(bestDirection);
                    setChanged();
                    notifyObservers();
                }
            }
        }).start();
        
        
    }

    public void setGrid(Case[][] grid) {
        this.tabCases = grid;
    }
}
