/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symulacja.dziekan;

import symulacja.student_do_dziekana.StudentDoDziekana;
import symulacja.student_do_dziekana.StudentDoDziekanaPrzyjscieEvent;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;
import java.util.Random;
import symulacja.Dziekanat;

/**
 *
 * @author lukasz
 */
public class DziekanObslugaPoczatekEvent extends Event<Dziekan>{
    
    public DziekanObslugaPoczatekEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);		
	}

    @Override
    public void eventRoutine(Dziekan dziekan) {
        Dziekanat model = (Dziekanat) this.getModel();

        dziekan.aktualnyStudent = model.kolejkaDziekan.removeFirst();
        if(dziekan.aktualnyStudent == null)
        {
            dziekan.wyslijTrace("Jakis Blad, student jest pusty.");
            return;
        }
        dziekan.wyslijTrace("Zaczynam obsluge studenta "+dziekan.aktualnyStudent.getId());
        dziekan.aktualnyStudent.wyslijTrace("Jestem obslugiwany przez Dziekana"); 

        DziekanObslugaKoniecEvent event =
                new DziekanObslugaKoniecEvent(model, getModel().getName(), true);
        event.schedule(dziekan, new SimTime(Dziekan.czasObslugi));

    }
}
