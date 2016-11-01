package edu.towson.database;

import javax.swing.JPanel;
import javax.swing.JLabel;

public class ErrorPanel extends JPanel
{

	/**
	 * Create the panel.
	 */
	public ErrorPanel()
	{
		setLayout(null);
		
		JLabel lblErrorSomethingHas = new JLabel("ERROR Something has happened");
		lblErrorSomethingHas.setBounds(124, 107, 200, 43);
		add(lblErrorSomethingHas);

	}
}
