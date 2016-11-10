package edu.towson.database;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;

public class ColumnSelect extends JPanel
{
	DefaultListModel<String> unselectedModel;
	DefaultListModel<String> selectedModel;
	/**
	 * Create the panel.
	 */
	
	
	public ColumnSelect(String tableName)
	{
		setLayout(new BorderLayout());
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints c1 = new GridBagConstraints();
		JLabel lblSelectedColumns = new JLabel("Selected Columns");
		c1.fill=GridBagConstraints.HORIZONTAL; 
		c1.gridx = 0; 
		c1.gridy = 0; 
		//lblSelectedColumns.setBounds(39, -10, 250, 80);
		mainPanel.add(lblSelectedColumns,c1);
		
		GridBagConstraints c2 = new GridBagConstraints();
		JLabel lblUnselectedColumns = new JLabel("Unselected Columns");
		c2.fill=GridBagConstraints.HORIZONTAL; 
		c2.gridx = 2; 
		c2.gridy = 0; 
		lblUnselectedColumns.setBounds(275, -10, 250, 80);
		mainPanel.add(lblUnselectedColumns,c2);
		
		GridBagConstraints c3 = new GridBagConstraints();
		JList<String> selected = new JList<String>();
		c3.fill=GridBagConstraints.HORIZONTAL; 
		c3.gridx = 0; 
		c3.gridy = 1;
		c3.gridheight=3; 
		selected.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		selected.setBounds(10, 50, 166, 206);
		selected.setVisible(true); 
		mainPanel.add(selected,c3);
		
		GridBagConstraints c4 = new GridBagConstraints();
		JList<String> unselected = new JList<String>();
		c4.fill=GridBagConstraints.HORIZONTAL; 
		c4.gridx = 2; 
		c4.gridy = 1;
		c4.gridheight=3;
		unselected.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		unselected.setBounds(259, 50, 181, 206);
		unselected.setVisibleRowCount(5); 
		mainPanel.add(unselected,c4);
		
		
		
		add(mainPanel,BorderLayout.CENTER);
		
		 
		
		ArrayList<ColumnData> tables = MySQLConnection.getInstance().getColumns("SELECT * FROM " + tableName);
		selectedModel = new DefaultListModel<String>();
		for (ColumnData item : tables)
		{
			selectedModel.addElement(item.Name);
		}
		selected.setModel(selectedModel);
		
		unselectedModel = new DefaultListModel<String>();
		unselected.setModel(unselectedModel);
		
		GridBagConstraints c5 = new GridBagConstraints();
		JButton remove_btn = new JButton(">>");
		c5.fill=GridBagConstraints.HORIZONTAL; 
		c5.gridx = 1; 
		c5.gridy = 1;
		remove_btn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// move from selected to unselected
				int[] indicies = selected.getSelectedIndices();
				int maxIndex = indicies.length - 1;
				
				while(maxIndex >= 0)
				{
					unselectedModel.addElement(selectedModel.getElementAt(indicies[maxIndex]));
					selectedModel.remove(indicies[maxIndex--]);
				}
			}
		});
		remove_btn.setBounds(186, 114, 63, 38);
		mainPanel.add(remove_btn,c5);
		
		
		
		GridBagConstraints c6 = new GridBagConstraints();
		JButton add_btn = new JButton("<<");
		c6.fill=GridBagConstraints.HORIZONTAL; 
		c6.gridx = 1; 
		c6.gridy = 2;
		add_btn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// move from unselected to selected
				int[] indicies = unselected.getSelectedIndices();
				int maxIndex = indicies.length - 1;
				
				while(maxIndex >= 0)
				{
					selectedModel.addElement(unselectedModel.getElementAt(indicies[maxIndex]));
					unselectedModel.remove(indicies[maxIndex--]);
				}
			}
		});
		add_btn.setBounds(186, 155, 63, 38);
		mainPanel.add(add_btn,c6);

		
		
		
		
		// main bottom layout
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());

		
		// bottom left layout
		Container leftBPanel = new Container();
		leftBPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JButton back_btn = new JButton("Back");
		back_btn.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				Swappable.getInstance().changeTo("view");
			}
		});
		leftBPanel.add(back_btn);
		bottomPanel.add(leftBPanel, BorderLayout.WEST);
		
		// bottom right layout
		JPanel rightBPanel = new JPanel();
		rightBPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));


		JButton finish_btn = new JButton("Finish");
		finish_btn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Swappable.getInstance().changeTo(new ViewPanel(createColumnsForSelect(tableName), tableName));
			}
		});
		rightBPanel.add(finish_btn);
		bottomPanel.add(rightBPanel,BorderLayout.EAST);
		
		add(bottomPanel,BorderLayout.SOUTH);
	}
	
	
	public String createColumnsForSelect(String table)
	{
		StringBuilder statement = new StringBuilder();
		
		for (int i = 0; i < selectedModel.size() - 1; i++)
		{
			statement.append(selectedModel.getElementAt(i));
			statement.append(", ");
		}
		statement.append(selectedModel.getElementAt(selectedModel.size() - 1));
		
		return statement.toString();
	}
	
}
