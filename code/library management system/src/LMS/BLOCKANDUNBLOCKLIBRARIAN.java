package LMS;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Color;

public class BLOCKANDUNBLOCKLIBRARIAN extends JFrame {

	private JPanel contentPane;
	private JComboBox TF_lib_id;
	JButton btnBlock;
	JButton btnUnblock;
	private String comboSelection="";
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BLOCKANDUNBLOCKLIBRARIAN frame = new BLOCKANDUNBLOCKLIBRARIAN();
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
	public BLOCKANDUNBLOCKLIBRARIAN() {
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
		
		JLabel lblDeleteLibrarian = new JLabel("BLOCK / UNBLOCK / DELETE LIBRARIAN");
		lblDeleteLibrarian.setBounds(188, 11, 300, 34);
		lblDeleteLibrarian.setFont(new Font("Tahoma", Font.BOLD, 14));
		contentPane.add(lblDeleteLibrarian);
		
		JLabel lblLibrarianId = new JLabel("LIBRARIAN ID");
		lblLibrarianId.setBounds(63, 59, 121, 39);
		contentPane.add(lblLibrarianId);
		
		TF_lib_id = new JComboBox();
		TF_lib_id.setBounds(203, 56, 250, 42);
		TF_lib_id.setMaximumRowCount(5);
		TF_lib_id.setEditable(false);
		contentPane.add(TF_lib_id);
		refreshComboBoxLib();
		
		contentPane.add(TF_lib_id);
		TF_lib_id.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboSelection=TF_lib_id.getSelectedItem().toString();
				System.out.println(comboSelection);
				refreshLibrarianDetails();
				activeBlocked();
			}
		});
		
		btnBlock = new JButton("BLOCK");
		btnBlock.setBounds(857, 460, 155, 42);
		contentPane.add(btnBlock);
		btnBlock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboSelection.equals("")) {
					JOptionPane.showMessageDialog(null, "Please Select the Librarian ID!");
				}
				else if(active()==0 && blocked()==0) {
					JOptionPane.showMessageDialog(null, "Librarian Record Not Found!!!\n Please provide a valid librarian ID");
					
				}
				else if(blocked()==1) {
					JOptionPane.showMessageDialog(null, "The Librarian is Already Blocked!!!");
					
				}
				else if(active()==1) {
					try {
						int action=JOptionPane.showConfirmDialog(null, "Do You really Want To Block The Librarian", "Block", JOptionPane.YES_NO_OPTION);
						if(action==0) {
						try {
							String libStatusBlock="BLOCKED";
							String libid=comboSelection;
							String query = "update LIBRARIANS set LOGIN_STATUS = '"+libStatusBlock+"' where LIBRARIAN_ID='"+libid+"' ";
							PreparedStatement pst=connection.prepareStatement(query);
							pst.execute();
							
							JOptionPane.showMessageDialog(null, "Librarian Blocked Successfully");
							
							pst.close();
							
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null, "An error occurred. Please Provide the Librarian ID!!!");
							
							ex.printStackTrace();
						}
						
						}
						
					}catch(Exception exCheck) {
						JOptionPane.showMessageDialog(null, "An error occurred. Please Provide the Librarian ID!!!");
						
						exCheck.printStackTrace();
					}
				}
				refreshLibrarianDetails();
				activeBlocked();
				
			}
		});
		
		btnUnblock = new JButton("UNBLOCK");
		btnUnblock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboSelection.equals("")) {
					JOptionPane.showMessageDialog(null, "Please Select the Librarian ID!");
				}
				else if(active()==0 && blocked()==0) {
					JOptionPane.showMessageDialog(null, "Librarian Record Not Found!!!\n Please provide a valid librarian ID");
					
				}
				else if(active()==1) {
					JOptionPane.showMessageDialog(null, "The Librarian is not Blocked!!!");
					
				}
				else if(blocked()==1) {
					try {
						int action=JOptionPane.showConfirmDialog(null, "Do You really Want To UNBLOCK The Librarian", "Unblock", JOptionPane.YES_NO_OPTION);
						if(action==0) {
						try {
							String libStatusUnblock="ACTIVE";
							String libid=comboSelection;
							String query = "update LIBRARIANS set LOGIN_STATUS = '"+libStatusUnblock+"' where LIBRARIAN_ID='"+libid+"' ";
							PreparedStatement pst=connection.prepareStatement(query);
							pst.execute();
							
							JOptionPane.showMessageDialog(null, "Librarian UnBlocked Successfully");
							
							pst.close();
						
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null, "An error occurred. Please Provide the Librarian ID!!!");
							
							ex.printStackTrace();
						}
						
						}
						
					}catch(Exception exCheck) {
						JOptionPane.showMessageDialog(null, "An error occurred. Please Provide the Librarian ID!!!");
						
						exCheck.printStackTrace();
					}
				}
				refreshLibrarianDetails();
				activeBlocked();
			}
		});
		btnUnblock.setBounds(623, 460, 155, 42);
		contentPane.add(btnUnblock);
		
		JButton btnDelete = new JButton("DELETE");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboSelection.equals("")) {
					JOptionPane.showMessageDialog(null, "Please Select the Librarian ID!");
				}
				else if(active()==0 && blocked()==0) {
					JOptionPane.showMessageDialog(null, "Librarian Record Not Found!!!\n Please provide a valid librarian ID");
					
				}
				else {
					try {
						int action=JOptionPane.showConfirmDialog(null, "Do You really Want To DELETE The Librarian", "Delete", JOptionPane.YES_NO_OPTION);
						if(action==0) {
						try {
							String libStatusDelete="*DELETED*";
							String libid=comboSelection;
							String query = "update LIBRARIANS set LOGIN_STATUS = '"+libStatusDelete+"' where LIBRARIAN_ID='"+libid+"' ";
							PreparedStatement pst=connection.prepareStatement(query);
							pst.execute();
							
							JOptionPane.showMessageDialog(null, "Librarian Deleted Successfully");
							TF_lib_id.removeItem(comboSelection);
							clearLibId();
							refreshLibrarianDetails();
							
							pst.close();
						
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null, "An error occurred. Please Provide the Librarian ID!!!");
							
							ex.printStackTrace();
						}
						
						}
						
					}catch(Exception exCheck) {
						JOptionPane.showMessageDialog(null, "An error occurred. Please Provide the Librarian ID!!!");
						
						exCheck.printStackTrace();
					}
				}
				refreshLibrarianDetails();
				activeBlocked();
			}
		});
		btnDelete.setBounds(409, 460, 155, 43);
		contentPane.add(btnDelete);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(203, 109, 809, 268);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setEnabled(false);
		scrollPane.setViewportView(table);
		
		JButton btnBack = new JButton("BACK");
		btnBack.setBounds(203, 460, 141, 42);
		contentPane.add(btnBack);
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ADMINMAINMENU.main(new String[]{});
			}
		});
	}
	private int active() {
		int active=-1;
		try {
			String libStatus="ACTIVE";
			String libid=comboSelection;
			String queryCheck = "select * from LIBRARIANS where LIBRARIAN_ID = '"+libid+"' and LOGIN_STATUS = '"+libStatus+"' ";
			PreparedStatement pstCheck=connection.prepareStatement(queryCheck);
			ResultSet rsCheck=pstCheck.executeQuery();
			if(rsCheck.next()) {
				active=1;
			}else {
				active=0;
			}
			pstCheck.close();
			rsCheck.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return active;
	}
	private int blocked() {
		int blocked=-1;
		try {
			String libStatus="BLOCKED";
			String libid=comboSelection;
			String queryCheck = "select * from LIBRARIANS where LIBRARIAN_ID = '"+libid+"' and LOGIN_STATUS = '"+libStatus+"' ";
			PreparedStatement pstCheck=connection.prepareStatement(queryCheck);
			ResultSet rsCheck=pstCheck.executeQuery();
			if(rsCheck.next()) {
				blocked=1;
			}else {
				blocked=0;
			}
			pstCheck.close();
			rsCheck.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return blocked;
	}
	private void refreshLibrarianDetails() {
		try {
			String libid=comboSelection;
			String query="select LIBRARIAN_ID,NAME,USERNAME,EMAIL,PHONE_NUMBER,LOGIN_STATUS from LIBRARIANS where LIBRARIAN_ID=? ";
			PreparedStatement pst=connection.prepareStatement(query);
			pst.setString(1,libid);
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
	private void activeBlocked() {
		if(active()==1) {
			btnUnblock.setEnabled(false);
			btnBlock.setEnabled(true);
			
		}
		else if(blocked()==1) {
			btnBlock.setEnabled(false);
			btnUnblock.setEnabled(true);
		}
	}
	private void refreshComboBoxLib() {
		try{
            Statement stmt = connection.createStatement();
            String query = "SELECT * FROM LIBRARIANS where LOGIN_STATUS = '"+"ACTIVE"+"' OR LOGIN_STATUS = '"+"BLOCKED"+"' ";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next())
            {
            	TF_lib_id.addItem(rs.getString("LIBRARIAN_ID")); 

            }	

            stmt.close();
            rs.close();
		}  
		catch(Exception ex)
		{	
    	ex.printStackTrace();
		}
	}
	private void clearLibId() {
		comboSelection="";
	}
	private void initialize() {
		// TODO Auto-generated method stub
		
	}
}
