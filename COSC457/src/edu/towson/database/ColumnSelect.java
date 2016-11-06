package edu.towson.database;

import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JList;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

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
		mainPanel.setLayout(null);
		
		JLabel lblSelectedColumns = new JLabel("Selected Columns");
		lblSelectedColumns.setBounds(49, 25, 84, 14);
		mainPanel.add(lblSelectedColumns);
		
		JLabel lblUnselectedColumns = new JLabel("Unselected columns");
		lblUnselectedColumns.setBounds(305, 25, 94, 14);
		mainPanel.add(lblUnselectedColumns);
		
		JList<String> selected = new JList<String>();
		selected.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		selected.setBounds(10, 50, 166, 206);
		mainPanel.add(selected);
		
		JList<String> unselected = new JList<String>();
		unselected.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		unselected.setBounds(259, 50, 181, 206);
		mainPanel.add(unselected);
		
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
		
		JButton remove_btn = new JButton(">>");
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
		mainPanel.add(remove_btn);
		
		JButton add_btn = new JButton("<<");
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
		mainPanel.add(add_btn);

		
		
		
		
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
