package edu.towson.database;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
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
	public ViewPanel(String statement)
	{
		initialize(statement);
		
		table.setModel(MySQLConnection.getInstance().getModel(statement)); // initial call to refresh
	}
	
	public void initialize(String statement)
	{
		setLayout(new BorderLayout());
		
		JScrollPane scrollPane = new JScrollPane((Component) null);
		add(scrollPane,BorderLayout.CENTER);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		Container topPanel = new Container();
		topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		Button refresh_btn = new Button("Refresh");
		refresh_btn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				table.setModel(MySQLConnection.getInstance().getModel(statement));
			}
		});
		
		
		topPanel.add(refresh_btn);
		
		
		Button tableSetup_btn = new Button("Table Setup");
		Swappable swap = Swappable.getInstance();
		swap.add("tableSelect", new TableSelect());
		tableSetup_btn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				swap.changeTo("tableSelect");
			}
		});
		topPanel.add(tableSetup_btn);
		
		
		add(topPanel,BorderLayout.NORTH);

		Container bottomPanel = new Container();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		Button createNew_btn = new Button("Create New");
		createNew_btn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				Swappable.getInstance().revert();
			}
		});
		bottomPanel.add(createNew_btn);
		add(bottomPanel,BorderLayout.SOUTH);
	}
	
}
