package edu.towson.database;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class MainWindow extends JFrame
{
	static final String URL = "jdbc:mysql://triton.towson.edu:3360";
	static final String USER = "rgott2";
	static final String PASS = "Cosc*9d8n";
	
	static final String USERDB = USER + "db";

	private MySQLConnection sqlConn;
	
	
	Button swapPages_btn;
	JPanel mainContentPanel;
	Swappable windowSwapper;

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
					
					ImageIcon img = new ImageIcon(new File("database.png").getCanonicalPath());
					window.setIconImage(img.getImage());
					window.setTitle("Database");
					
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
	 * @throws Exception 
	 */
	public MainWindow() throws Exception
	{
		// setup db to be called in other classes
		sqlConn = MySQLConnection.getInstance(); // singleton call pattern
		
		if(!sqlConn.connect(URL, USER, PASS, USERDB)) // connect to towson db
		{											   // using database
			MessageBox.show("Cannot Connect to Database. Please check your connection and try again.", "Connection ERROR");
			throw new Exception("Cannot Connect");
		}
		initialize();
		
		
		windowSwapper = Swappable.getInstance(this, mainContentPanel);
		windowSwapper.add("view", new ViewPanel("*","DEPARTMENT"));
		windowSwapper.add("Edit", new EditorPanel("DEPARTMENT"));
		
		windowSwapper.changeTo("view");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	private void initialize()
	{
		setMinimumSize(new Dimension(300, 200));
		setBounds(100, 100, 695, 359);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout());
		
		mainContentPanel = new JPanel();
		getContentPane().add(mainContentPanel,BorderLayout.CENTER);
		mainContentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainContentPanel.setLayout(new CardLayout(0, 0));
	}
}
