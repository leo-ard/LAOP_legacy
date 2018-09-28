package org.lrima.network.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;

import org.lrima.Interface.options.OptionsDialog;
import org.lrima.network.annotations.AlgorithmInformation;
import org.lrima.Interface.options.Option;

public abstract class NeuralNetworkModel<T extends NeuralNetwork> {
	
	public static NeuralNetworkModel getInstanceOf(Class<? extends NeuralNetworkModel> modelClass) {
        try {
            return modelClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
	
    /**
     * Characteristics of the algorithm. See {@link #getDefaultOptions} to know witch variable is available.
     */
    protected LinkedHashMap<String, Option> options;
    protected NeuralNetworkSuperviser superviser = new NaturalSelectionSupervisor();
    private OptionsDialog optionsDialog;

    /**
     * Set the default values of the hashmap.
     *
     * @return A hashmap populated with default values
     */
    protected abstract LinkedHashMap<String, Option> getDefaultOptions();

    public void displayOptions(){
        if(optionsDialog == null)
            optionsDialog = new OptionsDialog(this.getClass().getAnnotation(AlgorithmInformation.class).name(), this.getOptions());
        optionsDialog.setVisible(true);
    }

    public LinkedHashMap<String, Option> getOptions() {
        if(options == null){
            options = new LinkedHashMap<>();
            options = getDefaultOptions();
        }
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
