package org.lrima.network.interfaces.options;

import javax.swing.*;

public class OptionString implements Option<String>{
    JTextField textField;

    public OptionString(String defaultValue){
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
}
