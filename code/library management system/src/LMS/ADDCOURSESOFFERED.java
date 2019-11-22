package LMS;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;

public class ADDCOURSESOFFERED extends JFrame {

	private JPanel contentPane;
	private JTextField TF_course;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ADDCOURSESOFFERED frame = new ADDCOURSESOFFERED();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	Connection connection=null;
	private JTextField TF_lib_id;
	/**
	 * Create the frame.
	 */
	public ADDCOURSESOFFERED() {
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
		
		JLabel lblNewLabel = new JLabel("ADD COURSES OFFERED");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(187, 11, 419, 40);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_3 = new JLabel("LIB ID");
		lblNewLabel_3.setBounds(41, 128, 125, 40);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_1 = new JLabel("COURSE");
		lblNewLabel_1.setBounds(41, 222, 125, 40);
		contentPane.add(lblNewLabel_1);
		
		TF_lib_id = new JTextField();
		TF_lib_id.setEditable(false);
		TF_lib_id.setBounds(187, 128, 413, 40);
		contentPane.add(TF_lib_id);
		TF_lib_id.setColumns(10);
		LIBRARIANMAINMENU libid=new LIBRARIANMAINMENU();
		TF_lib_id.setText(libid.getLibrarianId());
		
		TF_course = new JTextField();
		TF_course.setBounds(187, 222, 419, 40);
		contentPane.add(TF_course);
		TF_course.setColumns(10);
		
		JButton btnSave = new JButton("SAVE");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(TF_lib_id.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter a Librarian ID!");
				}
				else if(TF_course.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter a Course!");
				}
				else if(!TF_course.getText().matches("[a-zA-Z\\s]*") || (TF_course.getText().length()>80)){
					JOptionPane.showMessageDialog(null, "Only alphabetic letters allowed for Courses !\nPlease Enter a Valid Course");
					TF_course.setText("");
				}
				else if(checkCourse()==1){
					JOptionPane.showMessageDialog(null, "The Course is already Registered!!!/n Please enter  a New Course");
					TF_course.setText("");
				}
				else if(checkCourse()==0) {
					try {
						String query="insert into COURSES_OFFERED (LIBRARIAN_ID,COURSE) values (?,?)";
						PreparedStatement pst=connection.prepareStatement(query);
						pst.setString(1,TF_lib_id.getText());
						pst.setString(2,TF_course.getText());
						pst.execute();
						
						JOptionPane.showMessageDialog(null, " Course Added Successfully");
						
						pst.close();
						TF_course.setText("");
						
					}catch(Exception ex1) {
						JOptionPane.showMessageDialog(null, "An error Occured!!");
						ex1.printStackTrace();
						TF_course.setText("");
					}
				}
				
				 
			}
		});
		btnSave.setBounds(405, 369, 201, 51);
		contentPane.add(btnSave);
		
		JButton btnBack = new JButton("BACK");
		btnBack.setBounds(187, 369, 176, 51);
		contentPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				LIBRARIANMAINMENU.main(new String[]{});
			}
		});
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(new javax.swing.ImageIcon(ADDCOURSESOFFERED.class.getResource("/images/library (1).jpg")).getImage().getScaledInstance(464,455, Image.SCALE_SMOOTH)));
		lblNewLabel_2.setBounds(616, 48, 558, 483);
		contentPane.add(lblNewLabel_2);
		
		
	}
	private int checkCourse() {
		int check=0;
		try {
			String courseCheck="select * from COURSES_OFFERED where COURSE = '"+TF_course.getText()+"' ";
			PreparedStatement pstCheck=connection.prepareStatement(courseCheck);
			ResultSet rsCheck=pstCheck.executeQuery();
			while(rsCheck.next()) {
				check=1;
			}
			pstCheck.close();
			rsCheck.close();
		}catch(Exception ex2) {
			ex2.printStackTrace();
		}
		return check;
			
	}

	private void initialize() {
		// TODO Auto-generated method stub
		
	}
}
