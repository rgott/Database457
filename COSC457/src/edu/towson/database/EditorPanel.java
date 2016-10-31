package edu.towson.database;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.LinkedList;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

public abstract class EditorPanel extends JPanel
{

	/**
	 * Created To prevent warning
	 */
	public int currentRow = 0;
	
	JPanel mainContentPanel;
	// Default editor panel
	public EditorPanel(int rows)
	{
		setLayout(new BorderLayout());
		
		mainContentPanel = new JPanel();
		this.add(mainContentPanel);
		
		setFormLayout(mainContentPanel,rows);

		Button addUpd_btn = new Button("Update");
		addUpd_btn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				updateButtonAction();
			}
		});
		this.add(addUpd_btn,BorderLayout.SOUTH);
		
	}
	
	public abstract void updateButtonAction();
	
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
