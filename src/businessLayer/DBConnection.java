package businessLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
		DBQueries dbQueries = new DBQueries();
		dbQueries.createDB();
		
		System.out.println(DBConnection.getDBConnection());
		Statement stm = DBConnection.getDBConnection().createStatement();
		stm.executeQuery("use AlphaNumericPhoneDB;");
		ResultSet rs = stm.executeQuery("show tables;");
		while (rs.next()) {
			System.out.println(rs.getString(1));
		}
	}

	public static Connection getDBConnection() throws SQLException, ClassNotFoundException {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root", "root");
		} catch (Exception e) {
			System.out.println(e);
		}
		return con;
	}

}
