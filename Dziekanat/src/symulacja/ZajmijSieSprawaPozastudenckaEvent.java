package symulacja;


import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;
import java.util.Random;

/**
 * Klasa reprezentuje zajmowanie sie przez pracownika dziekanatu sprawa pozastudencka
 * @author Kachat
 */
public class ZajmijSieSprawaPozastudenckaEvent extends Event<PracownikDziekanatu> {
	/**
	 * Konstruktor, wywolujacy konstruktor klasy nadrzednej
	 * 
	 * @param wlasciciel - model glowny
	 * @param nazwa - nazwa zdarzenia
	 * @param pokazTrace - czy zdarzenie ma byc sledzone
	 */
	public ZajmijSieSprawaPozastudenckaEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);		
	}
	/**
	* Rutynowe dzialanie na podejscie petenta
	* 
	* @param okno - okienko wydzialu do ktorego podszedl petent
	*/
	public void eventRoutine(PracownikDziekanatu okno) {
		Dziekanat mojModel = (Dziekanat)getModel();
                SprawyPozastudenckie sprawa = mojModel.getSprawyPozastudenckieKolejka().removeFirst(); //null??? 
		
		okno.wyslijTrace("Zajmuje sie sprawa pozastud: " + okno.getAktualnaSprawa().getIdSprawy());
                
                
                Random rand = new Random();
                int czasTrwania = rand.nextInt(10)+1 ;//sprawa zabiera od 1 do 10 minut
              
                
                //po zakonczeniu sprawy albo zajmij sie nastepna sprawa, albo wywolaj studenta
                
                //jesli sa do zalatwienia jakies sprawy pozastudenckie
                if(!mojModel.getSprawyPozastudenckieKolejka().isEmpty()){
                    
                    okno.setAktualnaSprawa(mojModel.getSprawyPozastudenckieKolejka().first());
                    ZajmijSieSprawaPozastudenckaEvent event = new ZajmijSieSprawaPozastudenckaEvent(mojModel, "Sprawa pozastudencka",true);
                    event.schedule(okno, new SimTime(czasTrwania));
                }
                else{//jesli nie ma spraw pozastudenckich do zalatwienia
                
                    if (!mojModel.getPetentKolejkaDoKierunku(okno.getKierunek()).isEmpty())
                    {
			
			okno.setAktualnyStudent(mojModel.getPetentKolejkaDoKierunku(okno.getKierunek()).first());
			WywolanieStudentaEvent event = new WywolanieStudentaEvent(mojModel, "Wywolanie studenta do " + mojModel.getWolneOkienkaKierunku(okno.getKierunek()).getName(), true);		
			event.schedule(okno, new SimTime(czasTrwania));
                    }
                    else {
      
			mojModel.getWolneOkienkaKierunku(okno.getKierunek()).insert(okno);
                    }
                }
                
                
                
        
	}
}