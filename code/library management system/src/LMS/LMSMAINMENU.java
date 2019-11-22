
package LMS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import javax.swing.SwingConstants;

public class LMSMAINMENU extends JFrame{

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LMSMAINMENU frame = new LMSMAINMENU();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	Connection connection=null;

	/*
	 * 
	 * Create the frame.
	 */
	public LMSMAINMENU() {
		super("Library Management System");
		//Database connection
		initialize();
		connection=dbConnection.dbConnector();
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(LMSMAINMENU.class.getResource("/images/images (7).jpeg")));
		setFont(new Font("Calibri", Font.ITALIC, 14));
		setForeground(Color.BLACK);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(75, 75, 1200, 600);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 153, 153));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblLibraryManagementSystem = new JLabel("LIBRARY MANAGEMENT SYSTEM  MAIN MENU");
		lblLibraryManagementSystem.setHorizontalAlignment(SwingConstants.CENTER);
		lblLibraryManagementSystem.setBounds(55, 11, 1032, 47);
		lblLibraryManagementSystem.setFont(new Font("Tahoma", Font.BOLD, 30));
		contentPane.add(lblLibraryManagementSystem);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(LMSMAINMENU.class.getResource("/images/slidescope-libraryshare-buy-used-books-online.jpg")));
		lblNewLabel.setBounds(10, 61, 459, 420);
		contentPane.add(lblNewLabel);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(LMSMAINMENU.class.getResource("/images/welcome-text-animation-over-bokeh-background_r7aij_yx__F0006.png")));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
		label.setBounds(479, 68, 633, 122);
		contentPane.add(label);
		
		JButton btnAdministratorLogin = new JButton("ADMINISTRATOR LOGIN");
		btnAdministratorLogin.setBounds(479, 214, 561, 57);
		btnAdministratorLogin.setBackground(new Color(220, 220, 220));
		btnAdministratorLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnAdministratorLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defaultAdminDetails();
				dispose();
				ADMINLOGIN.main(new String[]{});
			}
		});
		contentPane.setLayout(null);
		contentPane.add(btnAdministratorLogin);
		
		JButton btnLibrarianLogin = new JButton("LIBRARIAN LOGIN");
		btnLibrarianLogin.setBounds(479, 321, 561, 57);
		btnLibrarianLogin.setBackground(new Color(220, 220, 220));
		btnLibrarianLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnLibrarianLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				LIBRARIANLOGIN.main(new String[]{});
			}
			
		});
		contentPane.add(btnLibrarianLogin);
		
		JButton btnExit = new JButton("EXIT");
		btnExit.setBounds(899, 473, 141, 47);
		contentPane.add(btnExit);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
			
		});
	}
	private void defaultAdminDetails() {
		if(checkAdmin()==0) {
			try {
				String query="insert into ADMINS (ADMIN_ID,NAME,USERNAME,PASSWORD,PHONE_NUMBER,EMAIL) values (?,?,?,?,?,?)";
				PreparedStatement pst=connection.prepareStatement(query);
				String adminid="1";
				String name="Admin";
				String username="admin";
				char[] defaultPass= {'0','0','0','0'};
				String password=md5(defaultPass);
				String tel="0700000000";
				String email="defaultAdmin@gmail.com";
				
				pst.setString(1,adminid);
				pst.setString(2,name);
				pst.setString(3,username);
				pst.setString(4,password);
				pst.setString(5,tel);
				pst.setString(6,email);
				
				pst.execute();
				
				JOptionPane.showMessageDialog(null, "Default Admin Details Added Successfully");
				
				pst.close();
			}catch(Exception exDefaultAdmin) {
				exDefaultAdmin.printStackTrace();
			}
		}
	}
	private int checkAdmin() {
		int checkAdmin=-1;
		try {
			String queryFindAdmin="select * from ADMINS";
			PreparedStatement pstFindAdmin=connection.prepareStatement(queryFindAdmin);
			ResultSet rsFindAdmin=pstFindAdmin.executeQuery();
			if(rsFindAdmin.next()) {
				checkAdmin=1;
			}
			else {
				checkAdmin=0;
			}
			pstFindAdmin.close();
			rsFindAdmin.close();
		}catch(Exception exFindAdmin) {
			exFindAdmin.printStackTrace();
		}
		return checkAdmin;
	}
	private String md5(char[] c) {
		try {
			MessageDigest digs = MessageDigest.getInstance("MD5");
			digs.update((new String(c)).getBytes("UTF8"));
			String str = new String(digs.digest());
			return str;
		}catch(Exception exMd5) {
			return "";
		}
	}
	private void initialize() {
		// TODO Auto-generated method stub
		
	}
}
