package symulacja;


import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;
import java.util.Random;

/**
 * Klasa reprezentuje zajmowanie sie przez pracownika dziekanatu sprawa pozastudencka
 * @author Kachat
 */
public class PrzerwaPracownikaDziekanatuEvent extends Event<PracownikDziekanatu> {
	/**
	 * Konstruktor, wywolujacy konstruktor klasy nadrzednej
	 * 
	 * @param wlasciciel - model glowny
	 * @param nazwa - nazwa zdarzenia
	 * @param pokazTrace - czy zdarzenie ma byc sledzone
	 */
	public PrzerwaPracownikaDziekanatuEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);		
	}
	/**
	* Rutynowe dzialanie na podejscie petenta
	* 
	* @param okno - okienko wydzialu do ktorego podszedl petent
	*/
	public void eventRoutine(PracownikDziekanatu okno) {
		Dziekanat mojModel = (Dziekanat)getModel();
                
                // przerwa trwa, po jej skonczeniu pracownik dziekantu zajmuje sie albo sprawa pozastudencka (jesli sa jakies),
                // albo wywoluje kolejnego studenta.
                
                //pobierz czas trwania przerwy z danych wejsciowych symulacji:
                double czasTrwaniaPrzerwy = mojModel.getCzasTrwaniaPrzerwy();
                okno.wyslijTrace("PRZERWA, trwac bedzie= "+czasTrwaniaPrzerwy);
                okno.setCzyZrobicTerazPrzerwe(false); // teraz robi przerwe, po nastepnym evencie jej nie bedzie potrzebowal.
                
                if(!mojModel.getSprawyPozastudenckieKolejka().isEmpty()){
                    
                    okno.setAktualnaSprawa(mojModel.getSprawyPozastudenckieKolejka().removeFirst());
                    
                    okno.wyslijTrace("Zajmuje sie sprawa pozastud: " + okno.getAktualnaSprawa().getIdSprawy());
                
                    
                    ZajmijSieSprawaPozastudenckaEvent event = new ZajmijSieSprawaPozastudenckaEvent(mojModel, "Sprawa pozastudencka",true);
                    event.schedule(okno, new SimTime(czasTrwaniaPrzerwy));
                }
                else{//jesli nie ma spraw pozastudenckich do zalatwienia
                
                    if (!mojModel.getPetentKolejkaDoKierunku(okno.getKierunek()).isEmpty())
                    {
			
			okno.setAktualnyStudent(mojModel.getPetentKolejkaDoKierunku(okno.getKierunek()).first());
			WywolanieStudentaEvent event = new WywolanieStudentaEvent(mojModel, "Wywolanie studenta do " + mojModel.getWolneOkienkaKierunku(okno.getKierunek()).getName(), true);		
			event.schedule(okno, new SimTime(czasTrwaniaPrzerwy));//wywoluje studenta od razu bo nie ma juz spraw do zalatwienia
                    }
                    else {
                        //nie ma nic absolutnie do roboty
			mojModel.getWolneOkienkaKierunku(okno.getKierunek()).insert(okno);
                    }
                }
                
                
                
        
	}
}
