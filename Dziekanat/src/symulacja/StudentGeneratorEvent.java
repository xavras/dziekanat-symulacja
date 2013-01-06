package symulacja;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;

/**
 * Klasa generujaca petentow
 * 
 * @author Kachat j.W.
 */
public class StudentGeneratorEvent extends ExternalEvent {
    
        private Student przychodzacyStudent = null;
	/**
	 * Konstruktor, wywolujacy konstruktor klasy nadrzednej
	 * 
	 * @param wlasciciel - model glowny
	 * @param nazwa - nazwa zdarzenia
	 * @param pokazTrace - czy zdarzenie ma byc sledzone
	 */
	public StudentGeneratorEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);
	}
                
        /**
         * Alternatywny konstruktor
         * @param wlasciciel
         * @param nazwa
         * @param pokazTrace
         * @param student student, który się zjawia kolejny raz
         */
        public StudentGeneratorEvent(Model wlasciciel, String nazwa, boolean pokazTrace, Student student)
        {
            super(wlasciciel, nazwa, pokazTrace);
            student.wyslijTrace("Student przychodzi kolejny raz.");
            przychodzacyStudent = student;
        }
    /**
     * Rutynowe dzialanie generowanie petenta
     * 
     */
    @Override
	public void eventRoutine() {
		Dziekanat mojModel = (Dziekanat)getModel();
		
                if(mojModel.otwarty)
                {
                    if(przychodzacyStudent == null)//jeśli pojawia się nowy student
                    {
                        mojModel.getPodajnikBloczkow().dodajStudenta(new Student(mojModel, "Student", true));
                        schedule(new SimTime(mojModel.getCzasPrzybyciaStudenta()));
                    }
                    else//student przychodzi kolejny raz, wywołany został konstruktor ze studentem
                    {
                        mojModel.getPodajnikBloczkow().dodajStudenta(przychodzacyStudent);
                        przychodzacyStudent.wyslijTrace("Przyszedlem ponownie, jest: "+mojModel.czasTeraz());
                    }
                }
                else
                {
                    mojModel.podajnikBloczkow.wyslijTrace("Czas sie skonczyl, zaden student juz dzisiaj sie nie wygeneruje");
                }
	}
}
