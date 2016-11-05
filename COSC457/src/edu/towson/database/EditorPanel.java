package edu.towson.database;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	Hashtable<String, JTextField> fields;
	JPanel mainContentPanel;
	// Default editor panel
	public EditorPanel(String tableName)
	{
		setLayout(new BorderLayout());
		
		mainContentPanel = new JPanel();
//		this.add(mainContentPanel,BorderLayout.CENTER);
		JScrollPane scollable = new JScrollPane(mainContentPanel);
		this.add(scollable);
		
		ArrayList<String> columns = MySQLConnection.getInstance().getColumns("SELECT * FROM " + tableName);
		setFormLayout(mainContentPanel, columns.size());

		fields = new Hashtable<>();
		
		for (String item : columns)
		{
			fields.put(item, createField(item));
		}
		
		// main bottom layout
		Container bottomPanel = new Container();
		bottomPanel.setLayout(new BorderLayout());

		
		// bottom left layout
		Container leftBPanel = new Container();
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
		
		
		// bottom right layout
		Container rightBPanel = new Container();
		rightBPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		Button addUpd_btn = new Button("Add");
		addUpd_btn.addActionListener(new ActionListener()
		{
			
			// TODO make into method
			public void actionPerformed(ActionEvent arg0)
			{
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
				
				
				// New Values
				fieldSet = fields.entrySet();
				fsIt = fieldSet.iterator();
				for (int i = 0; i < fieldSet.size() - 1; i++)
				{
					String currentText = fsIt.next().getValue().getText();
					currentText = (currentText == null)?"":currentText;
					insertQuery.append(currentText);
					insertQuery.append(", ");
				}
				insertQuery.append(fsIt.next().getKey());
				
				
				System.out.println(insertQuery.toString());
			}
		});

		rightBPanel.add(addUpd_btn);
		bottomPanel.add(rightBPanel,BorderLayout.EAST);
		add(bottomPanel,BorderLayout.SOUTH);
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
	public JTextField createField(String description)
	{
		return createField(mainContentPanel, description);
	}
	
	@Deprecated
	private void createRow(JPanel comp, JPanel panel)
	{
		int realRow = ((currentRow * 2) + 2);
		comp.add(panel, "2, " + realRow + ", left, center");

		currentRow++;
	}
	
	
	private JTextField createField(JPanel comp, String description)
	{	
		int realRow = ((currentRow * 2) + 2);
		JLabel label = new JLabel(description);
		comp.add(label, "2, " + realRow + ", left, center");
		
		JTextField textField = new JTextField();
		comp.add(textField, "4, " + realRow + ", fill, default");

		currentRow++;
		return textField;
	}
}
