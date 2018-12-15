package org.lrima.Interface.options;

import javax.swing.*;
import java.io.Serializable;
import java.util.prefs.Preferences;

public interface Option<T> extends Serializable {
    T getValue();
    JComponent show();
    void addOptionValueChangeListener(OptionValueChangeListener listener);

    void save(String key, Preferences preferences);
    Class<T> getClassValue();
    Option<T> clone();
}
