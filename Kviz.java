/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smidovaprojekt4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import static java.lang.Integer.max;
import static java.lang.Integer.min;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author smido
 */
public class Kviz extends javax.swing.JFrame {

    ArrayList<BodovaHranice> vicHranic; // jednotlive hranice
    ArrayList<KvizovaOtazka> otazky; // jednotlive kvizove otazky
    KvizovaOtazka otazka_data; // aktualne zobrazovana otazka
    int index = 0; // aktualni poloha uzivatele v kvizu
    int bodyInt = 0; // uklada uzivateluv dosazeny pocet bodu
    int celkemBodu = 0; // uklada celkovy pocet bodu

    /**
     * Creates new form Kviz
     */
    public Kviz() {
        initComponents();
        otazky = new ArrayList<KvizovaOtazka>(); // vytvori prazde pole otazek
        vicHranic = new ArrayList<BodovaHranice>(); // vytvori prazdne pole hranic
    }

    public void zobraz() {
        otazka_data = otazky.get(index); // nacita aktualni otazku
        otazka.setText(otazka_data.otazka); // zobrazi jmeno otazky
        pole.setText(""); // vycisti pole
        if (otazka_data.jeTextova) { // pokud je otazka otevrena, zobrazi textove pole
            pole.setEnabled(true);
            odpovedi.setEnabled(false);
            odpovedi.setListData(new String[0]);
        } else { // pokud se jedna o uzavrenou otazku, skryje textove pole a zobrazi dane moznosti
            odpovedi.setEnabled(true);
            pole.setEnabled(false);
            odpovedi.setListData(otazka_data.odpovediArray());
        }
    }

    public boolean spravnaVybrana() { 
        if (otazka_data.jeTextova) {
            return pole.getText().equalsIgnoreCase(otazka_data.odpovedi.get(0).odpoved);// porovnava uzivatelovu odpoved se spravnou odpovedi, nehledi na velka nebo mala pismena
        } else { // zjisti, kterou odpoved uzivatel vybral a jestli je spravna, take reaguje pokud uzivatel nic nevybral
            int vyber = odpovedi.getSelectedIndex();
            // kdyz neni nic vybrano
            if (vyber == -1) {
                return false;
            }
            return otazka_data.odpovedi.get(vyber).jeSpravne;
        }
    }

    public boolean nacti() {
        JOptionPane.showMessageDialog(
                new JFrame(), "Nacitam soubor", "Dialog",
                JOptionPane.INFORMATION_MESSAGE);
        var file = new File("D:\\UserData\\Vendy\\Skola\\GVID\\ivt\\javajive\\projekt-kvinta\\SmidovaProjekt/kviz.txt");
        Scanner scanner;
        try { // pokousi se otevrit soubor
            scanner = new Scanner(file);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(
                    new JFrame(), "Nenalezen soubor", "Dialog",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (scanner.hasNextLine()) {
            this.setTitle(scanner.nextLine()); // nastavi nazev podle prvniho radku
        } else { // pokud tento radek chybi, upozorni uzivatele
            JOptionPane.showMessageDialog(
                    new JFrame(), "Kviz nema nazev.", "Dialog",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        KvizovaOtazka otazka = null;

        while (scanner.hasNextLine()) { // cte kviz radek po radku
            var line = scanner.nextLine();
            if (line.startsWith("??")) { // nacteni radku s otazkou
                if (otazka != null) {
                    if (otazka.odpovedi.isEmpty()) {
                        JOptionPane.showMessageDialog(
                                new JFrame(), "Otazka nema odpovedi.", "Dialog",
                                JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if (otazka.jeTextova && otazka.odpovedi.size() > 1) {
                        JOptionPane.showMessageDialog(
                                new JFrame(), "Otazka ma moc odpovedi.", "Dialog",
                                JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    Collections.shuffle(otazka.odpovedi); // zamicha poradi odpovedi
                    otazky.add(otazka);
                }
                otazka = new KvizovaOtazka();
                otazka.otazka = line.substring(2);
                otazka.odpovedi = new ArrayList<KvizovaOdpoved>();
            } else if (line.startsWith("?=")) {
                if (otazka != null) {
                    if (otazka.odpovedi.isEmpty()) {
                        JOptionPane.showMessageDialog(
                                new JFrame(), "Otazka nema odpovedi.", "Dialog",
                                JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    if (otazka.jeTextova && otazka.odpovedi.size() > 1) {
                        JOptionPane.showMessageDialog(
                                new JFrame(), "Otazka ma moc odpovedi.", "Dialog",
                                JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    Collections.shuffle(otazka.odpovedi);
                    otazky.add(otazka);
                }
                otazka = new KvizovaOtazka();
                otazka.otazka = line.substring(2);
                otazka.odpovedi = new ArrayList<KvizovaOdpoved>();
                otazka.jeTextova = true;
            } else if (line.startsWith("#")) {
                if (otazka == null) {
                    System.out.println("CHYBA! Neni zapocata zadna otazka.");
                    JOptionPane.showMessageDialog(
                            new JFrame(), "Neni zapocata zadna otazka.", "Dialog",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                try {
                    otazka.body = min(Integer.parseInt(line.substring(1)), 1); // ochrana vstupu
                } catch (NumberFormatException ex) {
                    System.out.println("CHYBA! Body otazky nejsou cislo.");
                    JOptionPane.showMessageDialog(
                            new JFrame(), "Body otazky nejsou cislo.", "Dialog",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                celkemBodu += otazka.body;
            } else if (line.startsWith("+")) {
                if (otazka == null) {
                    System.out.println("CHYBA! Odpoved bez otazky.");
                    JOptionPane.showMessageDialog(
                            new JFrame(), "Odpoved bez otazky.", "Dialog",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                var odpoved = new KvizovaOdpoved();
                odpoved.jeSpravne = true;
                odpoved.odpoved = line.substring(1);;
                otazka.odpovedi.add(odpoved);
            } else if (line.startsWith("-")) {
                if (otazka == null) {
                    System.out.println("CHYBA! Odpoved bez otazky.");
                    JOptionPane.showMessageDialog(
                            new JFrame(), "Odpoved bez otazky.", "Dialog",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                var odpoved = new KvizovaOdpoved();
                odpoved.jeSpravne = false;
                odpoved.odpoved = line.substring(1);
                otazka.odpovedi.add(odpoved);
            } else if (line.startsWith("!")) {
                var poziceProcent = line.indexOf("%"); // ziska pozici procent v retezci
                if (poziceProcent == -1) {
                    System.out.println("CHYBA! V bodové hranici není symbol procenta.");
                    JOptionPane.showMessageDialog(
                            new JFrame(), "Bodová hranice bez symbolu procenta.", "Dialog",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                var hranice = new BodovaHranice();
                hranice.hodnoceni = line.substring(poziceProcent + 1);
                try {
                    hranice.hranice = min(max(Integer.parseInt(line.substring(1, poziceProcent)), 0), 100); //pojisteni proti zakernym programatorum, co by chteli zadavat cisla mimo hranice
                } catch (NumberFormatException ex) {
                    System.out.println("CHYBA! Body hranice nejsou cislo.");
                    JOptionPane.showMessageDialog(
                            new JFrame(), "Body hranice nejsou cislo.", "Dialog",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                vicHranic.add(hranice);
            }
        }
        if (otazka == null) {
            System.out.println("CHYBA! Kviz nema zadne otazky!");
            JOptionPane.showMessageDialog(
                    new JFrame(), "Kviz nema zadne otazky!", "Dialog",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (otazka.odpovedi.isEmpty()) {
            System.out.println("CHYBA! Otazka nema odpovedi.");
            JOptionPane.showMessageDialog(
                    new JFrame(), "Otazka nema odpovedi.", "Dialog",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (otazka.jeTextova && otazka.odpovedi.size() > 1) {
            JOptionPane.showMessageDialog(
                    new JFrame(), "Otazka ma moc odpovedi.", "Dialog",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        Collections.shuffle(otazka.odpovedi); // prehazi poradi odpovedi
        otazky.add(otazka);
        Collections.shuffle(otazky); // prehazi poradi otazek
        Collections.sort(vicHranic, new Comparator<BodovaHranice>() { // seřadí BodoveHranice podle bodove hranice
            public int compare(BodovaHranice o1, BodovaHranice o2) {
                return Integer.compare(o1.hranice, o2.hranice); // vrátí porovnání dvou hranic
            }
        });
        for (int i = 0; i < vicHranic.size(); i++) {
            vicHranic.get(i).vypis();
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        otazka = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        odpovedi = new javax.swing.JList<>();
        Ok = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        body = new javax.swing.JLabel();
        pole = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1980, 1024));

        otazka.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        otazka.setText("jLabel1");
        otazka.setToolTipText("");
        otazka.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        odpovedi.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        odpovedi.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(odpovedi);

        Ok.setText("Ok");
        Ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OkActionPerformed(evt);
            }
        });

        jLabel1.setText("Body:");

        body.setText("0");

        pole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                poleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(otazka, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(body)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 468, Short.MAX_VALUE)
                        .addComponent(Ok, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pole, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(otazka, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(pole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Ok)
                    .addComponent(jLabel1)
                    .addComponent(body))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OkActionPerformed
        // TODO add your handling code here:
        if (odpovedi.getSelectedIndex() == -1 && !otazka_data.jeTextova) { // resi, jestli je vybrana nejaka odpoved
            JOptionPane.showMessageDialog(
                    new JFrame(), "Neni vybrana odpoved!", "Dialog",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (otazka_data.jeTextova && pole.getText().equals("")) { // resi, jestli je vypsana nejaka odpoved
            JOptionPane.showMessageDialog(
                    new JFrame(), "Neni zadana odpoved!", "Dialog",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (this.spravnaVybrana()) { // kontroluje spravnost odpovedi
            JOptionPane.showMessageDialog(
                    new JFrame(), "Spravna odpoved!", "Dialog",
                    JOptionPane.INFORMATION_MESSAGE);
            bodyInt += otazka_data.body;
            body.setText(Integer.toString(bodyInt));
        } else {
            JOptionPane.showMessageDialog(
                    new JFrame(), "Spatna odpoved!", "Dialog",
                    JOptionPane.ERROR_MESSAGE);
        }
        if (index < otazky.size() - 1) {
            index += 1;
            this.zobraz();
        } else {
            this.setVisible(false); //skryje kvizove okno
            var dosazeneProcento = (int) ((float) bodyInt / (float) celkemBodu * 100);
            JOptionPane.showMessageDialog(
                    new JFrame(), "Hotovo! Máte " + bodyInt + " bodů. Neboli " + dosazeneProcento + "%.", "Dialog", // vypise pocet bodu
                    JOptionPane.INFORMATION_MESSAGE);
            if (vicHranic.size() < 1) {
                if (dosazeneProcento <= 33) {
                    JOptionPane.showMessageDialog(new JFrame(), "Hodnoceni: No moc ses nepředved!");
                } else if (dosazeneProcento <= 66) {
                    JOptionPane.showMessageDialog(new JFrame(), "Hodnoceni: Celkem se orientuješ, ale mohlo by to být lepší!");
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Hodnoceni: Jde vidět, že jsi na Mafii vyrůstal. Pan Salieri by tě určitě přivítal do rodiny!");
                }
            } else {
                for (int i = 0; i < vicHranic.size(); i++) {
                    if (vicHranic.get(i).hranice >= dosazeneProcento) {
                        JOptionPane.showMessageDialog(new JFrame(), "Hodnoceni: " + vicHranic.get(i).hodnoceni);
                        break;
                    }
                }

            }
            System.exit(0);
        }
    }//GEN-LAST:event_OkActionPerformed

    private void poleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_poleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_poleActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Kviz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Kviz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Kviz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Kviz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Hudba musicObject = new Hudba("D:\\UserData\\Vendy\\Skola\\GVID\\ivt\\javajive\\projekt-kvinta\\SmidovaProjekt\\hudba.wav");
                musicObject.pustHudbu(); // zavola funkci spust hudbu
                var okno = new Kviz();
                if (!okno.nacti()) {
                    System.exit(0);
                } else {
                    okno.zobraz();
                }
                okno.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Ok;
    private javax.swing.JLabel body;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> odpovedi;
    private javax.swing.JLabel otazka;
    private javax.swing.JTextField pole;
    // End of variables declaration//GEN-END:variables
}
