package org.lrima.core;

import org.lrima.Interface.options.*;
import org.lrima.Interface.options.types.*;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.prefs.Preferences;

public class UserPrefs {
    public static final String SRC_LRIMA = "/images/LRIMA.png";

    public static final String SRC_TOOLS_START = "/images/icons/tools/start.gif";
    public static final String SRC_TOOLS_MULTIPLE_LINE = "/images/icons/tools/multipleLines.png";
    public static final String SRC_TOOLS_LINE = "/images/icons/tools/line.png";
    public static final String SRC_TOOLS_REACTANGLE = "/images/icons/tools/rectangle.png";
    public static final String SRC_TOOLS_SELECTION = "/images/icons/tools/selection.png";


    public static Preferences preferences = Preferences.userRoot().node(UserPrefs.class.getName());
    private static LinkedHashMap<String, Option> allOptions = new LinkedHashMap<>();
    private static int NUMBERCARS;
    private static int VITESSE_VOITURE;
    private static double TURNRATE;

    private static boolean REAL_TIME;
    private static boolean FOLLOW_BEST;

    private static boolean SHOW_WINDOW_GRAPHIQUE;
    private static boolean SHOW_WINDOW_NEURAL_NETWORK;
    private static boolean SHOW_WINDOW_ESPECE_INFO;

    private static String MAP_TO_USE_PATH;

    //en millis
    private static int TIME_LIMIT = 10_000;

    //Si on devrait utiliser le meilleur NN sauvegarde
    private static boolean USE_BEST = false;

    //Preferences keys
    //final public static String KEY_NUMBER_OF_CAR = "NUMBER_OF_CAR";
    final public static String KEY_CAR_SPEED = "CAR_SPEED";
    final public static String KEY_TURN_RATE = "TURN_RATE";
    //final public static String KEY_NUMBER_SENSOR = "NUMBER_SENSOR";

    final public static String KEY_NUMBER_SIMULATION = "NUMBER_SIMULATION";
    final public static String KEY_NUMBER_GENERATION_PER_SIMULATION = "NUMBER_GENERATION";
          
    final public static String KEY_TIME_LIMIT = "TIME_LIMIT_IN_SECONDS";
    final public static String KEY_USE_LAST_SAVED = "USE_LAST_SAVED";
          
    final public static String KEY_REAL_TIME = "REAL_TIME";
    final public static String KEY_FOLLOW_BEST = "FOLLOW_BEST";
          
    final public static String KEY_WINDOW_GRAPHIQUE = "WINDOW_GRAPHIQUE";
    final public static String KEY_WINDOW_NEURAL_NET = "WINDOW_NEURAL_NET";
    final public static String KEY_WINDOW_ESPECE_INFO = "WINDOW_ESPECE_INFO";
          
    final public static String KEY_MAP_TO_USE = "MAP_TO_USE";

    //Defaults
    final private static HashMap<String, Option> defaultValues = new HashMap<>();

    static{
        //defaultValues.put(KEY_NUMBER_OF_CAR, new OptionInt(200, 1, 10_000, 10));
        defaultValues.put(KEY_CAR_SPEED,  new OptionInt(50));
        defaultValues.put(KEY_TURN_RATE, new OptionDouble(0.5, 0, 1, 0.1));
        defaultValues.put(KEY_TIME_LIMIT, new OptionInt(60_000, 0, 1_000_000, 10_000));
        defaultValues.put(KEY_USE_LAST_SAVED, new OptionBoolean(false));
        defaultValues.put(KEY_REAL_TIME, new OptionBoolean(false));
        defaultValues.put(KEY_FOLLOW_BEST, new OptionBoolean(true));
        defaultValues.put(KEY_WINDOW_GRAPHIQUE, new OptionBoolean(false));
        defaultValues.put(KEY_WINDOW_NEURAL_NET, new OptionBoolean(false));
        defaultValues.put(KEY_WINDOW_ESPECE_INFO, new OptionBoolean(false));
        defaultValues.put(KEY_MAP_TO_USE, new OptionFile(new File("./default.map")));
        defaultValues.put(KEY_NUMBER_SIMULATION, new OptionInt(1, 1, 10, 1));
        defaultValues.put(KEY_NUMBER_GENERATION_PER_SIMULATION, new OptionInt(10, 1, 1000, 1));
        //defaultValues.put(KEY_NUMBER_SENSOR, new OptionInt(5, 1, 180, 1));
    }

    public static int getInt(String key){
        return UserPrefs.<Integer>get(key).getValue();
    }

    public static double getDouble(String key){
        return UserPrefs.<Double>get(key).getValue();
    }

    public static boolean getBoolean(String key){
        return UserPrefs.<Boolean>get(key).getValue();
    }

    public static String getString(String key){
        return UserPrefs.<String>get(key).getValue();
    }

    public static File getFile(String key){
        return UserPrefs.<File>get(key).getValue();
    }


    private static <T> Option<T> get(String key){
        Option<T> currentOption = allOptions.get(key);

        if(currentOption == null){
            if(defaultValues.get(key).getClassValue() == String.class)
                currentOption = (Option<T>) new OptionString(preferences.get(key, (String) defaultValues.get(key).getValue()));
            else if(defaultValues.get(key).getClassValue() == Integer.class)
                currentOption = (Option<T>) new OptionInt(preferences.getInt(key, (int) defaultValues.get(key).getValue()), (OptionInt) defaultValues.get(key));
            else if(defaultValues.get(key).getClassValue() == Double.class)
                currentOption = (Option<T>) new OptionDouble(preferences.getDouble(key, (double) defaultValues.get(key).getValue()), (OptionDouble) defaultValues.get(key));
            else if(defaultValues.get(key).getClassValue() == Boolean.class)
                currentOption = (Option<T>) new OptionBoolean(preferences.getBoolean(key, (boolean) defaultValues.get(key).getValue()));
            else if(defaultValues.get(key).getClassValue() == File.class) {
                currentOption = (Option<T>) new OptionFile(new File(preferences.get(key, ((File) defaultValues.get(key).getValue()).getPath())));
            }
            currentOption.addOptionValueChangeListener(option -> option.save(key, preferences));
            allOptions.put(key, currentOption);
        }

        return currentOption;
    }

    public static void set(String key, Option value){
        allOptions.put(key, value);
        value.addOptionValueChangeListener(option -> option.save(key, preferences));
        value.save(key, preferences);
    }

    public static JComponent getComponent(String key){
        return get(key).show();
    }

    public static Option getOption(String key){
        return get(key);
    }
}
