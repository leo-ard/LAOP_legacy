package org.lrima.Interface.home.pages;

import org.lrima.Interface.FrameManager;
import org.lrima.Interface.home.ModelListDialog;
import org.lrima.Interface.home.HomeFrameManager;
import org.lrima.Interface.home.PagePanel;
import org.lrima.network.interfaces.NeuralNetworkModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.GroupLayout.Alignment;
import javax.swing.table.DefaultTableModel;

public class CompareAlgorithmsPanel extends PagePanel {
    private JTable algorithmList = new JTable();
    private JScrollPane algorithmScrollPane;
    private JLabel algorithmsLabel = new JLabel("Algorithms");
    private JButton addButton = new JButton("+");
    private JTextPane descriptionPane = new JTextPane();
    private JButton simulateButton = new JButton("Simulate");
    private JButton backButton = new JButton("Back");

    private ArrayList<NeuralNetworkModel> models = new ArrayList<>();

    public CompareAlgorithmsPanel(HomeFrameManager homeFrameManager){
        super(homeFrameManager);
        this.setupComponents();
        this.setUp();
    }

    private void setUp(){
        GroupLayout layout = new GroupLayout(this);

        layout.setHorizontalGroup(
                layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(backButton)
                                                        .addPreferredGap(ComponentPlacement.RELATED, 192, Short.MAX_VALUE)
                                                        .addComponent(simulateButton)
                                                        .addContainerGap())
                                                .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                                                                .addComponent(algorithmList, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                                                                .addComponent(descriptionPane, GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE))
                                                        .addContainerGap())
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(algorithmsLabel)
                                                        .addContainerGap(303, Short.MAX_VALUE)))
                                        .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(addButton)
                                                .addContainerGap())))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.LEADING)
                        .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(33)
                                .addComponent(descriptionPane, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)
                                .addGap(21)
                                .addComponent(algorithmsLabel)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(algorithmList, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(addButton)
                                .addPreferredGap(ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(simulateButton)
                                        .addComponent(backButton))
                                .addContainerGap())
        );

        setLayout(layout);
    }

    private void setupComponents(){
        //Description
        this.descriptionPane.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce ac lobortis nulla. Donec at turpis sed purus commodo fringilla vel nec ex. Vivamus placerat luctus malesuada. Ut odio dui, lobortis vitae finibus eget, fermentum eu sem. Etiam suscipit augue ut nunc hendrerit, consequat tincidunt lacus porttitor.");
        descriptionPane.setEditable(false);

        this.setupAlgorithmTable();

        //Add button
        this.addButton.addActionListener(e -> {
            ModelListDialog dialog = new ModelListDialog(this);
            dialog.setVisible(true);
        });

        //Bottom buttons
        this.backButton.addActionListener(e -> homeFrameManager.back());
        this.simulateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homeFrameManager.close();

                FrameManager frameManager = new FrameManager();
                for(NeuralNetworkModel model : models){
                    frameManager.addBatch(model, 2);
                }

                frameManager.setVisible(true);
                frameManager.startBatches();
            }
        });
    }
    

    private void setupAlgorithmTable(){
        String[] columnName = new String[]{"Algorithm Name", "Edit Button"};
        Object[][] tableRows = new Object[models.size()][columnName.length];

        for(int row = 0 ; row < tableRows.length ; row++){
            tableRows[row] = new Object[]{models.get(row).getAlgorithmInformationAnnotation().name(), "Options"};
        }
        
        algorithmList.setModel(new DefaultTableModel(tableRows, columnName));
        algorithmList.setShowGrid(false);
        algorithmList.setTableHeader(null);

        //TODO : peut-etre mettre un bouton à la place ?
        algorithmList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    int row = algorithmList.rowAtPoint(e.getPoint());
                    int col = algorithmList.columnAtPoint(e.getPoint());

                    if (col == 1) {
                       models.get(row).displayOptions();
                    }
                }catch (IndexOutOfBoundsException error){ }
            }
        });

    }

    /**
     * Adds a list of models to the models table
     * @param models the models to add
     */
    public void addModels(ArrayList<NeuralNetworkModel> models){
        this.models.addAll(models);
        this.setupAlgorithmTable();
    }

    @Override
    public String getName() {
        return "Compare algorithms frame";
    }
}
