/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symulacja.dziekan;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;
import symulacja.Dziekanat;

/**
 *
 * @author lukasz
 */
public class StudentDoDziekanaPrzyjscieEvent extends Event<StudentDoDziekana>{

    public StudentDoDziekanaPrzyjscieEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);		
	}
    
    @Override
    public void eventRoutine(StudentDoDziekana student) {
        Dziekanat mojModel = (Dziekanat)getModel();
        
        student.wyslijTrace("Przyszedlem do dziekana");
                    
        if(mojModel.kolejkaDziekan.isEmpty() && mojModel.dziekan.isObecny() 
                && !mojModel.dziekan.isZajety())//nikogo nie ma w kolejce, wymuszenie obslugi
        {
            DziekanObslugaEvent dziekanObsluga = 
                    new DziekanObslugaEvent(getModel(), getModel().getName(), traceIsOn());
            dziekanObsluga.schedule(mojModel.dziekan, new SimTime(StudentDoDziekana.czasPodchodzenia/60.0));
            student.wyslijTrace("Wszedlem od razu");
        }
        
        mojModel.kolejkaDziekan.insert(student);
        student.wyslijTrace("Musze poczekac");
    }
    
}
