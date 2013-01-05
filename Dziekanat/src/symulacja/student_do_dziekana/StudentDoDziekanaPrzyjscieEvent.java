/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symulacja.student_do_dziekana;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;
import symulacja.Dziekanat;
import symulacja.dziekan.DziekanObslugaPoczatekEvent;

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
        mojModel.kolejkaDziekan.insert(student);
                    
        if(mojModel.kolejkaDziekan.isEmpty() && mojModel.dziekan.isObecny() 
                && !mojModel.dziekan.isZajety())//nikogo nie ma w kolejce, wymuszenie obslugi
        {
            DziekanObslugaPoczatekEvent dziekanObsluga = 
                    new DziekanObslugaPoczatekEvent(getModel(), getModel().getName(), traceIsOn());
            dziekanObsluga.schedule(mojModel.dziekan, new SimTime(StudentDoDziekana.czasPodchodzenia));
            student.wyslijTrace("Wszedlem od razu, bez kolejki");
        }
        else
        {
            student.wyslijTrace("Musze poczekac. Dlugosc kolejki: "
                    +((Dziekanat)getModel()).kolejkaDziekan.length());
        }
    }
    
}
