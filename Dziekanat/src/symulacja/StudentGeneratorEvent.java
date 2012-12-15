package symulacja;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;

/**
 * Klasa generujaca petentow
 * 
 * @author Kachat j.W.
 */
public class StudentGeneratorEvent extends ExternalEvent {
	/**
	 * Konstruktor, wywolujacy konstruktor klasy nadrzednej
	 * 
	 * @param wlasciciel - model glowny
	 * @param nazwa - nazwa zdarzenia
	 * @param pokazTrace - czy zdarzenie ma byc sledzone
	 */
	public StudentGeneratorEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);
	}
    /**
     * Rutynowe dzialanie generowanie petenta
     * 
     */
	public void eventRoutine() {
		Dziekanat mojModel = (Dziekanat)getModel();
		
		mojModel.getPodajnikBloczkow().dodajStudenta(new Student(mojModel, "Student", true));
		schedule(new SimTime(mojModel.getCzasPrzybyciaStudenta()));
	}
}
