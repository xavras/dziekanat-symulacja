package symulacja;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;

/**
 * Klasa reprezentujaca zdarzenie obslugi petenta
 * 
 * @author Kachat j.W.
 */
public class KoniecObslugiEvent extends Event<PracownikDziekanatu> {
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
	public void eventRoutine(PracownikDziekanatu okno) {
		Dziekanat mojModel = (Dziekanat)getModel();
		
		okno.getAktualnyStudent().wyslijTrace("Odszedlem o:" + presentTime());
		okno.getAktualnyStudent().zamknijTrace();
		
		okno.wyslijTrace("Student: " + okno.getAktualnyStudent().getId() + " odszedl");
		okno.setAktualnyStudent(null);

                //sprawdzenie czy dany pracownik dziekanatu nie potrzebuje przerwy;
                double czasOstatniejPrzerwy = okno.getCzasOstatniejPrzerwy();
                
                double obecnyCzas = presentTime().getTimeAsDouble();
                
                if(mojModel.otwarty)//czy koniec pracy
                {
                    if (obecnyCzas - czasOstatniejPrzerwy > mojModel.getCzasMiedzyPrzerwami()) {
                        okno.setCzyZrobicTerazPrzerwe(true);
                        okno.setCzasOstatniejPrzerwy(obecnyCzas);
                    }

                    //nadrzedne: czy nalezy zrobic teraz przerwe?  
                    if (okno.getCzyZrobicTerazPrzerwe() == true) {
                        PrzerwaPracownikaDziekanatuEvent event = new PrzerwaPracownikaDziekanatuEvent(mojModel, "Przerwa pracownika", true);
                        event.schedule(okno, new SimTime(0.0)); //przerwa teraz.


                    } else { //jesli nie potrzebuje przerwy:


                        //sprawdzenie, czy kolejejka do petentow do wydzialu tego okna jest nie pusta
                        //jesli nie jest: uruchomienie procedury wywolywanie petenta; jesli jest to dodanie okna do oczekujacych okienek

                        //jesli sa do zalatwienia jakies sprawy pozastudenckie
                        if (!mojModel.getSprawyPozastudenckieKolejka().isEmpty()) {
                            //pracownik dziekanatu zajmuje sie nimi
                            okno.setAktualnyStudent(null);

                            okno.setAktualnaSprawa(mojModel.getSprawyPozastudenckieKolejka().first());


                            ZajmijSieSprawaPozastudenckaEvent event = new ZajmijSieSprawaPozastudenckaEvent(mojModel, "Sprawa pozastudencka", true);
                            event.schedule(okno, new SimTime(0.0));
                        } else {//jesli nie ma spraw pozastudenckich do zalatwienia

                            if (!mojModel.getPetentKolejkaDoKierunku(okno.getKierunek()).isEmpty()) {

                                okno.setAktualnyStudent(mojModel.getPetentKolejkaDoKierunku(okno.getKierunek()).first());
                                WywolanieStudentaEvent event = new WywolanieStudentaEvent(mojModel, "Wywolanie petenta do " + mojModel.getWolneOkienkaKierunku(okno.getKierunek()).getName(), true);
                                event.schedule(okno, new SimTime(0.0));
                            } else {


                                mojModel.getWolneOkienkaKierunku(okno.getKierunek()).insert(okno);
                            }
                        }
                    }
                }

	}
}