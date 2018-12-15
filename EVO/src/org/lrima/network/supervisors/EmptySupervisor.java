package org.lrima.network.supervisors;

import org.lrima.espece.Espece;
import org.lrima.network.interfaces.NeuralNetworkModel;
import org.lrima.network.interfaces.NeuralNetworkSuperviser;
import org.lrima.simulation.Simulation;

import java.util.ArrayList;

public class EmptySupervisor implements NeuralNetworkSuperviser {

    @Override
    public ArrayList<Espece> alterEspeceListAtGenerationFinish(ArrayList<Espece> especes, Simulation simulation) {
        return especes;
    }
}
