import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Disperseur extends Thread {

    private int           posLigne;
    private int           posColonne;
    private Pigeon[]      pigeons;
    private Terrain       terrain;
    private ReentrantLock lock;

    public Disperseur(int ligne, Pigeon[] pigeons, Terrain terrain){
        this.posLigne   = ligne;
        this.posColonne = 0;
        this.pigeons    = pigeons;
        this.terrain    = terrain;
        this.lock       = new ReentrantLock();
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

    public ReentrantLock getLock() {
        return lock;
    }

    /* ************************************************************************************************************ **
     *                                      F O N C T I O N S    P U B L I Q U E S                                   *
     * ************************************************************************************************************ **/

    @Override
    public void run() {
        while (this.posColonne < PigeonSquare.taille-1){
            ArrayList<Pigeon> pigeonsProches = trouverPigeonsProches();
            Random            randomizer     = new Random();

            // Disperse les pigeons proches
            for(Pigeon p : pigeonsProches){
                int decalageLigne   = 30 + randomizer.nextInt(45);
                int decalageColonne = 30 +randomizer.nextInt(45);

                int direction = randomizer.nextInt(4);

                switch (direction){
                    case 0:
                        // Le pigeon est au Nord-Est du disperseur
                        int objectif0[] = {Math.max(0, p.getPosLigne()-decalageLigne),
                                          Math.max(0, p.getPosColonne()-decalageColonne)};
                        p.setObjectifDispersion(objectif0);
                        p.setDispersion(true);
                        break;

                    case 1:
                        // Le pigeon est au Nord-Ouest du disperseur
                        int objectif1[] = {Math.max(0, p.getPosLigne()-decalageLigne),
                                          Math.min(PigeonSquare.taille-2, p.getPosColonne()+decalageColonne)};
                        p.setObjectifDispersion(objectif1);
                        p.setDispersion(true);
                        break;

                    case 2:
                        // Le pigeon est au Sud-Est du disperseur
                        int objectif2[] = {Math.min(PigeonSquare.taille-2, p.getPosLigne()+decalageLigne),
                                          Math.max(0, p.getPosColonne()-decalageColonne)};
                        p.setObjectifDispersion(objectif2);
                        p.setDispersion(true);
                        break;

                    case 3:
                        // Le pigeon est au Sud-Ouest du disperseur
                        int objectif3[] = {Math.min(PigeonSquare.taille-2, p.getPosLigne()+decalageLigne),
                                          Math.min(PigeonSquare.taille-2, p.getPosColonne()+decalageColonne)};
                        p.setObjectifDispersion(objectif3);
                        p.setDispersion(true);
                        break;
                    }
            }

            // Changement de la position du disperseur
            if (this.posColonne < PigeonSquare.taille-1) {
                this.posColonne++;
            }

            // Temporisation du déplacement
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    /* ************************************************************************************************************ **
     *                                  F O N C T I O N S     A U X I L I A I R E S                                  *
     * ************************************************************************************************************ **/

    /**
     * Trouver les pigeons proches du disperseur
     * @return liste des pigeons proches
     */
    private ArrayList<Pigeon> trouverPigeonsProches() {
        ArrayList<Pigeon> pigeons = new ArrayList<>();
        int minLigne   = Math.max(0, this.posLigne-35);
        int maxLigne   = Math.min(PigeonSquare.taille, this.posLigne+35);
        int minColonne =  Math.max(0, this.posColonne-35);
        int maxColonne = Math.min(PigeonSquare.taille, this.posColonne+35);

        for(Pigeon p : this.pigeons){
            int ligne   = p.getPosLigne();
            int colonne = p.getPosColonne();
            if (ligne > minLigne && ligne < maxLigne && colonne > minColonne && colonne < maxColonne && !p.isDispersion()){
                pigeons.add(p);
            }
        }
        return pigeons;
    }

}
