/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symulacja.dziekan;

import desmoj.core.report.HTMLTraceOutput;
import desmoj.core.report.TraceNote;
import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

/**
 *
 * @author lukasz
 */
public class Dziekan extends Entity{
    
    private StudentDoDziekana aktualnyStudent;
    public static double godzinaRozpoczecia;
    public static double godzinaZakonczenia;
    private boolean zajety;
    private boolean obecny;
    //public Queue<Podania> listaPodan;
    public static double czasObslugi;
    public static double czasPodpisywania;
    
    protected HTMLTraceOutput trace = new HTMLTraceOutput();
    
    public Dziekan(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);		
		aktualnyStudent = null;
                zajety = false;
                obecny = false;
                
                trace.open("traces", "Dziekan");
	}
    
    public void wyslijTrace(String note)
    {
       trace.receive(new TraceNote(getModel(), note, presentTime(), this, null));
    }
    
    public void zamknijTrace(){
           trace.close();
    }

    public StudentDoDziekana getAktualnyStudent() {
        return aktualnyStudent;
    }

    public boolean isZajety() {
        return zajety;
    }

    public boolean isObecny() {
        return obecny;
    }
}
