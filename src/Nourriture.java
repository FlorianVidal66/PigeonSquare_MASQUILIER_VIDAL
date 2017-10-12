import java.util.concurrent.locks.ReentrantLock;

/**
 * Classe définissant une unité de nourriture en fonction de sa fraîcheur
 *
 * @author Pierre MASQUILIER - Florian VIDAL
 *
 * @version 1.0
 */
public class Nourriture {

    private int tempsRestant = 50;
    private boolean estFraiche;
    private ReentrantLock lock;

    /**
     * Constructeur de la classe Nourriture
     */
    public Nourriture(){
        this.estFraiche = true;
        this.lock        = new ReentrantLock();
    }


    public ReentrantLock getLock() {
        return lock;
    }
    /* ************************************************************************************************************ **
     *                                               F O N C T I O N S                                               *
     * ************************************************************************************************************ **/

    /**
     * Mettre à jour le temps restant avant que la nourriture ne soit plus fraîche et la fraîcheur du produit en
     * conséquence
     */
    public void maj(){
        if (estFraiche) {
            this.tempsRestant--;
            this.estFraiche = this.tempsRestant > 0;
        }
    }

    /**
     * Prédicat sur la fraîcheur de la nourriture
     * @return Vrai si la nourriture est fraiche; Faux sinon
     */
    public boolean estFraiche(){
        return this.estFraiche;
    }


}
