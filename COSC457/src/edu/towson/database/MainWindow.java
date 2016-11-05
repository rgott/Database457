package edu.towson.database;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import edu.towson.database.editors.DepartmentEditor;

import java.awt.CardLayout;

public class MainWindow extends JFrame
{
	static final String URL = "jdbc:mysql://triton.towson.edu:3360";
	static final String USER = "rgott2";
	static final String PASS = "Cosc*9d8n";
	
	static final String USERDB = USER + "db";

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
					window.setVisible(true);
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

		if (sqlConn != null)
		{
			if(!sqlConn.connect(URL, USER, PASS, USERDB)) // connect to towson db
			{											   // using database
				System.out.println("Cannot Connect");
				// TODO: add error window poppup
				return;
			}
		}
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	Button swapPages_btn;
	JPanel mainContentPanel;
	Swappable windowSwapper;
	private void initialize()
	{
		setMinimumSize(new Dimension(300, 200));
		setBounds(100, 100, 695, 359);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout());
		
		mainContentPanel = new JPanel();
		getContentPane().add(mainContentPanel,BorderLayout.CENTER);
		mainContentPanel.setLayout(new CardLayout(0, 0));

		
		windowSwapper = Swappable.getInstance(mainContentPanel);
		windowSwapper.add("view", new ViewPanel(sqlConn.getPreparedStatement("SELECT * FROM DEPARTMENT")));
		windowSwapper.add("Edit", new DepartmentEditor());
		
		windowSwapper.changeTo("view");
		
	}
}
