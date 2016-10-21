package edu.towson.database;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainWindow
{
	static final String URL = "jdbc:mysql://triton.towson.edu:3360";
	static final String USER = "rgott2";
	static final String PASS = "Cosc*9d8n";
	
	static final String USERDB = USER + "db";

	private JFrame frame;
	private MySQLConnection sqlConn;

	/**
	 * Launch the application.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow()
	{
		// setup db to be called in other classes
		sqlConn = MySQLConnection.getInstance(); // singleton call pattern

		sqlConn.connect(URL, USER, PASS, USERDB); // connect to towson db using database
		
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setMinimumSize(new Dimension(300, 200));
		frame.setBounds(100, 100, 695, 359);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(new ViewPanel(sqlConn.getPreparedStatement("SELECT * FROM DEPARTMENT")),BorderLayout.CENTER);
		
		
		Container bottomPanel = new Container();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		Button create_btn = new Button("Create New");
		bottomPanel.add(create_btn);
		
		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

		
		
		
		
		
	}
	
	
	
	
}
