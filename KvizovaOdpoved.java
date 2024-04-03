/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smidovaprojekt4;

/**
 *
 * @author smido
 */
public class KvizovaOdpoved {
    public String odpoved; // uchovava odpoved
    public boolean jeSpravne; // urcuje, jestli je odpoved spravna
    
    public void vypis() { // vypise odpoved
        if (jeSpravne) {
            System.out.println("+" + odpoved);
        } else {
            System.out.println("-" + odpoved);
        }
    }
}
