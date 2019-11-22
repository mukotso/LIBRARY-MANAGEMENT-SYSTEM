package LMS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import net.proteanit.sql.DbUtils;

import javax.swing.JButton;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;

public class LIBRARIANLOGINHISTORY extends JFrame {

	private JPanel contentPane;
	private JTable table;
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
					LIBRARIANLOGINHISTORY frame = new LIBRARIANLOGINHISTORY();
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
	public LIBRARIANLOGINHISTORY() {
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
		
		JLabel lblLibrariansLoginHistory = new JLabel("LIBRARIANS LOGIN HISTORY");
		lblLibrariansLoginHistory.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblLibrariansLoginHistory.setBounds(438, 11, 367, 26);
		contentPane.add(lblLibrariansLoginHistory);
		
		JLabel lblSelectAPeriod = new JLabel("PERIOD:");
		lblSelectAPeriod.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 14));
		lblSelectAPeriod.setBounds(91, 64, 109, 28);
		contentPane.add(lblSelectAPeriod);
		
		JLabel lblFrom = new JLabel("FROM:");
		lblFrom.setBounds(225, 48, 46, 14);
		contentPane.add(lblFrom);
		
		JLabel lblTo = new JLabel("TO:");
		lblTo.setBounds(439, 48, 46, 14);
		contentPane.add(lblTo);

		periodFrom = new JDateChooser();
		periodFrom.setBounds(225, 72, 162, 20);
		contentPane.add(periodFrom);
		
		periodTo = new JDateChooser();
		periodTo.setBounds(439, 72, 182, 20);
		contentPane.add(periodTo);
		
		JButton btnLoadLoginHistory = new JButton("LOAD SUCCESSFULL ATTEMPTS");
		btnLoadLoginHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(((JTextField)periodFrom.getDateEditor().getUiComponent()).getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Select the Start of the Period to View Logs From!");
				}
				else if(((JTextField)periodTo.getDateEditor().getUiComponent()).getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Select the End of the Period to View Logs!");
				}
				else {
					try {
						String query = "select LIBRARIAN_ID,LOGIN_DATE_TIME,LOGOUT_DATE_TIME from LIBRARIAN_LOGIN_HISTORY where LONG_DATE_TIME >= '"+periodFromDate().getTime()+"' and LONG_DATE_TIME <= '"+periodToDate().getTime()+"'";
						PreparedStatement pst=connection.prepareStatement(query);
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
						
						reportHeader="SUCCESSFUL LOGINS FROM "+start+" TO "+end+" ";
						
						pst.close();
	                    rs.close();
					} catch(Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "Please Select the Dates from the dropdown Calendar!");
						((JTextField)periodFrom.getDateEditor().getUiComponent()).setText("");
						((JTextField)periodTo.getDateEditor().getUiComponent()).setText("");
					}
				}
			}
		});
		btnLoadLoginHistory.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnLoadLoginHistory.setBounds(92, 100, 389, 36);
		contentPane.add(btnLoadLoginHistory);
		
		JButton btnLoadfFailedLoginAttempts = new JButton("LOAD FAILED  ATTEMPTS");
		btnLoadfFailedLoginAttempts.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnLoadfFailedLoginAttempts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(((JTextField)periodFrom.getDateEditor().getUiComponent()).getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Select the Start of the Period to View Logs From!");
				}
				else if(((JTextField)periodTo.getDateEditor().getUiComponent()).getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please Select the End of the Period to View Logs!");
				}
				else {
					try {
						String query = "select LIBRARIAN_ID,USERNAME,DATE_TIME from FAILED_LIBRARIAN_LOGIN_ATTEMPTS where LONG_DATE_TIME >= '"+periodFromDate().getTime()+"' and LONG_DATE_TIME <= '"+periodToDate().getTime()+"' ";
						PreparedStatement pst=connection.prepareStatement(query);
						ResultSet rs=pst.executeQuery();
						table.setModel(DbUtils.resultSetToTableModel(rs));
						table.getColumnModel().getColumn(0).setCellRenderer(new WordWrapCellRenderer());
						table.getColumnModel().getColumn(1).setCellRenderer(new WordWrapCellRenderer());
						table.getColumnModel().getColumn(2).setCellRenderer(new WordWrapCellRenderer());
						
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
						
						reportHeader="FAILED LOGINS FROM "+start+" TO "+end+" ";
						
	                    pst.close();
	                    rs.close();
					} catch(Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "Please Select the Dates from the dropdown Calendar!");
						((JTextField)periodFrom.getDateEditor().getUiComponent()).setText("");
						((JTextField)periodTo.getDateEditor().getUiComponent()).setText("");
					}
				}
			}
		});
		btnLoadfFailedLoginAttempts.setBounds(595, 100, 453, 33);
		contentPane.add(btnLoadfFailedLoginAttempts);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(92, 148, 956, 336);
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
		btnPrint.setBounds(905, 495, 143, 42);
		contentPane.add(btnPrint);
		
		JButton btnBack = new JButton("BACK");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ADMINMAINMENU.main(new String[]{});
			}
		});
		btnBack.setBounds(709, 495, 149, 42);
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
