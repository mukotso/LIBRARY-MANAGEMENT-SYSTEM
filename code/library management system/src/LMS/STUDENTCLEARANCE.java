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

import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.awt.Color;

public class STUDENTCLEARANCE extends JFrame {

	private JPanel contentPane;
	private JTextField TF_regNo;
	private JTable table;
	private JTextField TF_totalFine;
	private JButton btnPayFine;
	private JButton btnClearStudent;
	private int totalfineDiplayed;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					STUDENTCLEARANCE frame = new STUDENTCLEARANCE();
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
	public STUDENTCLEARANCE() {
		super("Library Management System");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				LIBRARIANMAINMENU logout = new LIBRARIANMAINMENU();
				logout.logLogoutTime();
			}
		});
		
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
		
		JLabel lblStudentClearance = new JLabel("STUDENT CLEARANCE");
		lblStudentClearance.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblStudentClearance.setBounds(176, 11, 238, 35);
		contentPane.add(lblStudentClearance);
		
		JLabel lblNewLabel = new JLabel("REGISTRATION NUMBER");
		lblNewLabel.setBounds(10, 59, 142, 33);
		contentPane.add(lblNewLabel);
		
		TF_regNo = new JTextField();
		TF_regNo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					refreshTableBorrowingHistory();
					refreshTotalFine();
					deactiveButtonPayFineAndClear();
					
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		TF_regNo.setBounds(180, 57, 293, 35);
		contentPane.add(TF_regNo);
		TF_regNo.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(176, 115, 964, 264);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setEnabled(false);
		scrollPane.setViewportView(table);
		
		JLabel lblTotalFine = new JLabel("TOTAL FINE(Ksh)");
		lblTotalFine.setBounds(176, 411, 110, 32);
		contentPane.add(lblTotalFine);
		
		TF_totalFine = new JTextField();
		TF_totalFine.setForeground(new Color(139, 0, 0));
		TF_totalFine.setToolTipText("");
		TF_totalFine.setEditable(false);
		TF_totalFine.setBounds(296, 410, 167, 35);
		contentPane.add(TF_totalFine);
		TF_totalFine.setColumns(10);
		
		btnPayFine = new JButton("PAY FINE");
		btnPayFine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(TF_regNo.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please Enter the Student's Registration Number!");
				}
				else {
					try{
						String fineCleared="0";
						String statusReturn="Returned";
						String queryCheckRecord="select * from ISSUED_BOOKS where REGISTRATION_NUMBER = '"+TF_regNo.getText()+"' and STATUS = '"+statusReturn+"' ";
						PreparedStatement pstCheckRecord=connection.prepareStatement(queryCheckRecord);
						ResultSet rsCheckRecord=pstCheckRecord.executeQuery();
						if(rsCheckRecord.next()) {
							int displayedFine=Integer.parseInt(TF_totalFine.getText());
							int checkFine=0;
							if(displayedFine==checkFine)
							{
								JOptionPane.showMessageDialog(null, "The Student has no Outstanding Fine!!!");
							}
							else {
								String queryPayFine="update ISSUED_BOOKS set FINE_Ksh = '"+fineCleared+"' where REGISTRATION_NUMBER = '"+TF_regNo.getText()+"' and STATUS = '"+statusReturn+"' ";
								PreparedStatement pstPayFine=connection.prepareStatement(queryPayFine);
								
								pstPayFine.execute();
								
								JOptionPane.showMessageDialog(null, "Total Fine Paid Successfully");
								logFinePaid();
								pstPayFine.close();
								refreshTableBorrowingHistory();
								refreshTotalFine();
								deactiveButtonPayFineAndClear();
							}
							
							
						}else {
							
							JOptionPane.showMessageDialog(null, "The Student has no Record of Returned Books\n Please provide a valid Registration Number!!!");
						}
						pstCheckRecord.close();
						rsCheckRecord.close();
					} catch(Exception excPay) {
						excPay.printStackTrace();
						
					}
				}	
			}
		});
		btnPayFine.setBounds(310, 469, 153, 44);
		contentPane.add(btnPayFine);
		
		btnClearStudent = new JButton("CLEAR STUDENT");
		btnClearStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(TF_regNo.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please Enter the Student's Registration Number!");
				}
				else {
					try{
						
						String queryCheckRecord="select * from ISSUED_BOOKS where REGISTRATION_NUMBER = '"+TF_regNo.getText()+"' ";
						PreparedStatement pstCheckRecord=connection.prepareStatement(queryCheckRecord);
						ResultSet rsCheckRecord=pstCheckRecord.executeQuery();
						if(rsCheckRecord.next()) {
							String statusIssued="Issued";
							
							String issueStatus="select * from ISSUED_BOOKS where REGISTRATION_NUMBER = '"+TF_regNo.getText()+"' and STATUS = '"+statusIssued+"' ";
							PreparedStatement pstStatus=connection.prepareStatement(issueStatus);
							ResultSet rsStatus=pstStatus.executeQuery();
							
							if(!rsStatus.next()) 
							{
								int displayedFine=Integer.parseInt(TF_totalFine.getText());
								int checkFine=0;
								if(displayedFine==checkFine) {
									clearStudentBooksIssuedHistory();
									refreshTableBorrowingHistory();
									refreshTotalFine();
									
								}else {
									JOptionPane.showMessageDialog(null, "The Student has to pay a fine of Ksh "+TF_totalFine.getText()+" before he/she can be Cleared!!!");
								}
								
							}else 
							{
								int action=JOptionPane.showConfirmDialog(null, "The Student has a Book that should be returned before he/she can be Cleared!!!\n Please Return The Book", "Return", JOptionPane.YES_NO_OPTION);
								if(action==0) {
									dispose();
									RETURNBOOKS.main(new String[]{});								
								}
								TF_regNo.setText("");
								
							}

							pstStatus.close();
							rsStatus.close();
							
							
						}else {
							JOptionPane.showMessageDialog(null, "The Student has no Record of Borrowed/Returned Books\n Please provide a valid Registration Number!!!");
							TF_regNo.setText("");
						}
						pstCheckRecord.close();
						rsCheckRecord.close();
						
					
					}
					catch(Exception Status) {
						JOptionPane.showMessageDialog(null, "An error occurred. Please fill out all the Required fields!!!");
						Status.printStackTrace();
						TF_regNo.setText("");
					}
				}
			}
		});
		btnClearStudent.setBounds(634, 469, 163, 44);
		contentPane.add(btnClearStudent);
		
		JButton btnBack = new JButton("BACK");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				STUDENTINFORMATION.main(new String[]{});
			}
		});
		btnBack.setBounds(987, 469, 153, 44);
		contentPane.add(btnBack);
	}
	private void initialize() {
		// TODO Auto-generated method stub
		
	}
	public void refreshTableBorrowingHistory() {
		try {
			String statusReturn="Returned";
			
			String query="select ISSUED_BOOKS.ACCESSION_NUMBER,BOOKS.ISBN,BOOKS.TITLE,ISSUED_BOOKS.ISSUE_DATE,ISSUED_BOOKS.DUE_DATE,ISSUED_BOOKS.RETURN_DATE,ISSUED_BOOKS.FINE_Ksh from ISSUED_BOOKS,BOOKS where ISSUED_BOOKS.ACCESSION_NUMBER = BOOKS.ACCESSION_NUMBER and REGISTRATION_NUMBER=? and STATUS=? ";
			PreparedStatement pst=connection.prepareStatement(query);
			pst.setString(1,TF_regNo.getText());
			pst.setString(2,statusReturn);
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
			
			
		} catch(Exception ex5) {
			ex5.printStackTrace();
		}
	}
	public void refreshTotalFine() {
		try {
			String statusReturn="Returned";
			String queryFine="select ISSUED_BOOKS.ACCESSION_NUMBER,BOOKS.ISBN,BOOKS.TITLE,ISSUED_BOOKS.ISSUE_DATE,ISSUED_BOOKS.DUE_DATE,ISSUED_BOOKS.RETURN_DATE,ISSUED_BOOKS.FINE_Ksh from ISSUED_BOOKS,BOOKS where ISSUED_BOOKS.ACCESSION_NUMBER = BOOKS.ACCESSION_NUMBER and REGISTRATION_NUMBER=? and STATUS=? ";
			PreparedStatement pstFineRefresh=connection.prepareStatement(queryFine);
			pstFineRefresh.setString(1,TF_regNo.getText());
			pstFineRefresh.setString(2,statusReturn);
			ResultSet rsFine=pstFineRefresh.executeQuery();
			
			int TotalFine=0;
			while(rsFine.next()) {
				String fineDB=rsFine.getString("FINE_Ksh");
				int fine=Integer.parseInt(fineDB);
				TotalFine+=fine;
				
			}
			
			String fineDisplay=String.valueOf(TotalFine);
			TF_totalFine.setText(fineDisplay);
			totalfineDiplayed=TotalFine;
			
			pstFineRefresh.close();
			rsFine.close();
			
			
		} catch(Exception ex5) {
			ex5.printStackTrace();
		}
	}
	public void clearStudentBooksIssuedHistory(){
		int action=JOptionPane.showConfirmDialog(null, "Confirm If You Want To Clear The Student", "Clear", JOptionPane.YES_NO_OPTION);
		if(action==0) {
		try {
			String statusCleared="Cleared";
			String queryClear = "update ISSUED_BOOKS set STATUS = '"+statusCleared+"' where REGISTRATION_NUMBER='"+TF_regNo.getText()+"' ";
			PreparedStatement pstClear=connection.prepareStatement(queryClear);
			pstClear.execute();
			
			JOptionPane.showMessageDialog(null, "Student Cleared Successfully");
			logClearedStudents();
			pstClear.close();
			TF_regNo.setText("");
			
		} catch (Exception exClear) {
			JOptionPane.showMessageDialog(null, "An error occurred. Please Provide the Student's Registration Number!!!");
			exClear.printStackTrace();
		}
		
		}
	}
	private void logFinePaid() {
		try {
			String logFinePaid = "insert into FINE_PAYMENT (LIBRARIAN_ID,STUDENT_REGISTRATION_NUMBER,AMOUNT_Ksh,DATE_TIME,LONG_DATE_TIME) values (?,?,?,?,?)";
			PreparedStatement pst=connection.prepareStatement(logFinePaid);
			LIBRARIANMAINMENU libid=new LIBRARIANMAINMENU();
			String librarianID=libid.getLibrarianId();
			String registrationNumber=TF_regNo.getText();
			String amount=TF_totalFine.getText();
			Calendar today=Calendar.getInstance();
			String Date=today.getTime().toString();
			Date longDate=today.getTime();
			long longDateTime=longDate.getTime();
			
			pst.setString(1,librarianID);
			pst.setString(2,registrationNumber);
			pst.setString(3,amount);
			pst.setString(4,Date);
			pst.setLong(5,longDateTime);
			
			pst.execute();
			
			//JOptionPane.showMessageDialog(null, "Fine Payment Logged Successfully");
			
			pst.close();
		}catch(Exception exFine){
			exFine.printStackTrace();
		}
	}
	private void logClearedStudents() {
		try {
			String logClearedStudent = "insert into STUDENTS_CLEARED (LIBRARIAN_ID,STUDENT_REGISTRATION_NUMBER,DATE_TIME,LONG_DATE_TIME) values (?,?,?,?)";
			PreparedStatement pst=connection.prepareStatement(logClearedStudent);
			LIBRARIANMAINMENU libid=new LIBRARIANMAINMENU();
			String librarianID=libid.getLibrarianId();
			String registrationNumber=TF_regNo.getText();
			Calendar today=Calendar.getInstance();
			String Date=today.getTime().toString();
			Date longDate=today.getTime();
			long longDateTime=longDate.getTime();
			
			pst.setString(1,librarianID);
			pst.setString(2,registrationNumber);
			pst.setString(3,Date);
			pst.setLong(4,longDateTime);
			
			pst.execute();
			
			//JOptionPane.showMessageDialog(null, "Cleared Student Logged Successfully");
			
			pst.close();
		}catch(Exception exClear){
			exClear.printStackTrace();
		}
	}
	private int checkIfCleared() {
		int ifCleared=-1;
		try {
			String queryIfCleared="select * from STUDENTS_CLEARED where STUDENT_REGISTRATION_NUMBER = '"+TF_regNo.getText()+"' ";
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
	private void deactiveButtonPayFineAndClear() {
		if(totalfineDiplayed==0) {
			btnPayFine.setEnabled(false);
		}
		if(checkIfCleared()==1) {
			btnPayFine.setEnabled(false);
			btnClearStudent.setEnabled(false);
			JOptionPane.showMessageDialog(null, "Student is Already Cleared!");
			TF_regNo.setText("");
			btnPayFine.setEnabled(true);
			btnClearStudent.setEnabled(true);
		}
		if(totalfineDiplayed!=0) {
			btnPayFine.setEnabled(true);
		}
		
	}
}
