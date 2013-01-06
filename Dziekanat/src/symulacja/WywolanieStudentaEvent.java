package symulacja;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;

/**
 * Klasa reprezentujaca powtorne wywolanie petenta przez
 * 
 * @author Kachat j.W.
 */
public class WywolanieStudentaEvent extends Event<PracownikDziekanatu> {
	/**
	 * Konstruktor, wywolujacy konstruktor klasy nadrzednej
	 * 
	 * @param wlasciciel - model glowny
	 * @param nazwa - nazwa zdarzenia
	 * @param pokazTrace - czy zdarzenie ma byc sledzone
	 */
	public WywolanieStudentaEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);		
	}
	/**
	* Rutynowe dzialanie na wywolanie petenta
	* 
	* @param okno - okienko wydzialu do ktorego podszedl petent
	*/
        
        
	public void eventRoutine(PracownikDziekanatu okno) {
		Dziekanat mojModel = (Dziekanat)getModel();
		
		Student student = mojModel.getPetentKolejkaDoKierunku(okno.getKierunek()).first();
		mojModel.getPodajnikBloczkow().wyslijTrace(okno.getAktualnyStudent().getNumer() + " numer proszony do okienka: " + okno.getName());        
                okno.setAktualnyStudent(student);
        
                if(!mojModel.otwarty || (student == null))//koniec pracy
                {
                    mojModel.getWolneOkienkaKierunku(okno.getKierunek()).insert(okno);
                    return;
                }
                
                // czy student sobie nie poszedl?
                
                double obecnyCzas = presentTime().getTimeAsDouble();
                double czasPrzybyciaStudenta = student.getCzasPrzybycia();
                
                boolean czyJeszczeJest = (obecnyCzas - czasPrzybyciaStudenta > student.getCzasTolerancjiStudenta()) ? false : true;
                
        // jesli student jeszcze jest (nieprzekroczony zostal czas tolerancji) i ma czas podchodzenia <15sek.        
       	if (student.getCzasPodchodzeniaStudenta() < 15 && czyJeszczeJest == true){
       		StudentPodchodziEvent klientPodchodzi = new StudentPodchodziEvent(mojModel, "Student podchodzi do " + mojModel.getWolneOkienkaKierunku(student.getKierunek()).getName(), true);
       		klientPodchodzi.schedule(okno, new SimTime((double)student.getCzasPodchodzeniaStudenta()/60));
                
        } else{
              
            //test:System.out.println("powtorne wywolanie STUDENT: "+student.getId()+"; tolerancja= "+student.getCzasTolerancjiStudenta());
                
       		PowtorneWywolanieEvent powtorneWywolaniePetenta = new PowtorneWywolanieEvent(mojModel, "Powtorne Wywolanie do " + mojModel.getWolneOkienkaKierunku(student.getKierunek()).getName(), true);
       		powtorneWywolaniePetenta.schedule(okno, new SimTime(15.0/60));
       	}
	}
}