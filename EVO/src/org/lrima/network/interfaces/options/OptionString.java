package org.lrima.espece.network.interfaces.options;

import javax.swing.*;
import java.awt.*;

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
