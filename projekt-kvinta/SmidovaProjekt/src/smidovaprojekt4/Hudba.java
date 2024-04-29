
package smidovaprojekt4;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Hudba {

    private Clip clip = null;

    public Hudba(String cesta) {
        try {
            File hudbaCesta = new File(cesta);
            if (hudbaCesta.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(hudbaCesta);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Soubor s hudbou nenalezen!", "Dialog", JOptionPane.ERROR_MESSAGE);
                System.out.println("Zde není soubor s hudbou!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(new JFrame(), "Nepodařilo se načíst hudbu :'( víc info v konzoli.", "Dialog", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            clip = null;
        }
    }

    void pustHudbu() { // pokud se podarilo nacist hudbu, spusti ji
        if (clip != null) {
            try {
                clip.start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
