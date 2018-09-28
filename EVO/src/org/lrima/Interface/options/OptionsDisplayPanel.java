package org.lrima.Interface.options;

import org.lrima.Interface.options.types.OptionBoolean;
import org.lrima.Interface.options.types.OptionDouble;
import org.lrima.Interface.options.types.OptionString;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class OptionsDisplayPanel extends JPanel {

    public static void main(String args[]) {
    	JFrame frame = new JFrame("juste a name");
    	LinkedHashMap<String, Option> options = new LinkedHashMap<>();
    	OptionString ob = new OptionString("heyhey");
    	ob.addOptionValueChangeListener(option -> System.out.println(option.getValue()));
    	OptionDouble od = new OptionDouble(10.0);
    	od.addOptionValueChangeListener(option -> System.out.println(option.getValue()));
    	options.put("test1", ob);
        options.put("test2", od);
        options.put("test3", new OptionBoolean(true));
        options.put("test4", new OptionString("Ha ha hA"));
        options.put("test5", new OptionString("Ha ha hA"));
        options.put("test6", new OptionString("Ha ha hA"));
        options.put("test7", new OptionString("Ha ha hA"));
        options.put("test8", new OptionString("Ha ha hA"));
        options.put("test9", new OptionString("Ha ha hA"));
        options.put("test10", new OptionString("Ha ha hA"));
        options.put("test11", new OptionString("Ha ha hA"));
        options.put("Tres lon nom .......... Tres tres long", new OptionString("Ha ha hA"));
        options.put("test13", new OptionString("Ha ha hA"));
        options.put("test14", new OptionString("Ha ha hA"));
        options.put("test15", new OptionString("Ha ha hA"));
        options.put("test16", new OptionString("Ha ha hA"));
        options.get("test2").getValue();

        //System.out.println(UserPrefs.getString(UserPrefs.KEY_MAP_TO_USE));

    	frame.getContentPane().add(new OptionsDisplayPanel(options));
    	frame.pack();
    	frame.setVisible(true);
    	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

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
        Iterator<String> iterator = options.keySet().iterator();
        while(iterator.hasNext()){
        	String name = iterator.next();
        	Option option = options.get(name);

            GridBagConstraints gbc_Name = new GridBagConstraints();
            gbc_Name.insets = new Insets(0, 0, 5, 5);
            gbc_Name.anchor = GridBagConstraints.EAST;
            gbc_Name.gridx = 1;
            gbc_Name.gridy = i;
            modificationPanel.add(new JLabel(OptionsDialog.fromKeyToNormalText(name) + " :"), gbc_Name);

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


        //this.setBackground(Color.white);
        //this.setLayout(new BoxLayout());
    }
}
