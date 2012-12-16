/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symulacja.dziekan;
import java.util.Random;
import desmoj.core.report.HTMLTraceOutput;
import desmoj.core.report.TraceNote;
import desmoj.core.simulator.*;
import java.io.File;
/**
 *
 * @author lukasz
 */
public class StudentDoDziekana extends Entity{

    private int dlugoscKolejkiTolerancja; //dlugosc kolejki, jaka jest w stanie zniesc (w osobach)
    private int czasTolerancji; //czas jaki student jest w stanie czekac zanim sie zniecierpliwi
    public static int licznikStudentow = 0;
    private int id;
    public static double czasPodchodzenia;
    
    protected HTMLTraceOutput trace = new HTMLTraceOutput();
    
    public StudentDoDziekana(Model wlasciciel, String nazwa, boolean pokazTrace)
    {
        super(wlasciciel, nazwa, pokazTrace);
        
        Random random = new Random();
        dlugoscKolejkiTolerancja = random.nextInt(10);
        czasTolerancji = random.nextInt(30);//w minutach
        id = licznikStudentow++;
        
        new File("traces/Studenci").mkdir();
	trace.open("traces/Studenci", "StudentDziekan-" + id);
    }
    
    /**
    * Wysyla Stringa z aktualnym czasem do trace'a, o sciezce zdefiniowanej w folderze "/treces/Studenci/Student-id"
    * 
    * @param note - String, ktory zostanie zapisany do trace'a
    */
    public void wyslijTrace(String note)
    {
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
    
    
}
