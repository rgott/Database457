package edu.towson.database;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

public class EditorPanel extends JPanel
{
	/**
	 * Created To prevent warning
	 */
	public int currentRow = 0;
	JButton addUpd_btn;
	Hashtable<String, JTextField> fields;
	ActionListener addUpd_btn_lstnr;
	JPanel mainContentPanel;
	JLabel errorMessageLabel;
	// Default editor panel
	public EditorPanel(String tableName)
	{
		this(tableName, null);
	}
	
//	public boolean isValidInput()
//	{
//		for (Entry<String, JTextField> item : fields.entrySet())
//		{
//			if(!item.getValue().getInputVerifier().verify(item.getValue()));
//				return false;
//			//QueryInputVerifier.getType((QueryInputVerifier)item.getValue().getInputVerifier());
//		}
//		return true;
//	}
	
	public ActionListener AddQueryAL(String tableName)
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
//				if(!isValidInput()) 
//				{
//					MessageBox.show("Please Check your input", "INPUT ERROR");
//					return;
//				}
				
				// build insert query
				StringBuilder insertQuery = new StringBuilder("INSERT INTO ");
				insertQuery.append(tableName);
				insertQuery.append(" (");
				
				Set<Entry<String, JTextField>> fieldSet;
				Iterator<Entry<String, JTextField>> fsIt;
				
				// columns
				fieldSet = fields.entrySet();
				fsIt = fieldSet.iterator();
				for (int i = 0; i < fieldSet.size() - 1; i++)
				{
					insertQuery.append(fsIt.next().getKey());
					insertQuery.append(", ");
				}
				insertQuery.append(fsIt.next().getKey());
				insertQuery.append(") VALUES (");
				
				// New Values
				fieldSet = fields.entrySet();
				fsIt = fieldSet.iterator();
				String currentText;
				for (int i = 0; i < fieldSet.size() - 1; i++)
				{
					currentText = fsIt.next().getValue().getText();
					currentText = (currentText == null)?"":currentText;
					insertQuery.append("'");
					insertQuery.append(currentText);
					insertQuery.append("', ");
				}
				currentText = fsIt.next().getValue().getText();
				insertQuery.append("'");
				insertQuery.append(currentText);
				insertQuery.append("');");
				
//				System.out.println(insertQuery.toString());
				MySQLConnection.getInstance().UpdateQuery(insertQuery.toString());
				
				Swappable.getInstance().changeTo("view");
			}
		};
	}
	
	public ActionListener EditQueryAL(String tableName, ResultSet rsCurrentOnly)
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
//				if(!isValidInput()) 
//				{
//					MessageBox.show("Please Check your input", "INPUT ERROR");
//					return;
//				}
				
				// build insert query
				StringBuilder updateQuery = new StringBuilder("UPDATE ");
				updateQuery.append(tableName);
				updateQuery.append(" SET ");
				
				Set<Entry<String, JTextField>> fieldSet;
				Iterator<Entry<String, JTextField>> fsIt;
				
				// columns
				fieldSet = fields.entrySet();
				fsIt = fieldSet.iterator();
				Entry<String,JTextField> entry;
				for (int i = 0; i < fieldSet.size() - 1; i++)
				{
					entry = fsIt.next();
					
					updateQuery.append(entry.getKey());
					updateQuery.append("=");
					updateQuery.append(entry.getValue().getText());
					updateQuery.append(",");
				}
				entry = fsIt.next();
				updateQuery.append(entry.getKey());
				updateQuery.append("=");
				updateQuery.append(entry.getValue().getText());


				try
				{
					// WHERE clause to find the statement
					updateQuery.append(" WHERE ");
					for (int j = 1; j < rsCurrentOnly.getMetaData().getColumnCount(); j++)
					{
						
						updateQuery.append(rsCurrentOnly.getMetaData().getColumnName(j));
						updateQuery.append("='");
						updateQuery.append(rsCurrentOnly.getString(j));
						updateQuery.append("' AND ");
					}
					
					updateQuery.append(rsCurrentOnly.getMetaData().getColumnName(rsCurrentOnly.getMetaData().getColumnCount()));
					updateQuery.append("='");
					updateQuery.append(rsCurrentOnly.getString(rsCurrentOnly.getMetaData().getColumnCount()));
					updateQuery.append("'");
				
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//				System.out.println(updateQuery.toString());
				MySQLConnection.getInstance().UpdateQuery(updateQuery.toString());
				
				Swappable.getInstance().changeTo("view");
			}
		};
	}
	
	public EditorPanel(String tableName, ResultSet rs)
	{
		setLayout(new BorderLayout());
		
		mainContentPanel = new JPanel();
//		this.add(mainContentPanel,BorderLayout.CENTER);
		JScrollPane scollable = new JScrollPane(mainContentPanel);
		this.add(scollable);
		
		errorMessageLabel = new JLabel("");
		errorMessageLabel.setForeground(Color.RED);
		
		
		ArrayList<ColumnData> columns = MySQLConnection.getInstance().getColumns("SELECT * FROM " + tableName);
		setFormLayout(mainContentPanel, columns.size());

		fields = new Hashtable<>();
		
		for (ColumnData item : columns)
		{
			fields.put(item.Name, createField(item));
		}
		
		// main bottom layout
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());

		
		// bottom left layout
		JPanel leftBPanel = new JPanel();
		leftBPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		Button back_btn = new Button("Back");
		back_btn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				Swappable.getInstance().changeTo("view");
			}
		});
		leftBPanel.add(back_btn);
		bottomPanel.add(leftBPanel, BorderLayout.WEST);
		
		
		bottomPanel.add(errorMessageLabel,BorderLayout.CENTER);
		// bottom right layout
		JPanel rightBPanel = new JPanel();
		rightBPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		if(rs == null)
		{
			addUpd_btn = new JButton("Add");
			addUpd_btn_lstnr = AddQueryAL(tableName);
		}
		else
		{
			addUpd_btn = new JButton("Update");
			addUpd_btn_lstnr = EditQueryAL(tableName,rs);
		}
		addUpd_btn.addActionListener(addUpd_btn_lstnr);

		
		
		rightBPanel.add(addUpd_btn);
		bottomPanel.add(rightBPanel,BorderLayout.EAST);
		add(bottomPanel,BorderLayout.SOUTH);
		
		if(rs != null)
		{
			// then set all fields that are available
			// TODO: need to query to find this row and then set values
			try
			{
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
				{
					fields.get(rs.getMetaData().getColumnName(i)).setText(rs.getString(i));
				}
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setFormLayout(JPanel panel, int rows)
	{
		// create row specification
		LinkedList<RowSpec> rowSpec = new LinkedList<>();
		rowSpec.add(FormSpecs.LINE_GAP_ROWSPEC);
		rowSpec.add(RowSpec.decode("20px"));
		
		for (int i = 0; i < rows; i++)
		{
			rowSpec.add(FormSpecs.RELATED_GAP_ROWSPEC);
			rowSpec.add(FormSpecs.DEFAULT_ROWSPEC);
		}
		
		// create column specification
		LinkedList<ColumnSpec> colSpec = new LinkedList<>();
		colSpec.add(ColumnSpec.decode("20px"));
		colSpec.add(ColumnSpec.decode("150px"));
		colSpec.add(FormSpecs.DEFAULT_COLSPEC);
		colSpec.add(ColumnSpec.decode("150px:grow"));
		colSpec.add(FormSpecs.DEFAULT_COLSPEC);
		colSpec.add(ColumnSpec.decode("30px"));
		
		panel.setLayout(new FormLayout(colSpec.toArray(new ColumnSpec[colSpec.size()]),rowSpec.toArray(new RowSpec[rowSpec.size()])));
	}
	
	// public interfacing method
	public JTextField createField(ColumnData data)
	{
		return createField(mainContentPanel, data);
	}
	
	private JTextField createField(JPanel comp, ColumnData data)
	{	
		int realRow = ((currentRow * 2) + 2);
		JLabel label = new JLabel(data.Name);
		comp.add(label, "2, " + realRow + ", left, center");
		
		JTextField textField = new JTextField();
		textField.setInputVerifier(new QueryInputVerifier(errorMessageLabel,data)); //TODO: make real error label
		comp.add(textField, "4, " + realRow + ", fill, default");

		currentRow++;
		return textField;
	}
}
