/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symulacja.student_do_dziekana;
import java.util.Random;
import desmoj.core.report.HTMLTraceOutput;
import desmoj.core.report.TraceNote;
import desmoj.core.simulator.*;
import java.io.File;
import symulacja.Dziekanat;
import symulacja.dziekan.Dziekan;
/**
 *
 * @author lukasz
 */
public class StudentDoDziekana extends Entity{

    private int dlugoscKolejkiTolerancja; //dlugosc kolejki, jaka jest w stanie zniesc (w osobach)
    private int czasTolerancji; //czas jaki student jest w stanie czekac zanim sie zniecierpliwi
    public static int licznikStudentow = 0;
    private int id;
    public static double czasPodchodzenia = 0.5;
    public static double czasGeneracji = 4.0;
    private double czasPrzyjscia;
    
    protected HTMLTraceOutput trace = new HTMLTraceOutput();
    
    public StudentDoDziekana(Model wlasciciel, String nazwa, boolean pokazTrace)
    {
        super(wlasciciel, nazwa, pokazTrace);
        
        Random random = new Random();
        dlugoscKolejkiTolerancja = random.nextInt(10)+5;
        czasTolerancji = random.nextInt(30)+5;//w minutach
        id = licznikStudentow++;
        
        new File("traces/StudenciDziekan").mkdir();
	trace.open("traces/StudenciDziekan", "StudentDziekan-" + id);
        ((Dziekanat)wlasciciel).traces.add(trace);
    }
    
    /**
    * Wysyla Stringa z aktualnym czasem do trace'a, o sciezce zdefiniowanej w folderze "/treces/Studenci/Student-id"
    * 
    * @param note - String, ktory zostanie zapisany do trace'a
    */
    public void wyslijTrace(String note)
    {
        note = ""+((Dziekanat)getModel()).czasTeraz()+"> "+note;
        trace.receive(new TraceNote(getModel(), note, presentTime(), this, null));
    }
    /**
    * Zamyka aktywnego trace'a
    */
    public void zamknijTrace(){
           trace.close();
    }

    public int getDlugoscKolejkiTolerancja() {
        return dlugoscKolejkiTolerancja;
    }

    public int getCzasTolerancji() {
        return czasTolerancji;
    }

    public int getId() {
        return id;
    }
    
    public double getScheduleKolejnegoDnia()
    {
        Random random = new Random();
        double godzTeraz = ((Dziekanat)getModel()).godzinaTeraz();
        double doKoncaDnia = Dziekanat.godzinaZamkniecia - godzTeraz;
        double czasPracy = Dziekan.godzinaZakonczenia - Dziekanat.godzinaOtwarcia;
        //mozna przyjsc wczesniej i czekac na dziekana
        
        double losowaGodzina = random.nextDouble()*czasPracy;

        return (losowaGodzina + doKoncaDnia + 0.5)*60.0;
        //dodaję 0.5 przerwy między dniami
    }

    public double getCzasPrzyjscia() {
        return czasPrzyjscia;
    }

    public void setCzasPrzyjscia(double czasPrzyjscia) {
        this.czasPrzyjscia = czasPrzyjscia;
    }
    
    public void zwiekszDeterminacje()
    {
        dlugoscKolejkiTolerancja+=5;
        czasTolerancji+=10;
    }
}
