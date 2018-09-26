package org.lrima.network.interfaces.options;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import org.lrima.Interface.options.OptionPanel;

public class AlgorithmOptionPanel extends JPanel implements OptionPanel{

    public static void main(String args[]) {
    	JFrame frame = new JFrame("juste a name");
    	LinkedHashMap<String, Option> options = new LinkedHashMap<>();
    	options.put("test1", new OptionInt(10));
        options.put("test2", new OptionDouble(10.0));
        options.put("test3", new OptionBoolean(true));
        options.put("test4", new OptionString("Ha ha hA"));
        options.get("test2").getValue();
    	frame.getContentPane().add(new AlgorithmOptionPanel(options));
    	frame.pack();
    	frame.setVisible(true);

    }

    public AlgorithmOptionPanel(LinkedHashMap<String, Option> options){
        super();
        setLayout(new BorderLayout(0, 0));

        JPanel modificationPanel = new JPanel();
        
        add(modificationPanel);
        GridBagLayout gbl_modificationPanel = new GridBagLayout();
        gbl_modificationPanel.columnWidths = new int[] {100, 300, 30};
        gbl_modificationPanel.rowHeights = options.keySet().stream().mapToInt(key->30).toArray();
        gbl_modificationPanel.columnWeights = new double[]{0.0, 1.0};
        gbl_modificationPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
        modificationPanel.setLayout(gbl_modificationPanel);

        int i = 0;
        Iterator<String> iterator = options.keySet().iterator();
        while(iterator.hasNext()){
        	String name = iterator.next();
        	Option option = options.get(name);

            GridBagConstraints gbc_Name = new GridBagConstraints();
            gbc_Name.insets = new Insets(0, 0, 5, 5);
            gbc_Name.anchor = GridBagConstraints.EAST;
            gbc_Name.gridx = 0;
            gbc_Name.gridy = i;
            modificationPanel.add(new JLabel(name + " :"), gbc_Name);

            GridBagConstraints gbc_option = new GridBagConstraints();
            gbc_option.fill = GridBagConstraints.HORIZONTAL;
            gbc_option.insets = new Insets(0, 0, 5, 5);
            gbc_option.gridx = 1;
            gbc_option.gridy = i;
            modificationPanel.add(option.show(), gbc_option);
            i++;
        }


        //this.setBackground(Color.white);
        //this.setLayout(new BoxLayout());
    }

    @Override
    public boolean save() {
        return false;
    }
}
