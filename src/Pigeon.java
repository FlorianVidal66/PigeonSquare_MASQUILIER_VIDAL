import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Classe définissant un Pigeon. Il s'afit d'un thread qui va animer le pigeon.
 * Ce dernier va se déplacer sur le terrain pour manger de la nourriture
 *
 * @author Pierre MASQUILIER - Florian VIDAL
 *
 * @version 1.0
 *
 */
public class Pigeon extends Thread{

    private Terrain terrain;
    private int     posLigne;
    private int     posColonne;
    private int[]   objectif;
    private boolean dispersion;
    private int[]   objectifDispersion;
    private boolean running;


    /**
     * Constructeur de la classe Pigeon
     * @param terrain terrain sur lequel sera positionné le pigeon
     * @param posLigne ligne sur laquelle est positionné le pigeon
     * @param posColonne colonne sur laquelle est possitionné le pigeon
     */
    public Pigeon(Terrain terrain, int posLigne, int posColonne) {
        this.terrain            = terrain;
        this.posLigne           = posLigne;
        this.posColonne         = posColonne;
        this.objectif           = null;
        this.dispersion         = false;
        this.objectifDispersion = null;
        this.running            = false;
    }

    /* ************************************************************************************************************ **
     *                                         G E T T E R    &    S E T T E R                                       *
     * ************************************************************************************************************ **/

    public int getPosLigne() {
        return posLigne;
    }

    public int getPosColonne() {
        return posColonne;
    }

    public boolean isDispersion() {
        return dispersion;
    }

    public void setDispersion(boolean dispersion) {
        this.dispersion = dispersion;
    }

    public void setObjectifDispersion(int[] objectifDispersion) {
        this.objectifDispersion = objectifDispersion;
    }

    /* ************************************************************************************************************ **
     *                                      F O N C T I O N S    P U B L I Q U E S                                   *
     * ************************************************************************************************************ **/

    /**
     * Cycle de vie du pigeon
     */
    @Override
    public void run(){
        this.running = true;
        while (running){
            if (!dispersion) {
                if (this.objectif != null) {
                    // Verifier que de la nourriture est toujours présente sur la case objectif. Si non, trouver un objectif
                    if (this.terrain.getCases()[this.objectif[0]][this.objectif[1]].getNourriture() == null) {
                        trouverNourritureLaPlusProche();
                    } else {
                        try {
                            if (this.terrain.getCases()[this.objectif[0]][this.objectif[1]].getNourriture().estFraiche()) {
                                if (posLigne == objectif[0] && posColonne == objectif[1]) {
                                    // Objectif atteint : on mange la nourriture
                                    manger();

                                } else {
                                    allerCaseSuivante(this.objectif);
                                }
                            } else {
                                trouverNourritureLaPlusProche();
                            }
                        } catch (NullPointerException e) {}
                    }
                }
            } else {
                // Cas où le pigeon est en cours de dispersion
                if (this.posLigne == this.objectifDispersion[0] && this.posColonne == this.objectifDispersion[1]) {
                    this.objectifDispersion = null;
                    this.dispersion         = false;
                } else {
                    allerCaseSuivante(this.objectifDispersion);
                }
            }

            // Temporiser la durée d'éxécution
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Arrêter le thread
     */
    public  void arreter(){
        this.running = false;
    }

    /**
     * Retire une unité de nourriture à la case sur laquelle se situe le pigeon et cherche un nouvel objectif
     */
    public void manger(){
        ReentrantLock lock = this.terrain.getCases()[posLigne][posColonne].getNourriture().getLock();
        lock.lock();
        try {
            this.terrain.getCases()[posLigne][posColonne].setNourriture(null);
        } finally {
            lock.unlock();
        }
        System.out.println(Thread.currentThread().getId() + " a mangé");
        this.trouverNourritureLaPlusProche();
    }


    /**
     * Annonce au pigeon qu'une nouvelle source de nourriture est disponible, si elle est plus proche que l'objectif
     * actuel du pigeon, elle deviendra le nouvel objectif du pigeon
     */
    public void annoncerNouvelleNourriture(int ligne, int colonne){
        if (this.objectif == null){
            this.objectif = new int[2];
            this.objectif[0] = ligne;
            this.objectif[1] = colonne;
        } else {
            if (calculerDistance(ligne, colonne) < calculerDistance(objectif[0], objectif[1])) {
                this.objectif = new int[2];
                this.objectif[0] = ligne;
                this.objectif[1] = colonne;
            }
        }
    }


    /**
     * Attribue une position aléatoire au pigeon et trouve la nourriture la plus proche de la nouvelle position
     */
    public void disperser(){
        int ligne;
        int colonne;
        Random randomizer = new Random();
        // Recherche d'une case non-occupées
        do {
            ligne   = randomizer.nextInt(this.terrain.getTaille());
            colonne = randomizer.nextInt(this.terrain.getTaille());
        } while (this.terrain.getCases()[ligne][colonne].getCaseOccupee());
        this.posLigne   = ligne;
        this.posColonne = colonne;
        trouverNourritureLaPlusProche();

    }


    /* ************************************************************************************************************ **
     *                                  F O N C T I O N S     A U X I L I A I R E S                                  *
     * ************************************************************************************************************ **/

    /**
     * Calcule la distance entre la position actuelle du pigeon et celle entrée en paramètre
     * @param ligne ligne de la case
     * @param colonne colonne de la case
     */
    private double calculerDistance(int ligne, int colonne){
        return Math.sqrt(Math.pow((double)ligne - (double)this.posLigne,2)
                          + Math.pow((double)colonne - (double)this.posColonne,2));
    }


    /**
     * Trouve la nourriture fraîche la plus proche et met à jour l'objectif. Si aucune nourriture n'est fraîche
     * alors l'objectif devient null
     */
    private void trouverNourritureLaPlusProche(){
        ArrayList<int[]> casesAvecNourriture = this.terrain.trouverNourriture();
        if (casesAvecNourriture.size() == 0) {
            this.objectif = null;
        } else {
            int nouvLigne   = casesAvecNourriture.get(0)[0];
            int nouvColonne = casesAvecNourriture.get(0)[1];
            double distanceMini = calculerDistance(nouvLigne, nouvColonne);
            for (int i=1; i < casesAvecNourriture.size(); i++){
                double distance = calculerDistance(casesAvecNourriture.get(i)[0],casesAvecNourriture.get(i)[1]);
                if (distanceMini > distance ){
                    nouvLigne   = casesAvecNourriture.get(i)[0];
                    nouvColonne = casesAvecNourriture.get(i)[1];
                    distanceMini = distance;
                }
            }
            this.objectif = new int[2];
            this.objectif[0] = nouvLigne;
            this.objectif[1] = nouvColonne;
        }
    }

    /**
     * Déplace le pigeon sur la case suivante afin d'atteindre son objectif
     * @param monObjectif tableau avec la ligne et la colonne de l'objectif à atteindre
     */
    private void allerCaseSuivante(int[] monObjectif) {
        // Déterminer la position de la case suivante
        int ligneSuivante = posLigne;
        int colonneSuivante = posColonne;
        if (posLigne < monObjectif[0]) {
            ligneSuivante++;
        }
        if (posLigne > monObjectif[0]) {
            ligneSuivante--;
        }
        if (posColonne < monObjectif[1]) {
            colonneSuivante++;
        }
        if (posColonne > monObjectif[1]) {
            colonneSuivante--;
        }
        // On réserve la case suivante
        this.terrain.getCases()[ligneSuivante][colonneSuivante].setCaseOccupee(true);

        // On libère la case courante
        this.terrain.getCases()[posLigne][posColonne].setCaseOccupee(false);

        // Mise à jour de la position
        this.posLigne = ligneSuivante;
        this.posColonne = colonneSuivante;
    }


}
