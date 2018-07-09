package org.lrima.core;

import java.util.prefs.Preferences;

public class UserPrefs {
    public static String SRC_VOITURE;
    public static final String SRC_LRIMA = "/images/LRIMA.png";
    public static int NUMBERCARS;
    public static int VITESSE_VOITURE;
    public static double TURNRATE;

    public static boolean REAL_TIME;
    public static boolean RANDOM_MAP;

    public static boolean SHOW_WINDOW_GRAPHIQUE;
    public static boolean SHOW_WINDOW_NEURAL_NETWORK;

    //en millis
    public static int TIME_LIMIT = 10_000;

    //Si on devrait utiliser le meilleur NN sauvegarde
    public static boolean USE_BEST = false;

    public static Preferences preferences;

    //Preferences keys
    final public static String KEY_CAR_IMAGE_URL = "CAR_IMAGE_URL";
    final public static String KEY_NUMBER_OF_CAR = "NUMBER_OF_CAR";
    final public static String KEY_CAR_SPEED = "CAR_SPEED";
    final public static String KEY_TURN_RATE = "TURN_RATE";

    final public static String KEY_TIME_LIMIT = "TIME_LIMIT";
    final public static String KEY_USE_LAST_SAVED = "USE_LAST_SAVED";

    final public static String KEY_REAL_TIME = "REAL_TIME";
    final public static String KEY_RANDOM_MAP = "RANDOM_MAP";

    final public static String KEY_WINDOW_GRAPHIQUE = "WINDOW_GRAPHIQUE";
    final public static String KEY_WINDOW_NEURAL_NET = "WINDOW_NEURAL_NET";

    //Defaults
    final public static String DEFAULT_CAR_IMAGE_URL = "/images/voiture.png";
    final public static int DEFAULT_NUMBER_OF_CAR = 200;
    final public static int DEFAULT_CAR_SPEED = 50;
    final public static double DEFAULT_TURN_RATE = 0.5;

    final public static int DEFAULT_TIME_LIMIT = 60000;
    final public static boolean DEFAULT_USE_LAST_SAVED = false;

    final public static boolean DEFAULT_REAL_TIME = false;
    final public static boolean DEFAULT_RANDOM_MAP = false;

    final public static boolean DEFAULT_WINDOW_GRAPHIQUE = false;
    final public static boolean DEFAULT_WINDOW_NEURAL_NET = false;

    public UserPrefs(){
        preferences = Preferences.userRoot().node(this.getClass().getName());
    }

    public static void load(){
        NUMBERCARS = preferences.getInt(KEY_NUMBER_OF_CAR, DEFAULT_NUMBER_OF_CAR);
        VITESSE_VOITURE = preferences.getInt(KEY_CAR_SPEED, DEFAULT_CAR_SPEED);
        TURNRATE = preferences.getDouble(KEY_TURN_RATE, DEFAULT_TURN_RATE);
        SRC_VOITURE = preferences.get(KEY_CAR_IMAGE_URL, DEFAULT_CAR_IMAGE_URL);

        TIME_LIMIT = preferences.getInt(KEY_TIME_LIMIT, DEFAULT_TIME_LIMIT);
        USE_BEST = preferences.getBoolean(KEY_USE_LAST_SAVED, DEFAULT_USE_LAST_SAVED);

        REAL_TIME = preferences.getBoolean(KEY_REAL_TIME, DEFAULT_REAL_TIME);
        RANDOM_MAP = preferences.getBoolean(KEY_RANDOM_MAP, DEFAULT_RANDOM_MAP);

        SHOW_WINDOW_GRAPHIQUE = preferences.getBoolean(KEY_WINDOW_GRAPHIQUE, DEFAULT_WINDOW_GRAPHIQUE);
        SHOW_WINDOW_NEURAL_NETWORK = preferences.getBoolean(KEY_WINDOW_NEURAL_NET, DEFAULT_WINDOW_NEURAL_NET);
    }
}
