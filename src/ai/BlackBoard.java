package ai;

import modele.Jeu;

public interface BlackBoard {

    public void clone(Jeu jeu);
    public int playScenarios();
}
