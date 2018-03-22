package simulation.selection;

import javax.swing.JPanel;

import espece.Espece;
import simulation.Simulation;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.JScrollPane;

public class SimulationPanel extends JPanel {
	
	private Simulation sim;
	private JTable table;
	private String[] tableList;
	private DefaultTableModel df;

	/**
	 * Create the panel.
	 */
	public SimulationPanel(Simulation s) {
		
		this.sim = s;
		
		preinit();
		update();
		init();
		
		Timer uploadCheckerTimer = new Timer(true);
		uploadCheckerTimer.scheduleAtFixedRate(
		    new TimerTask() {
		      public void run() { update(); }
		    }, 0, 16);
		
	}
	
	public void preinit() {
		table = new JTable();
		tableList = new String[] {"Id", "Type", "Fitness","Dead"};
		df = new DefaultTableModel(new Object[][] {}, tableList);
		
		table.setModel(df);
		
	}
	
	public void update() {
		
		ArrayList<Espece> especes = sim.getEspeces();
		Object[][] especesList = new Object[especes.size()][tableList.length];
		
		for(int i = 0 ; i < especes.size(); i++) {
			Espece e = especes.get(i);
			especesList[i][0] = i;
			especesList[i][1] = "RR";
			especesList[i][2] = e.getFitness();
			especesList[i][3] = e.isDead();
		}
		
		df.setDataVector(especesList, tableList);
		
		this.repaint();
		
	}
	
	public void init() {
		setBounds(new Rectangle(0, 0, 400, 700));
		JLabel lblGeneration = new JLabel("G\u00E9n\u00E9ration ");
		lblGeneration.setFont(new Font("Tahoma", Font.PLAIN, 30));
		
		JLabel label = new JLabel("0");
		label.setFont(new Font("Tahoma", Font.PLAIN, 30));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(35)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 325, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblGeneration)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(label, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(40, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(37)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblGeneration)
						.addComponent(label, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 576, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(32, Short.MAX_VALUE))
		);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Data", null, panel, "Hey there");
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(89)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		
		scrollPane.setViewportView(table);
		panel.setLayout(gl_panel);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_1, null);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_2, null);
		setLayout(groupLayout);
	}
}
