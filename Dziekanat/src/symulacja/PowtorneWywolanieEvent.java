package symulacja;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;

/**
 * Klasa reprezentujaca powtorne wywolanie petenta przez
 * 
 * @author Kachat j.W.
 */
public class PowtorneWywolanieEvent extends Event<Okienko> {
	/**
	 * Konstruktor, wywolujacy konstruktor klasy nadrzednej
	 * 
	 * @param wlasciciel - model glowny
	 * @param nazwa - nazwa zdarzenia
	 * @param pokazTrace - czy zdarzenie ma byc sledzone
	 */
	public PowtorneWywolanieEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);		
	}
	/**
	 * Rutynowe dzialanie powtornego wywolania petenta
	 * 
     * @param okno - okno, ktore oczekuje petenta
     */
	public void eventRoutine(Okienko okno) {
		Dziekanat mojModel = (Dziekanat)getModel();

        Student p = mojModel.getPetentKolejkaDoKierunku(okno.getKierunek()).first();
        mojModel.getPodajnikBloczkow().wyslijTraceAutomat(okno.getAktualnyStudent().getNumer() + " numer ponownie proszony do okienka: " + okno.getName());    
        
        //sprawdzenie czy petenta zostanie obsluzony
        if (p.getCzasPodchodzeniaStudenta() < 30){
        	StudentPodchodziEvent studentPodchodzi = new StudentPodchodziEvent(mojModel, "Klient podchodzi do " + mojModel.getPetentKolejkaDoKierunku(okno.getKierunek()).getName(), true);
        	studentPodchodzi.schedule(okno, new SimTime((double)((p.getCzasPodchodzeniaStudenta() - 15.0)/60)));
        } else{
        	okno.getAktualnyStudent().wyslijTrace("poszedlem sobie");
        	okno.getAktualnyStudent().zamknijTrace();
        	
        	mojModel.getPetentKolejkaDoKierunku(okno.getKierunek()).removeFirst();
        	okno.setAktualnyStudent(null);
        	
        	//uruchomienie procesu wywolujacego studenta
        	if (!mojModel.getPetentKolejkaDoKierunku(okno.getKierunek()).isEmpty()){	
        		okno.setAktualnyStudent(mojModel.getPetentKolejkaDoKierunku(okno.getKierunek()).first());
        		WywolanieStudentaEvent event = new WywolanieStudentaEvent(mojModel, "Wywolanie petenta do " + mojModel.getWolneOkienkaKierunku(okno.getKierunek()).getName(), true);		
        		event.schedule(okno, new SimTime(0.0));
        	} else{
        		mojModel.getWolneOkienkaKierunku(okno.getKierunek()).insert(okno);
        	}
        }
	}
}


