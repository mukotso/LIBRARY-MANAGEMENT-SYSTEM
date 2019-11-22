package LMS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;

public class BOOKSINFORMATION extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BOOKSINFORMATION frame = new BOOKSINFORMATION();
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
	public BOOKSINFORMATION() {
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
		
		JLabel lblBooksInformation = new JLabel("BOOKS INFORMATION");
		lblBooksInformation.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblBooksInformation.setBounds(415, 11, 247, 44);
		contentPane.add(lblBooksInformation);
		
		JButton btnAddBooks = new JButton("ADD BOOK");
		btnAddBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ADDBOOKS.main(new String[]{});
			}
		});
		btnAddBooks.setBounds(184, 79, 746, 68);
		contentPane.add(btnAddBooks);
		
		JButton btnViewBooks = new JButton("VIEW AVAILABLE BOOKS");
		btnViewBooks.setBounds(184, 170, 746, 68);
		contentPane.add(btnViewBooks);
		btnViewBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				VIEWBOOKS.main(new String[]{});
			}
		});

		
		
		JButton btnDeleteBook = new JButton("DELETE BOOK");
		btnDeleteBook.setBounds(184, 256, 746, 68);
		contentPane.add(btnDeleteBook);
		btnDeleteBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				DELETEBOOK.main(new String[]{});
			}
		});
		
		JButton btnSearchBooks = new JButton("SEARCH BOOKS");
		btnSearchBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				SEARCHBOOKS.main(new String[]{});
			}
		});
		btnSearchBooks.setBounds(184, 335, 746, 68);
		contentPane.add(btnSearchBooks);
		
		JButton btnBack = new JButton("BACK");
		btnBack.setBounds(735, 451, 195, 42);
		contentPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				LIBRARIANMAINMENU.main(new String[]{});
			}
		});

	}

}
