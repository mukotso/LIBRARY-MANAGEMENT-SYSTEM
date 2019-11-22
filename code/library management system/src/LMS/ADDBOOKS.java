package LMS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.ImageIcon;
import java.awt.Color;

public class ADDBOOKS extends JFrame {

	private JPanel contentPane;
	private JTextField TF_accessionNo;
	private JTextField TF_isbn;
	private JTextField TF_callNumber;
	private JTextField TF_lib_id;
	private JTextField TF_subject;
	private JTextField TF_title;
	private JTextField TF_author;
	private JTextField TF_publisher;
	private JTextField TF_edition;
	private JButton addCoverPic;
	private String filePath;
	private JLabel lblNewLabel;
	private String filename="";
	private byte[] coverPic;
	private int exists=0;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ADDBOOKS frame = new ADDBOOKS();
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
	public ADDBOOKS() {
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
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(ADDBOOKS.class.getResource("/images/images (7).jpeg")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(75, 75, 1200, 600);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 153, 153));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblAddBooks = new JLabel("ADD BOOKS");
		lblAddBooks.setBounds(227, 4, 233, 30);
		lblAddBooks.setFont(new Font("Tahoma", Font.BOLD, 14));
		contentPane.add(lblAddBooks);

		JLabel lblLibrarianId = new JLabel("LIBRARIAN ID");
		lblLibrarianId.setBounds(46, 53, 181, 35);
		contentPane.add(lblLibrarianId);
		
		JLabel lblAccessionNumber = new JLabel("ACCESSION NUMBER");
		lblAccessionNumber.setBounds(46, 102, 181, 35);
		contentPane.add(lblAccessionNumber);
		
		JLabel lblIsbn = new JLabel("ISBN");
		lblIsbn.setBounds(46, 151, 181, 35);
		contentPane.add(lblIsbn);

		JLabel lblCallNumber = new JLabel("CALL NUMBER");
		lblCallNumber.setBounds(46, 197, 181, 35);
		contentPane.add(lblCallNumber);
		
		JLabel lblSubject = new JLabel("SUBJECT");
		lblSubject.setBounds(46, 248, 181, 35);
		contentPane.add(lblSubject);
		
		JLabel lblTitle = new JLabel("TITLE");
		lblTitle.setBounds(46, 294, 181, 35);
		contentPane.add(lblTitle);
		
		JLabel lblAuthor = new JLabel("AUTHOR");
		lblAuthor.setBounds(46, 340, 181, 35);
		contentPane.add(lblAuthor);
		
		JLabel lblPublisher = new JLabel("PUBLISHER");
		lblPublisher.setBounds(46, 392, 181, 35);
		contentPane.add(lblPublisher);
		
		JLabel lblEditionNo = new JLabel("EDITION NUMBER");
		lblEditionNo.setBounds(46, 438, 181, 35);
		contentPane.add(lblEditionNo);

		TF_lib_id = new JTextField();
		TF_lib_id.setBounds(257, 45, 441, 35);
		TF_lib_id.setEditable(false);
		TF_lib_id.setColumns(10);
		contentPane.add(TF_lib_id);
		LIBRARIANMAINMENU libid=new LIBRARIANMAINMENU();
		TF_lib_id.setText(libid.getLibrarianId());
		
		TF_accessionNo = new JTextField();
		TF_accessionNo.setColumns(10);
		TF_accessionNo.setBounds(257, 97, 441, 35);
		TF_accessionNo.setEditable(false);
		contentPane.add(TF_accessionNo);
		TF_accessionNo.setText(generateAccessionNo());
		
		TF_isbn = new JTextField();
		TF_isbn.setBounds(257, 146, 441, 35);
		TF_isbn.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					String queryBook="select * from BOOKS where ISBN='"+TF_isbn.getText()+"' ";
					
					PreparedStatement pstBook=connection.prepareStatement(queryBook);
					ResultSet rsBook=pstBook.executeQuery();
					String accessInDb="";
					if(rsBook.next())
					{
						TF_callNumber.setText(rsBook.getString("CALL_NUMBER"));
						TF_subject.setText(rsBook.getString("SUBJECT"));
						TF_title.setText(rsBook.getString("TITLE"));
						TF_author.setText(rsBook.getString("AUTHOR"));
						TF_publisher.setText(rsBook.getString("PUBLISHER"));
						TF_edition.setText(rsBook.getString("EDITION_NUMBER"));
						accessInDb=rsBook.getString("ACCESSION_NUMBER");
						
						filename="exists";
						exists=1;
						
						JOptionPane.showMessageDialog(null, "The Book Already Exists..\nPlease Press the SAVE button to Add a new copy");
					}
					if(exists==1) {
						BOOKDETAILS obj=new BOOKDETAILS();
						obj.readPicture(accessInDb, "System.Environment.SpecialFolder.LocalApplicationData");
						lblNewLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon("System.Environment.SpecialFolder.LocalApplicationData").getImage().getScaledInstance(464,455, Image.SCALE_SMOOTH)));
						
					}
					pstBook.close();
					rsBook.close();
					
				}catch(Exception exTF) {
					JOptionPane.showMessageDialog(null, "Please fill out the ISBN field!!!");
					exTF.printStackTrace();
				}
			}
		});
		contentPane.add(TF_isbn);
		TF_isbn.setColumns(10);
		
		TF_callNumber = new JTextField();
		TF_callNumber.setBounds(257, 197, 441, 35);
		contentPane.add(TF_callNumber);
		TF_callNumber.setColumns(10);
		
		TF_subject = new JTextField();
		TF_subject.setBounds(257, 243, 441, 35);
		contentPane.add(TF_subject);
		TF_subject.setColumns(10);
		
		TF_title = new JTextField();
		TF_title.setBounds(257, 292, 441, 35);
		contentPane.add(TF_title);
		TF_title.setColumns(10);
		
		TF_author = new JTextField();
		TF_author.setBounds(257, 340, 441, 35);
		contentPane.add(TF_author);
		TF_author.setColumns(10);
		
		TF_publisher = new JTextField();
		TF_publisher.setBounds(257, 387, 441, 35);
		contentPane.add(TF_publisher);
		TF_publisher.setColumns(10);
		
		TF_edition = new JTextField();
		TF_edition.setBounds(257, 433, 441, 35);
		contentPane.add(TF_edition);
		TF_edition.setColumns(10);
		
		addCoverPic = new JButton("ADD COVER PAGE PICTURE");
		addCoverPic.setBounds(737, 50, 441, 35);
		contentPane.add(addCoverPic);
		addCoverPic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 getCoverPhoto();
				 if(exists==0) {
						lblNewLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(filename).getImage().getScaledInstance(464,455, Image.SCALE_SMOOTH)));
				 }
			}
		});
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(737, 108, 437, 417);
		lblNewLabel.setIcon(new ImageIcon(ADDBOOKS.class.getResource("/images/istockphoto-900301626-612x612.jpg")));
		contentPane.add(lblNewLabel);
		
		JButton btnSave = new JButton("SAVE");
		btnSave.setBounds(472, 483, 226, 42);
		contentPane.add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(TF_lib_id.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter a Librarian ID!");
				}
				else if(TF_accessionNo.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter the Book's Accession Number!");
				}
				else if(!TF_accessionNo.getText().matches("[0-9]*") || (!(TF_accessionNo.getText().length()==8))){
					JOptionPane.showMessageDialog(null, "Please Enter a Valid Accession Number!");
					TF_accessionNo.setText("");
				}
				else if(TF_isbn.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter a Book Isbn!");
				}
				else if(!TF_isbn.getText().matches("[0-9]*") || (TF_isbn.getText().length()!=10)){
					JOptionPane.showMessageDialog(null, "Please Enter a Valid Isbn!");
					TF_isbn.setText("");
				}
				else if(TF_callNumber.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please Enter the Book's Call Number!");	
				}
				else if(TF_subject.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter a Subject!");
				}
				else if(!TF_subject.getText().matches("[a-zA-Z\\s]*")){
					JOptionPane.showMessageDialog(null, "Only alphabetic letters allowed for subject !\nPlease Enter a Valid Subject");
					TF_subject.setText("");
				}
				else if(TF_title.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter the Title!");
				}
				else if(TF_author.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter the Author!");
				}
				else if(!TF_author.getText().matches("[a-zA-Z\\s]*")){
					JOptionPane.showMessageDialog(null, "Only alphabetic letters allowed for author !\nPlease Enter a Valid author");
					TF_author.setText("");
				}
				else if(TF_publisher.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter the Publisher!");
				}
				else if(!TF_publisher.getText().matches("[a-zA-Z\\s]*")){
					JOptionPane.showMessageDialog(null, "Only alphabetic letters allowed for publisher !\nPlease Enter a Valid publisher");
					TF_publisher.setText("");
				}
				else if(TF_edition.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter the Edition Number!");
				}
				else if(!TF_edition.getText().matches("[0-9]*") || (TF_edition.getText().length()>2)){
					JOptionPane.showMessageDialog(null, "Please Enter a Valid edition number!");
					TF_edition.setText("");
				}
				else if(filename.equals("")  || filename.equals(null)) {
					System.out.println("file"+filename);
					JOptionPane.showMessageDialog(null, "Please Select the Book Cover Photo!");
				}
				else if(filename.equals("exception")) {
					JOptionPane.showMessageDialog(null, "Please Select a Valid Book Cover Photo!");
				}
				else {
					if(exists==1) {
						coverPic=readFile("System.Environment.SpecialFolder.LocalApplicationData");
					}else if(exists==0) {
						coverPic=readFile(filename);
					}
					
					try {
						String availabilityStatus="AVAILABLE";
						String query="insert into BOOKS (LIBRARIAN_ID,ACCESSION_NUMBER,ISBN,CALL_NUMBER,SUBJECT,TITLE,AUTHOR,PUBLISHER,EDITION_NUMBER,AVAILABILITY,COVER_PHOTO) values (?,?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement pst=connection.prepareStatement(query);
						pst.setString(1,TF_lib_id.getText());
						pst.setString(2,TF_accessionNo.getText());
						pst.setString(3,TF_isbn.getText());
						pst.setString(4,TF_callNumber.getText());
						pst.setString(5,TF_subject.getText());
						pst.setString(6,TF_title.getText());
						pst.setString(7,TF_author.getText());
						pst.setString(8,TF_publisher.getText());
						pst.setString(9,TF_edition.getText());
						pst.setString(10,availabilityStatus);
						pst.setBytes(11,coverPic);
						
						
						pst.execute();
						
						JOptionPane.showMessageDialog(null, "Book Details Added Successfully");
						
						pst.close();
						addedBooks();
						clearTF();
						TF_accessionNo.setText(generateAccessionNo());
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "An error occurred. Please fill out all the Required fields!!!");
						clearTF();
						ex.printStackTrace();
					}
				}
			}
		});
		
		JButton btnBack = new JButton("BACK");
		btnBack.setBounds(243, 483, 195, 42);
		contentPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				BOOKSINFORMATION.main(new String[]{});
			}
		});
	}

	private void addedBooks() {
		try {
			String saveAddedBook = "insert into ADDED_DELETED_BOOKS (LIBRARIAN_ID,ACCESSION_NUMBER,DATE_TIME,BOOK_STATUS,LONG_DATE_TIME) values (?,?,?,?,?)";
			PreparedStatement pst=connection.prepareStatement(saveAddedBook);
			String librarianID=TF_lib_id.getText();
			String accessionNo=TF_accessionNo.getText();
			Calendar today=Calendar.getInstance();
			String Date=today.getTime().toString();
			String bookStatus="Added";
			Date longDate=today.getTime();
			long longDateTime=longDate.getTime();
			
			pst.setString(1,librarianID);
			pst.setString(2,accessionNo);
			pst.setString(3,Date);
			pst.setString(4,bookStatus);
			pst.setLong(5,longDateTime);
			
			pst.execute();
			
			//JOptionPane.showMessageDialog(null, "Added Book Logged Successfully");
			
			pst.close();
		}catch(Exception exAdd){
			exAdd.printStackTrace();
		}
	}
	private String generateAccessionNo() {
		String accessNo="";
		if(checkBookTableIfEmpty()==1) {
			String defaultAccessNo="10000001";
			accessNo=defaultAccessNo;
		}
		else if(checkBookTableIfEmpty()==0) {
			String lastAssignedAccessNo=getLastAssignedAccessNo();
			int newAccessNo=Integer.parseInt(lastAssignedAccessNo)+1;
			accessNo=String.valueOf(newAccessNo);
		}
		return accessNo;
	}
	private int checkBookTableIfEmpty() {
		int ifEmpty=-1;
		try {
			String queryIfEmpty="select * from BOOKS ";
			PreparedStatement pstIfEmpty=connection.prepareStatement(queryIfEmpty);
			ResultSet rsIfEmpty=pstIfEmpty.executeQuery();
			if(rsIfEmpty.next()) {
				ifEmpty=0;
			}
			else {
				ifEmpty=1;
			}
			pstIfEmpty.close();
			rsIfEmpty.close();
		}catch(Exception exIfEmpty) {
			exIfEmpty.printStackTrace();
		}
		return ifEmpty;
	}
	private String getLastAssignedAccessNo() {
		String lastAccessNo="";
		try {
			String getAccessNo="select * from BOOKS";
			PreparedStatement pstAccessNo=connection.prepareStatement(getAccessNo);
			ResultSet rsAccessNo=pstAccessNo.executeQuery();
			while(rsAccessNo.next()) {
				lastAccessNo=rsAccessNo.getString("ACCESSION_NUMBER");
			}
			pstAccessNo.close();
			rsAccessNo.close();
		}catch(Exception exLastAccessNo) {
			exLastAccessNo.printStackTrace();
		}
		return lastAccessNo;
	}
	 /**
     * Read the file and returns the byte array
     * @param file
     * @return the bytes of the file
     */
    private byte[] readFile(String file) {
        ByteArrayOutputStream bos = null;
        try {
            File f = new File(file);
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            bos = new ByteArrayOutputStream();
            for (int len; (len = fis.read(buffer)) != -1;) {
                bos.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e2) {
            System.err.println(e2.getMessage());
        }
        return bos != null ? bos.toByteArray() : null;
    }
    private void setFilePath(String file) {
    	this.filePath=file;
    }
    private String getFilePath() {
    	return filePath;
    }
    private String getCoverPhoto() {
    	final FileChooser fileChosen = new FileChooser();
        fileChosen.showFileDialogDemo();
        // Here it is always return null 
       	String fileName = fileChosen.getFilePath();
        filename=fileName;

        
        return filename;
    }

	private void clearTF() {
		TF_callNumber.setText("");
		TF_isbn.setText("");
		TF_subject.setText("");
		TF_title.setText("");
		TF_author.setText("");
		TF_publisher.setText("");
		TF_edition.setText("");
		filename="";
		exists=0;
		
		
		lblNewLabel.setIcon(new ImageIcon(ADDBOOKS.class.getResource("/images/istockphoto-900301626-612x612.jpg")));
	}
	private void initialize() {
		// TODO Auto-generated method stub
		
	}
}
