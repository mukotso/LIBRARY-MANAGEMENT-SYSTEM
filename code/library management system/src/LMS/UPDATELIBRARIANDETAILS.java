package LMS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.Color;
import javax.swing.ImageIcon;

public class UPDATELIBRARIANDETAILS extends JFrame {

	private JPanel contentPane;
	private JTextField TF_lib_id;
	private JPasswordField TF_newPassword;
	private JPasswordField TF_confirmNewPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UPDATELIBRARIANDETAILS frame = new UPDATELIBRARIANDETAILS();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	Connection connection=null;
	private JPasswordField TF_currentPassword;
	
	/**
	 * Create the frame.
	 */
	public UPDATELIBRARIANDETAILS() {
		super("Library Management System");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				LIBRARIANMAINMENU logout = new LIBRARIANMAINMENU();
				logout.logLogoutTime();
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
		
		JLabel lblUpdateLibrarianLogin = new JLabel("UPDATE LIBRARIAN LOGIN DETAILS");
		lblUpdateLibrarianLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblUpdateLibrarianLogin.setBounds(290, 11, 269, 36);
		contentPane.add(lblUpdateLibrarianLogin);
		
		JLabel lblLibrarianId = new JLabel("LIBRARIAN ID");
		lblLibrarianId.setBounds(68, 115, 171, 36);
		contentPane.add(lblLibrarianId);
		
		JLabel lblCurrentPassword = new JLabel("CURRENT PASSWORD");
		lblCurrentPassword.setBounds(68, 174, 171, 36);
		contentPane.add(lblCurrentPassword);
		
		JLabel lblNewPassword = new JLabel("NEW PASSWORD");
		lblNewPassword.setBounds(68, 233, 171, 36);
		contentPane.add(lblNewPassword);
		
		JLabel lblConfirmNewPassword = new JLabel("CONFIRM NEW PASSWORD");
		lblConfirmNewPassword.setBounds(68, 293, 171, 36);
		contentPane.add(lblConfirmNewPassword);
		
		TF_lib_id = new JTextField();
		TF_lib_id.setEditable(false);
		TF_lib_id.setBounds(254, 115, 305, 36);
		contentPane.add(TF_lib_id);
		TF_lib_id.setColumns(10);
		LIBRARIANMAINMENU libid=new LIBRARIANMAINMENU();
		TF_lib_id.setText(libid.getLibrarianId());

		TF_currentPassword = new JPasswordField();
		TF_currentPassword.setBounds(254, 174, 305, 36);
		contentPane.add(TF_currentPassword);
		TF_currentPassword.setColumns(10);
		
		TF_newPassword = new JPasswordField();
		TF_newPassword.setColumns(10);
		TF_newPassword.setBounds(254, 233, 305, 36);
		contentPane.add(TF_newPassword);
		
		TF_confirmNewPassword = new JPasswordField();
		TF_confirmNewPassword.setBounds(254, 293, 305, 36);
		contentPane.add(TF_confirmNewPassword);
		TF_confirmNewPassword.setColumns(10);
		
		JButton btnUpdate = new JButton("UPDATE");
		btnUpdate.setBounds(453, 427, 119, 48);
		contentPane.add(btnUpdate);
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(TF_lib_id.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please Enter Your Librarian ID!");
				}
				else if(String.valueOf(TF_currentPassword.getPassword()).trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter Your Current Password!");
				}
				else if(checkCurrentPassword()==0) {
					JOptionPane.showMessageDialog(null, "The Current Password is Incorrect!\n Please Retype the Correct one");
					TF_currentPassword.setText("");
				}
				else if(String.valueOf(TF_newPassword.getPassword()).trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please Enter Your new Password!");
				}
				else if(String.valueOf(TF_newPassword.getPassword()).length()>10){
					JOptionPane.showMessageDialog(null, "Password is too long!\nPlease Enter a Valid Password!");
					TF_newPassword.setText("");
				}
				else if(String.valueOf(TF_confirmNewPassword.getPassword()).trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please Retype the new Password!");
				}
				else {
					try {
						String queryCheck = "select * from LIBRARIANS where LIBRARIAN_ID = '"+TF_lib_id.getText()+"' ";
						PreparedStatement pstCheck=connection.prepareStatement(queryCheck);
						ResultSet rsCheck=pstCheck.executeQuery();
						if(rsCheck.next()) {
								int confirm=confirmPassword();
								if(confirm==1) 
								{
									String newPassword=String.valueOf(md5(TF_newPassword.getPassword()));
									String queryUpdate = "update LIBRARIANS set PASSWORD = '"+newPassword+"' where LIBRARIAN_ID = '"+TF_lib_id.getText()+"' ";
									PreparedStatement pstUpdate=connection.prepareStatement(queryUpdate);
									pstUpdate.execute();
									
									JOptionPane.showMessageDialog(null, "Login Details Updated Successfully");
									
									pstUpdate.close();
									clearTf();
								}
								else
								{
									JOptionPane.showMessageDialog(null, "The passwords don't match!!\n Please retype the password");
									TF_currentPassword.setText("");
									TF_newPassword.setText("");
									TF_confirmNewPassword.setText("");
								}
								
							}else {
								JOptionPane.showMessageDialog(null, "Librarian Record Not Found!!!\n Please provide a valid librarian ID");
							}
							pstCheck.close();
							rsCheck.close();
							
							
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null, "Please Provide a Stronger new password!!");
							ex.printStackTrace();
							clearTf();
						}
				}
			}
		});
		
		JButton btnBack = new JButton("BACK");
		btnBack.setBounds(290, 427, 134, 48);
		contentPane.add(btnBack);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(UPDATELIBRARIANDETAILS.class.getResource("/images/IMG_0735-1-e1511040507716-768x573.jpg")));
		lblNewLabel.setBounds(580, 67, 604, 438);
		contentPane.add(lblNewLabel);
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				LIBRARIANMAINMENU.main(new String[]{});
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
			String currentPasswordCheck="select * from LIBRARIANS where LIBRARIAN_ID=? and PASSWORD=?";
			PreparedStatement pstCurrentPassword=connection.prepareStatement(currentPasswordCheck);
			pstCurrentPassword.setString(1,TF_lib_id.getText());
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
	private void clearTf() {
		TF_currentPassword.setText("");
		TF_newPassword.setText("");
		TF_confirmNewPassword.setText("");
	}
	private void initialize() {
		// TODO Auto-generated method stub
		
	}
}
