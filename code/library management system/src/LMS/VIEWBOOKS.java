package LMS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
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
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Color;

public class VIEWBOOKS extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VIEWBOOKS frame = new VIEWBOOKS();
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
	public VIEWBOOKS() {
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
		
		JLabel lblAvailableBooks = new JLabel("VIEW AVAILABLE BOOKS");
		lblAvailableBooks.setBounds(537, 11, 281, 26);
		lblAvailableBooks.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JButton btnLoadAvailableBooks = new JButton("Load Available Books' Information");
		btnLoadAvailableBooks.setBounds(327, 48, 575, 34);
		btnLoadAvailableBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String availabilityStatus="AVAILABLE";
					String query = "select ACCESSION_NUMBER,ISBN,CALL_NUMBER,SUBJECT,TITLE,AUTHOR,PUBLISHER,EDITION_NUMBER from BOOKS where AVAILABILITY = '"+availabilityStatus+"' ";
					PreparedStatement pst=connection.prepareStatement(query);
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
					
					
					pst.close();
					rs.close();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(59, 93, 1055, 387);
		
		table = new JTable();
		table.setEnabled(false);
		scrollPane.setViewportView(table);
		contentPane.setLayout(null);
		contentPane.add(lblAvailableBooks);
		contentPane.add(btnLoadAvailableBooks);
		contentPane.add(scrollPane);

		JButton btnBack = new JButton("BACK");
		btnBack.setBounds(797, 500, 317, 34);
		contentPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				BOOKSINFORMATION.main(new String[]{});
			}
		});
	}

	private void initialize() {
		// TODO Auto-generated method stub
		
	}
}
