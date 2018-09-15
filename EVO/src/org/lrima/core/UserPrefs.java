package org.lrima.core;

import java.util.prefs.Preferences;

public class UserPrefs {
    public static final String SRC_LRIMA = "/images/LRIMA.png";

    public static final String SRC_TOOLS_START = "/images/icons/tools/start.gif";
    public static final String SRC_TOOLS_MULTIPLE_LINE = "/images/icons/tools/multipleLines.png";
    public static final String SRC_TOOLS_LINE = "/images/icons/tools/line.png";
    public static final String SRC_TOOLS_REACTANGLE = "/images/icons/tools/rectangle.png";
    public static final String SRC_TOOLS_SELECTION = "/images/icons/tools/selection.png";

    public static int NUMBERCARS;
    public static int VITESSE_VOITURE;
    public static double TURNRATE;

    public static boolean REAL_TIME;
    public static boolean FOLLOW_BEST;

    public static boolean SHOW_WINDOW_GRAPHIQUE;
    public static boolean SHOW_WINDOW_NEURAL_NETWORK;
    public static boolean SHOW_WINDOW_ESPECE_INFO;

    //en millis
    public static int TIME_LIMIT = 10_000;

    //Si on devrait utiliser le meilleur NN sauvegarde
    public static boolean USE_BEST = false;

    public static Preferences preferences;

    //Preferences keys
    final public static String KEY_NUMBER_OF_CAR = "NUMBER_OF_CAR";
    final public static String KEY_CAR_SPEED = "CAR_SPEED";
    final public static String KEY_TURN_RATE = "TURN_RATE";

    final public static String KEY_TIME_LIMIT = "TIME_LIMIT";
    final public static String KEY_USE_LAST_SAVED = "USE_LAST_SAVED";

    final public static String KEY_REAL_TIME = "REAL_TIME";
    final public static String KEY_FOLLOW_BEST = "FOLLOW_BEST";

    final public static String KEY_WINDOW_GRAPHIQUE = "WINDOW_GRAPHIQUE";
    final public static String KEY_WINDOW_NEURAL_NET = "WINDOW_NEURAL_NET";
    final public static String KEY_WINDOW_ESPECE_INFO = "WINDOW_ESPECE_INFO";

    //Defaults
    final public static int DEFAULT_NUMBER_OF_CAR = 200;
    final public static int DEFAULT_CAR_SPEED = 50;
    final public static double DEFAULT_TURN_RATE = 0.5;

    final public static int DEFAULT_TIME_LIMIT = 60000;
    final public static boolean DEFAULT_USE_LAST_SAVED = false;

    final public static boolean DEFAULT_REAL_TIME = false;
    final public static boolean DEFAULT_RANDOM_MAP = false;
    final public static boolean DEFAULT_FOLLOW_BEST = true;

    final public static boolean DEFAULT_WINDOW_GRAPHIQUE = false;
    final public static boolean DEFAULT_WINDOW_NEURAL_NET = false;
    final public static boolean DEFAULT_WINDOW_ESPECE_INFO = false;

    public UserPrefs(){
        preferences = Preferences.userRoot().node(this.getClass().getName());
    }

    public static void load(){
        NUMBERCARS = preferences.getInt(KEY_NUMBER_OF_CAR, DEFAULT_NUMBER_OF_CAR);
        VITESSE_VOITURE = preferences.getInt(KEY_CAR_SPEED, DEFAULT_CAR_SPEED);
        TURNRATE = preferences.getDouble(KEY_TURN_RATE, DEFAULT_TURN_RATE);

        TIME_LIMIT = preferences.getInt(KEY_TIME_LIMIT, DEFAULT_TIME_LIMIT);
        USE_BEST = preferences.getBoolean(KEY_USE_LAST_SAVED, DEFAULT_USE_LAST_SAVED);

        REAL_TIME = preferences.getBoolean(KEY_REAL_TIME, DEFAULT_REAL_TIME);
        FOLLOW_BEST = preferences.getBoolean(KEY_FOLLOW_BEST, DEFAULT_FOLLOW_BEST);

        SHOW_WINDOW_GRAPHIQUE = preferences.getBoolean(KEY_WINDOW_GRAPHIQUE, DEFAULT_WINDOW_GRAPHIQUE);
        SHOW_WINDOW_NEURAL_NETWORK = preferences.getBoolean(KEY_WINDOW_NEURAL_NET, DEFAULT_WINDOW_NEURAL_NET);
        SHOW_WINDOW_ESPECE_INFO = preferences.getBoolean(KEY_WINDOW_ESPECE_INFO, DEFAULT_WINDOW_ESPECE_INFO);
    }
}
