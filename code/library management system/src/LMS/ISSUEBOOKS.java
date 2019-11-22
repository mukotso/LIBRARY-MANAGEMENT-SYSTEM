package LMS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;

public class ISSUEBOOKS extends JFrame {

	private JPanel contentPane;
	//private JTextField TF_issueNo;
	private JTextField TF_studentRegNo;
	private JTextField TF_lib_id;
	private JTextField TF_accessionNo;
	private JComboBox TF_loanType;
	private String comboSelection="";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ISSUEBOOKS frame = new ISSUEBOOKS();
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
	public ISSUEBOOKS() {
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
		
		JLabel lblIssueBooks = new JLabel("ISSUE BOOKS");
		lblIssueBooks.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblIssueBooks.setBounds(298, 32, 158, 44);
		contentPane.add(lblIssueBooks);
		
		JLabel lblRegNo = new JLabel("STUDENT REG NO.");
		lblRegNo.setBounds(56, 107, 194, 41);
		contentPane.add(lblRegNo);
		
		JLabel lblIsbn = new JLabel("LIBRARIAN ID");
		lblIsbn.setBounds(56, 170, 194, 44);
		contentPane.add(lblIsbn);
		
		JLabel lblAccessionNumber = new JLabel("ACCESSION NUMBER");
		lblAccessionNumber.setBounds(56, 229, 194, 37);
		contentPane.add(lblAccessionNumber);
		
		JLabel lblTypeOfLoan = new JLabel("TYPE OF LOAN");
		lblTypeOfLoan.setBounds(56, 287, 194, 37);
		contentPane.add(lblTypeOfLoan);
		
		TF_studentRegNo = new JTextField();
		TF_studentRegNo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				studentIsCleared();
			}
		});
		TF_studentRegNo.setBounds(279, 107, 347, 41);
		contentPane.add(TF_studentRegNo);
		TF_studentRegNo.setColumns(10);
		
		TF_lib_id = new JTextField();
		TF_lib_id.setEditable(false);
		TF_lib_id.setBounds(279, 170, 347, 44);
		contentPane.add(TF_lib_id);
		TF_lib_id.setColumns(10);
		LIBRARIANMAINMENU libid=new LIBRARIANMAINMENU();
		TF_lib_id.setText(libid.getLibrarianId());
		
		TF_accessionNo = new JTextField();
		TF_accessionNo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
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
					table.getColumnModel().getColumn(6).setCellRenderer(new WordWrapCellRenderer());
					
					pst.close();
					rs.close();
					
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		TF_accessionNo.setColumns(10);
		TF_accessionNo.setBounds(279, 225, 347, 41);
		contentPane.add(TF_accessionNo);
		
		TF_loanType = new JComboBox();
		TF_loanType.setBounds(279, 287, 347, 37);
		TF_loanType.addItem("SHORT LOAN");
		TF_loanType.addItem("LONG LOAN");
		contentPane.add(TF_loanType);
		TF_loanType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboSelection=TF_loanType.getSelectedItem().toString();
			}
		});
		
		JButton btnIssue = new JButton("ISSUE");
		btnIssue.setBounds(993, 335, 155, 61);
		contentPane.add(btnIssue);
		btnIssue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(TF_studentRegNo.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter the Student Registration Number!");
				}
				else if(TF_lib_id.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please Enter Your Librarian ID!");
				}
				else if(TF_accessionNo.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Enter the Book's Accession Number!");
				}
				else if(checkDeletedBooks()==1 || checkIssuedBooks()==1) {
					JOptionPane.showMessageDialog(null, "The Book is Unavailabe!");
					TF_accessionNo.setText("");
				}
				else if(!TF_accessionNo.getText().matches("[0-9]*") || (!(TF_accessionNo.getText().length()==8))){
					JOptionPane.showMessageDialog(null, "Please Enter a Valid Accession Number!");
					TF_accessionNo.setText("");
				}
				else if(comboSelection.equals("")) {
					JOptionPane.showMessageDialog(null, "Please Select the Loan Type!");
				}
				else {
					try{
						int record=checkStudentDetails();
						if(record==1)
						{
							String copiesCheck="select * from BOOKS where ACCESSION_NUMBER = '"+TF_accessionNo.getText()+"' ";
							PreparedStatement pstCopies=connection.prepareStatement(copiesCheck);
							ResultSet rsCopies=pstCopies.executeQuery();
							
							if(rsCopies.next()) {
								int quantity=getBookIssued();
								if(quantity==1) 
								{
									JOptionPane.showMessageDialog(null, "Student has been Issued with this Book!!!\n One Book can't be issued twice to the same student");
									TF_accessionNo.setText("");
								}
								
								else
								{
									
									int count=0;
									int size=getRows();
									
									if(size>0) {
										String statusIssued="Issued";
										
										String issueStatus="select * from ISSUED_BOOKS where REGISTRATION_NUMBER = '"+TF_studentRegNo.getText()+"' and STATUS = '"+statusIssued+"' ";
										PreparedStatement pstStatus=connection.prepareStatement(issueStatus);
										ResultSet rsStatus=pstStatus.executeQuery();
										
										while(rsStatus.next()) {
										count++;
									
										String DueDateDB=rsStatus.getString("DUE_DATE");
										SimpleDateFormat sdf2=new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
										Date DueDate = sdf2.parse(DueDateDB);
										
										Calendar today=Calendar.getInstance();
										Date CurrentDate=today.getTime();
										
										long diff=CurrentDate.getTime()-DueDate.getTime();
										long daysPenaltied=(diff/1000/60/60/24);

											if(daysPenaltied>0) {
												int action=JOptionPane.showConfirmDialog(null, "The Student has a Book that should be returned before borrowing another!!!\n Please Return the Book", "Return", JOptionPane.YES_NO_OPTION);
												if(action==0) {
													dispose();
													RETURNBOOKS.main(new String[]{});
													
												}
												
												
												count=size;
											}
											else if(count==size)  {
												issueBook();
											}
											else {
												//JOptionPane.showMessageDialog(null, "Checking next record!!!");
											}
										}
										pstStatus.close();
										rsStatus.close();
									}else {
										issueBook();
									
										
									}
									
									clearTF();
									}
							}
							else {
								
								JOptionPane.showMessageDialog(null, "Book Accession Number Not Found!!!\nPlease Enter a valid Accession Number");
								clearTF();
							}
							pstCopies.close();
							rsCopies.close();
							
						}
						else
						{
							int action=JOptionPane.showConfirmDialog(null, "Student is not registered!!!\n Please Register the student before issuing a book", "Register", JOptionPane.YES_NO_OPTION);
							if(action==0) {
								clearTF();
								dispose();
								REGISTERSTUDENT.main(new String[]{});
							}
							
						}
						clearTF();
					}
					catch(Exception Status) {
						JOptionPane.showMessageDialog(null, "An error occurred. Please fill out all the Required fields!!!");
						Status.printStackTrace();
						clearTF();
					}
				}
			}
		});
		
		JButton btnBack = new JButton("BACK");
		btnBack.setBounds(993, 465, 155, 55);
		contentPane.add(btnBack);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(55, 335, 922, 185);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setEnabled(false);
		scrollPane.setViewportView(table);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(ISSUEBOOKS.class.getResource("/images/Bodmin-library-008.jpg")));
		lblNewLabel.setBounds(684, 10, 459, 313);
		contentPane.add(lblNewLabel);
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				STUDENTINFORMATION.main(new String[]{});
			}
		});
	}

	public void issueBook() {
		try {
			String copiesCheck="select * from BOOKS where ACCESSION_NUMBER = '"+TF_accessionNo.getText()+"' ";
			PreparedStatement pstCopies=connection.prepareStatement(copiesCheck);
			ResultSet rsCopies=pstCopies.executeQuery();
			
			
			if(rsCopies.next()) {
				String loantype=comboSelection;
				
				String query="insert into ISSUED_BOOKS (REGISTRATION_NUMBER,ACCESSION_NUMBER,LIBRARIAN_ID,ISSUE_DATE,DUE_DATE,RETURN_DATE,STATUS,FINE_Ksh,LOAN_TYPE) values (?,?,?,?,?,?,?,?,?)";
				PreparedStatement pst=connection.prepareStatement(query);
				Calendar today=Calendar.getInstance();
				String issueDate=today.getTime().toString();
				String shortloan="SHORT LOAN";
				String longloan="LONG LOAN";
				if(loantype.equals(shortloan)) {
					today.add(Calendar.WEEK_OF_YEAR,1);
				}else if(loantype.equals(longloan)){
					today.add(Calendar.WEEK_OF_YEAR,2);
				}
				String dueDate=today.getTime().toString();
				String returnDate="Pending";
				String status="Issued";
				String fine="Pending";
				
				pst.setString(1,TF_studentRegNo.getText());
				pst.setString(2,TF_accessionNo.getText());
				pst.setString(3,TF_lib_id.getText());
				pst.setString(4,issueDate);
				pst.setString(5,dueDate);
				pst.setString(6,returnDate);
				pst.setString(7,status);
				pst.setString(8,fine);
				pst.setString(9,loantype);
				
				
				
				pst.execute();
				
				JOptionPane.showMessageDialog(null, "Book issued Successfully");
				
				pst.close();
				updateBookAvailability();
				logIssuedBook();
			}
			else {
				JOptionPane.showMessageDialog(null, "Book Not Available!!!\n Please provide a valid ACCESSION NUMBER");
			}
			pstCopies.close();
			rsCopies.close();
			
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "An error occurred. Please fill out all the Required fields!!!");
			ex.printStackTrace();
		}
	}
	private void logIssuedBook() {
		try {
			String queryLogIssuedBook="insert into ISSUED_RETURNED_BOOKS (ACCESSION_NUMBER,STUDENT_REGISTRATION_NUMBER,LIBRARIAN_ID,DATE_TIME,FINE_INCURRED,STATUS,LONG_DATE_TIME) values (?,?,?,?,?,?,?)";
			PreparedStatement pst=connection.prepareStatement(queryLogIssuedBook);
			Calendar today=Calendar.getInstance();
			String Date=today.getTime().toString();
			String fineincurred="Pending";
			String status="Issued";
			Date longDate=today.getTime();
			long longDateTime=longDate.getTime();
			
			
			pst.setString(1,TF_accessionNo.getText());
			pst.setString(2,TF_studentRegNo.getText());
			pst.setString(3,TF_lib_id.getText());
			pst.setString(4,Date);
			pst.setString(5,fineincurred);
			pst.setString(6,status);
			pst.setLong(7,longDateTime);
			
			pst.execute();
			
			//JOptionPane.showMessageDialog(null, "Issued Book Logged Successfully");
			
			pst.close();
		}catch(Exception exLogBook) {
			exLogBook.printStackTrace();
		}
	}
	private int getRows() {
		int row=0;
		try {
			String statusIssued="Issued";
			String queryCountRow="select * from ISSUED_BOOKS  where REGISTRATION_NUMBER = '"+TF_studentRegNo.getText()+"' and STATUS = '"+statusIssued+"' ";
			PreparedStatement pstRow=connection.prepareStatement(queryCountRow);
			ResultSet rsRow=pstRow.executeQuery();
			
			while(rsRow.next()) {
				row++;
			}
			
			pstRow.close();
			rsRow.close();
		}catch(Exception exRow) {
			JOptionPane.showMessageDialog(null, "An error occurred.No rows found!!!");
			exRow.printStackTrace();
		}
		
		return row;
	}
	private int getBookIssued() {
		int quantity=0;
		try {
			String statusIssued="Issued";
			String queryCountBook="select * from ISSUED_BOOKS  where REGISTRATION_NUMBER = '"+TF_studentRegNo.getText()+"' and STATUS = '"+statusIssued+"' and ACCESSION_NUMBER = (select ACCESSION_NUMBER from BOOKS where ISBN = '"+getIsbn()+"'); ";
			PreparedStatement pstCount=connection.prepareStatement(queryCountBook);
			ResultSet rsCount=pstCount.executeQuery();
			
			if(rsCount.next()) {
				quantity=1;
			}else {
				quantity=0;
			}
			pstCount.close();
			rsCount.close();
		}catch(Exception exCount) {
			JOptionPane.showMessageDialog(null, "An error occurred.No issued Books found!!!");
			exCount.printStackTrace();
		}
		
		return quantity;
	}
	private String getIsbn() {
		String isbn="";
		try {
			String queryIsbn="select * from BOOKS where ACCESSION_NUMBER = '"+TF_accessionNo.getText()+"' ";
			PreparedStatement pstIsbn=connection.prepareStatement(queryIsbn);
			ResultSet rsIsbn=pstIsbn.executeQuery();
			while(rsIsbn.next()) {
				isbn=rsIsbn.getString("ISBN");
			}
			pstIsbn.close();
			rsIsbn.close();
		}catch(Exception exGetIsbn) {
			exGetIsbn.printStackTrace();
		}
		return isbn;
	}
	
	private int checkStudentDetails() {
		int record=-1;
		try {
			String queryStudent="select * from STUDENTS  where REGISTRATION_NUMBER = '"+TF_studentRegNo.getText()+"'  ";
			PreparedStatement pstStudent=connection.prepareStatement(queryStudent);
			ResultSet rsStudent=pstStudent.executeQuery();
			
			if(rsStudent.next()) {
				record=1;
			}else {
				record=0;
			}
			pstStudent.close();
			rsStudent.close();
		}catch(Exception exStu) {
			JOptionPane.showMessageDialog(null, "An error occurred.No Student Details found!!!");
			exStu.printStackTrace();
		}
		
		return record;
	}
	private int checkDeletedBooks() {
		int deletedBooks=-1;
		try {
			String statusDeleted="DELETED";
			String queryDelBooks="select * from BOOKS where ACCESSION_NUMBER = '"+TF_accessionNo.getText()+"' and AVAILABILITY = '"+statusDeleted+"' ";
			PreparedStatement pstDelBooks=connection.prepareStatement(queryDelBooks);
			ResultSet rsDelBooks=pstDelBooks.executeQuery();
			if(rsDelBooks.next()) {
				deletedBooks=1;
			}
			else {
				deletedBooks=0;
			}
			pstDelBooks.close();
			rsDelBooks.close();
		}catch(Exception exDelBooks) {
			exDelBooks.printStackTrace();
		}
		return deletedBooks;
	}
	private int checkIssuedBooks() {
		int issuedbooks=-1;
		try {
			String statusIssued="ISSUED";
			String queryIssuedBooks="select * from BOOKS where ACCESSION_NUMBER = '"+TF_accessionNo.getText()+"' and AVAILABILITY = '"+statusIssued+"' ";
			PreparedStatement pstIssuedBooks=connection.prepareStatement(queryIssuedBooks);
			ResultSet rsIssuedBooks=pstIssuedBooks.executeQuery();
			if(rsIssuedBooks.next()) {
				issuedbooks=1;
			}
			else {
				issuedbooks=0;
			}
			pstIssuedBooks.close();
			rsIssuedBooks.close();
		}catch(Exception exIssuedBooks) {
			exIssuedBooks.printStackTrace();
		}
		return issuedbooks;
	}
	private void updateBookAvailability() {
		try {
			String availability="ISSUED";
			String queryUpdateAvailability="update BOOKS set AVAILABILITY = '"+availability+"' where ACCESSION_NUMBER = '"+TF_accessionNo.getText()+"' ";
			PreparedStatement pstUpdateAvailability=connection.prepareStatement(queryUpdateAvailability);
			
			pstUpdateAvailability.execute();
			
			pstUpdateAvailability.close();
			//JOptionPane.showMessageDialog(null, "Book's Availability Updated Successfully");
		}catch(Exception exUpdateAvailability) {
			exUpdateAvailability.printStackTrace();
		}
	}
	private int clearedStudents() {
		int ifCleared=-1;
		try {
			String queryIfCleared="select * from STUDENTS_CLEARED where STUDENT_REGISTRATION_NUMBER = '"+TF_studentRegNo.getText()+"' ";
			PreparedStatement pstIfCleared=connection.prepareStatement(queryIfCleared);
			ResultSet rsIfCleared=pstIfCleared.executeQuery();
			if(rsIfCleared.next()) {
				ifCleared=1;
			}else {
				ifCleared=0;
			}
			pstIfCleared.close();
			rsIfCleared.close();
		}catch(Exception exIfCleared) {
			exIfCleared.printStackTrace();
		}
		return ifCleared;
	}
	private void studentIsCleared() {
		if(clearedStudents()==1) {
			JOptionPane.showMessageDialog(null, "Student is Cleared and therefore can't be Issued a Book!");
			TF_studentRegNo.setText("");
		}
		
	}
	private void clearTF() {
		TF_studentRegNo.setText("");
		TF_accessionNo.setText("");
		TF_loanType.setSelectedIndex(0);
		comboSelection="";
		
	}
	
	private void initialize() {
		// TODO Auto-generated method stub
		
	}
}
