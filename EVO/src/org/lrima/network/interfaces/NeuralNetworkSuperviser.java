package org.lrima.espece.network.interfaces;

import org.lrima.espece.Espece;
import org.lrima.simulation.Simulation;

import java.util.ArrayList;

public interface NeuralNetworkSuperviser {
    public ArrayList<Espece> alterEspeceListAtGenerationFinish(ArrayList<Espece> especes, Simulation simulation);
}
