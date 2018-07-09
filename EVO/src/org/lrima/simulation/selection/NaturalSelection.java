package org.lrima.simulation.selection;

import java.util.ArrayList;
import java.util.Comparator;

import org.lrima.core.UserPrefs;
import org.lrima.espece.Espece;
import org.lrima.map.Map;
import org.lrima.simulation.FrameManager;
import org.lrima.utils.Random;

public class  NaturalSelection {
	ArrayList<Espece> especes;
	public static Espece best = null;
	
	public NaturalSelection(ArrayList<Espece> especes) {
		this.especes = especes;
	}
	
	public void sort() {
		especes.sort(new Comparator<Espece>() {
			@Override
			public int compare(Espece e1, Espece e2) {
				if(e1.getFitness() == e2.getFitness())
					return 0;
				return e1.getFitness()>e2.getFitness()?-1:1;
			}
		});
	}
	
	public ArrayList<Espece> getMutatedList(Map m) {
		this.sort();
		this.getBest();
		FrameManager.addGeneration(especes);
		this.kill50();
		this.resetList(m);
		this.repopulate(m);

		return especes;

	}

	private void getBest(){
	    best = especes.get(0);
    }
	
	private void repopulate(Map m) {

		int numberOfCar = UserPrefs.preferences.getInt(UserPrefs.KEY_NUMBER_OF_CAR, UserPrefs.DEFAULT_NUMBER_OF_CAR);

		while(especes.size() < numberOfCar) {
			int nb =  (int)Math.pow(Random.getRandomDoubleValue(Math.sqrt(especes.size())), 2);

			Espece e = new Espece(m.depart, m.orientation, especes.get(nb), m);
			e.mutate();
			especes.add(e);
		}
		
	}

	private void kill50() {
		//just a test
        int numberOfCar = UserPrefs.preferences.getInt(UserPrefs.KEY_NUMBER_OF_CAR, UserPrefs.DEFAULT_NUMBER_OF_CAR);
		while(especes.size() >= numberOfCar*0.5) {
			int nb =  (int) Math.sqrt(Random.getRandomIntegerValue(especes.size()*especes.size()));
			especes.remove(nb);
		}
	}

	public void resetList(Map m) {
		for(int i = 0; i < especes.size(); i++) {
			especes.get(i).tpLikeNew(m.depart, m.orientation);
		}
	}
}
