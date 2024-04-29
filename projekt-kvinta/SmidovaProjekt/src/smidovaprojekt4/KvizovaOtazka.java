/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smidovaprojekt4;

import java.util.ArrayList;

/**
 *
 * @author smido
 */
public class KvizovaOtazka {
    public String otazka; // nazev otazky
    public ArrayList<KvizovaOdpoved> odpovedi; // kolekce odpovedi
    public int body; // kolik bodu je za danou otazku dle vstupniho souboru
    public boolean jeTextova = false; // rozhoduje, jestli je otazka otevrena nebo uzavrena
    
    public void vypis() { // vypise otazku tak, jak je ve vstupnim souboru
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
    
    public String[] odpovediArray(){ // prizpusobeni pro GUI
        String arr[] = new String[odpovedi.size()];
        for (int i = 0; i < odpovedi.size(); i++) {
            arr[i] = odpovedi.get(i).odpoved;
        }
        return arr;
    }
}
