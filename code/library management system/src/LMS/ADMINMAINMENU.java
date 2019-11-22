package LMS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;

public class ADMINMAINMENU extends JFrame {

	private JPanel contentPane;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ADMINMAINMENU frame = new ADMINMAINMENU();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ADMINMAINMENU() {
		super("Library Management System");
		setIconImage(Toolkit.getDefaultToolkit().getImage(LMSMAINMENU.class.getResource("/images/images (7).jpeg")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(75, 75, 1200, 600);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 153, 153));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblAdministratorMainMenu = new JLabel("ADMINISTRATOR MAIN MENU");
		lblAdministratorMainMenu.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAdministratorMainMenu.setBounds(483, 11, 281, 40);
		contentPane.add(lblAdministratorMainMenu);
		
		JButton btnAddLibrarian = new JButton("ADD LIBRARIAN");
		btnAddLibrarian.setBounds(260, 134, 670, 40);
		contentPane.add(btnAddLibrarian);
		btnAddLibrarian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ADDLIBRARIAN.main(new String[]{});
			}
			
		});
		JButton btnViewLibLoginHistory = new JButton("VIEW LIBRARIANS LOGIN HISTORY");
		btnViewLibLoginHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				LIBRARIANLOGINHISTORY.main(new String[]{});
			}
		});
		btnViewLibLoginHistory.setBounds(260, 324, 670, 40);
		contentPane.add(btnViewLibLoginHistory);
		
		JButton btnViewLibrarian = new JButton("VIEW LIBRARIANS");
		btnViewLibrarian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				VIEWLIBRARIAN.main(new String[]{});
			}
		});
		btnViewLibrarian.setBounds(260, 202, 670, 40);
		contentPane.add(btnViewLibrarian);
		
		JButton btnBlockUnblockLibrarian = new JButton("BLOCK / UNBLOCK / DELETE LIBRARIAN");
		btnBlockUnblockLibrarian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				BLOCKANDUNBLOCKLIBRARIAN.main(new String[]{});
			}
		});
		btnBlockUnblockLibrarian.setBounds(260, 263, 670, 40);
		contentPane.add(btnBlockUnblockLibrarian);
		
		JButton btnMonitorLibrarians = new JButton("MONITOR LIBRARIANS ACTIVITIES");
		btnMonitorLibrarians.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				MONITORLIBRARIANACTIVITIES.main(new String[]{});
			}
		});
		btnMonitorLibrarians.setBounds(260, 388, 670, 40);
		contentPane.add(btnMonitorLibrarians);

		JButton btnGenerateReports = new JButton("GENERATE REPORTS");
		btnGenerateReports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				GENERATE_REPORTS.main(new String[]{});
			}
		});
		btnGenerateReports.setBounds(260, 446, 670, 40);
		contentPane.add(btnGenerateReports);
		
		JButton btnUpdateAdminDetails = new JButton("UPDATE ADMIN DETAILS");
		btnUpdateAdminDetails.setBounds(260, 70, 670, 40);
		contentPane.add(btnUpdateAdminDetails);
		btnUpdateAdminDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				UPDATE_ADMIN_DETAILS.main(new String[]{});
			}
		});
		
		JButton btnLogout = new JButton("LOGOUT");
		btnLogout.setBounds(735, 508, 195, 42);
		contentPane.add(btnLogout);
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				LMSMAINMENU.main(new String[]{});
			}
		});
	}
}
