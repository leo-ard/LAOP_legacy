package org.lrima.espece.network.interfaces.options;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.scene.control.CheckBox;
import org.graalvm.compiler.nodes.calc.IntegerDivRemNode;

import javax.swing.*;

public class OptionBoolean implements Option<Boolean>{
    private JCheckBox checkbox;

    public OptionBoolean(Boolean defaultValue){
        checkbox = new JCheckBox("", defaultValue.booleanValue());
    }


    @Override
    public Boolean getValue() {
        return (Boolean) this.checkbox.isSelected();
    }

    @Override
    public JComponent show() {
        return this.checkbox;
    }
}
