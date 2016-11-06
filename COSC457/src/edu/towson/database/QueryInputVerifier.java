package edu.towson.database;

import java.math.BigDecimal;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class QueryInputVerifier extends InputVerifier
{
	private enum QueryType
	{
		Number,
		String,
		Character
	}
	
	QueryType type;
	public QueryInputVerifier(int queryType)
	{
		switch (queryType)
		{
			case java.sql.Types.BIGINT:
			case java.sql.Types.INTEGER:
				type = QueryType.Number;
				break;
			case java.sql.Types.LONGNVARCHAR:
			case java.sql.Types.LONGVARCHAR:
			case java.sql.Types.NVARCHAR:
			case java.sql.Types.VARCHAR:
				type = QueryType.String;
				break;
			default:
				type = QueryType.String;
				break;
		}
	}
	
    @Override
    public boolean verify(JComponent input) 
    {
    	
        String text = ((JTextField) input).getText();
        
        switch (type)
		{
			case Character:
				return text.length() == 1;
			case Number:
				try
		        {
		    		new BigDecimal(text);
		    		return true;
		        		
		        } catch (NumberFormatException e) 
		        {
		            return false;
		        }
		}
		return true;
    }
}
