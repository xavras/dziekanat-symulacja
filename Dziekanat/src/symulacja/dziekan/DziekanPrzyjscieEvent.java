/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symulacja.dziekan;

import symulacja.student_do_dziekana.StudentDoDziekana;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;
import symulacja.Dziekanat;

/**
 *
 * @author lukasz
 */
public class DziekanPrzyjscieEvent extends Event<Dziekan>{
    
    public DziekanPrzyjscieEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);		
	}
    
    @Override
    public void eventRoutine(Dziekan dziekan) {
        Dziekanat model = (Dziekanat) this.getModel();
        
        dziekan.wyslijTrace("Zaczynam prace");
        
        dziekan.setObecny(true);
        dziekan.setZajety(false);
        
        if(!model.kolejkaDziekan.isEmpty())
        {
            dziekan.setZajety(true);
            DziekanObslugaPoczatekEvent dziekanObsluga = 
                    new DziekanObslugaPoczatekEvent(getModel(), getModel().getName(), traceIsOn());
            dziekanObsluga.schedule(model.dziekan, new SimTime(StudentDoDziekana.czasPodchodzenia));
        }
        //else if(jest cos do podpisania) uruchom podpisywanie event
        
        DziekanWyjscieEvent dziekanWyjscie = 
                    new DziekanWyjscieEvent(getModel(), getModel().getName(), traceIsOn());
        dziekanWyjscie.schedule(model.dziekan, 
                    new SimTime(Dziekan.godzinaZakonczenia-Dziekan.godzinaRozpoczecia));
    }
}
