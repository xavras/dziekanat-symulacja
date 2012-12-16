/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symulacja.dziekan;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;

/**
 *
 * @author lukasz
 */
public class DziekanObslugaEvent extends Event<Dziekan>{
    
    public DziekanObslugaEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);		
	}

    @Override
    public void eventRoutine(Dziekan dziekan) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
