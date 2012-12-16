package symulacja;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;


public class SprawyPozastudenckieGeneratorEvent extends ExternalEvent {
	/**
	 * Konstruktor, wywolujacy konstruktor klasy nadrzednej
	 * 
	 * @param wlasciciel - model glowny
	 * @param nazwa - nazwa zdarzenia
	 * @param pokazTrace - czy zdarzenie ma byc sledzone
	 */
	public SprawyPozastudenckieGeneratorEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);
	}
    /**
     * Rutynowe dzialanie generowanie petenta
     * 
     */
	public void eventRoutine() {
		Dziekanat mojModel = (Dziekanat)getModel();
		
		mojModel.dodajSprawe(new SprawyPozastudenckie(mojModel, "SprawaPozastudencka", true));
		schedule(new SimTime(mojModel.getCzasNowejSprawyPozastudenckiej()));
	}
}
