package symulacja;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;

/**
 * Klasa reprezentujaca zdarzenie obslugi petenta
 * 
 * @author Kachat j.W.
 */
public class KoniecObslugiEvent extends Event<Okienko> {
	/**
	 * Konstruktor, wywolujacy konstruktor klasy nadrzednej
	 * 
	 * @param wlasciciel - model glowny
	 * @param nazwa - nazwa zdarzenia
	 * @param pokazTrace - czy zdarzenie ma byc sledzone
	 */
	public KoniecObslugiEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);
	}
    /**
     * Rutynowe dzialanie na obsluzenie petenta
     * 
     * @param okno - okienko wydzialu do ktorego podszedl petent
     */
	public void eventRoutine(Okienko okno) {
		Dziekanat mojModel = (Dziekanat)getModel();
		
		okno.getAktualnyStudent().wyslijTrace("Odszedlem o:" + presentTime());
		okno.getAktualnyStudent().zamknijTrace();
		
		okno.wyslijTrace("Petetnt: " + okno.getAktualnyStudent().getId() + " odszedl");
		okno.setAktualnyStudent(null);

		//sprawdzenie, czy kolejejka do petentow do wydzialu tego okna jest nie pusta
		//jesli nie jest: uruchomienie procedury wywolywanie petenta; jesli jest to dodanie okna do oczekujacych okienek
		if (!mojModel.getPetentKolejkaDoKierunku(okno.getKierunek()).isEmpty())
		{
			
			okno.setAktualnyStudent(mojModel.getPetentKolejkaDoKierunku(okno.getKierunek()).first());
			WywolanieStudentaEvent event = new WywolanieStudentaEvent(mojModel, "Wywolanie petenta do " + mojModel.getWolneOkienkaKierunku(okno.getKierunek()).getName(), true);		
			event.schedule(okno, new SimTime(0.0));
		}
		else {
			mojModel.getWolneOkienkaKierunku(okno.getKierunek()).insert(okno);
		}

	}
}