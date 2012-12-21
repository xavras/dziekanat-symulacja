/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symulacja.dziekan;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;
import java.util.Random;
import symulacja.Dziekanat;
import symulacja.student_do_dziekana.StudentDoDziekana;
import symulacja.student_do_dziekana.StudentDoDziekanaPrzyjscieEvent;

/**
 *
 * @author lukasz
 */
public class DziekanObslugaKoniecEvent extends Event<Dziekan>{

    public DziekanObslugaKoniecEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);		
    }

    @Override
    public void eventRoutine(Dziekan dziekan) {
        Dziekanat model = (Dziekanat) this.getModel();
        
        dziekan.wyslijTrace("Koncze obsluge studenta "+dziekan.aktualnyStudent.getId());
        dziekan.aktualnyStudent.wyslijTrace("Jestem obslugiwany przez Dziekana"); 

        Random random = new Random();
        if(random.nextInt(10) > 0)//szansa 90% na pozytywna decyzje
        {
            dziekan.wyslijTrace("Akceptuje prosbe studenta");
            dziekan.aktualnyStudent.wyslijTrace("Dziekan sie zgodzil, umieram :P");
        }
        else//odmowa
        {
            dziekan.wyslijTrace("Odmawiam studentowi");
            dziekan.aktualnyStudent.wyslijTrace("Dziekan sie nie zgodzil, przyjde jeszcze raz");
            
            //TODO: podchodzenie studenta po raz kolejny (timing)
            /*StudentDoDziekanaPrzyjscieEvent event = 
                    new StudentDoDziekanaPrzyjscieEvent(getModel(), getModel().getName(), traceIsOn());
            event.schedule(dziekan.aktualnyStudent, new SimTime(//TODO));*/
        }
        dziekan.aktualnyStudent = null;
        
        //if(cos jest do podpisania) wywolaj event poczatek podpisywania
        /*else */if(!model.kolejkaDziekan.isEmpty())//ktos jest w kolejce, wiec go wywolam
        {
            DziekanObslugaPoczatekEvent event =
                new DziekanObslugaPoczatekEvent(model, getModel().getName(), true);
            event.schedule(dziekan, new SimTime(StudentDoDziekana.czasPodchodzenia/60.0));
        }
        else
        {
            dziekan.wyslijTrace("Nic do roboty, czekam");
            dziekan.setZajety(false);
        }
        
        
    }
    
}
