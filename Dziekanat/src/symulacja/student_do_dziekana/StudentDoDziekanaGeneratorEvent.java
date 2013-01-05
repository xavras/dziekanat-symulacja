/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symulacja.student_do_dziekana;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;
import symulacja.Dziekanat;
import symulacja.dziekan.Dziekan;

/**
 *
 * @author lukasz
 */
public class StudentDoDziekanaGeneratorEvent extends ExternalEvent{

    public StudentDoDziekanaGeneratorEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);		
	}
    
    public void eventRoutine() {
        Dziekanat model = (Dziekanat)getModel();
        
        if(model.godzinaTeraz() <= Dziekan.godzinaZakonczenia)
        {
            StudentDoDziekana student= new StudentDoDziekana(model, "StudentDoDziekana", true);
            student.wyslijTrace("Pojawilem sie na swiecie");
            
            StudentDoDziekanaPrzyjscieEvent event = 
                    new StudentDoDziekanaPrzyjscieEvent(getModel(), getModel().getName(), traceIsOn());
            event.schedule(student, new SimTime(StudentDoDziekana.czasPodchodzenia));
            
            schedule(new SimTime(StudentDoDziekana.czasGeneracji));
        }
        else
        {
            model.dziekan.wyslijTrace("Juz zaden student sie nie wygeneruje, bo dziekan idzie do domu. Koniec Eventa. HA HA.");
            model.dziekan.wyslijTrace("This is what you get, this is what you get, This is what you get when you mess with us. Karma police, I've given all I can, it's not enough, I've given all I can but we're still on the payroll");
        }
    }
    
}
