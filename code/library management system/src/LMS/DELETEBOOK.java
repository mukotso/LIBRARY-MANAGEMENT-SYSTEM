package LMS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
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

import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Color;

public class DELETEBOOK extends JFrame {

	private JPanel contentPane;
	private JTextField TF_accessionNo;
	private JButton btnDelete;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DELETEBOOK frame = new DELETEBOOK();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	Connection connection=null;
	private JTable table;

	/**
	 * Create the frame.
	 */
	public DELETEBOOK() {
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
		
		JLabel lblDeleteBook = new JLabel("DELETE BOOK");
		lblDeleteBook.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblDeleteBook.setBounds(320, 28, 161, 40);
		contentPane.add(lblDeleteBook);
		
		JLabel lblAccessionNo = new JLabel("ACCESSION NUMBER");
		lblAccessionNo.setBounds(29, 84, 124, 37);
		contentPane.add(lblAccessionNo);
		
		TF_accessionNo = new JTextField();
		TF_accessionNo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				refreshBookDetails();
				disableDelete();
				
			}
		});
		TF_accessionNo.setBounds(172, 84, 298, 40);
		contentPane.add(TF_accessionNo);
		TF_accessionNo.setColumns(10);
		
		btnDelete = new JButton("DELETE");
		btnDelete.setBounds(826, 491, 226, 42);
		contentPane.add(btnDelete);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(TF_accessionNo.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please Enter the Book's Accession Number!");
				}
				else if(!TF_accessionNo.getText().matches("[0-9]*") || (!(TF_accessionNo.getText().length()==8))){
					JOptionPane.showMessageDialog(null, "Please Enter a Valid Accession Number!");
					TF_accessionNo.setText("");
				}
				else if(!checkBooKStatus().equals("AVAILABLE")) {
					JOptionPane.showMessageDialog(null, "The Book isn't Available for Deleting!");
					TF_accessionNo.setText("");
					refreshBookDetails();
				}
				else {
					try {
						String copiesCheck="select * from BOOKS where ACCESSION_NUMBER = '"+TF_accessionNo.getText()+"' ";
						PreparedStatement pstCopies=connection.prepareStatement(copiesCheck);
						ResultSet rsCopies=pstCopies.executeQuery();
						
						if(rsCopies.next()) {
							int action=JOptionPane.showConfirmDialog(null, "Do You really Want To Delete The Book", "Delete", JOptionPane.YES_NO_OPTION);
							if(action==0) {
							try { 
								String availabilityStatus="DELETED";
								
								String queryBookDeleted="update BOOKS set AVAILABILITY = '"+availabilityStatus+"' where ACCESSION_NUMBER = '"+TF_accessionNo.getText()+"' ";
								PreparedStatement pstDeleteBook=connection.prepareStatement(queryBookDeleted);
								
								pstDeleteBook.execute();
								JOptionPane.showMessageDialog(null, "Book Deleted Successfully");
								disableDelete();
								deletedBooks();
								pstDeleteBook.close();
								TF_accessionNo.setText("");
							
							} catch (Exception ex) {
								JOptionPane.showMessageDialog(null, "An error occurred. Please Provide the Book's Accession Number!!!");
								ex.printStackTrace();
							}
							}
						}	
						else {
							
							JOptionPane.showMessageDialog(null, "Book Details Not Found!!!");
							clearTF();
						}
						pstCopies.close();
						rsCopies.close();
						refreshBookDetails();
						
						
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "An error occurred. Please fill out all the Required fields!!!");
						clearTF();
						ex.printStackTrace();
					}
				}
			}
		});
		
		JButton btnBack = new JButton("BACK");
		btnBack.setBounds(597, 491, 195, 42);
		contentPane.add(btnBack);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(172, 164, 965, 262);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setEnabled(false);
		
		scrollPane.setViewportView(table);
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				BOOKSINFORMATION.main(new String[]{});
			}
		});

	}

	public void refreshBookDetails(){
		try {
			String query="select ACCESSION_NUMBER,ISBN,SUBJECT,TITLE,AUTHOR,PUBLISHER,EDITION_NUMBER from BOOKS where ACCESSION_NUMBER=? ";
			PreparedStatement pst=connection.prepareStatement(query);
			pst.setString(1,TF_accessionNo.getText());
			ResultSet rs=pst.executeQuery();
			
			table.setModel(DbUtils.resultSetToTableModel(rs));
			table.getColumnModel().getColumn(0).setCellRenderer(new WordWrapCellRenderer());
			table.getColumnModel().getColumn(1).setCellRenderer(new WordWrapCellRenderer());
			table.getColumnModel().getColumn(2).setCellRenderer(new WordWrapCellRenderer());
			table.getColumnModel().getColumn(3).setCellRenderer(new WordWrapCellRenderer());
			table.getColumnModel().getColumn(4).setCellRenderer(new WordWrapCellRenderer());
			table.getColumnModel().getColumn(5).setCellRenderer(new WordWrapCellRenderer());
			
			pst.close();
			rs.close();
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	private void deletedBooks() {
		try {
			String saveDeletedBook = "insert into ADDED_DELETED_BOOKS (LIBRARIAN_ID,ACCESSION_NUMBER,DATE_TIME,BOOK_STATUS,LONG_DATE_TIME) values (?,?,?,?,?)";
			PreparedStatement pst=connection.prepareStatement(saveDeletedBook);
			LIBRARIANMAINMENU libid=new LIBRARIANMAINMENU();
			String librarianID=libid.getLibrarianId();
			String accessNo=TF_accessionNo.getText();
			Calendar today=Calendar.getInstance();
			String Date=today.getTime().toString();
			String bookStatus="Deleted";
			Date longDate=today.getTime();
			long longDateTime=longDate.getTime();
			
			pst.setString(1,librarianID);
			pst.setString(2,accessNo);
			pst.setString(3,Date);
			pst.setString(4,bookStatus);
			pst.setLong(5,longDateTime);
			
			pst.execute();
			
			//JOptionPane.showMessageDialog(null, "Deleted Book Logged Successfully");
			
			pst.close();
		}catch(Exception exDel){
			exDel.printStackTrace();
		}
	}
	

	private String checkBooKStatus() {
		String bookStatus="null";
		try {
			String queryStatus="select * from BOOKS where ACCESSION_NUMBER = '"+TF_accessionNo.getText()+"' ";
			PreparedStatement pstStatus=connection.prepareStatement(queryStatus);
			ResultSet rsStatus=pstStatus.executeQuery();
			
			if(rsStatus.next()) {
				bookStatus=rsStatus.getString("AVAILABILITY");
			}
		
			pstStatus.close();
			rsStatus.close();
		}catch(Exception exBookStatus) {
			exBookStatus.printStackTrace();
		}
		return bookStatus;
	}
	private void clearTF() {
		TF_accessionNo.setText("");
	}
	private void disableDelete() {
		if(checkBooKStatus().equals("DELETED")) {
			JOptionPane.showMessageDialog(null, "The Book Already Deleted!");
			btnDelete.setEnabled(false);
		}else if(!checkBooKStatus().equals("DELETED")) {
			btnDelete.setEnabled(true);
		}
		
	}
	
	private void initialize() {
		// TODO Auto-generated method stub
		
	}
}
