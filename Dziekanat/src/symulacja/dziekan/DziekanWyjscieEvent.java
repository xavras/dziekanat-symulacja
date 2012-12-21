/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symulacja.dziekan;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;
import symulacja.Dziekanat;
import symulacja.student_do_dziekana.StudentDoDziekana;

/**
 *
 * @author lukasz
 */
public class DziekanWyjscieEvent extends Event<Dziekan>{
    
    public DziekanWyjscieEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);		
	}
    
    @Override
    public void eventRoutine(Dziekan dziekan) {
        Dziekanat model = (Dziekanat) this.getModel();
        
        dziekan.wyslijTrace("Koncze juz prace, za chwile wyjde");
        
        dziekan.setObecny(false);
        
        model.kolejkaDziekan.removeAll();//TODO: żeby każdy ze studentów przyszedl w inny dzien
        //TODO: ustawienie przyjscia dziekana w kolejny dzień
    }
}

