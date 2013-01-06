package symulacja;

import java.io.File;
import java.util.Random;
import desmoj.core.report.HTMLTraceOutput;
import desmoj.core.report.TraceNote;
import desmoj.core.simulator.*;
/**
 * Klasa reprezentujaca pojedynczego petenta
 * 
 * @author Kachat j.W.
 */
public class Student extends Entity {    
        int czasPodchodzeniaStudenta;
        double czasTolerancji; //czas jaki student jest w stanie czekac zanim sie zniecierpliwi
        double czasPrzybycia;
        //0- E 1- AiR 2-IS 3-IB
        int kierunek;
        static int licznikStudentow = 0;
        int id;
        int numer; //numer na bloczku z automatu
        
        int sprawa; //0-pytanie 1-zlozenia podania 2-odbior podania
        public static int licznikDziennyStudentow = 0;
        
        
        
        
        
        protected HTMLTraceOutput trace = new HTMLTraceOutput();;
    	/**	 
    	 * Konstruktor, wywolujacy konstruktor klasy nadrzednej, ustalajacy zwloke petenta, 
    	 * do jakeigo wydzialu chce isc, identyfikator i sciezke trace'a
    	 *  
    	 * @param wlasciciel - model glowny
    	 * @param nazwa - nazwa zdarzenia
    	 * @param pokazTrace - czy zdarzenie ma byc sledzone
    	 */
		public Student(Model wlasciciel, String nazwa, boolean pokazTrace) {
			super(wlasciciel, nazwa, pokazTrace);
			
			Random rand= new Random();
			czasPodchodzeniaStudenta = rand.nextInt(40);// w sekundach
                        kierunek = rand.nextInt(4);
                        czasTolerancji = rand.nextInt(Dziekanat.getLimitTolerancjiStudentow()); //w minutach
                        id = licznikStudentow;
                        licznikStudentow++;
                        licznikDziennyStudentow++;
                        
                        sprawa = rand.nextInt(2); //poczatkowo moze miec tylko wart 0 lub 1, nie moze miec 2 bo zeby odebrac podanie najpierw musi je zlozyc
	        
	        new File("traces/Studenci").mkdir();
	        trace.open("traces/Studenci", "Student-" + id);
                ((Dziekanat)getModel()).traces.add(trace);
		}
		
        public void setKierunek(int kierunek)
        {
            
            this.kierunek = kierunek;
        }   
        
        /**
		 * 
		 * @return zwraca kierunek na jakim jest student
		 */
        public int getKierunek(){
           return this.kierunek;
        }
        
        public int getSprawa(){
            return this.sprawa;
        }
        public void setSprawa(int spr){
            this.sprawa = spr;
        }
        
        /**
         * Zmienia zwloke petenta
         * @param zwloka - nowa zwloka
         */
        public void setCzasPodchodzeniaStudenta(int zwloka){
            this.czasPodchodzeniaStudenta = zwloka;
        }
    	/**
    	 * 
    	 * @return zwraca zwloke, jaka bedzie miec student
    	 */
        public int getCzasPodchodzeniaStudenta(){
           return this.czasPodchodzeniaStudenta;
        }
        
        public double getCzasTolerancjiStudenta(){
            return czasTolerancji;
        }
        
        public double getCzasPrzybycia(){
            return czasPrzybycia;
        }
        public void setCzasPrzybycia (double czas){
            this.czasPrzybycia = czas;
        }
        /**
         * 
         * @return identyfikator studenta
         */
        public int getId(){
        	return id;
        }
        /**
         * Zmienia aktualny numer na bloczku z automatu petenta
         * @param numer - nowy numer
         */
        public void setNumer(int numer){
            this.numer = numer;
        }
        /**
         * 
         * @return zwraca numer na bloczku z automatu petenta
         */
        public int getNumer(){
        	return numer;
        }
        /**
         * 
         * @return liczba studentow, ktora odwiedizla Dziekanat
         */
        public int getLicznikPetentow(){
        	return licznikStudentow;
        }
    	/**
    	 * Wysyla Stringa z aktualnym czasem do trace'a, o sciezce zdefiniowanej w folderze "/treces/Studenci/Student-id"
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
        
        public double getScheduleKolejnegoDnia()
        {
            Random random = new Random();
            double godzTeraz = ((Dziekanat)getModel()).godzinaTeraz();
            double doKoncaDnia = Dziekanat.godzinaZamkniecia - godzTeraz;
            double czasPracy = Dziekanat.godzinaZamkniecia - Dziekanat.godzinaOtwarcia;
            
            double losowaGodzina = random.nextDouble()*czasPracy;
            
            return (losowaGodzina + doKoncaDnia + 0.5)*60.0; //dodaję 0.5 przerwy między dniami
        }
        
        public void zwiekszDeterminacje()
        {
            if(czasPodchodzeniaStudenta>10) czasPodchodzeniaStudenta-=10;
            czasTolerancji+=10;
        }
}
