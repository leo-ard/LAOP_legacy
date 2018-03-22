package simulation.selection;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;

import core.CONSTANTS;
import espece.Espece;
import map.Map;
import utils.Random;

public class  NaturalSelection {
	ArrayList<Espece> especes;
	
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
		this.kill50();
		this.resetList(m);
		this.repopulate(m);
		//System.out.println(especes.size());
		return especes;
	}
	
	private void repopulate(Map m) {
		
		while(especes.size() < CONSTANTS.NUMBERCARS) {
			//System.out.println(":::::::::"+especes.get(0).getFitness());
			//System.out.println("------"+especes.get(especes.size()-1).getFitness());
			int nb =  (int)Math.pow(Random.getRandomDoubleValue(Math.sqrt(especes.size())), 2);
			//System.out.println(nb);
			//System.out.println(nb);
			Espece e = new Espece(m.depart, m.orientation, especes.get(nb));
			e.mutate();
			especes.add(e);
		}
		
	}

	private void kill50() {
		
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
