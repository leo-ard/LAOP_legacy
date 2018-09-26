package org.lrima.network.interfaces.options;

import javax.swing.*;

public class OptionDouble implements Option<Double> {
    private JSpinner spinner;

    public OptionDouble(Double defaultValue){
        this.spinner = new JSpinner(new SpinnerNumberModel(defaultValue.doubleValue(), 10, 10, 1));
    }

    public OptionDouble(Double defaultValue, double min, double max){
        this.spinner = new JSpinner(new SpinnerNumberModel(defaultValue.doubleValue(), min, max, 1));
    }

    public OptionDouble(Double defaultValue, double min, double max, double step){
        this.spinner = new JSpinner(new SpinnerNumberModel(defaultValue.doubleValue(), min, max, step));
    }

    @Override
    public Double getValue() {
        return (Double) this.spinner.getValue();
    }

    @Override
    public JComponent show() {
        return new JSpinner();
    }
}
