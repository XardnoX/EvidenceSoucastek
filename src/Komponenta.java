import java.time.LocalDate;

public class Komponenta {
    private String popis;
    private int doba;
    private String jeNova;
    private String stav;
    private double cena;
    private LocalDate datum;

    public Komponenta(String popis, int doba, String jeNova, String stav, double cena, LocalDate datum) {
        this.popis = popis;
        this.doba = doba;
        this.jeNova = jeNova;
        this.stav = stav;
        this.cena = cena;
        this.datum = datum;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public int getDoba() {
        return doba;
    }

    public void setDoba(int doba) {
        this.doba = doba;
    }

    public String getJeNova() {
        return jeNova;
    }

    public void setJeNova(String jeNova) {
        this.jeNova = jeNova;
    }

    public String getStav() {
        return stav;
    }

    public void setStav(String stav) {
        this.stav = stav;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }
}
