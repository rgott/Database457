package edu.towson.database;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class QueryInputVerifier extends InputVerifier
{
	private enum QueryType
	{
		Number,
		String,
		Character, 
		Date
	}
	ColumnData cdata;
	QueryType type;
	JLabel errorMessage;
	public QueryInputVerifier(JLabel errorMessage, ColumnData data)
	{
		cdata = data;
		this.errorMessage = errorMessage;
		switch (data.QueryType)
		{
			case java.sql.Types.DATE:
				type = QueryType.Date;
				break;
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
        
        if(cdata.Precision < text.length())
        {
        	// must be of length cdata.Precision
        	return false;
        }
        
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
		        	// number not valid
		            return false;
		        }
			case Date:
				SimpleDateFormat df = new SimpleDateFormat("MM-dd-yy");//TODO: Check if this date format is correct
			    try 
			    {
			        df.parse(text);
			        return true;
			    } catch (ParseException e) 
			    {
			    	// not valid format (MM-dd-yy)
			        return false;
			    }
		}
		return true;
    }
}
