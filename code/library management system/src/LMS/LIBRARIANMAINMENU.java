package LMS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.awt.Color;

public class LIBRARIANMAINMENU extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LIBRARIANMAINMENU frame = new LIBRARIANMAINMENU();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	Connection connection=null;


	/**
	 * Create the frame.
	 */
	public LIBRARIANMAINMENU() {
		super("Library Management System");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				logLogoutTime();
			}
		});
		//Database connection
		initialize();
		connection=dbConnection.dbConnector();
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(LMSMAINMENU.class.getResource("/images/images (7).jpeg")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(75, 75, 1200, 600);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 153, 153));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLibrarianMainMenu = new JLabel("LIBRARIAN MAIN MENU");
		lblLibrarianMainMenu.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblLibrarianMainMenu.setBounds(397, 11, 286, 49);
		contentPane.add(lblLibrarianMainMenu);
		
		JButton btnBooksInformation = new JButton("BOOKS INFORMATION");
		btnBooksInformation.setBounds(225, 175, 593, 55);
		contentPane.add(btnBooksInformation);
		btnBooksInformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				BOOKSINFORMATION.main(new String[]{});
			}
		});
		
		JButton btnStudentInformation = new JButton("STUDENT INFORMATION");
		btnStudentInformation.setBounds(225, 343, 593, 55);
		contentPane.add(btnStudentInformation);
		btnStudentInformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				STUDENTINFORMATION.main(new String[]{});
			}
		});
		
		JButton btnAddCourses = new JButton("ADD COURSES OFFERED");
		btnAddCourses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ADDCOURSESOFFERED.main(new String[]{});
			}
		});
		btnAddCourses.setBounds(225, 262, 593, 55);
		contentPane.add(btnAddCourses);
		
		
		JButton btnUpdateLoginDetails = new JButton("UPDATE LOGIN DETAILS");
		btnUpdateLoginDetails.setBounds(225, 88, 594, 55);
		contentPane.add(btnUpdateLoginDetails);
		btnUpdateLoginDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				UPDATELIBRARIANDETAILS.main(new String[]{});
			}
		});
		
		JButton btnLogout = new JButton("LOGOUT");
		btnLogout.setBounds(653, 424, 165, 49);
		contentPane.add(btnLogout);
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				LMSMAINMENU.main(new String[]{});
				logLogoutTime();
			}
		});
	}

	public void logLogoutTime() {
		try {
			String pendingLogout="Pending";
			Calendar today=Calendar.getInstance();
			Date logoutDate=today.getTime();
			String logoutDateString=logoutDate.toString();
			
			String querylogout="update LIBRARIAN_LOGIN_HISTORY set LOGOUT_DATE_TIME = '"+logoutDateString+"' where LIBRARIAN_ID = '"+getLibrarianId()+"' and LOGOUT_DATE_TIME = '"+pendingLogout+"' ";
			PreparedStatement pstLogout=connection.prepareStatement(querylogout);
			
			pstLogout.execute();
			
			pstLogout.close();
			//JOptionPane.showMessageDialog(null, "Librarian Logout Time Saved successfully");
	
		}catch(Exception exLogLibId) {
			exLogLibId.printStackTrace();
		}
	}
	public String getLibrarianId() {
		String libId="Not Found";
		try {
			String querySelectLidId="select * from LIBRARIAN_LOGIN_HISTORY ";
			PreparedStatement pstSelectLibId=connection.prepareStatement(querySelectLidId);
			ResultSet rsSelectLibId=pstSelectLibId.executeQuery();
			
			while(rsSelectLibId.next()) {
				libId=rsSelectLibId.getString("LIBRARIAN_ID");
			}
			pstSelectLibId.close();
			rsSelectLibId.close();
		}catch(Exception exLibId) {
			exLibId.printStackTrace();
		}
		
		return libId;
	}
	private void initialize() {
		// TODO Auto-generated method stub
		
	}
}
