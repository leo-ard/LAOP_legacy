package org.lrima.Interface.options.types;

import org.lrima.Interface.options.Option;
import org.lrima.Interface.options.OptionValueChangeListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.prefs.Preferences;

public class OptionString implements Option<String> {
    JTextField textField;
    String defaultValue;

    public OptionString(String defaultValue){
        this.defaultValue = defaultValue;
        textField = new JTextField(defaultValue);
    }

    @Override
    public String getValue() {
        return textField.getText();
    }

    @Override
    public JComponent show() {
        return textField;
    }

    @Override
    public void addOptionValueChangeListener(OptionValueChangeListener listener) {
        final OptionString thisObject = this;
        this.textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            private void warn(){
                listener.optionChange(thisObject);
            }
        });
    }

    @Override
    public void save(String key, Preferences preferences) {
        preferences.put(key, this.getValue());
    }

    @Override
    public Class<String> getClassValue() {
        return String.class;
    }

    @Override
    public Option<String> clone() {
        return new OptionString(defaultValue);
    }
}
