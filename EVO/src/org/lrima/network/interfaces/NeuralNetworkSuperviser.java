package org.lrima.network.interfaces;

import org.lrima.espece.Espece;
import org.lrima.simulation.Simulation;

import java.io.Serializable;
import java.util.ArrayList;

public interface NeuralNetworkSuperviser extends Serializable {
    public ArrayList<Espece> alterEspeceListAtGenerationFinish(ArrayList<Espece> especes, Simulation simulation);
}
