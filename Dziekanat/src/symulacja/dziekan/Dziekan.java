/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symulacja.dziekan;

import symulacja.student_do_dziekana.StudentDoDziekana;
import desmoj.core.report.HTMLTraceOutput;
import desmoj.core.report.TraceNote;
import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

/**
 *
 * @author lukasz
 */
public class Dziekan extends Entity{
    
    public StudentDoDziekana aktualnyStudent;
    public static double godzinaRozpoczecia = 10.0;
    public static double godzinaZakonczenia = 40.0;
    private boolean zajety;
    private boolean obecny;
    //public Queue<Podania> listaPodan;
    public static double czasObslugi = 3.0;
    public static double czasPodpisywania = 0.3;
    
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

    public boolean isZajety() {
        return zajety;
    }

    public boolean isObecny() {
        return obecny;
    }

    public void setZajety(boolean zajety) {
        this.zajety = zajety;
    }

    public void setObecny(boolean obecny) {
        this.obecny = obecny;
    }
}
