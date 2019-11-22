package LMS;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;

public class ISSUEDBOOKDETAILS extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ISSUEDBOOKDETAILS frame = new ISSUEDBOOKDETAILS();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	Connection connection=null;
	private JTextField TF_accessNo;
	private JTextField TF_isbn;
	private JTextField TF_title;
	private JTextField TF_regNo;
	private JTextField TF_issueDate;
	private JTextField TF_dueDate;
	/**
	 * Create the frame.
	 */
	public ISSUEDBOOKDETAILS() {
		super("Library Management System");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				LIBRARIANMAINMENU logout = new LIBRARIANMAINMENU();
				logout.logLogoutTime();
			}
		});
		//Database connection.
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
		
		JLabel lblBooksInformation = new JLabel("ISSUED BOOK DETAILS");
		lblBooksInformation.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblBooksInformation.setBounds(415, 11, 247, 44);
		contentPane.add(lblBooksInformation);

		JLabel lblBookCoverPhoto = new JLabel("BOOK COVER PHOTO");
		lblBookCoverPhoto.setBounds(128, 74, 255, 31);
		contentPane.add(lblBookCoverPhoto);
		
		JLabel imageLabel = new JLabel("");
		imageLabel.setBounds(44, 116, 399, 395);
		contentPane.add(imageLabel);
		
		JLabel lblAccessionNumber = new JLabel("ACCESSION NUMBER");
		lblAccessionNumber.setBounds(494, 95, 207, 37);
		contentPane.add(lblAccessionNumber);
		
		JLabel lblIsbn = new JLabel("ISBN");
		lblIsbn.setBounds(494, 145, 207, 37);
		contentPane.add(lblIsbn);
		
		JLabel lblTitle = new JLabel("TITLE");
		lblTitle.setBounds(494, 199, 207, 37);
		contentPane.add(lblTitle);
		
		JLabel lblRegNo = new JLabel("STUDENT REGISTRATION NUMBER");
		lblRegNo.setBounds(494, 249, 207, 37);
		contentPane.add(lblRegNo);
		
		JLabel lblIssueDate = new JLabel("ISSUE DATE");
		lblIssueDate.setBounds(494, 299, 207, 37);
		contentPane.add(lblIssueDate);
		
		JLabel lblDueDate = new JLabel("DUE DATE");
		lblDueDate.setBounds(494, 349, 207, 37);
		contentPane.add(lblDueDate);
		
		TF_accessNo = new JTextField();
		TF_accessNo.setEditable(false);
		TF_accessNo.setBounds(711, 95, 408, 37);
		contentPane.add(TF_accessNo);
		TF_accessNo.setColumns(10);
		SEARCHBOOKS accessNumber=new SEARCHBOOKS();
		TF_accessNo.setText(accessNumber.getAccessionNo());
		
		TF_isbn = new JTextField();
		TF_isbn.setEditable(false);
		TF_isbn.setColumns(10);
		TF_isbn.setBounds(711, 145, 408, 37);
		contentPane.add(TF_isbn);
		
		TF_title = new JTextField();
		TF_title.setEditable(false);
		TF_title.setColumns(10);
		TF_title.setBounds(711, 199, 408, 37);
		contentPane.add(TF_title);
		
		TF_regNo = new JTextField();
		TF_regNo.setEditable(false);
		TF_regNo.setColumns(10);
		TF_regNo.setBounds(711, 249, 408, 37);
		contentPane.add(TF_regNo);
		
		TF_issueDate = new JTextField();
		TF_issueDate.setEditable(false);
		TF_issueDate.setColumns(10);
		TF_issueDate.setBounds(711, 299, 408, 37);
		contentPane.add(TF_issueDate);
		
		TF_dueDate = new JTextField();
		TF_dueDate.setEditable(false);
		TF_dueDate.setColumns(10);
		TF_dueDate.setBounds(711, 349, 408, 37);
		contentPane.add(TF_dueDate);
		
		readBookDetails();
		BOOKDETAILS obj=new BOOKDETAILS();
		obj.readPicture(TF_accessNo.getText(), "System.Environment.SpecialFolder.LocalApplicationData");
		imageLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon("System.Environment.SpecialFolder.LocalApplicationData").getImage().getScaledInstance(464,455, Image.SCALE_SMOOTH)));
		
		JButton btnBack = new JButton("BACK");
		btnBack.setBounds(924, 469, 195, 42);
		contentPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SEARCHBOOKS accessNumber=new SEARCHBOOKS();
				accessNumber.clearSearchedAccessNo();
				
				dispose();
				SEARCHBOOKS.main(new String[]{});
			}
		});
	}
	private void readBookDetails() {
		String accessNo="";
		String isbn="";
		String title="";
		String regNo="";
		String issueDate="";
		String dueDate="";
		String status="Issued";
		try {
			String queryReadDb="select BOOKS.ISBN,BOOKS.TITLE,ISSUED_BOOKS.REGISTRATION_NUMBER,ISSUED_BOOKS.ISSUE_DATE,ISSUED_BOOKS.DUE_DATE from ISSUED_BOOKS,BOOKS where BOOKS.ACCESSION_NUMBER = ISSUED_BOOKS.ACCESSION_NUMBER and ISSUED_BOOKS.ACCESSION_NUMBER = '"+TF_accessNo.getText()+"' and ISSUED_BOOKS.STATUS = '"+status+"' ";
			PreparedStatement pstReadDb=connection.prepareStatement(queryReadDb);
			ResultSet rsReadDb=pstReadDb.executeQuery();
			while(rsReadDb.next()) {
				isbn=rsReadDb.getString("ISBN");
				title=rsReadDb.getString("TITLE");
				regNo=rsReadDb.getString("REGISTRATION_NUMBER");
				issueDate=rsReadDb.getString("ISSUE_DATE");
				dueDate=rsReadDb.getString("DUE_DATE");
			}
			TF_isbn.setText(isbn);
			TF_title.setText(title);
			TF_regNo.setText(regNo);
			TF_issueDate.setText(issueDate);
			TF_dueDate.setText(dueDate);
			
			pstReadDb.close();
			rsReadDb.close();
		}catch(Exception exRead) {
			exRead.printStackTrace();
		}
		
	}
	private void initialize() {
		// TODO Auto-generated method stub
		
	}
}

