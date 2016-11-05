package edu.towson.database;

import java.util.Hashtable;

import javax.swing.JPanel;

public class Swappable
{
	JPanel swapContainer;
	Hashtable<String, JPanel> Items;
	private static Swappable instance;
	public static Swappable getInstance(JPanel mainContentPanel)
	{
		if(instance == null)
			instance = new Swappable(mainContentPanel);
		return instance;
	}
	
	private Swappable(JPanel swapContainer)
	{
		Items = new Hashtable<>();
		this.swapContainer = swapContainer;
	}
	
	public void add(String viewName,JPanel viewWindow)
	{
		Items.put(viewName, viewWindow);
	}
	
	public boolean changeTo(String swapChoice)
	{
		JPanel choice = Items.get(swapChoice);
		
		if(choice != null)
		{
			swapContainer.removeAll();
			swapContainer.add(choice);
			swapContainer.revalidate();
			return true;
		}
		return false;
	}

	
}
