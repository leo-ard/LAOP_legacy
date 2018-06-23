package simulation;

import espece.Espece;
import espece.EspeceInfo;
import simulation.Simulation;

import java.util.ArrayList;

public class SimulationInfos {
    private Simulation simulation;
    private ArrayList<ArrayList<EspeceInfo>> espcesGen;
    private int generationCount;

    public SimulationInfos(Simulation s){
        this.simulation = s;
        this.espcesGen = new ArrayList<ArrayList<EspeceInfo>>();
    }

    public void addGeneration(ArrayList<Espece> especes){
        ArrayList<EspeceInfo> curgen = new ArrayList<EspeceInfo>();
        for(Espece e : especes){
            curgen.add(new EspeceInfo(e));
        }
        espcesGen.add(curgen);
        generationCount++;
    }


    public ArrayList<EspeceInfo> getLastGeneration() {
        return espcesGen.get(generationCount);
    }

    public ArrayList<EspeceInfo> get(int nb) {
        if(nb < 0 || nb >= this.getSize()){
            return new ArrayList<EspeceInfo>();
        }
        return espcesGen.get(nb);
    }

    public int getSize(){
        return espcesGen.size();
    }

    public int getGenerationCount() {
        return generationCount;
    }
}
