package symulacja;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;

/**
 * Klasa reprezentujaca powtorne wywolanie petenta przez
 * 
 * @author Kachat j.W.
 */
public class PowtorneWywolanieEvent extends Event<PracownikDziekanatu> {
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
	public void eventRoutine(PracownikDziekanatu okno) {
		Dziekanat mojModel = (Dziekanat)getModel();

        Student p = mojModel.getPetentKolejkaDoKierunku(okno.getKierunek()).first();
        mojModel.getPodajnikBloczkow().wyslijTrace(okno.getAktualnyStudent().getNumer() + " numer ponownie proszony do okienka: " + okno.getName());    
        
        if(!mojModel.otwarty || (p == null))//koniec pracy
        {
            mojModel.getWolneOkienkaKierunku(okno.getKierunek()).insert(okno);
            return;
        }
        
        // czy student sobie nie poszedl?
                
                double obecnyCzas = presentTime().getTimeAsDouble();
                double czasPrzybyciaStudenta = p.getCzasPrzybycia();
                
                boolean czyJeszczeJest = (obecnyCzas - czasPrzybyciaStudenta > p.getCzasTolerancjiStudenta()) ? false : true;
                
        
        
        
        //sprawdzenie czy petenta zostanie obsluzony
        if (p.getCzasPodchodzeniaStudenta() < 30 && czyJeszczeJest == true){
        	StudentPodchodziEvent studentPodchodzi = new StudentPodchodziEvent(mojModel, "Klient podchodzi do " + mojModel.getPetentKolejkaDoKierunku(okno.getKierunek()).getName(), true);
        	studentPodchodzi.schedule(okno, new SimTime((double)((p.getCzasPodchodzeniaStudenta() - 15.0)/60)));
        } else{
                //przyczyna odejscia
        	if(czyJeszczeJest ==true){
                    okno.getAktualnyStudent().wyslijTrace("poszedlem sobie: za dlugo podchodzilem");
                    okno.getAktualnyStudent().zwiekszDeterminacje();
                    StudentGeneratorEvent SGevent = new StudentGeneratorEvent(mojModel, "StudentGenerator: student wraca", true, okno.getAktualnyStudent());
                    //SGevent.schedule(new SimTime(presentTime().getTimeAsDouble()+4*60)); //4*60 czyli 4h-> student wraca po 1 dniu. 
                    double kiedyPrzyjdzie = okno.getAktualnyStudent().getScheduleKolejnegoDnia();
                    okno.getAktualnyStudent().wyslijTrace("Przyjde ponownie: "+mojModel.getCzasPoSchedule(kiedyPrzyjdzie) + " (" + 
                            kiedyPrzyjdzie + ")");
                    SGevent.schedule(new SimTime(kiedyPrzyjdzie));
                }
                else{
                    okno.getAktualnyStudent().wyslijTrace("poszedlem sobie: przekroczono tolerancje...");
                    okno.getAktualnyStudent().zwiekszDeterminacje();
                    StudentGeneratorEvent SGevent = new StudentGeneratorEvent(mojModel, "StudentGenerator: student wraca", true, okno.getAktualnyStudent());
                    //SGevent.schedule(new SimTime(presentTime().getTimeAsDouble()+4*60)); //4*60 czyli 4h-> student wraca po 1 dniu. 
                    double kiedyPrzyjdzie = okno.getAktualnyStudent().getScheduleKolejnegoDnia();
                    okno.getAktualnyStudent().wyslijTrace("Przyjde ponownie: "+mojModel.getCzasPoSchedule(kiedyPrzyjdzie) + " (" + 
                            kiedyPrzyjdzie + ")");
                    SGevent.schedule(new SimTime(kiedyPrzyjdzie));
                    
                }
        	//okno.getAktualnyStudent().zamknijTrace();
        	
        	mojModel.getPetentKolejkaDoKierunku(okno.getKierunek()).removeFirst();
        	okno.setAktualnyStudent(null);
        	
        	//uruchomienie procesu wywolujacego studenta
        	if (!mojModel.getPetentKolejkaDoKierunku(okno.getKierunek()).isEmpty()){	
        		okno.setAktualnyStudent(mojModel.getPetentKolejkaDoKierunku(okno.getKierunek()).first());
        		WywolanieStudentaEvent event = new WywolanieStudentaEvent(mojModel, "Wywolanie studenta do " + mojModel.getWolneOkienkaKierunku(okno.getKierunek()).getName(), true);		
        		event.schedule(okno, new SimTime(0.0));
        	} else{
        		mojModel.getWolneOkienkaKierunku(okno.getKierunek()).insert(okno);
        	}
        }
	}
}


