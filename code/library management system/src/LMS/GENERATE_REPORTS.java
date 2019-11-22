package LMS;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

public class GENERATE_REPORTS extends JFrame {

	private JPanel contentPane;
	private JDateChooser periodFrom;
	private JDateChooser periodTo;
	private String reportHeader;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GENERATE_REPORTS frame = new GENERATE_REPORTS();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	Connection connection=null;
	private JTable table;
	private JTextField textField;
	/**
	 * Create the frame.
	 */
	public GENERATE_REPORTS() {
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
		
		JLabel lblNewLabel = new JLabel("GENERATE REPORTS");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(399, 11, 440, 35);
		contentPane.add(lblNewLabel);

		JLabel lblSelectAPeriod = new JLabel("PERIOD:");
		lblSelectAPeriod.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 14));
		lblSelectAPeriod.setBounds(41, 64, 109, 28);
		contentPane.add(lblSelectAPeriod);
		
		JLabel lblFrom = new JLabel("FROM:");
		lblFrom.setBounds(175, 48, 46, 14);
		contentPane.add(lblFrom);
		
		JLabel lblTo = new JLabel("TO:");
		lblTo.setBounds(389, 48, 46, 14);
		contentPane.add(lblTo);

		periodFrom = new JDateChooser();
		periodFrom.setBounds(175, 72, 162, 20);
		contentPane.add(periodFrom);
		
		periodTo = new JDateChooser();
		periodTo.setBounds(389, 72, 182, 20);
		contentPane.add(periodTo);

		JButton btnLoadAddedBooks = new JButton("LOAD ADDED BOOKS");
		btnLoadAddedBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(((JTextField)periodFrom.getDateEditor().getUiComponent()).getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Select the Start of the Period to Generate Reports!");
				}
				else if(((JTextField)periodTo.getDateEditor().getUiComponent()).getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Select the End of the Period to Generate Reports!");
				}
				else {
					try {
						String bookStatus="Added";
						String addedBooks="select  ADDED_DELETED_BOOKS.LIBRARIAN_ID,ADDED_DELETED_BOOKS.ACCESSION_NUMBER,BOOKS.ISBN,BOOKS.TITLE,ADDED_DELETED_BOOKS.DATE_TIME from BOOKS,ADDED_DELETED_BOOKS  where BOOKS.ACCESSION_NUMBER = ADDED_DELETED_BOOKS.ACCESSION_NUMBER and ADDED_DELETED_BOOKS.BOOK_STATUS = '"+bookStatus+"' and ADDED_DELETED_BOOKS.LONG_DATE_TIME >= '"+periodFromDate().getTime()+"' and ADDED_DELETED_BOOKS.LONG_DATE_TIME <= '"+periodToDate().getTime()+"'";
						
						PreparedStatement pst=connection.prepareStatement(addedBooks);
						
						ResultSet rs=pst.executeQuery();
						table.setModel(DbUtils.resultSetToTableModel(rs));						
						
						Date fromPeriod1=periodFromDate();
						Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("EAT"));
						cal.setTime(fromPeriod1);
						int day1=cal.get(Calendar.DAY_OF_MONTH);
						int month1=cal.get(Calendar.MONTH)+1;
						int year1=cal.get(Calendar.YEAR);
						String start=day1+"/"+month1+"/"+year1;
						
						Date toPeriod1=periodToDate();
						cal.setTime(toPeriod1);
						int day2=cal.get(Calendar.DAY_OF_MONTH);
						int month2=cal.get(Calendar.MONTH)+1;
						int year2=cal.get(Calendar.YEAR);
						String end=day2+"/"+month2+"/"+year2;
						
						reportHeader="ADDED BOOKS FROM "+start+" TO "+end+" ";
						pst.close();
						rs.close();
					}catch(Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "Please Select the Dates from the dropdown Calendar!");
						((JTextField)periodFrom.getDateEditor().getUiComponent()).setText("");
						((JTextField)periodTo.getDateEditor().getUiComponent()).setText("");
					}
				}
					
			}
		});
		btnLoadAddedBooks.setBounds(38, 103, 350, 35);
		contentPane.add(btnLoadAddedBooks);
		
		JButton btnLoadDeletedBooks = new JButton("LOAD DELETED BOOKS");
		btnLoadDeletedBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(((JTextField)periodFrom.getDateEditor().getUiComponent()).getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Select the Start of the Period to Generate Reports!");
				}
				else if(((JTextField)periodTo.getDateEditor().getUiComponent()).getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Select the End of the Period to Generate Reports!");
				}
				else {
					try {
						String bookStatus="Deleted";
						String deletedBooks="select ADDED_DELETED_BOOKS.LIBRARIAN_ID,ADDED_DELETED_BOOKS.ACCESSION_NUMBER,BOOKS.ISBN,BOOKS.TITLE,ADDED_DELETED_BOOKS.DATE_TIME from BOOKS,ADDED_DELETED_BOOKS  where BOOKS.ACCESSION_NUMBER = ADDED_DELETED_BOOKS.ACCESSION_NUMBER and ADDED_DELETED_BOOKS.BOOK_STATUS = '"+bookStatus+"' and ADDED_DELETED_BOOKS.LONG_DATE_TIME >= '"+periodFromDate().getTime()+"' and ADDED_DELETED_BOOKS.LONG_DATE_TIME <= '"+periodToDate().getTime()+"'";
						
						PreparedStatement pst=connection.prepareStatement(deletedBooks);
						
						ResultSet rs=pst.executeQuery();
						table.setModel(DbUtils.resultSetToTableModel(rs));
						
						Date fromPeriod1=periodFromDate();
						Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("EAT"));
						cal.setTime(fromPeriod1);
						int day1=cal.get(Calendar.DAY_OF_MONTH);
						int month1=cal.get(Calendar.MONTH)+1;
						int year1=cal.get(Calendar.YEAR);
						String start=day1+"/"+month1+"/"+year1;
						
						Date toPeriod1=periodToDate();
						cal.setTime(toPeriod1);
						int day2=cal.get(Calendar.DAY_OF_MONTH);
						int month2=cal.get(Calendar.MONTH)+1;
						int year2=cal.get(Calendar.YEAR);
						String end=day2+"/"+month2+"/"+year2;
						
						reportHeader="DELETED BOOKS FROM "+start+" TO "+end+" ";
						
						pst.close();
						rs.close();
					}catch(Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "Please Select the Dates from the dropdown Calendar!");
						((JTextField)periodFrom.getDateEditor().getUiComponent()).setText("");
						((JTextField)periodTo.getDateEditor().getUiComponent()).setText("");
					}
				}
					
			}
		});
		btnLoadDeletedBooks.setBounds(38, 161, 350, 35);
		contentPane.add(btnLoadDeletedBooks);

		JButton btnLoadIssuedBooks = new JButton("LOAD ISSUED BOOKS");
		btnLoadIssuedBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(((JTextField)periodFrom.getDateEditor().getUiComponent()).getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Select the Start of the Period to Generate Reports!");
				}
				else if(((JTextField)periodTo.getDateEditor().getUiComponent()).getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Select the End of the Period to Generate Reports!");
				}
				else {
					try {
						String statusIssued="Issued";
						String issuedBooks="select ISSUED_RETURNED_BOOKS.LIBRARIAN_ID,ISSUED_RETURNED_BOOKS.ACCESSION_NUMBER,BOOKS.ISBN,BOOKS.TITLE,ISSUED_BOOKS.REGISTRATION_NUMBER,ISSUED_RETURNED_BOOKS.DATE_TIME,ISSUED_BOOKS.LOAN_TYPE from BOOKS,ISSUED_BOOKS,ISSUED_RETURNED_BOOKS  where BOOKS.ACCESSION_NUMBER = ISSUED_BOOKS.ACCESSION_NUMBER and ISSUED_BOOKS.ACCESSION_NUMBER = ISSUED_RETURNED_BOOKS.ACCESSION_NUMBER and ISSUED_RETURNED_BOOKS.STATUS = '"+statusIssued+"' and ISSUED_RETURNED_BOOKS.LONG_DATE_TIME >= '"+periodFromDate().getTime()+"' and ISSUED_RETURNED_BOOKS.LONG_DATE_TIME <= '"+periodToDate().getTime()+"'";
						
						PreparedStatement pst=connection.prepareStatement(issuedBooks);
						
						ResultSet rs=pst.executeQuery();
						table.setModel(DbUtils.resultSetToTableModel(rs));

						table.getColumnModel().getColumn(0).setCellRenderer(new WordWrapCellRenderer());
						table.getColumnModel().getColumn(1).setCellRenderer(new WordWrapCellRenderer());
						table.getColumnModel().getColumn(2).setCellRenderer(new WordWrapCellRenderer());
						table.getColumnModel().getColumn(3).setCellRenderer(new WordWrapCellRenderer());
						table.getColumnModel().getColumn(4).setCellRenderer(new WordWrapCellRenderer());
						table.getColumnModel().getColumn(5).setCellRenderer(new WordWrapCellRenderer());
						table.getColumnModel().getColumn(6).setCellRenderer(new WordWrapCellRenderer());
						
						Date fromPeriod1=periodFromDate();
						Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("EAT"));
						cal.setTime(fromPeriod1);
						int day1=cal.get(Calendar.DAY_OF_MONTH);
						int month1=cal.get(Calendar.MONTH)+1;
						int year1=cal.get(Calendar.YEAR);
						String start=day1+"/"+month1+"/"+year1;
						
						Date toPeriod1=periodToDate();
						cal.setTime(toPeriod1);
						int day2=cal.get(Calendar.DAY_OF_MONTH);
						int month2=cal.get(Calendar.MONTH)+1;
						int year2=cal.get(Calendar.YEAR);
						String end=day2+"/"+month2+"/"+year2;
						
						reportHeader="ISSUED BOOKS FROM "+start+" TO "+end+" ";
						
						rs.close();
						pst.close();
					}catch(Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "Please Select the Dates from the dropdown Calendar!");
						((JTextField)periodFrom.getDateEditor().getUiComponent()).setText("");
						((JTextField)periodTo.getDateEditor().getUiComponent()).setText("");
					}
				}
				
			}
		});
		btnLoadIssuedBooks.setBounds(419, 103, 350, 35);
		contentPane.add(btnLoadIssuedBooks);
		
		JButton btnLoadReturnedBooks = new JButton("LOAD RETURNED BOOKS");
		btnLoadReturnedBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(((JTextField)periodFrom.getDateEditor().getUiComponent()).getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Select the Start of the Period to Generate Reports!");
				}
				else if(((JTextField)periodTo.getDateEditor().getUiComponent()).getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Select the End of the Period to Generate Reports!");
				}
				else {
					try {
						String statusReturned="Returned";
						String returnedBooks="select ISSUED_RETURNED_BOOKS.LIBRARIAN_ID,ISSUED_RETURNED_BOOKS.ACCESSION_NUMBER,BOOKS.ISBN,BOOKS.TITLE,ISSUED_BOOKS.REGISTRATION_NUMBER as REGISTRATION_NO,ISSUED_RETURNED_BOOKS.DATE_TIME,ISSUED_BOOKS.LOAN_TYPE,ISSUED_RETURNED_BOOKS.FINE_INCURRED from BOOKS,ISSUED_RETURNED_BOOKS,ISSUED_BOOKS  where BOOKS.ACCESSION_NUMBER = ISSUED_BOOKS.ACCESSION_NUMBER and ISSUED_BOOKS.ACCESSION_NUMBER = ISSUED_RETURNED_BOOKS.ACCESSION_NUMBER and ISSUED_RETURNED_BOOKS.STATUS = '"+statusReturned+"' and ISSUED_RETURNED_BOOKS.LONG_DATE_TIME >= '"+periodFromDate().getTime()+"' and ISSUED_RETURNED_BOOKS.LONG_DATE_TIME <= '"+periodToDate().getTime()+"'";
						
						PreparedStatement pst=connection.prepareStatement(returnedBooks);
						
						ResultSet rs=pst.executeQuery();
						table.setModel(DbUtils.resultSetToTableModel(rs));

						table.getColumnModel().getColumn(0).setCellRenderer(new WordWrapCellRenderer());
						table.getColumnModel().getColumn(1).setCellRenderer(new WordWrapCellRenderer());
						table.getColumnModel().getColumn(2).setCellRenderer(new WordWrapCellRenderer());
						table.getColumnModel().getColumn(3).setCellRenderer(new WordWrapCellRenderer());
						table.getColumnModel().getColumn(4).setCellRenderer(new WordWrapCellRenderer());
						table.getColumnModel().getColumn(5).setCellRenderer(new WordWrapCellRenderer());
						table.getColumnModel().getColumn(6).setCellRenderer(new WordWrapCellRenderer());
						table.getColumnModel().getColumn(7).setCellRenderer(new WordWrapCellRenderer());
						
						Date fromPeriod1=periodFromDate();
						Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("EAT"));
						cal.setTime(fromPeriod1);
						int day1=cal.get(Calendar.DAY_OF_MONTH);
						int month1=cal.get(Calendar.MONTH)+1;
						int year1=cal.get(Calendar.YEAR);
						String start=day1+"/"+month1+"/"+year1;
						
						Date toPeriod1=periodToDate();
						cal.setTime(toPeriod1);
						int day2=cal.get(Calendar.DAY_OF_MONTH);
						int month2=cal.get(Calendar.MONTH)+1;
						int year2=cal.get(Calendar.YEAR);
						String end=day2+"/"+month2+"/"+year2;
						
						reportHeader="RETURNED BOOKS FROM "+start+" TO "+end+" ";
						
						rs.close();
						pst.close();
					}catch(Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "Please Select the Dates from the dropdown Calendar!");
						((JTextField)periodFrom.getDateEditor().getUiComponent()).setText("");
						((JTextField)periodTo.getDateEditor().getUiComponent()).setText("");
					}
				}
				
			}
		});
		btnLoadReturnedBooks.setBounds(419, 161, 350, 35);
		contentPane.add(btnLoadReturnedBooks);
		
		JButton btnLoadFinePayments = new JButton("LOAD FINE PAYMENTS");
		btnLoadFinePayments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(((JTextField)periodFrom.getDateEditor().getUiComponent()).getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Select the Start of the Period to Generate Reports!");
				}
				else if(((JTextField)periodTo.getDateEditor().getUiComponent()).getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Select the End of the Period to Generate Reports!");
				}
				else {
					try {
						String finesPaid="select FINE_PAYMENT.LIBRARIAN_ID,FINE_PAYMENT.STUDENT_REGISTRATION_NUMBER,STUDENTS.NAME,FINE_PAYMENT.AMOUNT_Ksh,FINE_PAYMENT.DATE_TIME from FINE_PAYMENT,STUDENTS where FINE_PAYMENT.STUDENT_REGISTRATION_NUMBER = STUDENTS.REGISTRATION_NUMBER and FINE_PAYMENT.LONG_DATE_TIME >= '"+periodFromDate().getTime()+"' and FINE_PAYMENT.LONG_DATE_TIME <= '"+periodToDate().getTime()+"' ";
						
						PreparedStatement pst=connection.prepareStatement(finesPaid);
						
						ResultSet rs=pst.executeQuery();
						table.setModel(DbUtils.resultSetToTableModel(rs));						
						
						Date fromPeriod1=periodFromDate();
						Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("EAT"));
						cal.setTime(fromPeriod1);
						int day1=cal.get(Calendar.DAY_OF_MONTH);
						int month1=cal.get(Calendar.MONTH)+1;
						int year1=cal.get(Calendar.YEAR);
						String start=day1+"/"+month1+"/"+year1;
						
						Date toPeriod1=periodToDate();
						cal.setTime(toPeriod1);
						int day2=cal.get(Calendar.DAY_OF_MONTH);
						int month2=cal.get(Calendar.MONTH)+1;
						int year2=cal.get(Calendar.YEAR);
						String end=day2+"/"+month2+"/"+year2;
						
						reportHeader="FINES PAID FROM "+start+" TO "+end+" ";
						
						rs.close();
						pst.close();
					}catch(Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "Please Select the Dates from the dropdown Calendar!");
						((JTextField)periodFrom.getDateEditor().getUiComponent()).setText("");
						((JTextField)periodTo.getDateEditor().getUiComponent()).setText("");
					}
				}
				
			}
		});
		btnLoadFinePayments.setBounds(793, 103, 350, 35);
		contentPane.add(btnLoadFinePayments);
		
		JButton btnLoadClearedStudents = new JButton("LOAD CLEARED STUDENTS");
		btnLoadClearedStudents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(((JTextField)periodFrom.getDateEditor().getUiComponent()).getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Select the Start of the Period to Generate Reports!");
				}
				else if(((JTextField)periodTo.getDateEditor().getUiComponent()).getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Select the End of the Period to Generate Reports!");
				}
				else {
					try {
						String clearedStudents="select STUDENTS_CLEARED.LIBRARIAN_ID,STUDENTS_CLEARED.STUDENT_REGISTRATION_NUMBER,STUDENTS.NAME,STUDENTS_CLEARED.DATE_TIME from STUDENTS_CLEARED,STUDENTS where STUDENTS_CLEARED.STUDENT_REGISTRATION_NUMBER = STUDENTS.REGISTRATION_NUMBER and STUDENTS_CLEARED.LONG_DATE_TIME >= '"+periodFromDate().getTime()+"' and STUDENTS_CLEARED.LONG_DATE_TIME <= '"+periodToDate().getTime()+"' ";
						
						PreparedStatement pst=connection.prepareStatement(clearedStudents);
						
						ResultSet rs=pst.executeQuery();
						table.setModel(DbUtils.resultSetToTableModel(rs));
						
						Date fromPeriod1=periodFromDate();
						Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("EAT"));
						cal.setTime(fromPeriod1);
						int day1=cal.get(Calendar.DAY_OF_MONTH);
						int month1=cal.get(Calendar.MONTH)+1;
						int year1=cal.get(Calendar.YEAR);
						String start=day1+"/"+month1+"/"+year1;
						
						Date toPeriod1=periodToDate();
						cal.setTime(toPeriod1);
						int day2=cal.get(Calendar.DAY_OF_MONTH);
						int month2=cal.get(Calendar.MONTH)+1;
						int year2=cal.get(Calendar.YEAR);
						String end=day2+"/"+month2+"/"+year2;
						
						reportHeader="STUDENTS CLEARED FROM "+start+" TO "+end+" ";
						
						rs.close();
						pst.close();
					}catch(Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "Please Select the Dates from the dropdown Calendar!");
						((JTextField)periodFrom.getDateEditor().getUiComponent()).setText("");
						((JTextField)periodTo.getDateEditor().getUiComponent()).setText("");
					}
				}
				
			}
		});
		btnLoadClearedStudents.setBounds(793, 161, 350, 35);
		contentPane.add(btnLoadClearedStudents);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(38, 207, 1105, 290);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setEnabled(false);
		scrollPane.setViewportView(table);
		
		JButton btnPrint = new JButton("PRINT");
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(isTableEmpty(table)==true) {
					JOptionPane.showMessageDialog(null, "The Report is Empty!\nPlease Select a Valid Period and try Again...");
				}else {
					MessageFormat header=new MessageFormat(reportHeader);
					MessageFormat footer=new MessageFormat("Library Management System");
					try {
						table.print(JTable.PrintMode.FIT_WIDTH, header, footer);
						
					}catch(Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "The Report has not been Printed!\nPlease try Again...");
					}
				}
			}
		});
		btnPrint.setBounds(1000, 508, 143, 42);
		contentPane.add(btnPrint);
		
		JButton btnBack = new JButton("BACK");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ADMINMAINMENU.main(new String[]{});
			}
		});
		btnBack.setBounds(768, 508, 135, 42);
		contentPane.add(btnBack);
	
	}
	private Date periodFromDate() {
		Date periodFromDate=periodFrom.getDate();
		
		Date fromDate=periodFromDate;
		
		return fromDate;
	}
	@SuppressWarnings("deprecation")
	private Date periodToDate() {
		Date periodToDate=periodTo.getDate();
		
		periodToDate.setHours(23);
		periodToDate.setMinutes(59);
		periodToDate.setSeconds(59);
		
		Date toDate=periodToDate;
		
		return toDate;
	}
	private static boolean isTableEmpty(JTable jTable) {
		if(jTable != null && jTable.getModel() != null) {
			return jTable.getModel().getRowCount()<=0?true:false;
		}
		return false;
	}
	
	private void initialize() {
		// TODO Auto-generated method stub
		
	}
}
