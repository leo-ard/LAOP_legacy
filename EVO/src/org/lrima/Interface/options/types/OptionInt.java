package org.lrima.Interface.options.types;

import org.lrima.Interface.options.Option;
import org.lrima.Interface.options.OptionValueChangeListener;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.util.prefs.Preferences;

public class OptionInt implements Option<Integer> {
    private JSpinner spinner;
    private SpinnerNumberModel model;

    private OptionInt(SpinnerNumberModel spinnerNumberModel) {
        this.model = spinnerNumberModel;
        createSpinner(spinnerNumberModel);
    }

    public OptionInt(Integer defaultValue){
        this(new SpinnerNumberModel(defaultValue.intValue(), null, null, 1));
    }

    public OptionInt(Integer defaultValue, int min, int max){
        this(new SpinnerNumberModel(defaultValue.intValue(), min, max, 1));
    }

    public OptionInt(Integer defaultValue, int min, int max, int step){
        this(new SpinnerNumberModel(defaultValue.intValue(), min, max, step));
    }

    public OptionInt(Integer defaultValue, OptionInt option){
        Comparable<Integer> maxComparator = (Comparable<Integer>) option.getModel().getMaximum();
        Comparable<Integer> minComparator = (Comparable<Integer>) option.getModel().getMinimum();
        SpinnerNumberModel model = option.getModel();

        if(maxComparator != null && minComparator != null && (maxComparator.compareTo(defaultValue) < 0 || minComparator.compareTo(defaultValue) > 0))
            defaultValue = option.getValue();

        this.model = new SpinnerNumberModel(defaultValue, model.getMinimum(), model.getMaximum(), model.getStepSize());
        createSpinner(model);
    }

    private void createSpinner(SpinnerNumberModel spinnerNumberModel){
        this.spinner = new JSpinner(spinnerNumberModel);
        JComponent comp = spinner.getEditor();
        JFormattedTextField field = ((JSpinner.DefaultEditor) comp).getTextField();
        DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
        formatter.setCommitsOnValidEdit(true);
    }

    private SpinnerNumberModel getModel(){
        return (SpinnerNumberModel) this.spinner.getModel();
    }

    @Override
    public Integer getValue() {
        return (Integer) this.spinner.getValue();
    }

    @Override
    public JComponent show() {
        return this.spinner;
    }

    @Override
    public void addOptionValueChangeListener(OptionValueChangeListener listener) {
        this.spinner.addChangeListener(e -> {
            listener.optionChange(OptionInt.this);
        });
    }

    @Override
    public void save(String key, Preferences preferences) {
        preferences.putInt(key, this.getValue());
    }

    @Override
    public Class<Integer> getClassValue() {
        return Integer.class;
    }

    @Override
    public Option<Integer> clone() {
        OptionInt a = new OptionInt(this.model);

        return a;
    }

    public JSpinner getSpinner() {
        return spinner;
    }
}
