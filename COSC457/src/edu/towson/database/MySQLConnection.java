package edu.towson.database;

import java.sql.*;

import javax.swing.table.DefaultTableModel;
public class MySQLConnection
{
	private static MySQLConnection instance = null;
	private Connection sqlConnect;
	public static MySQLConnection getInstance()
	{
		if(instance == null)
		{
			instance = new MySQLConnection();
		}
		return instance;
	}
	private boolean _isConnected = false;
	public boolean isConnected()
	{
		return _isConnected;
	}
	
	private MySQLConnection()
	{
		// keep to stop external calls
	}
	
	public Connection getConnection()
	{
		return sqlConnect;
	}
	
	/*
	 * Connection url in the form
	 * 	[jdbc]:[sqlType]://[serverURL]:[port]
	 * 	[jdbc]: required is the class name used in driver manager
	 *  [sqlType]: database type towson uses
	 *  [serverURL]: address where the sql server can be found
	 *  [port]: port of the server
	 */
	public boolean connect(String URL, String USER, String PASS, String userdb)
	{
		
		if(sqlConnect == null)
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver"); // ensures required .jar is included
				sqlConnect = DriverManager.getConnection(URL, USER, PASS);
				
				sqlConnect.prepareStatement("use rgott2db").executeQuery(); // set database to work with
				
			} catch (SQLException e)
			{
				// Tell the user they have the wrong password
				e.printStackTrace();
				//sqlConnect = null;
				return _isConnected = false;
				
			} catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return _isConnected = true;
	}
	
	public boolean disconnect() throws SQLException
	{
		if(sqlConnect != null && !sqlConnect.isClosed())
		{
			sqlConnect.close();
			return true;
		}
		return false;
	}
	
	public PreparedStatement getPreparedStatement(String sql)
	{
		try
		{
			return sqlConnect.prepareStatement(sql);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static DefaultTableModel getModel(PreparedStatement ps)
	{
		try
		{
			return getModel(ps.executeQuery());
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static DefaultTableModel getModel(ResultSet rs)
	{
		try
		{
			int columns = rs.getMetaData().getColumnCount() - 1; // get num of columns (1 -> x) - 1 == 0 -> x

			// get header info
			String[] columnNames = new String[columns];
			for (int i = 1; i < rs.getMetaData().getColumnCount(); i++)
			{
				// System.out.println(rs.getMetaData().getColumnName(i));// print the columns
				columnNames[i - 1] = rs.getMetaData().getColumnName(i);
			}

			// set header info to model
			DefaultTableModel model = new DefaultTableModel(columnNames, 0);

			// set rows to model
			while (rs.next())
			{
				String[] row = new String[columns];
				for (int i = 1; i < columnNames.length; i++)
				{
					row[i - 1] = rs.getString(i);
				}
				model.insertRow(model.getRowCount(), row);

			}
			return model;
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return null;
	}
	
	public void finalize()
	{
		try
		{
			disconnect();
		} catch (SQLException e)
		{
			System.err.println("SOMETHING WENT TERRIBLY WRONG");
			e.printStackTrace();
		}
	}
	
	
}
