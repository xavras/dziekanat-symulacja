package symulacja;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;

/**
 * Klasa reprezentujaca zdarzenie podejscia petenta
 * 
 * @author Kachat j.W.
 */
public class StudentPodchodziEvent extends Event<Okienko> {
	/**
	 * Konstruktor, wywolujacy konstruktor klasy nadrzednej
	 * 
	 * @param wlasciciel - model glowny
	 * @param nazwa - nazwa zdarzenia
	 * @param pokazTrace - czy zdarzenie ma byc sledzone
	 */
	public StudentPodchodziEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);		
	}
	/**
	* Rutynowe dzialanie na podejscie petenta
	* 
	* @param okno - okienko wydzialu do ktorego podszedl petent
	*/
	public void eventRoutine(Okienko okno) {
		Dziekanat mojModel = (Dziekanat)getModel();
		Student student = mojModel.getPetentKolejkaDoKierunku(okno.getKierunek()).removeFirst();
		
		okno.wyslijTrace("Przyjmuje studenta: " + okno.getAktualnyStudent().getId());

                //wygeneruj odpowiedni czas obsługi; 
                //w zaleznosci od tego jaka student ma sprawa, to poczyn pewne rzeczy
                int sprawa = student.getSprawa();
                
                switch(sprawa){
                    case 0: //pytanie
                            //student zadaje pytanie i wychodzi
                        
                        break;
                    case 1: //zlozenie podania
                            // wrzucamy podanie na liste podan niepodpisanych; nr podania = id_studenta (1 student 1 podanie dla uproszczenia)
                            mojModel.zlozeniePodania(student.getId());
                            student.setSprawa(2); //teraz student bedzie chcial odebrac podanie
                            //student po kilku dniach powraca do dziekanatu
                            student.wyslijTrace("zlozylem podanie");
                        
                            //TO DO: Event ze wraca
                            
                        
                        break;
                    case 2: //sprawdzenie czy podanie jest podpisane
                            if(mojModel.odbiorPodania(student.getId())){
                                //student idzie do domu zadowoolny
                                student.wyslijTrace("odebralem podanie, odchodze");
                            }
                            else{
                                //musi wrocic jeszcze raz 
                                student.wyslijTrace("nie odebralem podania, wroce jeszcze raz");
                                //TO DO: Event ze wraca
                            }
                        break;
                    
                }
                
                
                
        KoniecObslugiEvent koniecObslugi = new KoniecObslugiEvent(mojModel, "Koniec obslugi " + mojModel.getWolneOkienkaKierunku(okno.getKierunek()).getName(), true);
     	koniecObslugi.schedule(okno, new SimTime(mojModel.getCzasObslugi()));
	}
}
