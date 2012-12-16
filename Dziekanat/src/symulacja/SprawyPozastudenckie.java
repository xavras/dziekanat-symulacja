
package symulacja;


import java.io.File;
import java.util.Random;
import desmoj.core.report.HTMLTraceOutput;
import desmoj.core.report.TraceNote;
import desmoj.core.simulator.*;
/**
 * Klasa reprezentujaca sprawe pozastudencka
 **/

public class SprawyPozastudenckie extends Entity {    
       
    
        int wagaSprawy; //implikuje czas jaki ta sprawa zajmie 1-10 (minut)
        int idSprawy;
        static int licznikSpraw=0;
        
        protected HTMLTraceOutput trace = new HTMLTraceOutput();;
    	/**	 
    	 * Konstruktor, wywolujacy konstruktor klasy nadrzednej, ustalajacy zwloke petenta, 
    	 * do jakeigo wydzialu chce isc, identyfikator i sciezke trace'a
    	 *  
    	 * @param wlasciciel - model glowny
    	 * @param nazwa - nazwa zdarzenia
    	 * @param pokazTrace - czy zdarzenie ma byc sledzone
    	 */
		public SprawyPozastudenckie(Model wlasciciel, String nazwa, boolean pokazTrace) {
			super(wlasciciel, nazwa, pokazTrace);
			
                        Random rand = new Random();
                        wagaSprawy = rand.nextInt(10) +1;
			licznikSpraw++;
                        idSprawy = licznikSpraw;
                       
	        new File("traces/SprawyPozastudenckie").mkdir();
	        trace.open("traces/SprawyPozastudenckie", "SprawyPozastudenckie"+idSprawy);              
		}
	
       
        public int getIdSprawy(){
        	return idSprawy;
        }
      
      
        public int getLicznikSpraw(){
        	return licznikSpraw;
        }
        
        public int getWagaSprawy(){
            return wagaSprawy;
        }
        public void setWagaSprawy(int waga){
            this.wagaSprawy=waga;
        }
    	/**
    	 * Wysyla Stringa z aktualnym czasem do trace'a, o sciezce zdefiniowanej w folderze "/treces/Studenci/Student-id"
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

