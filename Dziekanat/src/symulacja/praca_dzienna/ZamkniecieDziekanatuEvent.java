/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symulacja.praca_dzienna;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;
import symulacja.Automat;
import symulacja.Dziekanat;
import symulacja.SprawyPozastudenckieGeneratorEvent;
import symulacja.Student;
import symulacja.StudentGeneratorEvent;
import symulacja.dziekan.Dziekan;
import symulacja.dziekan.DziekanPrzyjscieEvent;
import symulacja.student_do_dziekana.StudentDoDziekanaGeneratorEvent;

/**
 *
 * @author lukasz
 */
    public class ZamkniecieDziekanatuEvent extends ExternalEvent{
        
    public ZamkniecieDziekanatuEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);
	}
    
    public void eventRoutine() {
		Dziekanat mojModel = (Dziekanat)getModel();
		mojModel.otwarty = false;
		mojModel.wyslijTracePracownikom("Koniec czasu, ide za chwile do domu.");
                
                for(int i=0; i<4; i++)
                {
                    Automat.licznikStudentowZKierunku[i] = 0;
                }
                Student.licznikDziennyStudentow = 0;

                mojModel.wyczyscKolejkeStudentow();
                
                //otwieramy za pół godziny (czyli nowy dzień)
                OtwarcieDziekanatuEvent openTheDoors = new OtwarcieDziekanatuEvent(getModel(), "Otwieramy Dziekanat", true);
                openTheDoors.schedule(new SimTime(0.5*60.0));//za 0.5 godziny
    }
}
