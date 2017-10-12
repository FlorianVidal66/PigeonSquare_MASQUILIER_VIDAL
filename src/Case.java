
/**
 * Classe définissant une case du terrain.
 *
 * @author Pierre MASQUILIER - Florian VIDAL
 *
 * @version 1.0
 *
 */
public class Case {

    private boolean caseOccupee;
    private Nourriture nourriture;


    /**
     * Constructeur de la classe Case
     */
    public Case(){
        this.caseOccupee = false;
        this.nourriture  = null;

    }


    /* ************************************************************************************************************ **
     *                                         G E T T E R    &    S E T T E R                                       *
     * ************************************************************************************************************ **/

    public boolean getCaseOccupee(){
        return this.caseOccupee;
    }

    public void setCaseOccupee(boolean caseOccupee) {
        this.caseOccupee = caseOccupee;
    }

    public Nourriture getNourriture(){
        return this.nourriture;
    }

    public void setNourriture(Nourriture nourriture) {
        this.nourriture = nourriture;
    }


}
