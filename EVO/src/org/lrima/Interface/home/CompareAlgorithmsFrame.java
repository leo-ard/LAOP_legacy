package org.lrima.Interface.home;

import org.lrima.network.algorithms.AlgorithmManager;
import org.lrima.network.interfaces.NeuralNetworkModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CompareAlgorithmsFrame extends JFrame {

    private JTable algorithmList = new JTable();
    private JTextPane descriptionPane = new JTextPane();
    private JButton simulateButton = new JButton("Simulate");
    private JButton backButton = new JButton("Back");

    private JPanel content = new JPanel();
    private JFrame lastFrame;

    public CompareAlgorithmsFrame(JFrame lastFrame){
        this.lastFrame = lastFrame;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(Constant.bounds);
        this.setContentPane(content);
        this.setName("Algorithm Options");

        this.setupComponents();

        GroupLayout layout = new GroupLayout(content);



        this.getContentPane().setLayout(layout);
    }

    private void setupComponents(){
        //Description
        this.descriptionPane.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce ac lobortis nulla. Donec at turpis sed purus commodo fringilla vel nec ex. Vivamus placerat luctus malesuada. Ut odio dui, lobortis vitae finibus eget, fermentum eu sem. Etiam suscipit augue ut nunc hendrerit, consequat tincidunt lacus porttitor.");
        descriptionPane.setEditable(false);

        this.setupAlgorithmTable();

        //Bottom buttons
        this.backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lastFrame.setVisible(true);
                setVisible(false);
            }
        });
    }

    private void setupAlgorithmTable(){
        this.algorithmList.setShowGrid(false);
        this.algorithmList.setTableHeader(null);

        String[] rowName = new String[]{"Algorithm Name", "Edit Button"};

        ArrayList<Class<?extends NeuralNetworkModel>> models = AlgorithmManager.algorithms;
        String[] algorithmNames = new String[models.size()];
        for(int i = 0 ; i < algorithmNames.length ; i++){
            String name = AlgorithmManager.algorithmsName.get(i);
            algorithmNames[i] = name;
        }

        Object[][] tableRows = new Object[algorithmNames.length][rowName.length];

        /*for(int algorithmName = 0 ; algorithmName < tableRows.length ; algorithmName++){
            for(int algori)
        }*/
    }
}
