package org.lrima.network.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.lrima.network.annotations.AlgorithmInformation;
import org.lrima.network.interfaces.options.Option;

public abstract class NeuralNetworkModel<T extends NeuralNetwork> {
    /**
     * Characteristics of the algorithm. See {@link #getDefaultOptions} to know witch variable is available.
     */
    protected LinkedHashMap<String, Option> options = new LinkedHashMap<>();
    protected NeuralNetworkSuperviser superviser = new NaturalSelectionSupervisor();

    /**
     * Set the default values of the hashmap.
     *
     * @return A hashmap populated with default values
     */
    public abstract LinkedHashMap<String, Option> getDefaultOptions();


    public LinkedHashMap<String, Option> getOptions() {
        if(options == null)
            return getDefaultOptions();
        return options;
    }

    public NeuralNetworkSuperviser getSuperviser() {
        return superviser;
    }

    /**
     * Gets a new instance of the neural network.
     *
     * @return a instance of T
     */
    public T getInstance(){
        try {
            return (T) getNeuralNetworkClass().getConstructor(new Class[]{LinkedHashMap.class}).newInstance(getOptions());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            System.err.println("There was an error while instancing the neural network");
            e.printStackTrace();
        }
        return null;
    }

    public AlgorithmInformation getAlgorithmInformationAnnotation() {
        return this.getClass().getAnnotation(AlgorithmInformation.class);
    }


    /**
     * Set this value to put the main Class of your algorithm
     *
     * @return the main class of the algorithm
     */
    public abstract Class<T> getNeuralNetworkClass();
}
