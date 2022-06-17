package ai;

import modele.Case;
import modele.Direction;
import modele.Jeu;


public class BehaviorTree2048 implements BlackBoard{

    private Jeu[] jeux = new Jeu[4];
    private Boolean[] moved = new Boolean[4];
    private Direction[] directions = new Direction[4];
    private int[] averageScores = new int[4];
    private int PLAYED_SCENARIOS = 1000;

    public BehaviorTree2048(){

    }
    
    public Direction getBestDestination(Jeu jeu){

    //    MCTS test = new MCTS(jeu);
    //    test.start();
    //    return test.getBestDirection();

        for (int index = 0; index < 4; index++){
            averageScores[index] = 0;
        }

        for(int turn = 0; turn < PLAYED_SCENARIOS; turn++){
            clone(jeu);
            playScenarios();
        }
        
        
        Direction bestDirection = Direction.bas;
        int maxScore = 0;
        for(int index = 0; index < jeux.length; index++){
            averageScores[index] /= PLAYED_SCENARIOS;
            if(averageScores[index] >= maxScore && moved[index]){
                maxScore = averageScores[index];
                bestDirection = directions[index];
            }
        }
        //System.out.println(bestDirection);
        return bestDirection;

    }

    public int[] getScores(){
        return averageScores;
    }

    @Override
    public int playScenarios() {
        for(int index = 0; index < jeux.length; index++){
            jeux[index].score = 0;
            moved[index] = move(jeux[index], directions[index]);
            if(!moved[index]){ jeux[index].gameOver = true;}
            while(!jeux[index].gameOver){
                Direction dir = Direction.values()[(int)(Math.random()*Direction.values().length)];
                move(jeux[index], dir);
            }
            averageScores[index] += jeux[index].score;
        }
        return 0;
    }

    @Override
    public void clone(Jeu jeu) {
        jeux = new Jeu[]{jeu.clone(), jeu.clone(), jeu.clone(), jeu.clone()};
        directions = Direction.values();
        moved = new Boolean[]{false, false, false, false};
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

    public void display(Jeu jeu){
        System.out.println("display");
        Case[][] tabCases = jeu.getTabCases();
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
