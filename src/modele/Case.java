package modele;

public class Case{
    private int valeur;
    public Jeu j;
    public boolean hasFusionned;


    public Case(Jeu j, int _valeur) {
        this.j = j;
        valeur = _valeur;
    }

    public int getValeur() {
        return valeur;
    }
    public void setValeur(int valeur) {
        this.valeur = valeur;
    }
    public Boolean move(Direction d){
        //System.out.println("hmap case : " + j.getHmap());
        Case n = j.getNeighbor(d, this);
        Boolean moved = false;
        // Si la case n'est pas en bordure
        if(n != null){
            while(n.getValeur() == -1){
                moved = true;
                j.moveCase(d, this);
                n = j.getNeighbor(d, this);
                if(n == null){ break; };
            }
            // Si le voisin possède la meme valeur que la case
            if(n != null && !hasFusionned && !n.hasFusionned) {
                if (n.getValeur() == this.valeur) {
                    moved = true;
                    hasFusionned = true;
                    n.hasFusionned = true;
                    j.fusionCase(this, n);

                }
            }
        }
        return moved;
    }
    public Boolean haveANeighboor(Direction d){
        Case n = j.getNeighbor(d, this);
        if(n != null && (n.getValeur() == -1 || n.getValeur() == valeur)){
            return false;
        } return true;
    }

}
