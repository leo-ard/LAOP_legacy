package org.lrima.Interface.options.types;

import org.lrima.Interface.options.Option;
import org.lrima.Interface.options.OptionValueChangeListener;
import org.lrima.core.UserPrefs;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

public class OptionFile implements Option<File> {
    private JFileChooser fileChooser;
    private File file;
    private JLabel label;

    public OptionFile(File path){
        this.file = path;
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(path);
    }


    @Override
    public File getValue() {
        return this.file;
    }

    @Override
    public JComponent show() {
        //Creates the panel containing the name of the file and and button to load
        JPanel component = new JPanel();
        label = new JLabel(file.getName());
        JButton loadButton = new JButton("Load");

        //Show the file chooser
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = fileChooser.showOpenDialog(null);

                //Sets the new map file to use
                if(response == JFileChooser.APPROVE_OPTION){
                    file = fileChooser.getSelectedFile();
                    label.setText(file.getName());
                    save(UserPrefs.KEY_MAP_TO_USE, UserPrefs.preferences);
                }
            }
        });

        component.add(label);
        component.add(loadButton);

        return component;
    }

    @Override
    public void save(String key, Preferences preferences) {
        preferences.put(key, this.getValue().getPath());
    }

    @Override
    public Class<File> getClassValue() {
        return File.class;
    }

    @Override
    public Option<File> clone() {
        return new OptionFile(this.file);
    }


    public void addOptionValueChangeListener(OptionValueChangeListener listener) {
        fileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.optionChange(OptionFile.this);
            }
        });
    }
}
