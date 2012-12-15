package symulacja;

import java.io.File;
import desmoj.core.report.HTMLTraceOutput;
import desmoj.core.report.TraceNote;
import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
/**
 * Klasa reprezentujaca pojedyncze okienko obslugi petenta
 * 
 * @author Kachat j.W.
 */
public class Okienko extends Entity {
	protected Student aktualnyStudent;
	//0-ADM 1-POJAZDY 2-PODATKI
	protected int kierunek;
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
	public Okienko(Model wlasciciel, String nazwa, boolean pokazTrace, int kierunek) {
		super(wlasciciel, nazwa, pokazTrace);		
		this.kierunek = kierunek;
	}

	/**
	 * 
	 * @return zwraca aktualnego petenta przy okienku
	 */
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
		return kierunek;
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
