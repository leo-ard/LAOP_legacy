package org.lrima.Interface.options;

import javax.swing.*;
import java.util.prefs.Preferences;

public interface Option<T> {
    T getValue();
    JComponent show();
    void addOptionValueChangeListener(OptionValueChangeListener listener);

    void save(String key, Preferences preferences);
    Class<T> getClassValue();
}
