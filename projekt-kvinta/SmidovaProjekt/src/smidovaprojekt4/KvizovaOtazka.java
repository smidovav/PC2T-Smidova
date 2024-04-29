
package smidovaprojekt4;

import java.util.ArrayList;

public class KvizovaOtazka {
    public String otazka;
    public ArrayList<KvizovaOdpoved> odpovedi;
    public int body;
    public boolean jeTextova = false;
    
    public void vypis() {
        if (jeTextova) {
            System.out.println("?=" + otazka);
        } else {
            System.out.println("??" + otazka);
        }
        System.out.println("#" + Integer.toString(body));
        for (int i = 0; i < odpovedi.size(); i++) {
            odpovedi.get(i).vypis();
        }
    }
    
    public String[] odpovediArray(){
        String arr[] = new String[odpovedi.size()];
        for (int i = 0; i < odpovedi.size(); i++) {
            arr[i] = odpovedi.get(i).odpoved;
        }
        return arr;
    }
}
