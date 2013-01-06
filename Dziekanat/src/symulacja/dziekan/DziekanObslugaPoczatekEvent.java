/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symulacja.dziekan;

import symulacja.student_do_dziekana.StudentDoDziekana;
import symulacja.student_do_dziekana.StudentDoDziekanaPrzyjscieEvent;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;
import java.util.Random;
import symulacja.Dziekanat;

/**
 *
 * @author lukasz
 */
public class DziekanObslugaPoczatekEvent extends Event<Dziekan>{
    
    public DziekanObslugaPoczatekEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);		
	}

    @Override
    public void eventRoutine(Dziekan dziekan) {
        Dziekanat model = (Dziekanat) this.getModel();

        dziekan.aktualnyStudent = model.kolejkaDziekan.removeFirst();
        if(dziekan.aktualnyStudent == null)
        {
            dziekan.wyslijTrace("Jakis Blad, student jest pusty.");
            return;
        }
        
        /*//student juz sobie poszedl, bo za dlugo czekal
        if(dziekan.aktualnyStudent.getCzasTolerancji() > 
                (model.currentTime().getTimeValue() - dziekan.aktualnyStudent.getCzasPrzyjscia())){
            dziekan.aktualnyStudent.wyslijTrace("Czekalem za dlugo, wiec sobie poszedlem");
            dziekan.wyslijTrace("dlugosc kolejki: "+model.kolejkaDziekan.length());
            dziekan.wyslijTrace(dziekan.aktualnyStudent.getName() + " sobie poszedl");
            StudentDoDziekana student = dziekan.aktualnyStudent;
            student.zwiekszDeterminacje();
            dziekan.aktualnyStudent = null;
            StudentDoDziekanaPrzyjscieEvent SGevent = new StudentDoDziekanaPrzyjscieEvent(model, "Student wraca", true);
            double kiedyPrzyjdzie = student.getScheduleKolejnegoDnia();
            student.wyslijTrace("Przyjde ponownie: "+model.getCzasPoSchedule(kiedyPrzyjdzie) + " (" + 
                    kiedyPrzyjdzie + ")");
            SGevent.schedule(student, new SimTime(kiedyPrzyjdzie));
            
            if(dziekan.isObecny())
            {
                if(!model.kolejkaDziekan.isEmpty())//ktos jest w kolejce, wiec go wywolam
                {
                    DziekanObslugaPoczatekEvent event =
                        new DziekanObslugaPoczatekEvent(model, getModel().getName(), true);
                    event.schedule(dziekan, new SimTime(0.0));
                }
                //Jest cos do zrobienia, zamiast tego nieszczesnego studenta
                else if(model.listaPodan.isPodanieDoPodpisania()) //wywolaj event poczatek podpisywania
                {
                    DziekanPodpisPoczatekEvent event =
                        new DziekanPodpisPoczatekEvent(model, getModel().getName(), true);
                    event.schedule(dziekan, new SimTime(0.0));
                }
                else
                {
                    dziekan.wyslijTrace("Nic do roboty, czekam");
                    dziekan.setZajety(false);
                }
            }
            return;
        }*/
        
        dziekan.wyslijTrace("Zaczynam obsluge studenta "+dziekan.aktualnyStudent.getId());
        dziekan.aktualnyStudent.wyslijTrace("Jestem obslugiwany przez Dziekana"); 

        DziekanObslugaKoniecEvent event =
                new DziekanObslugaKoniecEvent(model, getModel().getName(), true);
        event.schedule(dziekan, new SimTime(Dziekan.czasObslugi));

    }
}
