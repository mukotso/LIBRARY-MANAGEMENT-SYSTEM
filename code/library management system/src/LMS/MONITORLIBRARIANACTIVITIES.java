package LMS;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class MONITORLIBRARIANACTIVITIES extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MONITORLIBRARIANACTIVITIES frame = new MONITORLIBRARIANACTIVITIES();
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
	public MONITORLIBRARIANACTIVITIES() {
		super("Library Management System");
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(LMSMAINMENU.class.getResource("/images/images (7).jpeg")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(75, 75, 1200, 600);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 153, 153));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblMonitorLibrarian = new JLabel("MONITOR LIBRARIANS ACTIVITIES");
		lblMonitorLibrarian.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblMonitorLibrarian.setBounds(380, 11, 391, 44);
		contentPane.add(lblMonitorLibrarian);
		
		JButton btnAddedDeleted = new JButton("ADDED AND DELETED BOOKS");
		btnAddedDeleted.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ADDED_DELETED_BOOKS.main(new String[]{});
			}
		});
		btnAddedDeleted.setBounds(184, 79, 746, 68);
		contentPane.add(btnAddedDeleted);
		
		JButton btnIssuedReturned = new JButton("ISSUED AND RETURNED BOOKS");
		btnIssuedReturned.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ISSUED_RETURNED_BOOKS.main(new String[]{});
			}
		});
		btnIssuedReturned.setBounds(184, 170, 746, 68);
		contentPane.add(btnIssuedReturned);
		
		JButton btnFineClearance = new JButton("FINE PAID AND STUDENTS CLEARED");
		btnFineClearance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				FINEPAID_STUDENTCLEARANCE.main(new String[]{});
			}
		});
		btnFineClearance.setBounds(184, 256, 746, 68);
		contentPane.add(btnFineClearance);
		
		JButton btnBack = new JButton("BACK");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ADMINMAINMENU.main(new String[]{});
			}
		});
		btnBack.setBounds(735, 451, 195, 42);
		contentPane.add(btnBack);
	}
}
