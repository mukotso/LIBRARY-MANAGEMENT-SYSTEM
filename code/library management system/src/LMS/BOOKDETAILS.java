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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;

public class BOOKDETAILS extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BOOKDETAILS frame = new BOOKDETAILS();
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
	private JTextField TF_callNo;
	private JTextField TF_subject;
	private JTextField TF_title;
	private JTextField TF_author;
	private JTextField TF_publisher;
	private JTextField TF_edition;
	/**
	 * Create the frame.
	 */
	public BOOKDETAILS() {
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
		
		JLabel lblBooksInformation = new JLabel("BOOK DETAILS");
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
		lblAccessionNumber.setBounds(494, 95, 148, 37);
		contentPane.add(lblAccessionNumber);
		
		JLabel lblIsbn = new JLabel("ISBN");
		lblIsbn.setBounds(494, 145, 148, 37);
		contentPane.add(lblIsbn);
		
		JLabel lblCallNumber = new JLabel("CALL NUMBER");
		lblCallNumber.setBounds(494, 195, 148, 39);
		contentPane.add(lblCallNumber);
		
		JLabel lblSubject = new JLabel("SUBJECT");
		lblSubject.setBounds(494, 245, 148, 37);
		contentPane.add(lblSubject);
		
		JLabel lblTitle = new JLabel("TITLE");
		lblTitle.setBounds(494, 295, 148, 37);
		contentPane.add(lblTitle);
		
		JLabel lblAuthor = new JLabel("AUTHOR");
		lblAuthor.setBounds(494, 345, 148, 37);
		contentPane.add(lblAuthor);
		
		JLabel lblPublisher = new JLabel("PUBLISHER");
		lblPublisher.setBounds(494, 395, 148, 37);
		contentPane.add(lblPublisher);
		
		JLabel lblEditionNumber = new JLabel("EDITION NUMBER");
		lblEditionNumber.setBounds(494, 445, 148, 37);
		contentPane.add(lblEditionNumber);
		
		TF_accessNo = new JTextField();
		TF_accessNo.setEditable(false);
		TF_accessNo.setBounds(685, 95, 390, 37);
		contentPane.add(TF_accessNo);
		TF_accessNo.setColumns(10);
		SEARCHBOOKS accessNumber=new SEARCHBOOKS();
		TF_accessNo.setText(accessNumber.getAccessionNo());
		
		TF_isbn = new JTextField();
		TF_isbn.setEditable(false);
		TF_isbn.setColumns(10);
		TF_isbn.setBounds(685, 145, 390, 37);
		contentPane.add(TF_isbn);
		
		TF_callNo = new JTextField();
		TF_callNo.setEditable(false);
		TF_callNo.setColumns(10);
		TF_callNo.setBounds(685, 195, 390, 37);
		contentPane.add(TF_callNo);
		
		TF_subject = new JTextField();
		TF_subject.setEditable(false);
		TF_subject.setColumns(10);
		TF_subject.setBounds(685, 245, 390, 37);
		contentPane.add(TF_subject);
		
		TF_title = new JTextField();
		TF_title.setEditable(false);
		TF_title.setColumns(10);
		TF_title.setBounds(685, 295, 390, 37);
		contentPane.add(TF_title);
		
		TF_author = new JTextField();
		TF_author.setEditable(false);
		TF_author.setColumns(10);
		TF_author.setBounds(685, 345, 390, 37);
		contentPane.add(TF_author);
		
		TF_publisher = new JTextField();
		TF_publisher.setEditable(false);
		TF_publisher.setColumns(10);
		TF_publisher.setBounds(685, 395, 390, 37);
		contentPane.add(TF_publisher);
		
		TF_edition = new JTextField();
		TF_edition.setEditable(false);
		TF_edition.setColumns(10);
		TF_edition.setBounds(685, 445, 390, 37);
		contentPane.add(TF_edition);
		readBookDetails();
		
		readPicture(TF_accessNo.getText(), "System.Environment.SpecialFolder.LocalApplicationData");
		imageLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon("System.Environment.SpecialFolder.LocalApplicationData").getImage().getScaledInstance(464,455, Image.SCALE_SMOOTH)));
		
		
		JButton btnBack = new JButton("BACK");
		btnBack.setBounds(880, 508, 195, 42);
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
		String isbn="";
		String callNo="";
		String subject="";
		String title="";
		String author="";
		String publisher="";
		String edition="";
		
		try {
			String queryReadDb="select * from BOOKS where ACCESSION_NUMBER = '"+TF_accessNo.getText()+"' ";
			PreparedStatement pstReadDb=connection.prepareStatement(queryReadDb);
			ResultSet rsReadDb=pstReadDb.executeQuery();
			while(rsReadDb.next()) {
				isbn=rsReadDb.getString("ISBN");
				callNo=rsReadDb.getString("CALL_NUMBER");
				subject=rsReadDb.getString("SUBJECT");
				title=rsReadDb.getString("TITLE");
				author=rsReadDb.getString("AUTHOR");
				publisher=rsReadDb.getString("PUBLISHER");
				edition=rsReadDb.getString("EDITION_NUMBER");
			}
			TF_isbn.setText(isbn);
			TF_callNo.setText(callNo);
			TF_subject.setText(subject);
			TF_title.setText(title);
			TF_author.setText(author);
			TF_publisher.setText(publisher);
			TF_edition.setText(edition);
			
			pstReadDb.close();
			rsReadDb.close();
		}catch(Exception exRead) {
			exRead.printStackTrace();
		}
		
	}
	 public void readPicture(String access, String filename) {
	        // update sql
	        String selectSQL = "SELECT COVER_PHOTO FROM BOOKS WHERE ACCESSION_NUMBER=?";
	        ResultSet rs = null;
	        FileOutputStream fos = null;
	        PreparedStatement pstmt = null;
	 
	        try {
	            pstmt = connection.prepareStatement(selectSQL);
	            pstmt.setString(1, access);
	            rs = pstmt.executeQuery();
	 
	            // write binary stream into file
	            File file = new File(filename);
	            fos = new FileOutputStream(file);
	 
	            //System.out.println("Writing BLOB to file " + file.getAbsolutePath());
	            while (rs.next()) {
	                InputStream input = rs.getBinaryStream("COVER_PHOTO");
	                byte[] buffer = new byte[1024];
	                while (input.read(buffer) > 0) {
	                    fos.write(buffer);
	                }
	            }
	        } catch (SQLException | IOException e) {
	            System.out.println(e.getMessage());
	        } finally {
	            try {
	                if (rs != null) {
	                    rs.close();
	                }
	                if (pstmt != null) {
	                    pstmt.close();
	                }
	 
	                /*if (connection != null) {
	                    connection.close();
	                }*/
	                if (fos != null) {
	                    fos.close();
	                }
	 
	            } catch (SQLException | IOException e) {
	                System.out.println(e.getMessage());
	            }
	        }
	    }
	private void initialize() {
		// TODO Auto-generated method stub
		
	}
}
