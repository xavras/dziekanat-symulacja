package symulacja;

import java.io.File;
import java.util.LinkedList;
import desmoj.core.report.HTMLTraceOutput;
import desmoj.core.report.TraceNote;
import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
/**
 * Klasa reprezentujaca pojedyncze okienko obslugi petenta
 * 
 * @author Kachat j.W.
 */
public class PracownikDziekanatu extends Entity {
	
    protected Student aktualnyStudent;
    protected SprawyPozastudenckie aktualnaSprawa;
	//0-E 1-AIR 2-IS 3-IB
	protected int obslugiwanyKierunek;
	protected HTMLTraceOutput trace = new HTMLTraceOutput();
        
        
        
	/**	 
	 * Konstruktor, wywolujacy konstruktor klasy nadrzednej i przypisujacy okienko do odpowiedniego kierunku
	 *  0 - E 1- AIR 2- IS 3-IB
	 *  
	 * @param wlasciciel - model glowny
	 * @param nazwa - nazwa zdarzenia
	 * @param pokazTrace - czy zdarzenie ma byc sledzone
	 * @param wydzial - wydzial do ktorego przypisane jest okno
	 */
	public PracownikDziekanatu(Model wlasciciel, String nazwa, boolean pokazTrace, int kierunek) {
		super(wlasciciel, nazwa, pokazTrace);		
		this.obslugiwanyKierunek = kierunek;

	}

        
	/**
	 * 
	 * @return zwraca aktualnego petenta przy okienku
	 */
        public SprawyPozastudenckie getAktualnaSprawa(){
            return this.aktualnaSprawa;
        }
        
        public void setAktualnaSprawa(SprawyPozastudenckie sprawa){
            this.aktualnaSprawa = sprawa;
        }
	public Student getAktualnyStudent() {
		return this.aktualnyStudent;
	}
	/**
	 * 
	 * @return zmienia aktualnego petenta przy okienku
	 */
	public void setAktualnyStudent(Student student) {
		this.aktualnyStudent = student;
	}
	/**
	 * 
	 * @return zwraca wydzial, do ktorego nalezy okienko
	 */
	public int getKierunek(){
		return obslugiwanyKierunek;
	}
	/**
	 * Ustawia sciezke zapisu trace'a
	 * 
	 * @param sciezka docelowego pliku: "...traces/nazwa_katalog/nazwa"
	 */
	public void ustawSciezke(String sciezka){
		new File("traces/" + sciezka).mkdir();
		trace.open("traces/" + sciezka, sciezka);
		trace.setTimeFloats(100);
	}
	/**
	 * Wysyla Stringa z aktualnym czasem do trace'a, o sciezce zdefiniowanej w tej klasie
	 * 
	 * @param note - String, ktory zostanie zapisany do trace'a
	 */
    public void wyslijTrace(String note)
    {
        trace.receive(new TraceNote(getModel(), note, presentTime(), this, null));
    }
    /**
     * Zamyka aktywnego trace'a
     */
    public void zamknijTrace(){
    	trace.close();
    }
}
