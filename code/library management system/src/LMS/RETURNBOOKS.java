package LMS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
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

import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;

public class RETURNBOOKS extends JFrame {

	private JPanel contentPane;
	private JTextField TF_registrationNumber;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RETURNBOOKS frame = new RETURNBOOKS();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	Connection connection=null;
	private JTable table;
	private String isbn="0";
	private String accessNo="0";

	/**
	 * Create the frame.
	 */
	public RETURNBOOKS() {
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
		
		JLabel lblReturnBooks = new JLabel("RETURN BOOKS");
		lblReturnBooks.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblReturnBooks.setBounds(147, 11, 159, 45);
		contentPane.add(lblReturnBooks);
		
		JLabel lblRegNo = new JLabel("STUDENT REG NO.");
		lblRegNo.setBounds(10, 67, 124, 35);
		contentPane.add(lblRegNo);
		
		TF_registrationNumber = new JTextField();
		TF_registrationNumber.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					refreshTable();
					
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		TF_registrationNumber.setBounds(147, 64, 350, 38);
		contentPane.add(TF_registrationNumber);
		TF_registrationNumber.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 130, 1142, 334);
		contentPane.add(scrollPane);
		
		table = new JTable(){
			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false;
			}
		};
		scrollPane.setViewportView(table);
		tableSelectionAccessNo();
		
		JButton btnReturn = new JButton("RETURN");
		btnReturn.setBounds(993, 479, 159, 64);
		contentPane.add(btnReturn);
		btnReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int TableFine=checkFine();
				if(TF_registrationNumber.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please Enter the Student's Registration Number!");
				}
				else if(tableSelectionAccessNo()=="0"){
					JOptionPane.showMessageDialog(null, "Please Select a Book from the Table!");
				}
				else if(TableFine==1){
					JOptionPane.showMessageDialog(null, "Please Update Return Date and Fine before Returning the Book!");
				}
				else if(TableFine==0){
					int action=JOptionPane.showConfirmDialog(null, "Please Confirm If You Want To Return The Book", "Return", JOptionPane.YES_NO_OPTION);
					if(action==0) {
					try {
						if(tableSelectionAccessNo()!="0") 
						{
						//update status
						String statusIssued="Issued";
						String statusReturned="Returned";
						String query = "update ISSUED_BOOKS set STATUS='"+statusReturned+"' where ACCESSION_NUMBER = '"+tableSelectionAccessNo()+"' and STATUS = '"+statusIssued+"' ";
						PreparedStatement pst=connection.prepareStatement(query);
						pst.execute();
						
						JOptionPane.showMessageDialog(null, "Book Returned Successfully");
						
						pst.close();
						updateBookAvailability();
						logReturnedBook();
						
						}else {
							JOptionPane.showMessageDialog(null, "Please Provide the Student's Registration Number and Select a Book From The Table To Return!!!");
						}
						
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "An error occurred. Please Provide the Student's Registration Number and Select The Book From The Table!!!");
						ex.printStackTrace();
						TF_registrationNumber.setText("");
					}
					
					}
					accessNo="0";
				}
				
				refreshTable();
			}

		});
		
		JButton btnUpdateFineIncurred = new JButton("UPDATE RETURN DATE AND FINE INCURRED");
		btnUpdateFineIncurred.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(TF_registrationNumber.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please Enter the Student's Registration Number!");
				}
				else if(tableSelectionAccessNo()=="0"){
					JOptionPane.showMessageDialog(null, "Please Select a Book from the Table!");
				}
				else {
					setReturnDate();
					updateFine();
					refreshTable();
					JOptionPane.showMessageDialog(null, "Book Return date and Fine Incurred updated successfully");
					accessNo="0";
				}
				
			}
		});
		btnUpdateFineIncurred.setBounds(586, 479, 318, 64);
		contentPane.add(btnUpdateFineIncurred);

		JButton btnBack = new JButton("BACK");
		btnBack.setBounds(358, 480, 154, 64);
		contentPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				STUDENTINFORMATION.main(new String[]{});
			}
		});
		refreshTable();
	}

	
	public void updateFine()
	{
		
		try 
		{

			String fine=calculateFine();
			String accessNumber=tableSelectionAccessNo();
			String statusIssued="Issued";
			
			String queryFine="update ISSUED_BOOKS set FINE_Ksh = '"+fine+"' where ACCESSION_NUMBER = '"+accessNumber+"' and STATUS = '"+statusIssued+"' ";
			PreparedStatement pstFine=connection.prepareStatement(queryFine);
			
			pstFine.execute();
			//JOptionPane.showMessageDialog(null, "Fine Updated Successfully");
			
			pstFine.close();
			refreshTable();
			
		} catch(Exception exc4) {
			exc4.printStackTrace();
			
		}
		refreshTable();
	}
	public void refreshTable() {
		try {
			String statusIssued="Issued";
			
			String query="select ISSUED_BOOKS.ACCESSION_NUMBER,BOOKS.ISBN,BOOKS.TITLE,ISSUED_BOOKS.ISSUE_DATE,ISSUED_BOOKS.DUE_DATE,ISSUED_BOOKS.RETURN_DATE,ISSUED_BOOKS.FINE_Ksh from ISSUED_BOOKS,BOOKS where ISSUED_BOOKS.ACCESSION_NUMBER = BOOKS.ACCESSION_NUMBER and ISSUED_BOOKS.REGISTRATION_NUMBER=? and ISSUED_BOOKS.STATUS=? ";
			PreparedStatement pst=connection.prepareStatement(query);
			pst.setString(1,TF_registrationNumber.getText());
			pst.setString(2,statusIssued);
			ResultSet rs=pst.executeQuery();
			
			table.setModel(DbUtils.resultSetToTableModel(rs));
			
			pst.close();
			rs.close();
			
		} catch(Exception ex5) {
			ex5.printStackTrace();
		}
	}

	public String calculateFine() {
		String fine="0";
		try {
			SimpleDateFormat sdf1=new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
			
			Date dueDate = sdf1.parse(getDueDate());
			SimpleDateFormat sdf2=new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
			Date returnDate = sdf2.parse(getReturnDate());
			long diff=returnDate.getTime()-dueDate.getTime();
			long daysPenaltied=(diff/1000/60/60/24);
			if(daysPenaltied<0) {
				fine="0";
			}else {
				long fineLong=0;
				if(checkLoanType()==0){
					fineLong=daysPenaltied*20;
				}
				else if(checkLoanType()==1){
					fineLong=daysPenaltied*10;
				}
				fine=Long.toString(fineLong);
			}
			
			//JOptionPane.showMessageDialog(null, "Fine Calculated Successfully");
		} catch(Exception ex6) {
			ex6.printStackTrace();
		}
		return fine;
	}

	private int checkLoanType() {
		int loanType=-1;
		try {
			String dBloantype="TYPE";
			String checkLoan="select * from ISSUED_BOOKS where ACCESSION_NUMBER = '"+tableSelectionAccessNo()+"' ";
			PreparedStatement pstLoan=connection.prepareStatement(checkLoan);
			ResultSet rsLoan=pstLoan.executeQuery();
			while(rsLoan.next()) {
				dBloantype=rsLoan.getString("LOAN_TYPE");
			}
			String shortloan="SHORT LOAN";
			String longloan="LONG LOAN";
			int dBloantype1=dBloantype.length();
			int shortloan1=shortloan.length();
			int longloan1=longloan.length();
			if(dBloantype1==shortloan1) {
				loanType=0;
			}else if(dBloantype1==longloan1){
				loanType=1;
			}
			pstLoan.close();
			rsLoan.close();
		}catch(Exception exLoan) {
			exLoan.printStackTrace();
		}
		return loanType;
	}
	public void setReturnDate() {
		try {
			Calendar today=Calendar.getInstance();
			Date returnDate=today.getTime();
			String returnDateString=returnDate.toString();
			String statusIssued="Issued";
			
			String queryReturnDate="update ISSUED_BOOKS set RETURN_DATE = '"+returnDateString+"' where ACCESSION_NUMBER = '"+tableSelectionAccessNo()+"' and STATUS = '"+statusIssued+"' ";
			PreparedStatement pstReturnDate=connection.prepareStatement(queryReturnDate);
			
			pstReturnDate.execute();
			//JOptionPane.showMessageDialog(null, "Return Date Updated Successfully");
			
			pstReturnDate.close();
			refreshTable();
		
	    } catch(Exception exc2) {
	    	exc2.printStackTrace();
		
	    }
		refreshTable();
		
	}
	
	public String getDueDate()
	{
		Calendar date=Calendar.getInstance();
		String dueDate=date.getTime().toString();
		try 
		{
			String statusIssued="Issued";
			
			String query="select * from ISSUED_BOOKS where REGISTRATION_NUMBER = '"+TF_registrationNumber.getText()+"' and ACCESSION_NUMBER = '"+tableSelectionAccessNo()+"' and STATUS = '"+statusIssued+"' ";
			PreparedStatement pst=connection.prepareStatement(query);
			ResultSet rs1=pst.executeQuery();
			
			while(rs1.next()) {
				dueDate=rs1.getString("DUE_DATE");
			}

			pst.close();
			rs1.close();
		} catch(Exception exc1) {
			exc1.printStackTrace();
		}
		return dueDate;
	}
	
	public String getReturnDate()
	{
		Calendar date=Calendar.getInstance();
		String returnDate=date.getTime().toString();
		try 
		{
			String statusIssued="Issued";
			
			String query="select * from ISSUED_BOOKS where REGISTRATION_NUMBER = '"+TF_registrationNumber.getText()+"' and ACCESSION_NUMBER = '"+tableSelectionAccessNo()+"' and STATUS = '"+statusIssued+"' ";
			PreparedStatement pstReturn=connection.prepareStatement(query);
			ResultSet rs2=pstReturn.executeQuery();
			
			while(rs2.next()) {
				returnDate=rs2.getString("RETURN_DATE");
			}

			pstReturn.close();
			rs2.close();
		} catch(Exception exc3) {
			exc3.printStackTrace();
		}
		return returnDate;
	}
	public String tableSelectionAccessNo (){
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					int row=table.getSelectedRow();
					accessNo=(table.getModel().getValueAt(row, 0)).toString();
				} catch(Exception exc) {
					accessNo="0";
					exc.printStackTrace();
				}
			}
		});
		return accessNo;
	}
	private int checkFine(){
		int fineSelected=0;
		try {
			String statusIssued="Issued";
			String finePending="Pending";
			String queryfine="select * from ISSUED_BOOKS where REGISTRATION_NUMBER = '"+TF_registrationNumber.getText()+"' and ACCESSION_NUMBER = '"+tableSelectionAccessNo()+"' and STATUS = '"+statusIssued+"' and FINE_Ksh = '"+finePending+"' ";
			PreparedStatement pstfine=connection.prepareStatement(queryfine);
			ResultSet rsfine=pstfine.executeQuery();
			
			if(rsfine.next()) {
				fineSelected=1;
			}
			else {
				fineSelected=0;
			}
			pstfine.close();
			rsfine.close();
			
		} catch(Exception exc) {
			exc.printStackTrace();
		}
		return fineSelected;
	}
	
	private void logReturnedBook() {
		try {
			String logReturnedBook ="insert into ISSUED_RETURNED_BOOKS (ACCESSION_NUMBER,STUDENT_REGISTRATION_NUMBER,LIBRARIAN_ID,DATE_TIME,FINE_INCURRED,STATUS,LONG_DATE_TIME) values (?,?,?,?,?,?,?)";
			PreparedStatement pst=connection.prepareStatement(logReturnedBook);
			
			LIBRARIANMAINMENU libid=new LIBRARIANMAINMENU();
			String librarianID=libid.getLibrarianId();
			
			String accessionNumber=tableSelectionAccessNo();
			Calendar today=Calendar.getInstance();
			String Date=today.getTime().toString();
			String fineincurred=getFineIncurred();
			String status="Returned";
			Date longDate=today.getTime();
			long longDateTime=longDate.getTime();
			
			pst.setString(1,accessionNumber);
			pst.setString(2,TF_registrationNumber.getText());
			pst.setString(3,librarianID);
			pst.setString(4,Date);
			pst.setString(5,fineincurred);
			pst.setString(6,status);
			pst.setLong(7,longDateTime);
			
			pst.execute();
			
			//JOptionPane.showMessageDialog(null, "Returned Book Logged Successfully");
			
			pst.close();
		}catch(Exception exSave) {
			exSave.printStackTrace();
		}
	}
	
	private String getFineIncurred() {
		String fineincurred="";
		try {
			String queryFineIncurred="select * from ISSUED_BOOKS where ACCESSION_NUMBER = '"+tableSelectionAccessNo()+"' ";
			PreparedStatement pstFineIncurred=connection.prepareStatement(queryFineIncurred);
			ResultSet rsFineIncurred=pstFineIncurred.executeQuery();
			while(rsFineIncurred.next()) {
				fineincurred=rsFineIncurred.getString("FINE_Ksh");
			}
			pstFineIncurred.close();
			rsFineIncurred.close();
		}catch(Exception exGetFineIncurred) {
			exGetFineIncurred.printStackTrace();
		}
		return fineincurred;
	}
	private void updateBookAvailability() {
		try {
			String availability="AVAILABLE";
			String queryUpdateAvailability="update BOOKS set AVAILABILITY = '"+availability+"' where ACCESSION_NUMBER = '"+tableSelectionAccessNo()+"' ";
			PreparedStatement pstUpdateAvailability=connection.prepareStatement(queryUpdateAvailability);
			
			pstUpdateAvailability.execute();
			
			pstUpdateAvailability.close();
			//JOptionPane.showMessageDialog(null, "Book's Availability Updated Successfully");
		}catch(Exception exUpdateAvailability) {
			exUpdateAvailability.printStackTrace();
		}
	}
	private void initialize() {
		// TODO Auto-generated method stub
		
	}
}
