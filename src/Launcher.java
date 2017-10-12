import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Launcher extends JPanel implements Runnable, MouseListener {

    private Thread       animator;
    private PigeonSquare pigeonSquare;
    private final int    DELAY = 50;
    private BufferedImage imagePigeon;
    private BufferedImage imageNourritureFraiche;
    private BufferedImage imageNourriturePasFraiche;
    private BufferedImage imageDisperseur;

    public Launcher(){
        this.pigeonSquare = new PigeonSquare();
        int taille = PigeonSquare.taille;
        try {
            System.out.println(new File("").getAbsolutePath());
            this.imagePigeon = ImageIO.read(new File("poule.png"));
            this.imageNourritureFraiche =  ImageIO.read(new File("nourritureFraiche.png"));
            this.imageNourriturePasFraiche =  ImageIO.read(new File("nourriturePasFraiche.png"));
            this.imageDisperseur = ImageIO.read(new File("disperseur.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.addMouseListener(this);

        JFrame fenetre = new JFrame();
        fenetre.setTitle("PigeonSquare");
        fenetre.setSize(taille, taille);
        fenetre.setResizable(false);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.add(this);
        fenetre.setVisible(true);
    }


    @Override
    public void addNotify() {
        super.addNotify();
        animator = new Thread(this);
        animator.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        setBackground(new Color(102,191,67));
        // Affichage des pigeons
        for(Pigeon p : this.pigeonSquare.getPigeons()){
            // On centre le pigeon, son image fait 25px*24px
            g2d.drawImage(this.imagePigeon,p.getPosColonne()-12,p.getPosLigne()-12,null);
        }
        // Affichages des nourritures
        for(int[] elt : this.pigeonSquare.getTerrain().trouverTouteNourriture()){
            try {
                if (this.pigeonSquare.getTerrain().getCases()[elt[0]][elt[1]].getNourriture().estFraiche()) {
                    g2d.drawImage(this.imageNourritureFraiche, elt[1]-5, elt[0]-5, null);
                } else {
                    g2d.drawImage(this.imageNourriturePasFraiche, elt[1]-5, elt[0]-5, null);
                }
            } catch (NullPointerException e){}
        }
        // Affichage des disperseurs
        for(Disperseur d : this.pigeonSquare.getDisperseurs()){
            g2d.drawImage(this.imageDisperseur, d.getPosColonne() - 25, d.getPosLigne() - 25, null);
        }
        g.dispose();
    }


    public static void main(String[] args) {
        new Launcher();
    }

    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();
        while (true) {
            this.pigeonSquare.getTerrain().majNourriture();
            this.pigeonSquare.majDisperseur();
            this.pigeonSquare.disperser();
            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;
            if (sleep < 0)
                sleep = 2;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
            beforeTime = System.currentTimeMillis();
        }
    }

    /* ************************************************************************************************************ **
     *                                 G E S T I O N    D E    L A    S O U R I S                                    *
     * ************************************************************************************************************ **/
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("Click: ligne= "+e.getY()+" colonne = "+e.getX());
        this.pigeonSquare.poserNourriture(e.getY(), e.getX());

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}