package edu.towson.database;

public class TableDisplay
{
	String DisplayName;
	String[] ColumnDisplayNames;
	
	
	public TableDisplay(String DisplayName, String[] ColumnDisplayNames)
	{
		this.DisplayName = DisplayName;
		this.ColumnDisplayNames = ColumnDisplayNames;
	}
}