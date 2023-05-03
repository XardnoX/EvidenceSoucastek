import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Gui extends JFrame {
    private JPanel panel;
    private JTextArea text;
    private JButton addButton;
    private JButton backButton;
    private JButton nextButton;
    private JButton saveButton;
    private JCheckBox newBox;
    private JRadioButton vyborneRB;
    private JRadioButton dobreRB;
    private JRadioButton pouziteRB;
    private JFileChooser jFileChooser = new JFileChooser(".");
    public static List<Komponenta> listData = new ArrayList<>();
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
        JMenuItem statisky = new JMenuItem("Celková cena");
        stats.add(statisky);
        jMenu.add(uloz);
        jMenu.add(nacti);


        setContentPane(panel);
        setBounds(800, 350, 500, 500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public List<Komponenta> scan(File file) {
        List<Komponenta> list = new ArrayList<>();
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)))) {
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                int pocetDnu = Integer.parseInt(data[1]);
                Double cena = Double.valueOf(data[4]);
                LocalDate datum = LocalDate.parse(data[5]);
                list.add(new Komponenta(data[0], pocetDnu, data[2], data[3], cena, datum));
                text.append(data[0] + ", dodací doba " + pocetDnu + ", cena " + cena + "Kč , datum " + datum);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Soubor nelze přečíst!");
        }
        return list;
    }

    private List<Komponenta> loadData() {
        int result = jFileChooser.showOpenDialog(this);
        if (result == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(null, "špatný typ souboru!");
            return null;
        }

        listData = scan(jFileChooser.getSelectedFile());
        Komponenta komponenta = listData.get(0);
        checkBox(komponenta);
        radioButtons(komponenta);

        return null;
    }

    private void checkBox(Komponenta komponenta) {
        this.current = komponenta;
        if (komponenta.getJeNova() == "ano") {
            newBox.setSelected(true);
        } else if (komponenta.getJeNova() == "ne") {
            newBox.setSelected(false);
        } else {
            JOptionPane.showMessageDialog(null, "Špatně zadaná hodnota u jeNova");
        }
    }


    private void radioButtons(Komponenta komponenta) {
        this.current = komponenta;
        if (komponenta.getStav() == "výborné") vyborneRB.setSelected(true);
        else if (komponenta.getStav() == "dobré") dobreRB.setSelected(true);
        else if (komponenta.getStav() == "použité") pouziteRB.setSelected(true);
        else {
            JOptionPane.showMessageDialog(null, "Špatně zadaný stav komponenty!");
        }
    }

}

