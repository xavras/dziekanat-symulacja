package dziekanat;

import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.SimTime;
import java.io.File;


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

		exp.setShowProgressBar(true);
		exp.stop(new SimTime(60));
		exp.tracePeriod(new SimTime(0.0), new SimTime(60));
		
		exp.start();
		exp.report();	
		exp.finish();
		model.zamknijTrace();
		
	}
}
