/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package symulacja.dziekan;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

/**
 *
 * @author lukasz
 */
public class Dziekan extends Entity{
    
    private StudentDoDziekana aktualnyStudent;
    public static int godzinaRozpoczecia;
    public static int godzinaZakonczenia;
    private boolean zajety;
    //public Queue<Podania> listaPodan;
    public static float czasObslugi;
    public static float czasPodpisywania;
    
    public Dziekan(Model wlasciciel, String nazwa, boolean pokazTrace, int kierunek) {
		super(wlasciciel, nazwa, pokazTrace);		
		aktualnyStudent = null;
                zajety = false;
	}
}
