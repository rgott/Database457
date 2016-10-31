package edu.towson.database;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.sql.PreparedStatement;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.SpringLayout.Constraints;

import edu.towson.database.editors.DepartmentEditor;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;

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

		if (sqlConn != null)
		{
			if(!sqlConn.connect(URL, USER, PASS, USERDB)); // connect to towson db
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
	
	private enum CurrentWindow
	{
		Editor,
		Viewer
	}
	Button swapPages_btn;
	JPanel mainContentPanel;
	CurrentWindow window = CurrentWindow.Viewer;
	private void initialize()
	{
		frame = new JFrame();
		frame.setMinimumSize(new Dimension(300, 200));
		frame.setBounds(100, 100, 695, 359);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().setLayout(new BorderLayout());
		
		mainContentPanel = new JPanel();
		frame.getContentPane().add(mainContentPanel,BorderLayout.CENTER);
		mainContentPanel.setLayout(new CardLayout(0, 0));
		if(sqlConn != null)
		{
			mainContentPanel.add(new ViewPanel(sqlConn.getPreparedStatement("SELECT * FROM DEPARTMENT")));
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
				mainContentPanel.add(new ViewPanel(null));
				swapPages_btn.setLabel("Create New");
				break;
		}
		mainContentPanel.revalidate(); // allow form to refresh
	}
}
