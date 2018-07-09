package org.lrima.simulation.Interface;

import javax.swing.JPanel;

import org.lrima.espece.EspeceInfo;

import org.lrima.simulation.SimulationInfos;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import java.awt.CardLayout;
import javax.swing.DefaultComboBoxModel;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.BorderLayout;

public class SimulationPanel extends JPanel {
	
	private SimulationInfos sim;
	private JTable table;
	private String[] tableList;
	private DefaultTableModel df;
	private int generationSelect;
	private JComboBox generationSelectionComboBox;
	private JPanel overviewPanel;
	private JPanel GenerationPanelSwitcher;
	private JComboBox displaySelectionComboBox;
	private JScrollPane dataPane;
	private JLabel lblGeneration;
	private JPanel chartPanelHolder;

	/**
	 * Create the panel.
	 */
	public SimulationPanel(SimulationInfos s) {
		
		this.sim = s;
		
		preinit();
        init();
	}
	
	public void preinit() {
		tableList = new String[]{"Id", "Type", "Fitness"};
		df = new DefaultTableModel(new Object[][]{}, tableList);
		//generationChartPanel = ChartPanelHolder.getChartPanel(100,100, new double[0], new double[0]);
		chartPanelHolder = new JPanel();
		chartPanelHolder.setLayout(new BorderLayout(0, 0));
	}
	public void update() {
        if(this.sim.getSize() != generationSelectionComboBox.getItemCount()) {
            generationSelectionComboBox.addItem("Generation " + (1+ this.generationSelectionComboBox.getItemCount()));
        }
        lblGeneration.setText("Generation " + (1+ this.generationSelectionComboBox.getItemCount()));


		ArrayList<EspeceInfo> especes = sim.get(generationSelectionComboBox.getSelectedIndex());
		Object[][] especesList = new Object[especes.size()][tableList.length];
		double[] serie1y = new double[especes.size()];
		double[] serie1x = new double[especes.size()];
		for(int i = 0 ; i < especes.size(); i++) {
			EspeceInfo e = especes.get(i);
			especesList[i][0] = i;
			especesList[i][1] = e.getType();
			especesList[i][2] = e.getFitness();
			serie1y[especes.size()-i-1] = e.getFitness();
			serie1x[i] = i;
		}
		String select;
		//System.out.println(displaySelectionComboBox.getSelectedIndex());
		if(displaySelectionComboBox.getSelectedIndex() == 0) {
			select = "overviewPanel";
		}
		else {
			df.setDataVector(especesList, tableList);
			select = "dataPanel";
		}
		//System.out.println(select);
		CardLayout l = (CardLayout) GenerationPanelSwitcher.getLayout();
		l.show(GenerationPanelSwitcher, select);
		chartPanelHolder.removeAll();
		chartPanelHolder.add(ChartPanelHolder.getChartPanel(100, 100, serie1x, serie1y, false), BorderLayout.CENTER);

		this.repaint();
	}
	
	public void init() {
		setBounds(new Rectangle(0, 0, 400, 700));
		lblGeneration = new JLabel("G\u00E9n\u00E9ration ");
		lblGeneration.setFont(new Font("Tahoma", Font.PLAIN, 30));
		
		JLabel label = new JLabel("0");
		label.setFont(new Font("Tahoma", Font.PLAIN, 30));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(35)
							.addComponent(lblGeneration)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(label, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(37)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblGeneration)
						.addComponent(label, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Data", null, panel, "Hey there");
		
		generationSelectionComboBox = new JComboBox();
		generationSelectionComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                System.out.println(1);
                update();

            }
		});
		
		GenerationPanelSwitcher = new JPanel();
		
		displaySelectionComboBox = new JComboBox();
		displaySelectionComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
				System.out.println(2);
                update();
            }
		});
		displaySelectionComboBox.setModel(new DefaultComboBoxModel(new String[] {"Overview", "Data"}));
		displaySelectionComboBox.setSelectedIndex(0);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(GenerationPanelSwitcher, GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(generationSelectionComboBox, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(displaySelectionComboBox, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(generationSelectionComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(displaySelectionComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(GenerationPanelSwitcher, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
					.addContainerGap())
		);
		GenerationPanelSwitcher.setLayout(new CardLayout(0, 0));
		
		dataPane = new JScrollPane();
		GenerationPanelSwitcher.add(dataPane, "dataPanel");
		table = new JTable();
		
		table.setModel(df);
		
		dataPane.setViewportView(table);
		
		overviewPanel = new JPanel();
		overviewPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		GenerationPanelSwitcher.add(overviewPanel, "overviewPanel");
		
		JPanel panel_3 = new JPanel();
		GroupLayout gl_graphPanel = new GroupLayout(overviewPanel);
		gl_graphPanel.setHorizontalGroup(
			gl_graphPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_graphPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_graphPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(chartPanelHolder, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
						.addComponent(panel_3, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_graphPanel.setVerticalGroup(
			gl_graphPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_graphPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(chartPanelHolder, GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_3.setLayout(new GridLayout(2, 3, 0, 0));
		
		JLabel lblE_2 = new JLabel("E44");
		lblE_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblE_2.setHorizontalAlignment(SwingConstants.CENTER);
		panel_3.add(lblE_2);
		
		JLabel lblE_1 = new JLabel("121E22");
		lblE_1.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblE_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel_3.add(lblE_1);
		
		JLabel lblE_3 = new JLabel("E33");
		lblE_3.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblE_3.setHorizontalAlignment(SwingConstants.CENTER);
		panel_3.add(lblE_3);
		
		JPanel panel_5 = new JPanel();
		panel_3.add(panel_5);
		panel_5.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblNewLabel_1 = new JLabel("(1002)");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5.add(lblNewLabel_1);
		
		JLabel lblBest = new JLabel("Meilleur");
		lblBest.setHorizontalAlignment(SwingConstants.CENTER);
		lblBest.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_5.add(lblBest);
		
		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblWors = new JLabel("Moyenne");
		lblWors.setFont(new Font("Tahoma", Font.PLAIN, 10));
		panel_4.add(lblWors);
		
		JLabel label_1 = new JLabel("(3000)");
		panel_4.add(label_1);
		
		JPanel panel_6 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_6.getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setHgap(0);
		panel_3.add(panel_6);
		
		JLabel lblNewLabel = new JLabel("Pire");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 9));
		panel_6.add(lblNewLabel);
		overviewPanel.setLayout(gl_graphPanel);
		
		//JScrollPane scrollPane = new JScrollPane();
		//tab_generation.addTab("New tab", null, scrollPane, null);
		
		
		//scrollPane.setViewportView(table);
		panel.setLayout(gl_panel);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_1, null);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_2, null);
		setLayout(groupLayout);
	}
}
