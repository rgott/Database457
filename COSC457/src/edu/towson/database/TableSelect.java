package edu.towson.database;

import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TableSelect extends JPanel
{
	JButton btnSelectColumns;
	
	/**
	 * Create the panel.
	 */
	public TableSelect()
	{
		setLayout(null);
		
		JList<String> list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent arg0) 
			{
				btnSelectColumns.setEnabled(true);
			}
		});
		list.setBounds(10, 36, 512, 133);
		add(list);
		
		JLabel lblSelectTable = new JLabel("Select Table");
		lblSelectTable.setBounds(10, 11, 92, 14);
		add(lblSelectTable);
		
		
		Swappable swap = Swappable.getInstance();
		
		btnSelectColumns = new JButton("Select Columns");
		btnSelectColumns.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				swap.changeTo(new ColumnSelect(list.getSelectedValue()));
			}
		});
		btnSelectColumns.setBounds(397, 192, 125, 23);
		btnSelectColumns.setEnabled(false);
		add(btnSelectColumns);
		
		ArrayList<String> tables = MySQLConnection.getInstance().getTables();
		DefaultListModel<String> listval = new DefaultListModel<String>();
		for (String item : tables)
		{
			listval.addElement(item);
		}
		list.setModel(listval);
	}
}
