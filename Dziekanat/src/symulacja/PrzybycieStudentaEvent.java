package symulacja;

import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;

/**
 * Klasa generujaca petentow
 * 
 * @author Kachat j.W.
 */
public class PrzybycieStudentaEvent extends Event<Student> {
	/**
	 * Konstruktor, wywolujacy konstruktor klasy nadrzednej
	 * 
	 * @param wlasciciel - model glowny
	 * @param nazwa - nazwa zdarzenia
	 * @param pokazTrace - czy zdarzenie ma byc sledzone
	 */
	public PrzybycieStudentaEvent(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);
	}

	/**
	 * Rutynowe dzialanie  wywolania studenta
	 * 
     * @param okno - okno, ktore oczekuje studenta
     */
	public void eventRoutine(Student student) {
		Dziekanat mojModel = (Dziekanat)getModel();
		
                //sprawdzenie czy jest wolne okienko + czy nikt nie stoi w kolejce
		if (!mojModel.getWolneOkienkaKierunku(student.getKierunek()).isEmpty() &&
                        mojModel.studentKolejka[student.getKierunek()].isEmpty()){
			PracownikDziekanatu okno = mojModel.getWolneOkienkaKierunku(student.getKierunek()).first();
			mojModel.getWolneOkienkaKierunku(student.getKierunek()).remove(okno);
			okno.setAktualnyStudent(student);
			
			WywolanieStudentaEvent wywolanieStudenta = new WywolanieStudentaEvent(mojModel, "Wywolanie studenta przez " + mojModel.getPetentKolejkaDoKierunku(student.getKierunek()).getName(), true);
			wywolanieStudenta.schedule(okno, new SimTime(0.0));
		} 
                
                //ktos jednak stoi
                student.wyslijTrace("Musze poczekac, dlugosc kolejki: "
                        +mojModel.studentKolejka[student.getKierunek()].length());
                student.wyslijTrace("Moj kierunek to: "+Dziekanat.nazwyKierunkow[student.getKierunek()]);
                mojModel.getPetentKolejkaDoKierunku(student.getKierunek()).insert(student);
	}       
}