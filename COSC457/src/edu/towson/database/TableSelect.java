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
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

public class TableSelect extends JPanel
{
	JButton selectColumns_btn;
	JButton back_btn;
	
	/**
	 * Create the panel.
	 */
	public TableSelect()
	{
		setLayout(new BorderLayout());
		
		JList<String> list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent arg0) 
			{
				selectColumns_btn.setEnabled(true);
			}
		});
		add(list,BorderLayout.CENTER);
		
		JLabel lblSelectTable = new JLabel("Select Table");
		add(lblSelectTable,BorderLayout.NORTH);
		
		
		// main bottom layout
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());

		
		// bottom left layout
		JPanel leftBPanel = new JPanel();
		leftBPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		back_btn = new JButton("Back");
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

		selectColumns_btn = new JButton("Select Columns");
		selectColumns_btn.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				Swappable.getInstance().changeTo(new ColumnSelect(list.getSelectedValue()));
			}
		});
		selectColumns_btn.setEnabled(false);
		rightBPanel.add(selectColumns_btn);
		bottomPanel.add(rightBPanel,BorderLayout.EAST);
		
		add(bottomPanel,BorderLayout.SOUTH);
		
		
		ArrayList<String> tables = MySQLConnection.getInstance().getTables();
		DefaultListModel<String> listval = new DefaultListModel<String>();
		for (String item : tables)
		{
			listval.addElement(item);
		}
		list.setModel(listval);
	}
}
