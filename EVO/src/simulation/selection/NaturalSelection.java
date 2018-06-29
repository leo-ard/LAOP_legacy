package simulation.selection;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;

import core.CONSTANTS;
import espece.Espece;
import map.Map;
import simulation.FrameManager;
import utils.Random;

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
		
		while(especes.size() < CONSTANTS.NUMBERCARS) {
			int nb =  (int)Math.pow(Random.getRandomDoubleValue(Math.sqrt(especes.size())), 2);

			Espece e = new Espece(m.depart, m.orientation, especes.get(nb), m);
			e.mutate();
			especes.add(e);
		}
		
	}

	private void kill50() {
		//just a test
		while(especes.size() >= CONSTANTS.NUMBERCARS*0.5) {
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
