package LMS;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import net.proteanit.sql.DbUtils;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class ISSUED_RETURNED_BOOKS extends JFrame {

	private JPanel contentPane;
	private JDateChooser periodFrom;
	private JDateChooser periodTo;
	private String reportHeader;
	private String comboSelection="";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ISSUED_RETURNED_BOOKS frame = new ISSUED_RETURNED_BOOKS();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	Connection connection=null;
	private JComboBox TF_lib_id;
	private JTable table;

	/**
	 * Create the frame.
	 */
	public ISSUED_RETURNED_BOOKS() {
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
		
		JLabel lblNewLabel = new JLabel("ISSUED AND RETURNED BOOKS");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(399, 11, 440, 51);
		contentPane.add(lblNewLabel);
		
		JLabel lblSelectAPeriod = new JLabel("PERIOD:");
		lblSelectAPeriod.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSelectAPeriod.setBounds(42, 64, 109, 28);
		contentPane.add(lblSelectAPeriod);
		
		JLabel lblFrom = new JLabel("FROM:");
		lblFrom.setBounds(195, 48, 46, 14);
		contentPane.add(lblFrom);
		
		JLabel lblTo = new JLabel("TO:");
		lblTo.setBounds(409, 48, 46, 14);
		contentPane.add(lblTo);

		periodFrom = new JDateChooser();
		periodFrom.setBounds(195, 72, 162, 20);
		contentPane.add(periodFrom);
		
		periodTo = new JDateChooser();
		periodTo.setBounds(409, 72, 182, 20);
		contentPane.add(periodTo);
		
		JLabel lblLibid = new JLabel("LIBRARIAN ID");
		lblLibid.setBounds(44, 104, 118, 32);
		contentPane.add(lblLibid);
		
		TF_lib_id = new JComboBox();
		TF_lib_id.setBounds(193, 104, 217, 32);
		TF_lib_id.setMaximumRowCount(5);
		TF_lib_id.setEditable(false);
		contentPane.add(TF_lib_id);
		try{
            Statement stmt = connection.createStatement();
            String query = "SELECT * FROM LIBRARIANS";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next())
            {
            	TF_lib_id.addItem(rs.getString("LIBRARIAN_ID")); 

            }	
            stmt.close();
            rs.close();
		}  
		catch(Exception ex)
		{	
    	ex.printStackTrace();
		}
		TF_lib_id.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboSelection=TF_lib_id.getSelectedItem().toString();
				System.out.println(comboSelection);
			}
		});

		JButton btnLoadIssuedBooks = new JButton("LOAD ISSUED BOOKS");
		btnLoadIssuedBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(((JTextField)periodFrom.getDateEditor().getUiComponent()).getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Select the Start of the Period to Generate Reports!");
				}
				else if(((JTextField)periodTo.getDateEditor().getUiComponent()).getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Select the End of the Period to Generate Reports!");
				}
				else if(comboSelection.equals("")) {
					JOptionPane.showMessageDialog(null, "Please Select the Librarian ID!");
				}
				else if(checkLibID()==0){
					JOptionPane.showMessageDialog(null, "The Librarian ID does not exist!\nPlease provide a valid Librarian ID");
				}
				else if(checkLibID()==1){
					try {
						String libid=comboSelection;
						String statusIssued="Issued";
						String issuedBooks="select ISSUED_RETURNED_BOOKS.ACCESSION_NUMBER,BOOKS.ISBN,BOOKS.TITLE,ISSUED_BOOKS.REGISTRATION_NUMBER,ISSUED_RETURNED_BOOKS.DATE_TIME,ISSUED_BOOKS.LOAN_TYPE from BOOKS,ISSUED_RETURNED_BOOKS,ISSUED_BOOKS  where BOOKS.ACCESSION_NUMBER = ISSUED_BOOKS.ACCESSION_NUMBER and ISSUED_BOOKS.ACCESSION_NUMBER = ISSUED_RETURNED_BOOKS.ACCESSION_NUMBER and ISSUED_RETURNED_BOOKS.STATUS = '"+statusIssued+"' and ISSUED_RETURNED_BOOKS.LIBRARIAN_ID = '"+libid+"' and ISSUED_RETURNED_BOOKS.LONG_DATE_TIME >= '"+periodFromDate().getTime()+"' and ISSUED_RETURNED_BOOKS.LONG_DATE_TIME <= '"+periodToDate().getTime()+"'";
						
						PreparedStatement pst=connection.prepareStatement(issuedBooks);
					
						ResultSet rs=pst.executeQuery();
						table.setModel(DbUtils.resultSetToTableModel(rs));
						table.getColumnModel().getColumn(0).setCellRenderer(new WordWrapCellRenderer());
						table.getColumnModel().getColumn(1).setCellRenderer(new WordWrapCellRenderer());
						table.getColumnModel().getColumn(2).setCellRenderer(new WordWrapCellRenderer());
						table.getColumnModel().getColumn(3).setCellRenderer(new WordWrapCellRenderer());
						table.getColumnModel().getColumn(4).setCellRenderer(new WordWrapCellRenderer());
						table.getColumnModel().getColumn(5).setCellRenderer(new WordWrapCellRenderer());
						
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
						
						reportHeader=libid+" ISSUED BOOKS FROM "+start+" TO "+end;
						
						pst.close();
						rs.close();
					}catch(Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "Please Select the Dates from the dropdown Calendar!");
						((JTextField)periodFrom.getDateEditor().getUiComponent()).setText("");
						((JTextField)periodTo.getDateEditor().getUiComponent()).setText("");
						clearLibId();
					}
				}	
			}
		});
		btnLoadIssuedBooks.setBounds(193, 147, 400, 35);
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
				else if(comboSelection.equals("")) {
					JOptionPane.showMessageDialog(null, "Please Select the Librarian ID!");
				}
				else if(checkLibID()==0){
					JOptionPane.showMessageDialog(null, "The Librarian ID does not exist!\nPlease provide a valid Librarian ID");
				}
				else if(checkLibID()==1){
					try {
						String libid=comboSelection;
						String statusReturned="Returned";
						String returnedBooks="select ISSUED_RETURNED_BOOKS.ACCESSION_NUMBER,BOOKS.ISBN,BOOKS.TITLE,ISSUED_BOOKS.REGISTRATION_NUMBER as REGISTRATION_NO,ISSUED_RETURNED_BOOKS.DATE_TIME,ISSUED_BOOKS.LOAN_TYPE,ISSUED_RETURNED_BOOKS.FINE_INCURRED from BOOKS,ISSUED_RETURNED_BOOKS,ISSUED_BOOKS  where BOOKS.ACCESSION_NUMBER = ISSUED_BOOKS.ACCESSION_NUMBER and ISSUED_BOOKS.ACCESSION_NUMBER = ISSUED_RETURNED_BOOKS.ACCESSION_NUMBER and ISSUED_RETURNED_BOOKS.STATUS = '"+statusReturned+"' and ISSUED_RETURNED_BOOKS.LIBRARIAN_ID = '"+libid+"' and ISSUED_RETURNED_BOOKS.LONG_DATE_TIME >= '"+periodFromDate().getTime()+"' and ISSUED_RETURNED_BOOKS.LONG_DATE_TIME <= '"+periodToDate().getTime()+"'";
						
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
						
						reportHeader=libid+" RETURNED BOOKS FROM "+start+" TO "+end;
						
						pst.close();
						rs.close();
					}catch(Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "Please Select the Dates from the dropdown Calendar!");
						((JTextField)periodFrom.getDateEditor().getUiComponent()).setText("");
						((JTextField)periodTo.getDateEditor().getUiComponent()).setText("");
						clearLibId();
					}
				}
			}
		});
		btnLoadReturnedBooks.setBounds(718, 147, 400, 35);
		contentPane.add(btnLoadReturnedBooks);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(193, 197, 923, 270);
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
		btnPrint.setBounds(975, 491, 143, 42);
		contentPane.add(btnPrint);
		
		JButton btnBack = new JButton("BACK");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				MONITORLIBRARIANACTIVITIES.main(new String[]{});
			}
		});
		btnBack.setBounds(743, 491, 135, 42);
		contentPane.add(btnBack);
		
	}

	private int checkLibID() {
		int libCheck=-1;
		try {
			String libid=comboSelection;
			String queryLibrarian="select * from LIBRARIANS where LIBRARIAN_ID = '"+libid+"' ";
			PreparedStatement pstLib=connection.prepareStatement(queryLibrarian);
			ResultSet rsLib=pstLib.executeQuery();
			if(rsLib.next())
			{
				libCheck=1;
			}else {
				libCheck=0;
			}
			pstLib.close();
			rsLib.close();
		}catch(Exception ex1) {
			ex1.printStackTrace();
		}
		return libCheck;
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
	private void clearLibId() {
		comboSelection="";
	}
	private void initialize() {
		// TODO Auto-generated method stub
		
	}

}
