package org.lrima.network.algorithms.manual_algorithm;

import org.lrima.Interface.options.Option;
import org.lrima.network.annotations.AlgorithmInformation;
import org.lrima.network.interfaces.NeuralNetwork;
import org.lrima.network.interfaces.NeuralNetworkReceiver;
import org.lrima.network.interfaces.NeuralNetworkTransmitter;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;


public class ManualAlgorithm extends NeuralNetwork {

    public ManualAlgorithm(LinkedHashMap<String, Option> options) {
        super(options);
    }

    @Override
    public void feedForward() {
        //Valeurs entre 0 et 1 des capteurs de la voiture.     1 -> loin    0 -> proche
        double[] inputs = this.getValeurCapteurs();
        double valeurRoueDroite = 1;
        double valeurRoueGauche = 1;

        ////  VOTRE ALGORITHME ICI  /////
        // Modifiez valeurRoueDroite et valeurRoueGauche en fonction des inputs




        ///////



        //Les valeurs des roues sont envoyées à la voiture
        this.receiver.setNeuralNetworkOutput(new double[]{valeurRoueDroite, valeurRoueGauche});
    }

    /**
     * Pour avoir les valeurs des capteurs dans un tableau
     * @return un tableau contenant les valeurs des capteurs
     */
    private double[] getValeurCapteurs(){
        double[] capteursValue = new double[this.transmitters.size()];
        for(int i = 0 ; i < capteursValue.length ; i++){
            capteursValue[i] = this.transmitters.get(i).getNeuralNetworkInput();
        }
        return capteursValue;
    }

    @Override
    public NeuralNetwork crossOver(NeuralNetwork network1, NeuralNetwork network2) {
        return new ManualAlgorithm(this.options);
    }
}
