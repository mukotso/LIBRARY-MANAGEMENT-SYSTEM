package LMS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.sql.Connection;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.Color;
import javax.swing.ImageIcon;

public class UPDATE_ADMIN_DETAILS extends JFrame {

	private JPanel contentPane;
	private JTextField TF_name;
	private JTextField TF_username;
	private JPasswordField TF_newPassword;
	private JTextField TF_tel;
	private JTextField TF_email;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UPDATE_ADMIN_DETAILS frame = new UPDATE_ADMIN_DETAILS();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	Connection connection=null;
	private JPasswordField TF_confirmNewPassword;
	private JPasswordField TF_currentPassword;

	/**
	 * Create the frame.
	 */
	public UPDATE_ADMIN_DETAILS() {
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
		
		JLabel lblUpdateAdminstratorDetails = new JLabel("UPDATE ADMINISTRATOR DETAILS");
		lblUpdateAdminstratorDetails.setBounds(116, 22, 262, 37);
		lblUpdateAdminstratorDetails.setFont(new Font("Tahoma", Font.BOLD, 14));
		contentPane.add(lblUpdateAdminstratorDetails);
		
		JLabel lblName = new JLabel("NAME");
		lblName.setBounds(46, 72, 180, 30);
		contentPane.add(lblName);
		
		JLabel lblUsername = new JLabel("USERNAME");
		lblUsername.setBounds(46, 132, 180, 30);
		contentPane.add(lblUsername);
		
		JLabel lblCurrentPassword = new JLabel("CURRENT PASSWORD");
		lblCurrentPassword.setBounds(46, 180, 180, 37);
		contentPane.add(lblCurrentPassword);
		
		JLabel lblNewPassword = new JLabel("NEW PASSWORD");
		lblNewPassword.setBounds(46, 248, 180, 30);
		contentPane.add(lblNewPassword);
		
		JLabel lblConfirmNewPassword = new JLabel("CONFIRM NEW PASSWORD");
		lblConfirmNewPassword.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		lblConfirmNewPassword.setBounds(46, 308, 180, 30);
		contentPane.add(lblConfirmNewPassword);
		
		JLabel lblPhoneNo = new JLabel("PHONE NUMBER");
		lblPhoneNo.setBounds(46, 368, 180, 30);
		contentPane.add(lblPhoneNo);
		
		JLabel lblEmail = new JLabel("EMAIL");
		lblEmail.setBounds(46, 428, 180, 30);
		contentPane.add(lblEmail);
		
		TF_name = new JTextField();
		TF_name.setBounds(260, 62, 440, 40);
		TF_name.setColumns(10);
		contentPane.add(TF_name);
		
		TF_username = new JTextField();
		TF_username.setBounds(260, 122, 440, 40);
		TF_username.setColumns(10);
		contentPane.add(TF_username);

		TF_currentPassword = new JPasswordField();
		TF_currentPassword.setBounds(260, 180, 440, 40);
		contentPane.add(TF_currentPassword);
		TF_currentPassword.setColumns(10);
		
		TF_newPassword = new JPasswordField();
		TF_newPassword.setBounds(260, 238, 440, 40);
		TF_newPassword.setColumns(10);
		contentPane.add(TF_newPassword);
		
		TF_confirmNewPassword = new JPasswordField();
		TF_confirmNewPassword.setBounds(260, 298, 440, 40);
		contentPane.add(TF_confirmNewPassword);
		TF_confirmNewPassword.setColumns(10);
		
		TF_tel = new JTextField();
		TF_tel.setBounds(260, 358, 440, 40);
		TF_tel.setColumns(10);
		contentPane.add(TF_tel);
		
		TF_email = new JTextField();
		TF_email.setBounds(260, 418, 440, 40);
		TF_email.setColumns(10);
		contentPane.add(TF_email);
		currentAdmin();
		
		JButton btnRegister = new JButton("UPDATE");
		btnRegister.setBounds(472, 483, 226, 42);
		contentPane.add(btnRegister);
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(TF_name.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter a Name!");
				}
				else if(!TF_name.getText().matches("[a-zA-Z\\s]*") || (TF_name.getText().length()>30)){
					JOptionPane.showMessageDialog(null, "Please Enter a Valid Name!");
					TF_name.setText("");
				}
				else if(TF_username.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter a Username!");
				}
				else if(TF_username.getText().length()>10){
					JOptionPane.showMessageDialog(null, "Username is too long!\nPlease Enter a Valid Username!");
					TF_username.setText("");
				}
				else if(String.valueOf(TF_currentPassword.getPassword()).trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter Your Current Password!");
				}
				else if(checkCurrentPassword()==0) {
					JOptionPane.showMessageDialog(null, "The Current Password is Incorrect!\n Please Retype the Correct one");
					TF_currentPassword.setText("");
				}
				else if(String.valueOf(TF_newPassword.getPassword()).trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter a New Password!");
				}
				else if(String.valueOf(TF_newPassword.getPassword()).length()>10){
					JOptionPane.showMessageDialog(null, "Password is too long!\nPlease Enter a Valid Password!");
					TF_newPassword.setText("");
				}
				else if(String.valueOf(TF_confirmNewPassword.getPassword()).trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Retype the New Password!");
				}
				else if(TF_tel.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter a Phone Number!");
				}
				else if(!TF_tel.getText().matches("[0-9]*") || (TF_name.getText().length()>15)){
					JOptionPane.showMessageDialog(null, "Please Enter a Valid Telephone Number!");
					TF_tel.setText("");
				}
				else if(TF_email.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter an Email!");
				}
				else if(!isValid(TF_email.getText()) || (TF_email.getText().length()>30)){
					JOptionPane.showMessageDialog(null, "Please Enter a Valid Email address!");
					TF_email.setText("");
				}
				else {
					try {
						int confirm=confirmPassword();
						if(confirm==1) 
						{
							String adminid="1";
							String query="update ADMINS set NAME = '"+TF_name.getText()+"', USERNAME = '"+TF_username.getText()+"',	PASSWORD = '"+String.valueOf(md5(TF_newPassword.getPassword()))+"', PHONE_NUMBER = '"+TF_tel.getText()+"', EMAIL = '"+TF_email.getText()+"' where ADMIN_ID = '"+adminid+"' ";
							PreparedStatement pst=connection.prepareStatement(query);
							
							pst.execute();
							
							JOptionPane.showMessageDialog(null, "Administrator Details Updated Successfully");
							dispose();
							ADMINMAINMENU.main(new String[]{});
							
							pst.close();
							clearTF();
						}
						else
						{
							JOptionPane.showMessageDialog(null, "The passwords don't match!!\n Please retype the password");
							TF_newPassword.setText("");
							TF_confirmNewPassword.setText("");
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "An error occurred. Please fill out all the Required fields!!!");
						ex.printStackTrace();
						clearTF();
					}
				}	
			}
		});
		
		JButton btnBack = new JButton("BACK");
		btnBack.setBounds(243, 483, 195, 42);
		contentPane.add(btnBack);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(UPDATE_ADMIN_DETAILS.class.getResource("/images/library-management-system-background-image-2.jpg")));
		lblNewLabel.setBounds(723, 70, 451, 480);
		contentPane.add(lblNewLabel);
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ADMINMAINMENU.main(new String[]{});
			}
		});
	}
    private void currentAdmin() {
    	try {
			String queryAdmin="select * from ADMINS where ADMIN_ID = '"+1+"' ";
			
			PreparedStatement pstAdmin=connection.prepareStatement(queryAdmin);
			ResultSet rsAdmin=pstAdmin.executeQuery();
			while(rsAdmin.next())
			{
				TF_name.setText(rsAdmin.getString("NAME"));
				TF_username.setText(rsAdmin.getString("USERNAME"));
				TF_tel.setText(rsAdmin.getString("PHONE_NUMBER"));
				TF_email.setText(rsAdmin.getString("EMAIL"));
				
				JOptionPane.showMessageDialog(null, "The following are previous Admin details\n Please Update the necessary Fields");
			}
			
			
			pstAdmin.close();
			rsAdmin.close();
			
		}catch(Exception exTF) {
			exTF.printStackTrace();
		}
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
	private int confirmPassword() {
		int passwordCheck;
		String first=String.valueOf(TF_newPassword.getPassword());
		String second=String.valueOf(TF_confirmNewPassword.getPassword());
		if(first.equals(second)) {
			passwordCheck=1;
		}else {
			passwordCheck=0;
		}
		return passwordCheck;
	}
	private int checkCurrentPassword() {
		int check=-1;
		try {
			String currentPasswordCheck="select * from ADMINS where ADMIN_ID=? and PASSWORD=?";
			PreparedStatement pstCurrentPassword=connection.prepareStatement(currentPasswordCheck);
			pstCurrentPassword.setString(1,"1");
			pstCurrentPassword.setString(2,String.valueOf(md5(TF_currentPassword.getPassword())));
			ResultSet rsCurrentPassword=pstCurrentPassword.executeQuery();
			
			if(rsCurrentPassword.next()) {
				check=1;
			}
			else {
				check=0;
			}
			pstCurrentPassword.close();
			rsCurrentPassword.close();
			
		}catch(Exception exCheckCurrent) {
			exCheckCurrent.printStackTrace();
		}
		return check;
	}
	private boolean isValid(String email) {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(regex);
	}
	private void clearTF() {
		TF_name.setText("");
		TF_username.setText("");
		TF_currentPassword.setText("");
		TF_newPassword.setText("");
		TF_confirmNewPassword.setText("");
		TF_tel.setText("");
		TF_email.setText("");
	}

	private void initialize() {
		// TODO Auto-generated method stub
		
	}
}
