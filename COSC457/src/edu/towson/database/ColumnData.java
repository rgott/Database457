package edu.towson.database;

public class ColumnData
{
	public String Name;
	public int Precision;
	public int QueryType;
	
	public ColumnData(String Name, int Precision,int QueryType)
	{
		this.Name = Name;
		this.Precision = Precision;
		this.QueryType = QueryType;
	}
}
