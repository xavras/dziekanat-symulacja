/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symulacja.dziekan;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;

/**
 *
 * @author lukasz
 */
public class DziekanPodpisPoczatekEvent extends Event<Dziekan>{

    public DziekanPodpisPoczatekEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);		
	}
    
    @Override
    public void eventRoutine(Dziekan e) {
        e.wyslijTrace("Zaczynam podpisywanie jakiegos podania.");
        
        DziekanPodpisKoniecEvent event =
                new DziekanPodpisKoniecEvent(getModel(), getModel().getName(), true);
        event.schedule(e, new SimTime(Dziekan.czasPodpisywania));
    }
    
}
