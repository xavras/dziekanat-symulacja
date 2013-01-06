package symulacja;

import desmoj.core.report.HTMLTraceOutput;
import desmoj.core.report.TraceNote;
import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimTime;
/**
 * Klasa reprezentujaca podajnik bloczkow w Urzedzie Miasta
 * 
 * @author Kachat j.W.
 */
public class Automat extends Entity {
        public final static int iloscKierunkow = 4;
    
	protected final int maxStudentow = 80;
        protected static int maxStudentowKierunek = 50;
	protected HTMLTraceOutput trace = new HTMLTraceOutput();
	//licznik petentow do okienek: 0 - Elektrotechnika 1 - AiR  2 - Stosowana 3 - Inż. Biomedyczna
	public static int licznikStudentowZKierunku[] = new int[iloscKierunkow];
	/**	 
	 * Konstruktor, wywolujacy konstruktor klasy nadrzednej i ustalajacy sciezke trace'a
	 *  
	 * @param wlasciciel - model glowny
	 * @param nazwa - nazwa zdarzenia
	 * @param pokazTrace - czy zdarzenie ma byc sledzone
	 */
	public Automat(Model wlasciciel, String nazwa, boolean pokazTrace) {
		super(wlasciciel, nazwa, pokazTrace);		

		trace.open("traces/", "Automat");
	}
	/**
	 * Dodaje petenta do kolejek (petent pobiera bloczek)
	 * 
	 * @param student - nowy petent
	 */
	public void dodajStudenta(Student student){
		Dziekanat mojModel = (Dziekanat)getModel();
		student.wyslijTrace("Przyszedlem o: " + presentTime());
		
                //zapisz czas przybycia
                student.setCzasPrzybycia(mojModel.presentTime().getTimeAsDouble());
                
		//sprawdzenie czy Urzad moze jeszcze przyjac petentow
		if (student.licznikDziennyStudentow < maxStudentow){
			
			//sprawdzenie czy dane okienko moze jeszcze przyjac studentow
			if (licznikStudentowZKierunku[student.getKierunek()] < maxStudentowKierunek){
				
				//liczenie szacowanego czasu oczekiwania = (sr_okres_czekania) * ilosc_osob_w_kolejce
				double szacowanyCzasOczekiwania = ((mojModel.getMinCzasObslugi() + mojModel.getMaxCzasObslugi()) / 2) * mojModel.getPetentKolejkaDoKierunku(student.getKierunek()).length();
				student.setNumer(licznikStudentowZKierunku[student.getKierunek()]++);
				trace.receive(new TraceNote(getModel(), "Student " + student.getId() + " o " + presentTime() + "\n szacowany czas oczekiwania: " + szacowanyCzasOczekiwania + "min\n liczba osob w kolejce: " + mojModel.getPetentKolejkaDoKierunku(student.getKierunek()).length(), presentTime(), this, null));
                                wyslijTrace("Student " + student.getId() + " otrzymal numerek: " + student.getNumer() + ", do okienka nr: " + student.getKierunek());        
                                
				PrzybycieStudentaEvent przybycieStudenta = new PrzybycieStudentaEvent(mojModel, "Przybycie Petenta", true);
				przybycieStudenta.schedule(student, new SimTime(0.0));
			}else{
				student.wyslijTrace("Przekroczona liczba studentow do obslugi przez tego pracownika dzisiaj");
				//student.zamknijTrace();
                                StudentGeneratorEvent SGevent = new StudentGeneratorEvent(mojModel, "StudentGenerator: student wraca", true, student);
                                //SGevent.schedule(new SimTime(presentTime().getTimeAsDouble()+4*60)); //4*60 czyli 4h-> student wraca po 1 dniu. 
                                double kiedyPrzyjdzie = student.getScheduleKolejnegoDnia();
                                student.wyslijTrace("Przyjde ponownie: "+mojModel.getCzasPoSchedule(kiedyPrzyjdzie) + " (" + 
                                        kiedyPrzyjdzie + ")");
                                SGevent.schedule(new SimTime(kiedyPrzyjdzie));
			}			
		}else{
			student.wyslijTrace("Dziekanat dzisiaj juz nikogo nie przyjmuje");
			//student.zamknijTrace();
                        StudentGeneratorEvent SGevent = new StudentGeneratorEvent(mojModel, "StudentGenerator: student wraca", true, student);
                        //SGevent.schedule(new SimTime(presentTime().getTimeAsDouble()+4*60)); //4*60 czyli 4h-> student wraca po 1 dniu. 
                        double kiedyPrzyjdzie = student.getScheduleKolejnegoDnia();
                        student.wyslijTrace("Przyjde ponownie: "+mojModel.getCzasPoSchedule(kiedyPrzyjdzie) + " (" + 
                                kiedyPrzyjdzie + ")");
                        SGevent.schedule(new SimTime(kiedyPrzyjdzie));
		}
	}
	/**
	 * Wysyla Stringa z aktualnym czasem do trace'a, o sciezce zdefiniowanej w folderze "/treces/Podajnik Bloczkow"
	 * 
	 * @param note - String, ktory zostanie zapisany do trace'a
	 */
    public void wyslijTrace(String note)
    {
        note = ""+((Dziekanat)getModel()).czasTeraz()+"> "+note;
        trace.receive(new TraceNote(getModel(), note, presentTime(), this, null));
    }
    
    /**
     * Zamyka aktywnego trace'a
     */
    public void zamknijTrace(){
    	trace.close();
    }
}
