import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class PigeonSquare {

    private Terrain               terrain;
    private Pigeon[]              pigeons;
    private ArrayList<Disperseur> disperseurs;

    public static int taille      = 700;
    private static int nbrPigeons = 15;

    /**
     * Constructeur de la classe PigeonSquare. Demande le nombre de pigeons à placer sur le terrain
     */
    public PigeonSquare(){
        this.disperseurs = new ArrayList<>();
        // Instanciation du terrain
        this.terrain = new Terrain(taille);
        this.terrain.start();
        //Instanciation des pigeons
        this.pigeons = ajouterPigeonsPositionAleatoire(nbrPigeons);
        for (Pigeon p : this.pigeons){
            p.start();
        }
    }


    /* ************************************************************************************************************ **
     *                                                   G E T T E R                                                 *
     * ************************************************************************************************************ **/

    public Pigeon[] getPigeons() {
        return pigeons;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public ArrayList<Disperseur> getDisperseurs() {
        return disperseurs;
    }

    /* ************************************************************************************************************ **
     *                                      F O N C T I O N S    P U B L I Q U E S                                   *
     * ************************************************************************************************************ **/

    /**
     * Pose une unité de nourriture sur la case demandée
     * @param ligne ligne de la case
     * @param colonne colonne de la case
     */
    public void poserNourriture(int ligne, int colonne){
        this.terrain.getCases()[ligne][colonne].setNourriture(new Nourriture());
        // Alerter les pigeons
        for (Pigeon p : this.pigeons){
            p.annoncerNouvelleNourriture(ligne, colonne);
        }
    }


    /**
     * Ajoute potentiellement une instance de Disperseur
     */
    public void disperser(){
        // Apparition potentielle d'un disperseur
        Random randomizer = new Random();
        int probabiliteDeDispersion = randomizer.nextInt(10);
        if (randomizer.nextInt(101) < probabiliteDeDispersion) {
            int ligneApparition = randomizer.nextInt(this.taille);
            Disperseur disperseur = new Disperseur(ligneApparition, this.pigeons, this.terrain);
            disperseur.start();
            this.disperseurs.add(disperseur);
        }
    }


    /**
     * Retire le disperseur de la liste des disperseurs s'il est arrivé à l'autre bout du terrain
     */
    public void majDisperseur(){
        for(int i=0; i < this.disperseurs.size(); i++){
            this.disperseurs.get(i).getLock().lock();
            Disperseur d = this.disperseurs.get(i);
            d.getLock().lock();
            try {
                if (d.getPosColonne() == taille-1){
                    this.disperseurs.remove(d);
                }
            } finally {
                d.getLock().unlock();
            }


        }
    }


    /* ************************************************************************************************************ **
     *                                  F O N C T I O N S     A U X I L I A I R E S                                  *
     * ************************************************************************************************************ **/

    /**
     * Retourne un tableau de pigeons positionnés aléatoirement sur le terrain
     * @param nbrPigeons nombre de pigeons à placer
     * @return tableau de pigeon
     */
    private Pigeon[] ajouterPigeonsPositionAleatoire(int nbrPigeons){
        Pigeon[] tabPigeons = new Pigeon[nbrPigeons];
        int ligne, colonne;
        Random randomizer = new Random();
        for (int i=0; i < nbrPigeons; i++){
            do {
                ligne   = randomizer.nextInt(this.terrain.getTaille());
                colonne = randomizer.nextInt(this.terrain.getTaille());
            } while (this.terrain.getCases()[ligne][colonne].getCaseOccupee());
            tabPigeons[i] = new Pigeon(this.terrain, ligne, colonne);
        }
        return tabPigeons;
    }


}
