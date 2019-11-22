package LMS;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;

import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SEARCHBOOKS extends JFrame {

	private JPanel contentPane;
	private JTextField TF_bookTitle;
	private JTable table;
	private String accessNo="0";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SEARCHBOOKS frame = new SEARCHBOOKS();
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
	public SEARCHBOOKS() {
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
		
		JLabel lblBooksInformation = new JLabel("SEARCH BOOKS");
		lblBooksInformation.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblBooksInformation.setBounds(415, 11, 247, 44);
		contentPane.add(lblBooksInformation);
		
		JLabel lblBookTitle = new JLabel("BOOK TITLE");
		lblBookTitle.setBounds(27, 70, 85, 44);
		contentPane.add(lblBookTitle);
		
		TF_bookTitle = new JTextField();
		TF_bookTitle.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				searchBook();
			}
		});
		TF_bookTitle.setBounds(127, 70, 328, 44);
		contentPane.add(TF_bookTitle);
		TF_bookTitle.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(27, 125, 1128, 360);
		contentPane.add(scrollPane);
		
		table = new JTable() {
			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false;
			}
		};
		
		scrollPane.setViewportView(table);
		tableSelectionAccessNo();

		JButton btnViewBookProfile = new JButton("VIEW BOOK PROFILE");
		btnViewBookProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tableSelectionAccessNo()=="0") {
					JOptionPane.showMessageDialog(null, "Please Select a Book from the Table!");
				}
				else {
					viewBookProfile();
				}
			}
		});
		btnViewBookProfile.setBounds(960, 496, 195, 42);
		contentPane.add(btnViewBookProfile);
		
		JButton btnBack = new JButton("BACK");
		btnBack.setBounds(713, 496, 195, 42);
		contentPane.add(btnBack);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				BOOKSINFORMATION.main(new String[]{});
			}
		});

	}
	private void searchBook() {
		try {
			String queryBook="select ACCESSION_NUMBER as ACCESSION_NO,ISBN,CALL_NUMBER,SUBJECT,TITLE,AUTHOR,PUBLISHER,EDITION_NUMBER as EDITION,AVAILABILITY from BOOKS where TITLE like '"+TF_bookTitle.getText()+"%' or TITLE like '%"+TF_bookTitle.getText()+"%' ";
			PreparedStatement pstSearchBook=connection.prepareStatement(queryBook);
			ResultSet rsSearchBook=pstSearchBook.executeQuery();
			
			table.setModel(DbUtils.resultSetToTableModel(rsSearchBook));
			
			pstSearchBook.close();
			rsSearchBook.close();
		}catch(Exception exSearchBook) {
			exSearchBook.printStackTrace();
		}
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
	private void viewBookProfile() {
		if(availability()==1) {
			setAccessionNo();
			dispose();
			BOOKDETAILS.main(new String[]{});
		}
		else if(availability()==2) {
			setAccessionNo();
			dispose();
			ISSUEDBOOKDETAILS.main(new String[]{});
		}
		else if(availability()==3) {
			JOptionPane.showMessageDialog(null, "Book Profile Isn't Available since the Book is Deleted!");
		}
	}
	private int availability() {
		int status=-1;
		try {
			String availability="";
			String queryAvail="select * from BOOKS where ACCESSION_NUMBER = '"+tableSelectionAccessNo()+"' ";
			PreparedStatement pstAvail=connection.prepareStatement(queryAvail);
			ResultSet rsAvail=pstAvail.executeQuery();
			while(rsAvail.next()) {
				availability=rsAvail.getString("AVAILABILITY");
			}
			String available="AVAILABLE";
			String issued="ISSUED";
			String deleted="DELETED";
			if(availability.equals(available)) {
				status=1;
			}else if(availability.equals(issued)) {
				status=2;
			}else if(availability.equals(deleted)) {
				status=3;
			}
			pstAvail.close();
			rsAvail.close();
		}catch(Exception exAvailability) {
			exAvailability.printStackTrace();
		}
		return status;
	}
	private void setAccessionNo() {
		try {
			String querySaveSearchedAccessNo="insert into SEARCHED_ACCESSION_NUMBERS (ACCESSION_NUMBER) values (?)";
			PreparedStatement pstSaveSearchedAccessNo=connection.prepareStatement(querySaveSearchedAccessNo);
			
			pstSaveSearchedAccessNo.setString(1,accessNo);
			
			pstSaveSearchedAccessNo.execute();
			//JOptionPane.showMessageDialog(null, "Accession Number Saved Successfully");
			
			pstSaveSearchedAccessNo.close();
		}catch(Exception exGetAccessNo) {
			exGetAccessNo.printStackTrace();
		}
	}
	public String getAccessionNo() {
		String searchedAccession="";
		try {
			String querySearchedAccessNo="select * from SEARCHED_ACCESSION_NUMBERS ";
			PreparedStatement pstSearchedAccessNo=connection.prepareStatement(querySearchedAccessNo);
			ResultSet rsSearchedAccessNo=pstSearchedAccessNo.executeQuery();
			while(rsSearchedAccessNo.next()) {
				searchedAccession=rsSearchedAccessNo.getString("ACCESSION_NUMBER");
			}
			pstSearchedAccessNo.close();
			rsSearchedAccessNo.close();
		}catch(Exception exGetAccessNo) {
			exGetAccessNo.printStackTrace();
		}
		return searchedAccession;
	}
	public void clearSearchedAccessNo() {
		try {
			String queryClearSearchedAccessNo="delete from SEARCHED_ACCESSION_NUMBERS ";
			PreparedStatement pstClearSearchedAccessNo=connection.prepareStatement(queryClearSearchedAccessNo);
			
			pstClearSearchedAccessNo.execute();
			
			pstClearSearchedAccessNo.close();
		}catch(Exception exRead) {
			exRead.printStackTrace();
		}
	}
	private void initialize() {
		// TODO Auto-generated method stub
		
	}
}
