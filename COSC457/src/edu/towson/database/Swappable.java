package edu.towson.database;

import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Swappable
{
	JPanel swapContainer;
	JFrame frame;
	protected Hashtable<String, JPanel> Items;
	private static Swappable instance;
	
	private String PreviousSwap = null;
	private String CurrentSwap = null;
	public static Swappable getInstance(JFrame frame,JPanel mainContentPanel)
	{
		if(instance == null)
			instance = new Swappable(frame, mainContentPanel);
		return instance;
	}
	
	private Swappable(JFrame frame, JPanel swapContainer)
	{
		Items = new Hashtable<>();
		this.swapContainer = swapContainer;
		this.frame = frame;
	}
	public static Swappable getInstance()
	{
		if(instance == null)
			System.err.println("Swappable instance not set with parameter (NULL)");
		return instance;
	}
	
	public void add(String viewName,JPanel viewWindow)
	{
		Items.put(viewName, viewWindow);
	}
	
	public boolean revert()
	{
		if(PreviousSwap != null)
		{
			return changeTo(PreviousSwap);
		}
		return false;
	}
	
	public boolean changeTo(String swapChoice)
	{
		PreviousSwap = CurrentSwap;
		CurrentSwap = swapChoice;
		
		JPanel choice = (JPanel)Items.get(swapChoice);
		
		if(choice != null)
		{
			swapContainer.removeAll();
			swapContainer.add(choice);
			swapContainer.revalidate();
			frame.repaint();
			return true;
		}
		return false;
	}
	public boolean changeTo(JPanel choice)
	{
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
