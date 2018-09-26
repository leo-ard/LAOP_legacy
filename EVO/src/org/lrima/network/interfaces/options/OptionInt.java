package org.lrima.network.interfaces.options;

import javax.swing.*;

public class OptionInt implements Option<Integer> {
    private JSpinner spinner;

    public OptionInt(Integer defaultValue){
        this.spinner = new JSpinner(new SpinnerNumberModel(defaultValue.intValue(), 10, 10, 1));
    }

    public OptionInt(Integer defaultValue, int min, int max){
        this.spinner = new JSpinner(new SpinnerNumberModel(defaultValue.intValue(), min, max, 1));
    }

    public OptionInt(Integer defaultValue, int min, int max, int step){
        this.spinner = new JSpinner(new SpinnerNumberModel(defaultValue.intValue(), min, max, step));
    }

    @Override
    public Integer getValue() {
        return (Integer) this.spinner.getValue();
    }

    @Override
    public JComponent show() {
        return new JSpinner();
    }
}
