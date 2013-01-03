package symulacja;


import java.io.File;

import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import desmoj.core.report.HTMLTraceOutput;
import java.util.ArrayList;

import java.util.LinkedList;
import symulacja.dziekan.Dziekan;
import symulacja.dziekan.DziekanPrzyjscieEvent;
import symulacja.student_do_dziekana.StudentDoDziekana;
import symulacja.student_do_dziekana.StudentDoDziekanaGeneratorEvent;

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
    protected static double czasPrzybyciaStudentaDouble = 0.5;
        
    protected RealDistExponential czasNowejSprawyPozastudenckiej;
    protected static double czasNowejSprawyPozastudenckiejDouble = 5;
    
    protected RealDistExponential czasTrwaniaPrzerwy;
    protected static double czasTrwaniaPrzerwyDouble = 5;
    
    protected static double czasMiedzyPrzerwami =10; //czas po ktorym pracownik dziekanatu stwierdza ze musi isc na przerwae
        
    protected RealDistUniform czasObslugi;
    protected static double minCzasObslugi = 2.0;
    protected static double maxCzasObslugi = 5.0;
    
    //listy podan do dziekana oczekujacych na podpisanie i podpisanych
    public LinkedList<Integer> podaniaLista;
    public LinkedList<Integer> gotowePodaniaLista; 
    
    //alternatywna wersja, trochę chyba prostsza
    public ListaPodan listaPodan;
      
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
		StudentGeneratorEvent generatorStudentow = new StudentGeneratorEvent(this, "StudentGenerator", true);
		generatorStudentow.schedule(new SimTime(0.0));
                
                SprawyPozastudenckieGeneratorEvent generatorSpraw = new SprawyPozastudenckieGeneratorEvent(this,"PetentGenerator", true);
                generatorSpraw.schedule(new SimTime(0.0));
                
                StudentDoDziekanaGeneratorEvent generatorDziekan = new StudentDoDziekanaGeneratorEvent(this, "StudentDoDziekanaGen", true);
                generatorDziekan.schedule(new SimTime(0.0));
                
                DziekanPrzyjscieEvent przyjscieDziekana = new DziekanPrzyjscieEvent(this, "Przyjscie Dziekana", true);
                przyjscieDziekana.schedule(dziekan, new SimTime(Dziekan.godzinaRozpoczecia));
                
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
} 
