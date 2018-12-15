package org.lrima.Interface.options.types;

import org.lrima.Interface.options.Option;
import org.lrima.Interface.options.OptionValueChangeListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

public class OptionBoolean implements Option<Boolean> {
    private JCheckBox checkbox;
    private Boolean defaultValue;

    public OptionBoolean(Boolean defaultValue){
        checkbox = new JCheckBox("", defaultValue.booleanValue());
        this.defaultValue = defaultValue;
    }


    @Override
    public Boolean getValue() {
        return (Boolean) this.checkbox.isSelected();
    }

    @Override
    public JComponent show() {
        return this.checkbox;
    }

    @Override
    public void save(String key, Preferences preferences) {
        preferences.putBoolean(key, this.getValue());
    }

    @Override
    public Class<Boolean> getClassValue() {
        return Boolean.class;
    }

    @Override
    public Option<Boolean> clone() {
        return new OptionBoolean(defaultValue);
    }


    public void addOptionValueChangeListener(OptionValueChangeListener listener) {
        final OptionBoolean thisObject = this;
        checkbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.optionChange(thisObject);
            }
        });
    }
}
