import javax.swing.*;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class Gui extends JFrame {
    private JPanel panel;
    private JTextArea popis;
    private JButton addButton;
    private JButton backButton;
    private JButton nextButton;
    private JButton saveButton;
    private JCheckBox newBox;
    private JRadioButton vyborneRB;
    private JRadioButton dobreRB;
    private JRadioButton pouziteRB;
    private JTextArea pocetDnu;
    private JTextArea cena;
    private JTextArea datum;
    private JFileChooser jFileChooser = new JFileChooser(".");
    public static List<Komponenta> listData = new ArrayList<>();
    public static List<BigDecimal> numberList = new LinkedList<>();
    private Komponenta current;

    public static void main(String[] args) {
        new Gui();
    }

    public Gui() {

        JMenu jMenu = new JMenu("Soubor");
        JMenu stats = new JMenu("Statistiky");
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.add(jMenu);
        jMenuBar.add(stats);
        setJMenuBar(jMenuBar);
        JMenuItem nacti = new JMenuItem("Načti");
        nacti.addActionListener(e -> loadData());
        JMenuItem uloz = new JMenuItem("Ulož");
        uloz.addActionListener(e -> saved());
        JMenuItem statisky = new JMenuItem("Celková cena");
        statisky.addActionListener(e -> stats());
        stats.add(statisky);
        jMenu.add(uloz);
        jMenu.add(nacti);
        saveButton.addActionListener(e -> saved());
        addButton.addActionListener(e -> refresh());
        backButton.addActionListener(e -> backKomponenta());
        nextButton.addActionListener(e -> nextKomponenta());


        setContentPane(panel);
        setBounds(800, 350, 500, 500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public List<Komponenta> scan(File file) {
        List<Komponenta> listData = new ArrayList<>();
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)))) {
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                int pocetDnu = Integer.parseInt(data[1]);
                BigDecimal cena = new BigDecimal(data[4]);
                LocalDate datum = LocalDate.parse(data[5]);
                listData.add(new Komponenta(data[0], pocetDnu, data[2], data[3], cena, datum));
                numberList.add(cena);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Soubor nelze přečíst!");

        }
        return listData;
    }

    private List<Komponenta> loadData() {
        int result = jFileChooser.showOpenDialog(this);
        if (result == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(null, "špatný typ souboru!");
        }
        listData = scan(jFileChooser.getSelectedFile());
        Komponenta komponenta = listData.get(0);
        showData(komponenta);
        return listData;
    }

    private void showData(Komponenta komponenta) {
        this.current = komponenta;
        datum.append("datum " + komponenta.getDatum());
        cena.append("cena " + komponenta.getCena() + " Kč");
        pocetDnu.append("dodací doba " + komponenta.getDoba() + " dnů");
        popis.append(komponenta.getPopis());
        if (Objects.equals(komponenta.getJeNova(), "ano")) {
            newBox.setSelected(true);
        } else if (Objects.equals(komponenta.getJeNova(), "ne")) {
            newBox.setSelected(false);
        } else {
            JOptionPane.showMessageDialog(null, "Špatně zadaná hodnota u jeNova");
        }
        if (Objects.equals(komponenta.getStav(), "výborné")) vyborneRB.setSelected(true);
        else if (Objects.equals(komponenta.getStav(), "dobré")) dobreRB.setSelected(true);
        else if (Objects.equals(komponenta.getStav(), "použité")) pouziteRB.setSelected(true);
        else {
            JOptionPane.showMessageDialog(null, "Špatně zadaný stav komponenty!");
        }
    }


    private void nextKomponenta() {
        refresh();
        Komponenta komponenta;
        if (listData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "soubor je prázdný!");
        }
        try {
            komponenta = listData.get(listData.indexOf(current) + 1);
            showData(komponenta);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "není zde žádná další komponenta!");
        }
    }

    private void backKomponenta() {
        refresh();
        Komponenta komponenta;
        if (listData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Soubor je prázdný!");
        }
        if (current == null) {
            JOptionPane.showMessageDialog(this, "není zde žádná předešlá komponenta!");
        }
        try {
            komponenta = listData.get(listData.indexOf(current) - 1);
            showData(komponenta);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "není zde žádná předešlá komponenta!");
        }

    }

    private void saved() {
        save();
        Komponenta komponenta;
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter((jFileChooser.getSelectedFile()))))) {
            listData.forEach(current -> {
                writer.println(current.getPopis() + "," + current.getDoba() + "," + current.getJeNova() + "," + current.getStav() + "," + current.getCena() + "," + current.getDatum());
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void save() {
        try {
            String stav = "";
            if (dobreRB.isSelected()) {
                stav = "dobré";
            } else if (vyborneRB.isSelected()) {
                stav = "výborné";
            } else if (pouziteRB.isSelected()) {
                stav = "použité";
            }

            String jeNova = newBox.isSelected() ? "ano" : "ne";
            String popisValue = popis.getText();
            BigDecimal cenaValue = new BigDecimal(cena.getText());
            int pocetDnuValue = Integer.parseInt(pocetDnu.getText());
            LocalDate datumValue = LocalDate.parse(datum.getText());

            Komponenta komponenta = new Komponenta(popisValue, pocetDnuValue, jeNova, stav, cenaValue, datumValue);
            listData.add(komponenta);
            numberList.add(cenaValue);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "jedna z hodnot je špatně zadána!");
        }
    }

    private void stats() {

        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal amt : numberList) {
            sum = sum.add(amt);
        }
        JOptionPane.showMessageDialog(this, sum);
    }


    private void refresh() {
        dobreRB.setSelected(false);
        vyborneRB.setSelected(false);
        pouziteRB.setSelected(false);
        newBox.setSelected(false);
        popis.setText("");
        cena.setText("");
        datum.setText("");
        pocetDnu.setText("");
    }
}

