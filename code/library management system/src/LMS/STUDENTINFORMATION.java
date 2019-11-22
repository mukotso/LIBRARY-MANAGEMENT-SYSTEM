package LMS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;

public class STUDENTINFORMATION extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					STUDENTINFORMATION frame = new STUDENTINFORMATION();
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
	public STUDENTINFORMATION() {
		super("Library Management System");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				LIBRARIANMAINMENU logout = new LIBRARIANMAINMENU();
				logout.logLogoutTime();
			}
		});
		setIconImage(Toolkit.getDefaultToolkit().getImage(LMSMAINMENU.class.getResource("/images/images (7).jpeg")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(75, 75, 1200, 600);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 153, 153));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblStudentInformation = new JLabel("STUDENT INFORMATION");
		lblStudentInformation.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblStudentInformation.setBounds(495, 11, 240, 37);
		contentPane.add(lblStudentInformation);
		
		JButton btnRegisterStudent = new JButton("REGISTER STUDENT");
		btnRegisterStudent.setBounds(243, 59, 702, 57);
		contentPane.add(btnRegisterStudent);
		btnRegisterStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				REGISTERSTUDENT.main(new String[]{});
			}
		});
		
		JButton btnIssueBooks = new JButton("ISSUE BOOKS");
		btnIssueBooks.setBounds(244, 143, 701, 57);
		contentPane.add(btnIssueBooks);
		btnIssueBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ISSUEBOOKS.main(new String[]{});
			}
		});
		
		JButton btnViewIssuedBooks = new JButton("VIEW ISSUED BOOKS");
		btnViewIssuedBooks.setBounds(244, 222, 701, 57);
		contentPane.add(btnViewIssuedBooks);
		btnViewIssuedBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				VIEWISSUEDBOOKS.main(new String[]{});
			}
		});
		
		JButton btnReturnBooks = new JButton("RETURN BOOKS");
		btnReturnBooks.setBounds(244, 307, 701, 57);
		contentPane.add(btnReturnBooks);
		btnReturnBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				RETURNBOOKS.main(new String[]{});
			}
		});

		JButton btnStudentClearance = new JButton("STUDENT CLEARANCE");
		btnStudentClearance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				STUDENTCLEARANCE.main(new String[]{});
			}
		});
		btnStudentClearance.setBounds(244, 391, 701, 57);
		contentPane.add(btnStudentClearance);
		
		JButton btnBack = new JButton("BACK");
		btnBack.setBounds(744, 480, 201, 49);
		contentPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				LIBRARIANMAINMENU.main(new String[]{});
			}
		});
	}
}
