/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symulacja.dziekan;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;
import symulacja.Dziekanat;
import symulacja.student_do_dziekana.StudentDoDziekana;

/**
 *
 * @author lukasz
 */
public class DziekanPodpisKoniecEvent extends Event<Dziekan>{

    public DziekanPodpisKoniecEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);		
	}
    
    @Override
    public void eventRoutine(Dziekan e) {
        e.setZajety(true);
        Dziekanat model = (Dziekanat)getModel();
        int idPodania = model.listaPodan.podpiszPodanie();
        e.wyslijTrace("Podpisalem podanie Studenta "+idPodania);
        
        if(e.isObecny())//bo mógł już skończyć, czyż nie?
        {
            if(model.listaPodan.isPodanieDoPodpisania())//o, coś leży na stole. A, podpiszę.
            {
                DziekanPodpisPoczatekEvent event =
                    new DziekanPodpisPoczatekEvent(model, getModel().getName(), true);
                event.schedule(e, new SimTime(0.0));
            }
            else if(!model.kolejkaDziekan.isEmpty())//jest student, przyjme studenta
            {
                DziekanObslugaPoczatekEvent event =
                    new DziekanObslugaPoczatekEvent(model, getModel().getName(), true);
                double czasPrzyjscia = StudentDoDziekana.czasPodchodzenia/60.0+model.godzinaTeraz();
                if(czasPrzyjscia < Dziekan.godzinaZakonczenia)
                {
                    event.schedule(e, new SimTime(StudentDoDziekana.czasPodchodzenia));
                }
            }
            else
            {
                e.wyslijTrace("Nic nie mam do roboty :(");
                e.setZajety(false);
            }
        }
    }
    
}
