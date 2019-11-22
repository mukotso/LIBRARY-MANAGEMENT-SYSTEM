package LMS;

import java.sql.*;
import javax.swing.*;


public class dbConnection {
	Connection c=null;
	public static Connection dbConnector() {
		
		try {
			Class.forName("org.sqlite.JDBC");
			Connection c=DriverManager.getConnection("jdbc:sqlite:LMS.db");
			return c;
		}
		catch(Exception e) {
			
			JOptionPane.showMessageDialog(null, e);
			return null;
		}

	}

}