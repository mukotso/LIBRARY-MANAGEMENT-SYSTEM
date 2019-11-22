package LMS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.Color;

public class VIEWISSUEDBOOKS extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VIEWISSUEDBOOKS frame = new VIEWISSUEDBOOKS();
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
	public VIEWISSUEDBOOKS() {
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
		
		JLabel lblViewIssuedBooks = new JLabel("VIEW ISSUED BOOKS");
		lblViewIssuedBooks.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblViewIssuedBooks.setBounds(537, 11, 281, 26);
		contentPane.add(lblViewIssuedBooks);
		
		JButton btnBack = new JButton("BACK");
		btnBack.setBounds(797, 500, 317, 34);
		contentPane.add(btnBack);
		
		JButton btnLoadIssuedBooks = new JButton("Load Issued Books' Details");
		btnLoadIssuedBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String status="Issued";
					
					String query="select ISSUED_BOOKS.ACCESSION_NUMBER,BOOKS.ISBN,BOOKS.TITLE,ISSUED_BOOKS.REGISTRATION_NUMBER,ISSUED_BOOKS.ISSUE_DATE,ISSUED_BOOKS.DUE_DATE,ISSUED_BOOKS.LOAN_TYPE from BOOKS,ISSUED_BOOKS  where BOOKS.ACCESSION_NUMBER = ISSUED_BOOKS.ACCESSION_NUMBER and ISSUED_BOOKS.STATUS = '"+status+"' ";
					PreparedStatement pst=connection.prepareStatement(query);
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
		});
		btnLoadIssuedBooks.setBounds(327, 48, 575, 34);
		contentPane.add(btnLoadIssuedBooks);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(59, 93, 1055, 387);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setEnabled(false);
		scrollPane.setViewportView(table);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				STUDENTINFORMATION.main(new String[]{});
			}
		});
	}

	private void initialize() {
		// TODO Auto-generated method stub
		
	}

}
