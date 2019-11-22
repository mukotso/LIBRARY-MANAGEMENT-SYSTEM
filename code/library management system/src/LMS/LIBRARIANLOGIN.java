package LMS;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import java.awt.Color;

public class LIBRARIANLOGIN extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField TF_username;
	private JPasswordField TF_password;
	private JLabel lblLibrarianLogin;
	private JButton btnBack;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LIBRARIANLOGIN frame = new LIBRARIANLOGIN();
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
	public LIBRARIANLOGIN() {
		super("Library Management System");
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
		
		lblLibrarianLogin = new JLabel("LIBRARIAN LOGIN");
		lblLibrarianLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblLibrarianLogin.setBounds(194, 44, 210, 45);
		contentPane.add(lblLibrarianLogin);
		
		JLabel lblUsername = new JLabel("USERNAME");
		lblUsername.setBounds(46, 100, 180, 30);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("PASSWORD");
		lblPassword.setBounds(46, 200, 180, 30);
		contentPane.add(lblPassword);
		
		TF_username = new JTextField();
		TF_username.setBounds(260, 100, 440, 40);
		contentPane.add(TF_username);
		TF_username.setColumns(10);
		
		TF_password = new JPasswordField();
		TF_password.setFont(new Font("Tahoma", Font.PLAIN, 12));
		TF_password.setBounds(260, 200, 440, 40);
		contentPane.add(TF_password);
		TF_password.setColumns(10);
		
		JButton btnLogin = new JButton("LOGIN");
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnLogin.setBackground(UIManager.getColor("Button.darkShadow"));
		btnLogin.setBounds(472, 483, 226, 42);
		contentPane.add(btnLogin);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						if(TF_username.getText().trim().isEmpty()) {
							JOptionPane.showMessageDialog(null, "Please Enter a Username!");
						}
						else if(String.valueOf(TF_password.getPassword()).trim().isEmpty()){
							JOptionPane.showMessageDialog(null, "Please Enter the Password!");
						}
						else if(libStatus()==0) {
							JOptionPane.showMessageDialog(null, "Librarian is Blocked. Please contact the Administrator..!");
							logFailedAttempts();
							clearTF();
						}
						else if(libStatus()==-1) {
							JOptionPane.showMessageDialog(null, "Username and password don't match. Try Again..");
							logFailedAttempts();
							clearTF();
						}
						else if(libStatus()==1){
							try {
								String query="select * from LIBRARIANS where USERNAME=? and PASSWORD=? ";
								PreparedStatement pst=connection.prepareStatement(query);
								pst.setString(1,TF_username.getText());
								pst.setString(2,String.valueOf(md5(TF_password.getPassword())));
								ResultSet rs=pst.executeQuery();
								int count = 0;
								while(rs.next()) {
									count=count+1;
								}
								if(count==1)
								{
									dispose();
									JOptionPane.showMessageDialog(null, "Welcome");
									LIBRARIANMAINMENU.main(new String[]{});
									logLibID();
								}
								else {
									JOptionPane.showMessageDialog(null, "Username and password don't match. Try Again..");
									logFailedAttempts();
								}
								rs.close();
								pst.close();
								clearTF();
							}catch (Exception e) {
								JOptionPane.showMessageDialog(null, "An error occurred!!! Try Again..");
								e.printStackTrace();
								clearTF();
							}	
							

						} 
					}
				});
			}
		});
		
		btnBack = new JButton("BACK");
		btnBack.setBounds(243, 483, 195, 42);
		contentPane.add(btnBack);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(LIBRARIANLOGIN.class.getResource("/images/1543575482phpWGhZLn.jpeg")));
		lblNewLabel.setBounds(710, 100, 464, 406);
		contentPane.add(lblNewLabel);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				LMSMAINMENU.main(new String[]{});
			}
		});
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
	public void logFailedAttempts() {
		try {
			Calendar today=Calendar.getInstance();
			Date loginDate=today.getTime();
			String loginDateString=loginDate.toString();
			 String lib_id=searchLibIDAttempt();
			 long longDateTime=loginDate.getTime();
			
			String querylogFailedAttempts="insert into FAILED_LIBRARIAN_LOGIN_ATTEMPTS (USERNAME,LIBRARIAN_ID,DATE_TIME,LONG_DATE_TIME) values (?,?,?,?) ";
			PreparedStatement pstLogLibId=connection.prepareStatement(querylogFailedAttempts);
			pstLogLibId.setString(1,TF_username.getText());
			pstLogLibId.setString(2,lib_id);
			pstLogLibId.setString(3,loginDateString);
			pstLogLibId.setLong(4,longDateTime);
			
			pstLogLibId.execute();
			
			pstLogLibId.close();
	
		}catch(Exception exLogLibId) {
			exLogLibId.printStackTrace();
		}
	}

	public void logLibID() {
		try {
			Calendar today=Calendar.getInstance();
			Date loginDate=today.getTime();
			String loginDateString=loginDate.toString();
			String logoutDateString="Pending";
			long longDateTime=loginDate.getTime();
			
			String querylogLibId="insert into LIBRARIAN_LOGIN_HISTORY (LIBRARIAN_ID,LOGIN_DATE_TIME,LOGOUT_DATE_TIME,LONG_DATE_TIME) values (?,?,?,?) ";
			PreparedStatement pstLogLibId=connection.prepareStatement(querylogLibId);
			pstLogLibId.setString(1,searchLibID());
			pstLogLibId.setString(2,loginDateString);
			pstLogLibId.setString(3,logoutDateString);
			pstLogLibId.setLong(4,longDateTime);
			
			pstLogLibId.execute();
			
			pstLogLibId.close();
			//JOptionPane.showMessageDialog(null, "Librarian Log In Time Saved successfully");
	
		}catch(Exception exLogLibId) {
			exLogLibId.printStackTrace();
		}
	}
	public String searchLibIDAttempt() {
		String libId="Not Found";
		try {
			String queryGetLibId="select * from LIBRARIANS where USERNAME=? ";
			PreparedStatement pstGetLibId=connection.prepareStatement(queryGetLibId);
			pstGetLibId.setString(1,TF_username.getText());
			ResultSet rsGetLibId=pstGetLibId.executeQuery();
			
			while(rsGetLibId.next()) {
				libId=rsGetLibId.getString("LIBRARIAN_ID");
			}
			pstGetLibId.close();
			rsGetLibId.close();
		}catch(Exception exLibId) {
			exLibId.printStackTrace();
		}
		return libId;
	}
	public String searchLibID() {
		String libId="Not Found";
		try {
			String queryGetLibId="select * from LIBRARIANS where USERNAME=? and PASSWORD=? ";
			PreparedStatement pstGetLibId=connection.prepareStatement(queryGetLibId);
			pstGetLibId.setString(1,TF_username.getText());
			pstGetLibId.setString(2,String.valueOf(md5(TF_password.getPassword())));
			ResultSet rsGetLibId=pstGetLibId.executeQuery();
			
			while(rsGetLibId.next()) {
				libId=rsGetLibId.getString("LIBRARIAN_ID");
			}
			pstGetLibId.close();
			rsGetLibId.close();
		}catch(Exception exLibId) {
			exLibId.printStackTrace();
		}
		return libId;
	}
	private int libStatus() {
		int status=0;
		String dBStatus="NO_MATCH";
		try {
			String query="select * from LIBRARIANS where USERNAME=? and PASSWORD=? ";
			PreparedStatement pst=connection.prepareStatement(query);
			pst.setString(1,TF_username.getText());
			pst.setString(2,String.valueOf(md5(TF_password.getPassword())));
			ResultSet rs=pst.executeQuery();
			while(rs.next()) {
				dBStatus=rs.getString("LOGIN_STATUS");
				System.out.println("status: "+dBStatus);
			}
			int dBStatus1=dBStatus.length();
			String active="ACTIVE";
			int active1=active.length();
			String blocked="BLOCKED";
			int blocked1=blocked.length();
			String noMatch="NO_MATCH";
			String deleted="*DELETED*";
			int deleted1=deleted.length();
			int noMatch1=noMatch.length();
			
			if(dBStatus1==active1) {
				status=1;
				System.out.println("status: "+status);
				
			}else if(dBStatus1==blocked1){
				status=0;
				System.out.println("status: "+status);
			}else if(dBStatus1==noMatch1 || dBStatus1==deleted1) {
				status=-1;
			}
			pst.close();
			rs.close();
		}catch(Exception exStatus) {
			exStatus.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error, Librarian Status not Found");
		}
		return status;
	}
	private void clearTF() {
		TF_username.setText("");
	    TF_password.setText("");
	}

	private void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
