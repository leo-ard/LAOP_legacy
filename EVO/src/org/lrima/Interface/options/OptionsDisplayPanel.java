package org.lrima.Interface.options;

import org.lrima.Interface.options.types.OptionBoolean;
import org.lrima.Interface.options.types.OptionDouble;
import org.lrima.Interface.options.types.OptionString;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class OptionsDisplayPanel extends JPanel {
    public OptionsDisplayPanel(LinkedHashMap<String, Option> options){
        super();
        setLayout(new BorderLayout(0, 0));
        
        JPanel modificationPanel = new JPanel();
        
        GridBagLayout gbl_modificationPanel = new GridBagLayout();
        gbl_modificationPanel.columnWidths = new int[] {30, 100, 100, 30};
        gbl_modificationPanel.rowHeights = options.keySet().stream().mapToInt(key->30).toArray();
        gbl_modificationPanel.columnWeights = new double[]{0.0, 0.0, 1.0};
        gbl_modificationPanel.rowWeights = new double[]{};
        modificationPanel.setLayout(gbl_modificationPanel);

        int i = 0;
        for(String key : options.keySet()){
        	Option option = options.get(key);

            GridBagConstraints gbc_Name = new GridBagConstraints();
            gbc_Name.insets = new Insets(0, 0, 5, 5);
            gbc_Name.anchor = GridBagConstraints.EAST;
            gbc_Name.gridx = 1;
            gbc_Name.gridy = i;
            modificationPanel.add(new JLabel(OptionsDialog.fromKeyToNormalText(key) + " :"), gbc_Name);

            GridBagConstraints gbc_option = new GridBagConstraints();
            gbc_option.fill = GridBagConstraints.HORIZONTAL;
            gbc_option.insets = new Insets(0, 0, 5, 5);
            gbc_option.gridx = 2;
            gbc_option.gridy = i;
            modificationPanel.add(option.show(), gbc_option);
            i++;
        }
        
        JScrollPane scrollPane = new JScrollPane(modificationPanel);
        add(scrollPane, BorderLayout.CENTER);
    }
}
