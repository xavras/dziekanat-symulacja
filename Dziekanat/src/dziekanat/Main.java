package dziekanat;

import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.SimTime;
import symulacja.Dziekanat;


/**
 * Klasa odpowiadająca za uruchomienie aplikacji. 
 * Wykonywane są wszystkie czynności związane z utworzeniem nowego modelu i eksperymentu, połączeniu ich ze sobą, uruchomieniem symulacji oraz 
 * tworzeniem plików z raportem i trace'ami. 
 * 
 * @author Kachat j.w.
 */
public class Main {
	public static void main(java.lang.String[] args) {

		symulacja.Dziekanat model = new symulacja.Dziekanat(null, "Dziekanat", true, true); // null - nie ma macierzystego modelu
		Experiment exp = new Experiment("Symulacja pracy dziakanatu");
		model.connectToExperiment(exp);

                //5 dni * (godzinyPracy+0.5h)
                double czasSymulacji = 5*60.0*(0.5+Dziekanat.godzinaZamkniecia-Dziekanat.godzinaOtwarcia);
                
		exp.setShowProgressBar(true);
		exp.stop(new SimTime(czasSymulacji));
		exp.tracePeriod(new SimTime(0.0), new SimTime(czasSymulacji));
		
		exp.start();
		exp.report();	
		exp.finish();
		model.zamknijTrace();
		
	}
}
