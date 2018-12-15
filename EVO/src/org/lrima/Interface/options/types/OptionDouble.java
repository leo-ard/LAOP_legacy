package org.lrima.Interface.options.types;

import org.lrima.Interface.options.Option;
import org.lrima.Interface.options.OptionValueChangeListener;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.util.prefs.Preferences;

public class OptionDouble implements Option<Double> {
    private JSpinner spinner;
    private SpinnerNumberModel model;

    public OptionDouble(SpinnerNumberModel spinnerNumberModel){
        this.model = spinnerNumberModel;
        this.spinner = new JSpinner(spinnerNumberModel);
        JComponent comp = spinner.getEditor();
        JFormattedTextField field = ((JSpinner.DefaultEditor) comp).getTextField();
        DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
        formatter.setCommitsOnValidEdit(true);
    }

    public OptionDouble(Double defaultValue){
        this(new SpinnerNumberModel(defaultValue.doubleValue(), null, null,1));
    }

    public OptionDouble(Double defaultValue, double min, double max){
        this(new SpinnerNumberModel(defaultValue.doubleValue(), min, max, 1));
    }

    public OptionDouble(Double defaultValue, double min, double max, double step){
        this(new SpinnerNumberModel(defaultValue.doubleValue(), min, max, step));
    }

    public OptionDouble(Double defaultValue, OptionDouble model){
        this(new SpinnerNumberModel(defaultValue, model.getModel().getMinimum(), model.getModel().getMaximum(), model.getModel().getStepSize()));
    }

    public SpinnerNumberModel getModel(){
        return (SpinnerNumberModel) this.spinner.getModel();
    }

    @Override
    public Double getValue() {
        return (Double) this.spinner.getValue();
    }

    @Override
    public JComponent show() {
        return this.spinner;
    }

    @Override
    public void addOptionValueChangeListener(OptionValueChangeListener listener) {
        final OptionDouble thisOption = this;
        this.spinner.addChangeListener(e -> {
            listener.optionChange(thisOption);
        });

    }

    @Override
    public void save(String key, Preferences preferences) {
        preferences.putDouble(key, this.getValue());
    }

    @Override
    public Class<Double> getClassValue() {
        return Double.class;
    }

    @Override
    public Option<Double> clone() {
        return new OptionDouble(this.model);
    }
}
