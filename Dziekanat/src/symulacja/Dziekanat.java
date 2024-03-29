package symulacja;


import java.io.File;

import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import desmoj.core.report.HTMLTraceOutput;
import java.util.ArrayList;

import java.util.LinkedList;
import symulacja.dziekan.Dziekan;
import symulacja.dziekan.DziekanPrzyjscieEvent;
import symulacja.praca_dzienna.OtwarcieDziekanatuEvent;
import symulacja.student_do_dziekana.StudentDoDziekana;
import symulacja.student_do_dziekana.StudentDoDziekanaGeneratorEvent;
import symulacja.student_do_dziekana.StudentDoDziekanaPrzyjscieEvent;

/**
 * To jest klasa z modelem Urzedu Miasta. Jest to glowna klasa modelu zdarzeniowego.
 * Petenci podchodza do automatu z bloczkami, pobieraja bloczki (nie moze byc wiecej niz 20 
 * petentow do jednego okienka. Klienci czekaja na swoja kolej (wywolanie swojego numerka przez
 * obslugujacego okienko). Po 15 minutach numer wywolywany jest ponownie, po kolejnych 15 minutach, 
 * jestli klient nie podejdzie do okienka, wypada z kolejki i odchodzi z kwitkiem.
 * 
 * @author Kachat j.W.
 */
public class Dziekanat extends Model {
    protected static int NUM_OK_E = 1;
    protected static int NUM_OK_AIR = 1;
    protected static int NUM_OK_IS = 1;
    protected static int NUM_OK_IB = 1;
    protected RealDistExponential czasPrzybyciaStudenta;
    protected static double czasPrzybyciaStudentaDouble = 3;
        
    protected RealDistExponential czasNowejSprawyPozastudenckiej;
    protected static double czasNowejSprawyPozastudenckiejDouble = 30;
    
    protected RealDistExponential czasTrwaniaPrzerwy;
    protected static double czasTrwaniaPrzerwyDouble = 5;
    
    protected static double czasMiedzyPrzerwami =10; //czas po ktorym pracownik dziekanatu stwierdza ze musi isc na przerwae
    
    protected static int limitTolerancjiStudentow = 30; //w minutach -> w konstruktorze studenta potem losujemy czas tolerancji pojedynczego studenta z przedzialu <0;limit> //kch.
        
    public static double godzinaOtwarcia = 9.0;
    public static double godzinaZamkniecia = 12.0;
    
    protected RealDistUniform czasObslugi;
    protected static double minCzasObslugi = 2.0;
    protected static double maxCzasObslugi = 5.0;
    
    //listy podan do dziekana oczekujacych na podpisanie i podpisanych
    public LinkedList<Integer> podaniaLista;
    public LinkedList<Integer> gotowePodaniaLista; 
    
    //alternatywna wersja, trochę chyba prostsza
    public ListaPodan listaPodan;
    public boolean otwarty = false;
      
    public Queue<StudentDoDziekana> kolejkaDziekan;
    public Dziekan dziekan;
    
    public Queue<SprawyPozastudenckie> sprawyPozastudenckieKolejka ;
  
	//Kolejka reprezentujace klientow oczekujacych do:
	//0 - Wydzial Administracji 1 - Wydzial Pojazdow  2 - Wydzial podatkow
	protected Queue<Student>[] studentKolejka = new Queue[4];
	
    //Kolejka reprezentujaca wolnych urzednikow w okienkach, odpowiednio:
	//0-3 poszczegolne kierunki
	protected Queue<PracownikDziekanatu>[] wolneOkienka = new Queue[4];
	//Kolejka trzymajaca wszystkie okienka
	protected Queue<PracownikDziekanatu> okienka;
	protected Automat podajnikBloczkow;
        
        public static final String nazwyKierunkow[] = 
        {"Elektrotechnika","AiR","IS","IB"};
        
        public ArrayList<HTMLTraceOutput> traces = new ArrayList<>();
	/**
	 * Konstruktor, wywolujacy konstruktor klasy nadrzednej
	 * 
	 * @param wlasciciel - model glowny
	 * @param nazwa - nazwa zdarzenia
	 * @param pokazTrace - czy zdarzenie ma byc sledzone
	 */
	public Dziekanat(Model wlasciciel, String nazwaModelu, boolean pokazRaport, boolean pokazTrace) {
		super(wlasciciel, nazwaModelu, pokazRaport, pokazTrace);	
	}
	/**
	 * @return opis symulacji
	 */
	public String description() {
		return "Model ten opisuje prace Dziekanatu. Studenci 4 kierunkow EAIIIB Pobieraja bloczki z automatu do odpowiednich okienek." +
				" okienek. \"Okienka\"  dwa razy wywoluja numerek klienta, po czym usuwaja go z kolejki.";
	}
	/**
	 * Uruchamia zdarzenie generatora studentow
	 */
	public void doInitialSchedules() {
            
                OtwarcieDziekanatuEvent openTheDoors = new OtwarcieDziekanatuEvent(this, "Otwieramy Dziekanat", true);
                openTheDoors.schedule(new SimTime(0.0));
                
                podaniaLista = new LinkedList();
                gotowePodaniaLista = new LinkedList();
	
                listaPodan = new ListaPodan();
        }
	/**
	 * Inicjalizuje podstawowe elementy symulacji pracy Urzedu Miasta
	 */
	public void init() {
		podajnikBloczkow = new Automat(getModel(), getModel().getName(), false);
		new File("traces").mkdir();

		czasObslugi= new RealDistUniform(this, "czasObslugiStrumien", minCzasObslugi, maxCzasObslugi, true, false);
		czasPrzybyciaStudenta= new RealDistExponential(this, "czasPrzybyciaPetentaStrumien", czasPrzybyciaStudentaDouble, true, false);
		czasPrzybyciaStudenta.setNonNegative(true);
               
                czasNowejSprawyPozastudenckiej = new RealDistExponential(this, "czasNowejSprawyStrumien", czasNowejSprawyPozastudenckiejDouble, true, false);
                czasNowejSprawyPozastudenckiej.setNonNegative(true);
		
                czasTrwaniaPrzerwy = new RealDistExponential(this, "czasTrwaniaPrzerwy", czasTrwaniaPrzerwyDouble, true, false);
                czasTrwaniaPrzerwy.setNonNegative(true);
                
                sprawyPozastudenckieKolejka =new Queue<SprawyPozastudenckie>(this, "sprawyPozastudKolejka", true, true);
                
		studentKolejka[0] = new Queue<Student>(this, "Kolejka studentow do okienka dla Elektrotechniki", true, true);
		studentKolejka[1] = new Queue<Student>(this, "Kolejka studentow do okienka dla Automatyki i Robotyki", true, true);
		studentKolejka[2] = new Queue<Student>(this, "Kolejka studentow do okienka dla Inf.Stosowanej", true, true);
                studentKolejka[3] = new Queue<Student>(this, "Kolejka studentow do okienka dla Inż.Biomedycznej", true, true);

        wolneOkienka[0] = new Queue<PracownikDziekanatu>(this, "Kolejka okienek Elektrotechniki", true, true);
        wolneOkienka[1] = new Queue<PracownikDziekanatu>(this, "Kolejka okienek Automatyki i Robotyki", true, true);
        wolneOkienka[2] = new Queue<PracownikDziekanatu>(this, "Kolejka okienek Inf.Stosowanej", true, true);
        wolneOkienka[3] = new Queue<PracownikDziekanatu>(this, "Kolejka okienek Inż.Biomedycznej", true, true);
          
        okienka = new Queue<PracownikDziekanatu>(this, "Kolejka Okienek", false, false);

		// wkladamy okienka odpowiednych wydzialow do odpowiednich kolejek
		for (int i = 0; i < NUM_OK_E ; i++){
			// stworz nowe okienko i umiesc je w kolejce 
			wolneOkienka[0].insert(new PracownikDziekanatu(this, "Elektrotechnika", true, 0));
			wolneOkienka[0].get(i).ustawSciezke("Elektrotechnika");
			okienka.insert(wolneOkienka[0].get(i));
		}
		
		for (int i = 0; i < NUM_OK_AIR ; i++){	
			wolneOkienka[1].insert(new PracownikDziekanatu(this, "AiR", true, 1));
			wolneOkienka[1].get(i).ustawSciezke("AiR");
			okienka.insert(wolneOkienka[1].get(i));
		}

     	for (int i = 0; i < NUM_OK_IS ; i++){
     		wolneOkienka[2].insert(new PracownikDziekanatu(this, "IS", true, 2));
     		wolneOkienka[2].get(i).ustawSciezke("IS");
     		okienka.insert(wolneOkienka[2].get(i));
     	}
        
        for (int i = 0; i < NUM_OK_IB ; i++){
     		wolneOkienka[3].insert(new PracownikDziekanatu(this, "IB", true, 3));
     		wolneOkienka[3].get(i).ustawSciezke("IB");
     		okienka.insert(wolneOkienka[3].get(i));
     	}
        
        dziekan = new Dziekan(getModel(), getModel().getName(), true);
        kolejkaDziekan = new Queue<StudentDoDziekana>(this, "Kolejka Do Dziekana", true, true);
                
	}
	/**
	 * @return probka czasu obslugi petenta
	 */
	public double getCzasObslugi() {
		return czasObslugi.sample();
	}
	/**
	 * @return probka czasu generowania petenta
	 */
	public double getCzasPrzybyciaStudenta() {
		return czasPrzybyciaStudenta.sample();
	}
	/**
	 * 
	 * @param i - indeks kierunku
	 * @return kolejka do okienka dla danego kierunku
	 */
        
        
        public double getCzasNowejSprawyPozastudenckiej(){
            return czasNowejSprawyPozastudenckiej.sample();
        }
        
        
        public double getCzasTrwaniaPrzerwy(){
            return czasTrwaniaPrzerwy.sample();
        }
    public Queue<Student> getPetentKolejkaDoKierunku(int i) {
		return studentKolejka[i];
	}
    
    public double getCzasMiedzyPrzerwami(){
        return czasMiedzyPrzerwami;
    }
    
    public static int getLimitTolerancjiStudentow(){
        return limitTolerancjiStudentow; //ile sa sklonni czekac
    }
    public Queue<SprawyPozastudenckie> getSprawyPozastudenckieKolejka(){
        return sprawyPozastudenckieKolejka;
    }
    /**
     * 
     * @param i- indeks wydzialu
     * @return wolne okienka wydzialu
     */
	public Queue<PracownikDziekanatu> getWolneOkienkaKierunku(int i) {
		return wolneOkienka[i];
	}
	/**
	 * 
	 * @return minimalny czas obaslugi petanta
	 */
    public double getMinCzasObslugi() {
		return minCzasObslugi;
	}
	/**
	 * 
	 * @return maksymalny czas obaslugi petanta
	 */
	public double getMaxCzasObslugi() {
		return maxCzasObslugi;
	}
	/**
	 * 
	 * @return podajnik bloczkow Urzedu Miasta
	 */
	public Automat getPodajnikBloczkow(){
		return podajnikBloczkow;
	}
	/**
	 * zamyka trace'y: okienek, podajnika bloczkow i automatu
	 */
        
        public void zlozeniePodania(int id_studenta){
             Integer nr = new Integer(id_studenta);
             podaniaLista.add(nr);
        }
        
        public void podpisPodania(int id_podania){
            //znajdz na liscie podaniaLista:
            int index = podaniaLista.indexOf(id_podania);
            
            //dodaj do listy podpisanych podan
            gotowePodaniaLista.add(id_podania);
            
            //usun z listy podan do podpisu
            podaniaLista.remove(index);
        }
        //zwraca false jesli podania nie ma na liscie
        public boolean odbiorPodania(int id_podania){
            //znajdz na liscie podaniaLista:
            int index = gotowePodaniaLista.indexOf(id_podania);
            if (index == -1 ){
                return false;
            }
            else{
                gotowePodaniaLista.remove(index);
                return true;
            }
                
            
            
        }
        
        public void dodajSprawe(SprawyPozastudenckie spr){
		
		spr.wyslijTrace("Nowa sprawa o godz: " + presentTime());
                
                sprawyPozastudenckieKolejka.insert(spr);
		spr.zamknijTrace();
                
	}
	public void zamknijTrace(){
		for (int i = 0; i < okienka.size(); i++)
			okienka.get(i).zamknijTrace();
		podajnikBloczkow.zamknijTrace();
                dziekan.zamknijTrace();
                
                for(int i=0; i<traces.size(); i++)
                {
                    traces.get(i).close();
                }
	}
        
        public double godzinaTeraz(){
            double czasTeraz = this.currentTime().getTimeValue();
            double czasDnia = Dziekanat.godzinaZamkniecia-Dziekanat.godzinaOtwarcia+0.5;
            double wsp = Math.floor(czasTeraz/(czasDnia*60.0));
            //wsp+1 - ktory mamy dzien
            double teraz = (czasTeraz/60.0-wsp*czasDnia)+Dziekanat.godzinaOtwarcia;
            return teraz;
        }
        
        public double dzienTeraz(){
            double czasTeraz = this.currentTime().getTimeValue();
            double czasDnia = Dziekanat.godzinaZamkniecia-Dziekanat.godzinaOtwarcia+0.5;
            double wsp = Math.floor(czasTeraz/(czasDnia*60.0));
            //wsp+1 - ktory mamy dzien
            return (int)(wsp+1);
        }
        
        public String czasTeraz()
        {
            double czasTeraz = this.currentTime().getTimeValue();
            double czasDnia = Dziekanat.godzinaZamkniecia-Dziekanat.godzinaOtwarcia+0.5;
            double wsp = Math.floor(czasTeraz/(czasDnia*60.0));
            //wsp+1 - ktory mamy dzien
            double teraz = (czasTeraz-wsp*czasDnia*60.0)+Dziekanat.godzinaOtwarcia*60.0;
            double godz = Math.floor(teraz/60.0);
            double min = (teraz-godz*60.0);
            String ret = "";
            if(godz<10)ret+="0"+(int)godz;
            else ret += (int)godz;
            ret+=":";
            if(min<10)ret+="0"+(int)min;
            else ret += (int)min;
            ret = "dzien "+(int)(wsp+1)+", "+ret;
            return ret;
        }
        
        public void wyslijTracePracownikom(String note)
        {
            for(int i=0; i<okienka.size(); i++)
            {
                okienka.get(i).wyslijTrace(note);
            }
        }
        
        public void wyczyscKolejkeStudentow()
        {
            for(int i=0; i<studentKolejka.length; i++)
            {
                for(;studentKolejka[i].length()>0;)
                {
                    Student student = studentKolejka[i].removeFirst();
                    StudentGeneratorEvent SGevent = new StudentGeneratorEvent(this, "StudentGenerator: student wraca", true, student);
                    //SGevent.schedule(new SimTime(presentTime().getTimeAsDouble()+4*60)); //4*60 czyli 4h-> student wraca po 1 dniu. 
                    double kiedyPrzyjdzie = student.getScheduleKolejnegoDnia();
                    student.wyslijTrace("Koniec pracy dziekanatu, wyrzucaja mnie z budynku.");
                    student.wyslijTrace("Przyjde ponownie: "+this.getCzasPoSchedule(kiedyPrzyjdzie) + " (" + 
                            kiedyPrzyjdzie + ")");
                    SGevent.schedule(new SimTime(kiedyPrzyjdzie));
                }
            }
        }
        
        public String infoOkienka()
        {
            String ret = "";
            for(int i=0; i<wolneOkienka.length; i++)
            {
                ret+="Wolne Okienka, kierunek " + nazwyKierunkow[i] + ": " + 
                        wolneOkienka[i].length() + "<br />";
            }
            
            return ret;
        }
        
        public String getCzasPoSchedule(double schedule)
        {
            double czasTeraz = this.currentTime().getTimeValue() + schedule;
            double czasDnia = Dziekanat.godzinaZamkniecia-Dziekanat.godzinaOtwarcia+0.5;
            double wsp = Math.floor(czasTeraz/(czasDnia*60.0));
            //wsp+1 - ktory mamy dzien
            double teraz = (czasTeraz-wsp*czasDnia*60.0)+Dziekanat.godzinaOtwarcia*60.0;
            double godz = Math.floor(teraz/60.0);
            double min = (teraz-godz*60.0);
            String ret = "";
            if(godz<10)ret+="0"+(int)godz;
            else ret += (int)godz;
            ret+=":";
            if(min<10)ret+="0"+(int)min;
            else ret += (int)min;
            ret = "dzien "+(int)(wsp+1)+", "+ret;
            return ret;
        }
        
        public void wyczyscKolejkeStudentowDziekana()
        {
            for(int i=0; i<kolejkaDziekan.length(); i++)
            {
                    StudentDoDziekana student = kolejkaDziekan.removeFirst();
                    StudentDoDziekanaPrzyjscieEvent SGevent = new StudentDoDziekanaPrzyjscieEvent(this, "Student wraca", true);
                    //SGevent.schedule(new SimTime(presentTime().getTimeAsDouble()+4*60)); //4*60 czyli 4h-> student wraca po 1 dniu. 
                    double kiedyPrzyjdzie = student.getScheduleKolejnegoDnia();
                    student.wyslijTrace("Koniec pracy dziekana, wyrzucaja mnie z budynku.");
                    student.wyslijTrace("Przyjde ponownie: "+this.getCzasPoSchedule(kiedyPrzyjdzie) + " (" + 
                            kiedyPrzyjdzie + ")");
                    SGevent.schedule(student, new SimTime(kiedyPrzyjdzie));
            }
        }
} 
