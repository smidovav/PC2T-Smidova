
package smidovaprojekt4;

public class KvizovaOdpoved {
    public String odpoved;
    public boolean jeSpravne;
    
    public void vypis() {
        if (jeSpravne) {
            System.out.println("+" + odpoved);
        } else {
            System.out.println("-" + odpoved);
        }
    }
}
