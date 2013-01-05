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
        dziekan.setZajety(true);
        dziekan.wyslijTrace("Koncze obsluge studenta "+dziekan.aktualnyStudent.getId());
        dziekan.aktualnyStudent.wyslijTrace("Zostalem obsluzony przez Dziekana"); 

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
        
        if(dziekan.isObecny())
        {
            if(model.listaPodan.isPodanieDoPodpisania()) //wywolaj event poczatek podpisywania
            {
                DziekanPodpisPoczatekEvent event =
                    new DziekanPodpisPoczatekEvent(model, getModel().getName(), true);
                event.schedule(dziekan, new SimTime(0.0));
            }
            else if(!model.kolejkaDziekan.isEmpty())//ktos jest w kolejce, wiec go wywolam
            {
                DziekanObslugaPoczatekEvent event =
                    new DziekanObslugaPoczatekEvent(model, getModel().getName(), true);
                double czasPrzyjscia = StudentDoDziekana.czasPodchodzenia/60.0+model.godzinaTeraz();
                if(czasPrzyjscia < Dziekan.godzinaZakonczenia)
                {
                    event.schedule(dziekan, new SimTime(StudentDoDziekana.czasPodchodzenia));
                }
            }
            else
            {
                dziekan.wyslijTrace("Nic do roboty, czekam");
                dziekan.setZajety(false);
            }
        }
        
        
    }
    
}
