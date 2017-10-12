import java.util.ArrayList;

/**
 * Classe définissant le terrain représenté par un tableau 2D de taille donnée.
 *
 * @author Pierre MASQUILIER - Florian VIDAL
 *
 * @version 1.0
 *
 */
public class Terrain extends Thread {

    private Case[][] cases;
    private int      taille;

    /**
     * Constructeur de la classe Terrain. Creation d'un tableau 2D de la taille demandée. Un terrain est carré
     * @param taille taille du terrain
     */
    public Terrain(int taille){
        this.taille = taille-1;
        this.cases  = new Case[taille][taille];
        for (int i=0; i < this.taille; i++) {
            for (int j=0; j <= this.taille; j++) {
                this.cases[i][j] = new Case();
            }
        }
    }

    /* ************************************************************************************************************ **
     *                                                    G E T T E R                                                *
     * ************************************************************************************************************ **/

    // Le terrain n'est pas modifiable donc read-only

    public Case[][] getCases() {
        return cases;
    }

    public int getTaille() {
        return taille;
    }


    /* ************************************************************************************************************ **
     *                                      F O N C T I O N S    P U B L I Q U E S                                   *
     * ************************************************************************************************************ **/

    /**
     * Toutes les 100ms, la nourriture vieillie
     */
    public void run(){
        while (true){
            majNourriture();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Trouve toutes les cases possédant de la nourriture fraîche
     * @return Liste des positions des cases
     */
    public ArrayList<int[]> trouverNourriture() {
        ArrayList<int[]> listeCase = new ArrayList<>();
        for (int i=0; i < this.taille; i++) {
            for (int j=0; j <= this.taille; j++) {
                if (this.cases[i][j].getNourriture() != null) {
                    if (this.cases[i][j].getNourriture().estFraiche()) {
                        int[] position = {i, j};
                        listeCase.add(position);
                    }
                }
            }
        }
        return listeCase;
    }

    /**
     * Trouve toutes les cases possédant de la nourriture
     * @return Liste des positions des cases
     */
    public ArrayList<int[]> trouverTouteNourriture() {
        ArrayList<int[]> listeCase = new ArrayList<>();
        for (int i=0; i < this.taille; i++) {
            for (int j=0; j <= this.taille; j++) {
                if (this.cases[i][j].getNourriture() != null) {
                    int[] position = {i, j};
                    listeCase.add(position);
                }
            }
        }
        return listeCase;
    }

    /**
     * Mettre à jour l'état de la nourriture
     */
    public void majNourriture(){
        ArrayList<int[]> caseAvecNourriture = trouverNourriture();
        for (int[] elt : caseAvecNourriture){
            try {
                this.cases[elt[0]][elt[1]].getNourriture().maj();
            } catch (NullPointerException e){}
        }
    }


}
