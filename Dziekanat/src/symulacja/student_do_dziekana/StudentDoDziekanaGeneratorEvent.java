/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symulacja.student_do_dziekana;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;
import symulacja.Dziekanat;

/**
 *
 * @author lukasz
 */
public class StudentDoDziekanaGeneratorEvent extends Event{

    public StudentDoDziekanaGeneratorEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);		
	}
    
    @Override
    public void eventRoutine(Entity e) {
        Dziekanat model = (Dziekanat)getModel();
        StudentDoDziekana student= new StudentDoDziekana(model, "StudentDoDziekana", true);
        student.wyslijTrace("Pojawilem sie na swiecie");
        
        if(model.dziekan.isObecny())
        {
            StudentDoDziekanaPrzyjscieEvent event = 
                    new StudentDoDziekanaPrzyjscieEvent(getModel(), getModel().getName(), traceIsOn());
            event.schedule(student, new SimTime(StudentDoDziekana.czasPodchodzenia/60.0));
            
            e.schedule(this, new SimTime(StudentDoDziekana.czasGeneracji/60.0));
        }
        else
        {
            student.wyslijTrace("Chyba sie spoznilem...");
        }
    }
    
}
