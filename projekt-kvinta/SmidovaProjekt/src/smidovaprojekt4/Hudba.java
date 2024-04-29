/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smidovaprojekt4;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author smido
 */
public class Hudba {

    private Clip clip = null;

    public Hudba(String cesta) { // pri vytvoreni tridy se pokusi nacist soubor s hudbou
        try {
            File hudbaCesta = new File(cesta);
            if (hudbaCesta.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(hudbaCesta); // nacte soubor s hudbou
                clip = AudioSystem.getClip(); // ziska systemovy prehravac
                clip.open(audioInput); // nacte hudbu do prehravace
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
