package edu.towson.database;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import java.awt.CardLayout;

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
			}
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
<<<<<<< HEAD
=======
		
		if(sqlConn.isConnected())
		{
			mainContentPanel.add(new ViewPanel(sqlConn.getPreparedStatement("SELECT * FROM DEPARTMENT")));
		}
		else
		{
			mainContentPanel.add(new ErrorPanel());
		}
		
		Container bottomPanel = new Container();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		swapPages_btn = new Button("Create New");
		swapPages_btn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				swapPanelWindow();
				setMainPanelContentBasedOnCurrentWindow();
			}
		});
		bottomPanel.add(swapPages_btn);
		
		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
	}
	public void swapPanelWindow()
	{
		// swap
		if(window == CurrentWindow.Viewer)
		{
			window = CurrentWindow.Editor;
		}
		else if(window == CurrentWindow.Editor)
		{
			window = CurrentWindow.Viewer;
		}
	}
	public void setMainPanelContentBasedOnCurrentWindow()
	{
		mainContentPanel.removeAll();
		// TODO: fix Layout not updating
		switch (window)
		{
			case Editor:
				mainContentPanel.add(new DepartmentEditor());
				swapPages_btn.setLabel("Back");
				break;
			case Viewer:
				if(sqlConn.isConnected())
				{
					mainContentPanel.add(new ViewPanel(sqlConn.getPreparedStatement("SELECT * FROM DEPARTMENT")));
				}
				else
				{
					mainContentPanel.add(new ErrorPanel());
				}
				swapPages_btn.setLabel("Create New");
				break;
		}
		mainContentPanel.revalidate(); // allow form to refresh
>>>>>>> refs/remotes/origin/rob-dev
	}
}