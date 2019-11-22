package LMS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JPasswordField;
import java.awt.Color;
import javax.swing.ImageIcon;

public class ADDLIBRARIAN extends JFrame {

	private JPanel contentPane;
	private JTextField TF_lib_id;
	private JTextField TF_name;
	private JTextField TF_username;
	private JPasswordField TF_password;
	private JTextField TF_email;
	private JTextField TF_tel;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ADDLIBRARIAN frame = new ADDLIBRARIAN();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	Connection connection=null;
	private JPasswordField TF_confirmPassword;

	/**
	 * Create the frame.
	 */
	public ADDLIBRARIAN() {
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
		
		JLabel lblAddLibrarian = new JLabel("ADD LIBRARIAN");
		lblAddLibrarian.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAddLibrarian.setBounds(116, 22, 185, 14);
		contentPane.add(lblAddLibrarian);
		
		JLabel lblLibrarianId = new JLabel("LIBRARIAN ID");
		lblLibrarianId.setBounds(46, 80, 180, 30);
		contentPane.add(lblLibrarianId);
		
		JLabel lblName = new JLabel("NAME");
		lblName.setBounds(46, 130, 180, 30);
		contentPane.add(lblName);
		
		JLabel lblUsername = new JLabel("USERNAME");
		lblUsername.setBounds(46, 180, 180, 30);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("PASSWORD");
		lblPassword.setBounds(46, 230, 180, 30);
		contentPane.add(lblPassword);
		
		JLabel lblConfirmPassword = new JLabel("CONFIRM PASSWORD");
		lblConfirmPassword.setBounds(46, 280, 180, 30);
		contentPane.add(lblConfirmPassword);
		
		JLabel lblEmail = new JLabel("EMAIL");
		lblEmail.setBounds(46, 330, 180, 30);
		contentPane.add(lblEmail);
		
		JLabel lblPhoneNo = new JLabel("PHONE NUMBER");
		lblPhoneNo.setBounds(46, 380, 180, 30);
		contentPane.add(lblPhoneNo);
		
		TF_lib_id = new JTextField();
		TF_lib_id.setEditable(false);
		TF_lib_id.setColumns(10);
		TF_lib_id.setBounds(260, 70, 440, 40);
		contentPane.add(TF_lib_id);
		generateLibId();
		
		TF_name = new JTextField();
		TF_name.setColumns(10);
		TF_name.setBounds(260, 120, 440, 40);
		contentPane.add(TF_name);
		
		TF_username = new JTextField();
		TF_username.setColumns(10);
		TF_username.setBounds(260, 170, 440, 40);
		contentPane.add(TF_username);
		
		TF_password = new JPasswordField();
		TF_password.setColumns(10);
		TF_password.setBounds(260, 220, 440, 40);
		contentPane.add(TF_password);
		
		TF_confirmPassword = new JPasswordField();
		TF_confirmPassword.setBounds(260, 270, 440, 40);
		contentPane.add(TF_confirmPassword);
		TF_confirmPassword.setColumns(10);
		
		TF_email = new JTextField();
		TF_email.setColumns(10);
		TF_email.setBounds(260,320, 440, 40);
		contentPane.add(TF_email);
		
		TF_tel = new JTextField();
		TF_tel.setColumns(10);
		TF_tel.setBounds(260, 370, 440, 40);
		contentPane.add(TF_tel);
		
		
		JButton btnSave = new JButton("SAVE");
		btnSave.setBounds(472, 483, 226, 42);
		contentPane.add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(TF_lib_id.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please Enter a Librarian ID!");
				}
				else if(TF_name.getText().trim().isEmpty()){
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
				else if(String.valueOf(TF_password.getPassword()).trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter a Password!");
				}
				else if(String.valueOf(TF_password.getPassword()).length()>10){
					JOptionPane.showMessageDialog(null, "Password is too long!\nPlease Enter a Valid Password!");
					TF_password.setText("");
				}
				else if(String.valueOf(TF_confirmPassword.getPassword()).trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Retype the Password!");
				}
				else if(TF_email.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter an Email!");
				}
				else if(!isValid(TF_email.getText()) || (TF_email.getText().length()>30)){
					JOptionPane.showMessageDialog(null, "Please Enter a Valid Email address!");
					TF_email.setText("");
				}
				else if(TF_tel.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter the Telephone Number!");
				}
				else if(!TF_tel.getText().matches("[0-9]*") || (TF_tel.getText().length()>15)){
					JOptionPane.showMessageDialog(null, "Please Enter a Valid Telephone Number!");
					TF_tel.setText("");
				}
				else {
					try {
						Statement pstId=connection.createStatement();
						String selectIdQuery = "select * from LIBRARIANS where LIBRARIAN_ID='"+TF_lib_id.getText()+"' ";
						ResultSet rsId=pstId.executeQuery(selectIdQuery);
						if(rsId.next()==true) 
						{
							JOptionPane.showMessageDialog(null, "Duplicate Librarian ID. Please use a different Librarian ID!");
							TF_lib_id.setText("");
						}
						else
						{
							try {
								Statement stat1=connection.createStatement();
								String selectQuery1 = "select * from LIBRARIANS where USERNAME='"+TF_username.getText()+"'";
								ResultSet rs1=stat1.executeQuery(selectQuery1);
								//Ensuring that there are no duplicate usernames in the database.
								if(rs1.next()==true)
								{
									JOptionPane.showMessageDialog(null, "Already Registered Username. Please use a different username!");
									TF_username.setText("");
								}
								else
								{
									int confirm=confirmPassword();
									if(confirm==1) 
									{
										String admin_id="1";
										String libStatus="ACTIVE";
										String query="insert into LIBRARIANS (LIBRARIAN_ID,ADMIN_ID,NAME,USERNAME,PASSWORD,EMAIL,PHONE_NUMBER,LOGIN_STATUS) values (?,?,?,?,?,?,?,?)";
										PreparedStatement pst=connection.prepareStatement(query);
										pst.setString(1,TF_lib_id.getText());
										pst.setString(2,admin_id);
										pst.setString(3,TF_name.getText());
										pst.setString(4,TF_username.getText());
										pst.setString(5,String.valueOf(md5(TF_password.getPassword())));
										pst.setString(6,TF_email.getText());
										pst.setString(7,TF_tel.getText());
										pst.setString(8,libStatus);
										
										
										pst.execute();
										
										JOptionPane.showMessageDialog(null, "Librarian Details Added Successfully");
										
										pst.close();
										clearTF();
										generateLibId();
									}
									else
									{
										JOptionPane.showMessageDialog(null, "The passwords don't match!!\n Please retype the password");
										TF_password.setText("");
										TF_confirmPassword.setText("");
									}
								}
								stat1.close();
								rs1.close();	
							}catch(Exception exIdD) {
								JOptionPane.showMessageDialog(null, "Error occured in checking librarian ID!!!");
							}
							
						}
						
						pstId.close();
						rsId.close();
						
						
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "An error occurred. Please fill out all the Required fields!!!");
						ex.printStackTrace();
						clearTF();
					}
				}	
			}
		});
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(ADDLIBRARIAN.class.getResource("/images/library.stack_-900x600.jpg")).getImage().getScaledInstance(464,455, Image.SCALE_SMOOTH)));
		lblNewLabel.setBounds(710, 70, 464, 455);
		contentPane.add(lblNewLabel);
		
		JButton btnBack = new JButton("BACK");
		btnBack.setBounds(243, 483, 195, 42);
		contentPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ADMINMAINMENU.main(new String[]{});
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
		String first=String.valueOf(TF_password.getPassword());
		String second=String.valueOf(TF_confirmPassword.getPassword());
		if(first.equals(second)) {
			passwordCheck=1;
		}else {
			passwordCheck=0;
		}
		return passwordCheck;
	}
	private boolean isValid(String email) {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(regex);
	}
	private void generateLibId() {
		int row=0;
		try {
			
			String queryCountRow="select * from LIBRARIANS ";
			PreparedStatement pstRow=connection.prepareStatement(queryCountRow);
			ResultSet rsRow=pstRow.executeQuery();
			
			while(rsRow.next()) {
				row++;
			}
			row++;
			String libid="lib"+String.valueOf(row);
			TF_lib_id.setText(libid);
			pstRow.close();
			rsRow.close();
		}catch(Exception exRow) {
			JOptionPane.showMessageDialog(null, "An error occurred.No rows found!!!");
			exRow.printStackTrace();
		}
	}

	private void clearTF() {
		TF_lib_id.setText("");
		TF_name.setText("");
		TF_username.setText("");
	    TF_password.setText("");
	    TF_confirmPassword.setText("");
		TF_email.setText("");
		TF_tel.setText("");
	}
	private void initialize() {
		// TODO Auto-generated method stub
		
	}

}
