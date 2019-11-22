package LMS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.awt.Color;
import javax.swing.UIManager;

import java.sql.*;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class ADMINLOGIN extends JFrame{

	private JPanel contentPane;
	private JTextField TF_username;
	private JPasswordField TF_password;
	private JLabel lblAdminLogin;
	private JButton btnBack;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ADMINLOGIN frame = new ADMINLOGIN();
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
	public ADMINLOGIN() {
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
		
		lblAdminLogin = new JLabel("ADMIN LOGIN");
		lblAdminLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAdminLogin.setBounds(657, 39, 141, 40);
		contentPane.add(lblAdminLogin);
		
		JLabel lblUsername = new JLabel("USERNAME");
		lblUsername.setBounds(545, 100, 129, 30);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("PASSWORD");
		lblPassword.setBounds(545, 200, 129, 30);
		contentPane.add(lblPassword);
		
		TF_username = new JTextField();
		TF_username.setBounds(708, 100, 440, 40);
		contentPane.add(TF_username);
		TF_username.setColumns(10);
		
		TF_password = new JPasswordField();
		TF_password.setFont(new Font("Tahoma", Font.PLAIN, 12));
		TF_password.setBounds(708, 200, 440, 40);
		contentPane.add(TF_password);
		TF_password.setColumns(10);
		
		JButton btnLogin = new JButton("LOGIN");
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnLogin.setBackground(UIManager.getColor("Button.darkShadow"));
		btnLogin.setBounds(920, 483, 226, 42);
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
						else { 
							try {
								String query="select * from ADMINS where USERNAME=? and PASSWORD=?";
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
									JOptionPane.showMessageDialog(null, "Welcome");
									dispose();
									ADMINMAINMENU.main(new String[]{});
								}
								else {
									JOptionPane.showMessageDialog(null, "Username and password don't match. Try Again..");
								}
								rs.close();
								pst.close();
								clearTF();

							} catch (Exception ex) {
								JOptionPane.showMessageDialog(null, "Login Failed!! \n Please Try Again");
								ex.printStackTrace();
								clearTF();
							}
						}
							
					}
				});
			}
		});
		
		btnBack = new JButton("BACK");
		btnBack.setBounds(691, 483, 195, 42);
		contentPane.add(btnBack);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(ADMINLOGIN.class.getResource("/images/admin-login-icon-15.jpg")));
		lblNewLabel.setBounds(30, 100, 505, 413);
		contentPane.add(lblNewLabel);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				LMSMAINMENU.main(new String[]{});
			}
		});
		
		getAdminID();
		
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
	private String adminID;
	private JLabel lblNewLabel;
	public String getAdminID() {
		return adminID;
	}
	public void setAdminID(String adminID) {
		this.adminID=searchAdminID();
	}

	private String searchAdminID() {
		String adminId="Not Found";
		try {
			String queryGetAdminId="select * from ADMINS where USERNAME=? and PASSWORD=?";
			PreparedStatement pstGetAdminId=connection.prepareStatement(queryGetAdminId);
			pstGetAdminId.setString(1,TF_username.getText());
			pstGetAdminId.setString(2,String.valueOf(md5(TF_password.getPassword())));
			ResultSet rsGetAdminId=pstGetAdminId.executeQuery();
			
			while(rsGetAdminId.next()) {
				adminId=rsGetAdminId.getString("ADMIN_ID");
			}
			
		}catch(Exception exAdminId) {
			exAdminId.printStackTrace();
		}
		return adminId;
	}
	private void clearTF() {
		TF_username.setText("");
	    TF_password.setText("");
	}
	private void initialize() {
		// TODO Auto-generated method stub
		
	}


}
