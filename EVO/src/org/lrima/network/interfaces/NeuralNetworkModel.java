package org.lrima.network.interfaces;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;

import org.lrima.Interface.options.OptionsDialog;
import org.lrima.Interface.options.types.OptionInt;
import org.lrima.network.annotations.AlgorithmInformation;
import org.lrima.Interface.options.Option;
import org.lrima.network.supervisors.NaturalSelectionSupervisor;

public abstract class NeuralNetworkModel<T extends NeuralNetwork> implements Serializable {
    public static String KEY_NB_CARS = "NB_CARS";


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
    private String name = null;
    protected LinkedHashMap<String, Option> options;
    protected NeuralNetworkSuperviser superviser = new NaturalSelectionSupervisor();
    private LinkedHashMap<String, Option> simulationSettings;

    private OptionsDialog optionsDialog;

    public NeuralNetworkModel(){
        this(null);
    }
    public NeuralNetworkModel(String name){
        this.name = name;
    }

    /**
     * Set the default values of the hashmap.
     *
     * @return A hashmap populated with default values
     */
    protected abstract LinkedHashMap<String, Option> getDefaultOptions();

    public void displayOptions(){
        if(optionsDialog == null) {
            optionsDialog = new OptionsDialog(this.getClass().getAnnotation(AlgorithmInformation.class).name(), this.getOptions());
            optionsDialog.addTab("Simulations", this.getSimulationOption());
        }
        optionsDialog.setVisible(true);
    }

    public LinkedHashMap<String, Option> getOptions() {
        if(options == null){
            options = new LinkedHashMap<>();
            options = getDefaultOptions();
        }
        return options;
    }

    private LinkedHashMap<String, Option> getDefaultSimulationSettings() {
        simulationSettings.put(NeuralNetworkModel.KEY_NB_CARS, new OptionInt(50, 0, 10000, 1));


        //met les autres settings de simulation ici. Accede dans simulation avec algorithmeModel.getSimualtionOption(key)

        return simulationSettings;
    }

    public LinkedHashMap<String, Option> getSimulationOption(){
        if(simulationSettings == null){
            simulationSettings = new LinkedHashMap<>();
            simulationSettings = getDefaultSimulationSettings();
        }

        return simulationSettings;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getSimulationOption(String key) {
        return this.getSimulationOption().get(key).getValue();
    }
}
