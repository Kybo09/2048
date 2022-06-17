package ai;

import modele.Case;
import modele.Direction;
import modele.Jeu;

import java.util.ArrayList;

public class Node {

    public static int numberOfNodes;

    public double scoreAverage;
    public double wins;
    public int simulations;
    public Node parent;
    public Node root;
    public Direction direction;
    public Jeu state;
    public boolean simulated;
    public int id;
    public ArrayList<Node> children;

    public Node(Jeu j, Direction d, Node p, Node r){
        scoreAverage = 0;
        wins = 0;
        simulations = 0;
        parent = p;
        direction = d;
        state = j;
        simulated = false;
        id = numberOfNodes;
        numberOfNodes++;
        root = r;
        children = new ArrayList<>();
        simulate();
    }

    public Node(Jeu j){
        scoreAverage = 0;
        wins = 0;
        root = this;
        simulations = 0;
        parent = null;
        direction = null;
        state = j;
        simulated = false;
        id = 0;
        numberOfNodes = 1;
        children = new ArrayList<>();
    }

    public double getValue(double r){
        //double q = scoreAverage /r < 1 ? scoreAverage /r : 1;
        //return scoreAverage /r + 0.1*Math.sqrt(1)*Math.sqrt(Math.log(parent.simulations)/simulations);
        return (scoreAverage/3932156)/simulations + Math.sqrt(2)*Math.sqrt(Math.log(parent.simulations)/simulations);
    }

    public double getValue(){
        return scoreAverage;
    }

    public void addChildren(Node root){
        for (Direction dir: Direction.values()) {
            if(true){ //canIMove(state, dir)
                children.add(new Node(state.clone(), dir, this, root));
            }
        }
    }

    public void simulate(){
        move(state, direction);
        Jeu jeu = state.clone();
        while (!jeu.gameOver) {
            Direction dir = Direction.values()[(int) (Math.random() * Direction.values().length)];
            move(jeu, dir);
        }
        simulated = true;
        setSimulationsWins(jeu.score, 0);
    }

    private Boolean move(Jeu jeu, Direction d) {
        Case[][] tabCases = jeu.getTabCases();
        Boolean moved = false;
        for (int i = 0; i < tabCases.length; i++) {
            for (int j = 0; j < tabCases.length; j++) {
                if(tabCases[j][i] != null){
                    tabCases[j][i].hasFusionned = false;
                }
            }
        }
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

        if(moved && !jeu.gameOver && jeu.getHmap().size() < (tabCases.length*tabCases.length)){
            jeu.spawnTiles();
        }

        if(jeu.getHmap().size() == tabCases.length*tabCases.length){
            for (int i = 0; i < tabCases.length; i++) {
                for (int j = 0; j < tabCases.length; j++) {
                    for (Direction dir : Direction.values()) {
                        Case n = jeu.getNeighbor(dir, tabCases[i][j]);
                        if(n != null && n.getValeur() == tabCases[i][j].getValeur()){
                            return moved;
                        }
                    }
                }

            }
            jeu.gameOver = true;
        }

        return moved;
    }

    public boolean canIMove(Jeu jeu, Direction dir){
        Case[][] tabCases = jeu.getTabCases();
        for (int i = 0; i < tabCases.length; i++) {
            for (int j = 0; j < tabCases.length; j++) {
                if(tabCases[j][i] != null && !tabCases[j][i].haveANeighboor(dir)){
                    return true;
                }
            }
        }
        return false;

    }

    public void setSimulationsWins(double score, int win) {
        simulations++;
        if(children.size() == 0){
            scoreAverage += score/simulations;
            win = scoreAverage/root.scoreAverage > 1 ? 1 : 0;
            wins = win;
        }
        else{
            double tmpScore = 0;
            for (Node child: children) {
                tmpScore += child.scoreAverage;
            }
            scoreAverage = tmpScore/children.size();
            wins += win;
        }

        if(parent != null){
            parent.setSimulationsWins(score, win);
        }

    }
}
