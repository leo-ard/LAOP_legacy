package org.lrima.Interface.home;

import org.lrima.Interface.FrameManager;
import org.lrima.network.annotations.AlgorithmInformation;
import org.lrima.network.interfaces.NeuralNetworkModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.GroupLayout.Alignment;

public class CompareAlgorithmsFrame extends JFrame {

    private JTable algorithmList = new JTable();
    private JScrollPane algorithmScrollPane;
    private JLabel algorithmsLabel = new JLabel("Algorithms");
    private JButton addButton = new JButton("+");
    private JTextPane descriptionPane = new JTextPane();
    private JButton simulateButton = new JButton("Simulate");
    private JButton backButton = new JButton("Back");

    private JPanel content = new JPanel();
    private JFrame lastFrame;

    private ArrayList<Class<?extends NeuralNetworkModel>> modelClasses = new ArrayList<>();

    public CompareAlgorithmsFrame(JFrame lastFrame){
        this.lastFrame = lastFrame;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(lastFrame.getBounds().x, lastFrame.getBounds().y, Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);
        this.setTitle("Algorithm Options");
        this.setResizable(false);

        this.setupComponents();
        this.reload();
    }

    /**
     * Used to make the JTable reload its components
     */
    private void reload(){
        content = new JPanel();
        this.setContentPane(content);
        GroupLayout layout = new GroupLayout(content);

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

        this.getContentPane().setLayout(layout);
    }

    private void setupComponents(){
        //Description
        this.descriptionPane.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce ac lobortis nulla. Donec at turpis sed purus commodo fringilla vel nec ex. Vivamus placerat luctus malesuada. Ut odio dui, lobortis vitae finibus eget, fermentum eu sem. Etiam suscipit augue ut nunc hendrerit, consequat tincidunt lacus porttitor.");
        descriptionPane.setEditable(false);

        this.setupAlgorithmTable();

        //Add button
        this.addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModelListDialog dialog = new ModelListDialog(CompareAlgorithmsFrame.this);
                dialog.setVisible(true);
            }
        });

        //Bottom buttons
        this.backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastFrame.setVisible(true);
                setVisible(false);
            }
        });

        //simulate button
        this.simulateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(modelClasses.size() > 0) {
                    try {
                        dispose();

                        FrameManager frameManager = new FrameManager();

                        //Create an instance of each neural network models and add it as a batch
                        for (int i = 0; i < modelClasses.size(); i++) {
                            NeuralNetworkModel model = modelClasses.get(i).getConstructor().newInstance();
                            frameManager.addBatch(model, 1);
                        }

                        //Show the simulation
                        frameManager.setVisible(true);
                        frameManager.startBatches();
                    } catch (Exception error) {
                        error.printStackTrace();
                    }
                }
            }
        });
    }

    private void setupAlgorithmTable(){
        String[] columnName = new String[]{"Algorithm Name", "Edit Button"};

        String[] algorithmNames = new String[modelClasses.size()];
        for(int i = 0 ; i < algorithmNames.length ; i++){
            String name = modelClasses.get(i).getAnnotation(AlgorithmInformation.class).name();
            algorithmNames[i] = name;
        }

        Object[][] tableRows = new Object[algorithmNames.length][columnName.length];

        for(int row = 0 ; row < tableRows.length ; row++){

            tableRows[row] = new Object[]{algorithmNames[row], "Options"};
        }

        this.algorithmList = new JTable(tableRows, columnName);
        this.algorithmList.setShowGrid(false);
        this.algorithmList.setTableHeader(null);

        algorithmList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    int row = algorithmList.rowAtPoint(e.getPoint());
                    int col = algorithmList.columnAtPoint(e.getPoint());

                    if (col == 1) {
                        Class<? extends NeuralNetworkModel> model = modelClasses.get(row);
                        //TODO: Show option dialog for the model
                    }
                }catch (IndexOutOfBoundsException error){ }
            }
        });

        this.algorithmScrollPane = new JScrollPane(algorithmList);
    }

    /**
     * Adds a list of models to the models table
     * @param models the models to add
     */
    public void addModels(ArrayList<Class<?extends NeuralNetworkModel>> models){
        this.modelClasses.addAll(models);
        this.setupAlgorithmTable();
        this.reload();
    }
}
