package edu.towson.database.editors;

import javax.swing.JTextField;
import edu.towson.database.EditorPanel;
import edu.towson.database.MySQLConnection;
public class DepartmentEditor extends EditorPanel
{
	JTextField Name,Date,DepartmentNo;
	/**
	 * Create the panel.
	 */
	public DepartmentEditor()
	{
		super(4);
		Name = createField("Name");
		Date = createField("Date");
		DepartmentNo = createField("Dept");
		DepartmentNo = createField("Dept");
		
	}
	@Override
	public void updateButtonAction()
	{
		try
		{
			// TODO: make update statement
			MySQLConnection.getInstance().getPreparedStatement("Update statement here with" + Name.getText());
		}
		catch(Exception e)
		{
			//TODO: show window "Connection Error: Error updating values"
			e.printStackTrace();
		}
	}
}
