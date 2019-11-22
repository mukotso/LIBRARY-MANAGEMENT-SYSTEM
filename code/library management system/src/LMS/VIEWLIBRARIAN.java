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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Color;

public class VIEWLIBRARIAN extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VIEWLIBRARIAN frame = new VIEWLIBRARIAN();
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
	public VIEWLIBRARIAN() {
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
		
		JLabel lblLibrarianDetails = new JLabel("VIEW LIBRARIANS");
		lblLibrarianDetails.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblLibrarianDetails.setBounds(537, 11, 281, 26);
		contentPane.add(lblLibrarianDetails);
		
		JButton btnLoadLibrariansDetails = new JButton("Load LIbrarians' Details");
		btnLoadLibrariansDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String query = "select LIBRARIAN_ID,NAME,EMAIL,PHONE_NUMBER,LOGIN_STATUS from LIBRARIANS where LOGIN_STATUS = '"+"ACTIVE"+"' OR  LOGIN_STATUS = '"+"BLOCKED"+"' ";
					PreparedStatement pst=connection.prepareStatement(query);
					ResultSet rs=pst.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));
					table.getColumnModel().getColumn(0).setCellRenderer(new WordWrapCellRenderer());
					table.getColumnModel().getColumn(1).setCellRenderer(new WordWrapCellRenderer());
					table.getColumnModel().getColumn(2).setCellRenderer(new WordWrapCellRenderer());
					table.getColumnModel().getColumn(3).setCellRenderer(new WordWrapCellRenderer());
					table.getColumnModel().getColumn(4).setCellRenderer(new WordWrapCellRenderer());
					
					pst.close();
					rs.close();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnLoadLibrariansDetails.setBounds(327, 48, 575, 34);
		contentPane.add(btnLoadLibrariansDetails);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(59, 93, 1055, 387);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setEnabled(false);
		scrollPane.setViewportView(table);

		JButton btnBack = new JButton("BACK");
		btnBack.setBounds(797, 500, 317, 34);
		contentPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ADMINMAINMENU.main(new String[]{});
			}
		});
	}

	private void initialize() {
		// TODO Auto-generated method stub
		
	}
}
