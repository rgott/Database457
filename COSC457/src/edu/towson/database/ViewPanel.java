package edu.towson.database;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.sql.PreparedStatement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ViewPanel extends JPanel
{
	/**
	 * Created to prevent warning
	 */
	private JTable table;
	
	/**
	 * Create the panel.
	 * 
	 *
	 */
	public ViewPanel(PreparedStatement statement)
	{
		initialize(statement);
		
		
		//TODO:FIX this causes an error in the caller design viewer 
		try
		{
			table.setModel(MySQLConnection.getModel(statement)); // initial call to refresh
		}
		catch(Exception e)
		{
			
		}
	}
	
	
	public void initialize(PreparedStatement statement)
	{
		setLayout(new BorderLayout());
		
		JScrollPane scrollPane = new JScrollPane((Component) null);
		add(scrollPane,BorderLayout.CENTER);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		Container bottomPanel = new Container();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		Button refresh_btn = new Button("Refresh");
		refresh_btn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				table.setModel(MySQLConnection.getModel(statement));
			}
		});
		bottomPanel.add(refresh_btn);
			
		add(bottomPanel,BorderLayout.NORTH);
	}
	
}
