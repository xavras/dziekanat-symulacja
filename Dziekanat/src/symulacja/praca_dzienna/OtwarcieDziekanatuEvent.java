/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symulacja.praca_dzienna;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;
import symulacja.Dziekanat;
import symulacja.SprawyPozastudenckie;
import symulacja.SprawyPozastudenckieGeneratorEvent;
import symulacja.StudentGeneratorEvent;
import symulacja.dziekan.Dziekan;
import symulacja.dziekan.DziekanPrzyjscieEvent;
import symulacja.student_do_dziekana.StudentDoDziekanaGeneratorEvent;

/**
 *
 * @author lukasz
 */
public class OtwarcieDziekanatuEvent extends ExternalEvent{
    public OtwarcieDziekanatuEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);
	}
    
    public void eventRoutine() {
		Dziekanat mojModel = (Dziekanat)getModel();
		mojModel.otwarty = true;
                mojModel.wyslijTracePracownikom("Wstaje nowy dzien, poczatek pracy i bycia niemilym dla studentow :D");
		
                StudentGeneratorEvent generatorStudentow = new StudentGeneratorEvent(getModel(), "StudentGenerator", true);
		generatorStudentow.schedule(new SimTime(0.0));
                
                SprawyPozastudenckieGeneratorEvent generatorSpraw = new SprawyPozastudenckieGeneratorEvent(getModel(),"PetentGenerator", true);
                generatorSpraw.schedule(new SimTime(0.0));
                
                StudentDoDziekanaGeneratorEvent generatorDziekan = new StudentDoDziekanaGeneratorEvent(getModel(), "StudentDoDziekanaGen", true);
                generatorDziekan.schedule(new SimTime(0.0));
                
                DziekanPrzyjscieEvent przyjscieDziekana = new DziekanPrzyjscieEvent(getModel(), "Przyjscie Dziekana", true);
                przyjscieDziekana.schedule(mojModel.dziekan, new SimTime((Dziekan.godzinaRozpoczecia-Dziekanat.godzinaOtwarcia)*60.0));
                
                //zamkniecie dziekanatu za iles tam godzin
                ZamkniecieDziekanatuEvent closeTheDoors = new ZamkniecieDziekanatuEvent(getModel(), "Zamykamy Dziekanat", true);
                closeTheDoors.schedule(new SimTime((Dziekanat.godzinaZamkniecia-Dziekanat.godzinaOtwarcia)*60.0));
    }
}
