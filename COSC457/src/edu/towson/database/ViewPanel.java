package edu.towson.database;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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
	ResultSet ViewQueryResults;
	JButton refresh_btn;
	public ViewPanel(String select, String tableName)
	{
		StringBuilder selectStatement = new StringBuilder("SELECT ");
		selectStatement.append(select);
		selectStatement.append(" FROM ");
		selectStatement.append(tableName);
		
		this.tableName = tableName;
		this.statement = selectStatement.toString(); 

		initialize(statement);
		
		refresh_btn.doClick();
	}

	public void initialize(String statement)
	{
		setLayout(new BorderLayout());
		
		JScrollPane scrollPane = new JScrollPane();
		
		add(scrollPane,BorderLayout.CENTER);
		
		table = new JTable()
			{
				public boolean isCellEditable(int row, int column) 
				{                
	                return false;               
				};
			};
		table.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				int r = table.rowAtPoint(e.getPoint());
		        if (r >= 0 && r < table.getRowCount()) {
		            table.setRowSelectionInterval(r, r);
		        } else {
		            table.clearSelection();
		        }

		        int rowindex = table.getSelectedRow();
		        if (rowindex < 0)
		            return;
		        if (e.isPopupTrigger() && e.getComponent() instanceof JTable ) {
		            JPopupMenu popup = getTableMenu(rowindex);
		            popup.show(e.getComponent(), e.getX(), e.getY());
		        }
			}
		});
		scrollPane.setViewportView(table);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		refresh_btn = new JButton("Refresh");
		refresh_btn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				ViewQueryResults = MySQLConnection.getInstance().ExecuteQuery(statement);
				table.setModel(MySQLConnection.getModel(ViewQueryResults));
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
	
	private JPopupMenu getTableMenu(int rowindex)
	{
		JPopupMenu menu = new JPopupMenu();
		
		JMenuItem edit = new JMenuItem("Edit");
		edit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				Swappable.getInstance().changeTo(new EditorPanel(tableName,MySQLConnection.getRow(ViewQueryResults, rowindex + 1))); // convert 0 to 1 based index
			}
		});
		menu.add(edit);
		
		JMenuItem delete = new JMenuItem("Delete");
		delete.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				ResultSet deleteRow = MySQLConnection.getRow(ViewQueryResults, rowindex + 1);
				
				StringBuilder deleteQuery = new StringBuilder("DELETE FROM ");
				deleteQuery.append(tableName);
				deleteQuery.append(" WHERE ");
				
				try
				{
					for (int i = 1; i < deleteRow.getMetaData().getColumnCount(); i++)
					{
						deleteQuery.append(deleteRow.getMetaData().getColumnName(i));
						deleteQuery.append("='");
						deleteQuery.append(deleteRow.getString(i));
						deleteQuery.append("' AND ");
					}
					
					deleteQuery.append(deleteRow.getMetaData().getColumnName(deleteRow.getMetaData().getColumnCount()));
					deleteQuery.append("='");
					deleteQuery.append(deleteRow.getString(deleteRow.getMetaData().getColumnCount()));
					deleteQuery.append("'");
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				deleteQuery.append(" limit 1;");
				
//				System.out.println(deleteQuery.toString());
				MySQLConnection.getInstance().UpdateQuery(deleteQuery.toString());
				
				refresh_btn.doClick(); // refresh the view
			}
		});
		menu.add(delete);
		
		return menu;
		
	}
	
}
