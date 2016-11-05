package edu.towson.database;

import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JList;

import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
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
		setLayout(null);
		
		JLabel lblSelectedColumns = new JLabel("Selected Columns");
		lblSelectedColumns.setBounds(36, 26, 148, 14);
		add(lblSelectedColumns);
		
		JLabel lblUnselectedColumns = new JLabel("Unselected columns");
		lblUnselectedColumns.setBounds(272, 26, 130, 14);
		add(lblUnselectedColumns);
		
		
		
		JList<String> selected = new JList<String>();
		selected.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		selected.setBounds(10, 51, 163, 224);
		add(selected);
		
		JList<String> unselected = new JList<String>();
		unselected.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		unselected.setBounds(256, 51, 172, 224);
		add(unselected);
		
		
		ArrayList<String> tables = MySQLConnection.getInstance().getColumns("SELECT * FROM " + tableName);
		selectedModel = new DefaultListModel<String>();
		for (String item : tables)
		{
			selectedModel.addElement(item);
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
		remove_btn.setBounds(183, 90, 63, 38);
		add(remove_btn);
		
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
		add_btn.setBounds(183, 160, 63, 38);
		add(add_btn);

		JButton back_btn = new JButton("Back");
		back_btn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Swappable.getInstance().changeTo(new TableSelect());
			}
		});
		back_btn.setBounds(10, 301, 63, 23);
		add(back_btn);

		JButton finish_btn = new JButton("Finish");
		finish_btn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
//				System.out.println(createSelectFromList(tableName));
				Swappable.getInstance().changeTo(new ViewPanel(createSelectFromList(tableName)));
			}
		});
		finish_btn.setBounds(352, 301, 63, 23);
		add(finish_btn);
	}
	
	
	public String createSelectFromList(String table)
	{
		StringBuilder statement = new StringBuilder("SELECT ");
		
		for (int i = 0; i < selectedModel.size() - 1; i++)
		{
			statement.append(selectedModel.getElementAt(i));
			statement.append(", ");
		}
		statement.append(selectedModel.getElementAt(selectedModel.size() - 1));
		
		statement.append(" FROM ");
		statement.append(table);
		
		return statement.toString();
	}
	
}
