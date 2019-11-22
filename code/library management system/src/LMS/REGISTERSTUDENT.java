package LMS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.jidesoft.swing.ComboBoxSearchable;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;
import javax.swing.ImageIcon;

public class REGISTERSTUDENT extends JFrame {

	private JPanel contentPane;
	private JTextField TF_regNo;
	private JTextField TF_name;
	private JTextField TF_tel;
	private JTextField TF_lib_id;
	private String comboSelection="";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					REGISTERSTUDENT frame = new REGISTERSTUDENT();
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
	public REGISTERSTUDENT() {
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
		
		JLabel lblRegisterStudent = new JLabel("REGISTER STUDENT");
		lblRegisterStudent.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblRegisterStudent.setBounds(342, 22, 262, 37);
		contentPane.add(lblRegisterStudent);
		
		JLabel lblRegNo = new JLabel("REGISTRATION NUMBER");
		lblRegNo.setBounds(46, 80, 180, 30);
		contentPane.add(lblRegNo);
		
		JLabel lblName = new JLabel("NAME");
		lblName.setBounds(46, 150, 180, 30);
		contentPane.add(lblName);
		
		JLabel lblDegree = new JLabel("DEGREE");
		lblDegree.setBounds(46, 220, 180, 30);
		contentPane.add(lblDegree);
		
		JLabel lblPhoneNo = new JLabel("PHONE NUMBER");
		lblPhoneNo.setBounds(46, 290, 180, 30);
		contentPane.add(lblPhoneNo);
		
		JLabel lblLibrarianId = new JLabel("LIBRARIAN ID");
		lblLibrarianId.setBounds(46, 360, 180, 30);
		contentPane.add(lblLibrarianId);
		
		TF_regNo = new JTextField();
		TF_regNo.setBounds(260, 70, 440, 40);
		contentPane.add(TF_regNo);
		TF_regNo.setColumns(10);
		
		TF_name = new JTextField();
		TF_name.setColumns(10);
		TF_name.setBounds(260, 140, 440, 40);
		contentPane.add(TF_name);
		
		JComboBox<String> TF_degree = new JComboBox<String>();
		TF_degree.setMaximumRowCount(5);
		TF_degree.setEditable(false);
		TF_degree.setToolTipText("Please Select a Degree");
		TF_degree.setBounds(260, 210, 440, 40);
		contentPane.add(TF_degree);
		try{
            Statement stmt = connection.createStatement();
            String query = "SELECT * FROM COURSES_OFFERED";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next())
            {
            	TF_degree.addItem(rs.getString("COURSE")); 

            }	
            	stmt.close();
            	rs.close();
		}  
		catch(Exception ex)
		{	
    	ex.printStackTrace();
		}
		ComboBoxSearchable searchable = new ComboBoxSearchable(TF_degree);
		TF_degree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboSelection=TF_degree.getSelectedItem().toString();
				System.out.println(comboSelection);
			}
		});
		
		TF_tel = new JTextField();
		TF_tel.setColumns(10);
		TF_tel.setBounds(260, 280, 440, 40);
		contentPane.add(TF_tel);
		
		TF_lib_id = new JTextField();
		TF_lib_id.setEditable(false);
		TF_lib_id.setColumns(10);
		TF_lib_id.setBounds(260, 350, 440, 40);
		contentPane.add(TF_lib_id);
		LIBRARIANMAINMENU libid=new LIBRARIANMAINMENU();
		TF_lib_id.setText(libid.getLibrarianId());
		
		JButton btnRegister = new JButton("REGISTER");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(TF_regNo.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please Enter the Student Registration Number!");
				}
				else if(TF_regNo.getText().length()>20){
					JOptionPane.showMessageDialog(null, "Registration Number can't be more than 8 characters!\nPlease Enter a valid Registration Number!");
					TF_regNo.setText("");
				}
				else if(checkStudentRegNo()==1) {
					JOptionPane.showMessageDialog(null, "Student is Already Registered!");
					clearTF();
				}
				else if(TF_name.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter the Student's Name!");
				}
				else if(!TF_name.getText().matches("[a-zA-Z\\s]*") || (TF_name.getText().length()>30)){
					JOptionPane.showMessageDialog(null, "Please Enter a Valid Name!");
					TF_name.setText("");
				}
				else if(comboSelection.equals("")) {
					JOptionPane.showMessageDialog(null, "Please Enter the Student's Degree of Study!");
				}
				else if(TF_tel.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter the Student's Phone Number!");
				}
				else if(!TF_tel.getText().matches("[0-9]*") || (TF_tel.getText().length()>15)){
					JOptionPane.showMessageDialog(null, "Please Enter a Valid Telephone Number!");
					TF_tel.setText("");
				}
				else if(TF_lib_id.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter Your Librarian ID!");
				}
				else {
					try {
						String query="insert into STUDENTS (REGISTRATION_NUMBER,LIBRARIAN_ID,NAME,DEGREE,PHONE_NUMBER) values (?,?,?,?,?)";
						PreparedStatement pst=connection.prepareStatement(query);
						pst.setString(1,TF_regNo.getText());
						pst.setString(2,TF_lib_id.getText());
						pst.setString(3,TF_name.getText());
						pst.setString(4,comboSelection);
						pst.setString(5,TF_tel.getText());

						
						
						pst.execute();
						
						JOptionPane.showMessageDialog(null, "Student Details Added Successfully");
						
						pst.close();
						clearTF();
						
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "An error occurred. Please fill out all the Required fields!!!");
						ex.printStackTrace();
						clearTF();
					}
				}	
			}
		});
		btnRegister.setBounds(472, 483, 226, 42);
		contentPane.add(btnRegister);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(REGISTERSTUDENT.class.getResource("/images/magdalene_college_library.jpg")).getImage().getScaledInstance(464,455, Image.SCALE_SMOOTH)));
		lblNewLabel.setBounds(710, 45, 464, 505);
		contentPane.add(lblNewLabel);

		JButton btnBack = new JButton("BACK");
		btnBack.setBounds(243, 483, 195, 42);
		contentPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				STUDENTINFORMATION.main(new String[]{});
			}
		});
	}
	private int checkStudentRegNo() {
		int regNoExists=-1;
		try {
			String queryRegNo="select * from STUDENTS where REGISTRATION_NUMBER = '"+TF_regNo.getText()+"' ";
			PreparedStatement pstRegNo=connection.prepareStatement(queryRegNo);
			ResultSet rsRegNo=pstRegNo.executeQuery();
			if(rsRegNo.next()) {
				regNoExists=1;
			}
			else {
				regNoExists=0;
			}
			pstRegNo.close();
			rsRegNo.close();
		}catch(Exception exRegNo) {
			exRegNo.printStackTrace();
		}
		return regNoExists;
	}
	private void clearTF() {
		TF_regNo.setText("");
		TF_name.setText("");
		TF_tel.setText("");
		comboSelection="";
	}

	private void initialize() {
		// TODO Auto-generated method stub
		
	}
}
