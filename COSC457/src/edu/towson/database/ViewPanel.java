package edu.towson.database;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.BorderLayout;
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
	 */
	String statement;
	String tableName;
	public ViewPanel(String select, String tableName)
	{
		StringBuilder selectStatement = new StringBuilder("SELECT ");
		selectStatement.append(select);
		selectStatement.append(" FROM ");
		selectStatement.append(tableName);
		
		this.tableName = tableName;
		this.statement = selectStatement.toString(); 

		initialize(statement);
		
		table.setModel(MySQLConnection.getInstance().getModel(statement)); // initial call to refresh
	}

	public void initialize(String statement)
	{
		setLayout(new BorderLayout());
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane,BorderLayout.CENTER);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		JButton refresh_btn = new JButton("Refresh");
		refresh_btn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				table.setModel(MySQLConnection.getInstance().getModel(statement));
			}
		});
		topPanel.add(refresh_btn);
		
		
		JButton tableSetup_btn = new JButton("Table Setup");
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

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		JButton createNew_btn = new JButton("Create New");
		createNew_btn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				Swappable.getInstance().changeTo(new EditorPanel(tableName));
			}
		});
		bottomPanel.add(createNew_btn);
		add(bottomPanel,BorderLayout.SOUTH);
	}
	
}
