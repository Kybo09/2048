package ai;

import modele.Case;
import modele.Direction;
import modele.Jeu;

public class MCTS {

    public Node root;

    public MCTS(Jeu jeu){
        root = new Node(jeu);
    }

    public Direction getBestDirection(){
        Direction bestDirection = Direction.bas;
        double bestValue = 0;
        for (Node child: root.children) {
            if(child.scoreAverage > bestValue){
                bestDirection = child.direction;
                bestValue = child.scoreAverage;
            }
        }
        return bestDirection;
    }

    public void start(){
        for (Direction dir: Direction.values()) {
            if(root.canIMove(root.state, dir)){
                root.children.add(new Node(root.state.clone(), dir, root, root));
            }
        }
        for(int i=0; i < 700; i++){
            Node child = selection(root);
            child.addChildren(root);
        }
        display(root);
    }

    private Node selection(Node parent) {
        Node bestChild = null;
        while (parent.children.size() > 0){
            double bestValue = -1;
            for (Node child : parent.children) {
                if (child.getValue(root.getValue()) > bestValue) {
                    bestChild = child;
                    bestValue = child.getValue(root.getValue());
                }
            }
            parent=bestChild;
        }

        return bestChild;
    }

    private void display(Node parent){
        if(parent.id > -1){
            System.out.println("node " + parent.id + " : " + parent.wins + "/" + parent.simulations);
            display(parent.state);
            for (Node child: parent.children) {
                System.out.println("node " + child.id + " " + child.direction + " : " + child.wins + "/" + child.simulations + " | parent : " + parent.id);
                //display(child);
            }

        }

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
