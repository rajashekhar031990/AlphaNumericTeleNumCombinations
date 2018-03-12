package businessLayer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBQueries {
	public void createDB() throws ClassNotFoundException, SQLException {
		Connection con = DBConnection.getDBConnection();
		Statement stm = con.createStatement();
		boolean rs = stm.execute("CREATE DATABASE IF NOT EXISTS AlphaNumericPhoneDB;");
		// System.out.println((rs == true ? "DB Created" : "DB not created"));

		createTables();
	}

	public void createTables() throws ClassNotFoundException, SQLException {
		Connection con = DBConnection.getDBConnection();
		dropTable("AlphaNumericPhoneDB.AlphaNumericPhoneDetails");
		dropTable("AlphaNumericPhoneDB.AlphaNumericMap");
		Statement stm = con.createStatement();
		String sql_stmt = "CREATE TABLE IF NOT EXISTS AlphaNumericPhoneDB.`AlphaNumericMap` (\n"
				+ "    `Id` int(11) auto_increment not null,\n" + "    `phonenumber` VARCHAR(10) NOT NULL,\n"
				+ "    PRIMARY KEY (`Id`)\n" + ");";
		int rs = stm.executeUpdate(sql_stmt);

		sql_stmt = "CREATE TABLE IF NOT EXISTS AlphaNumericPhoneDB.`AlphaNumericPhoneDetails` (\n"
				+ "    `Id` int(11) auto_increment not null,\n" + "    `phonenumber` VARCHAR(10) NOT NULL,\n"
				+ "    `alphanumericid` int,\n"
				+ "    PRIMARY KEY (`Id`), foreign key (alphanumericid) references AlphaNumericPhoneDB.AlphaNumericMap(id) \n"
				+ ");";
		rs = stm.executeUpdate(sql_stmt);
		closeConnections(stm, con);
	}

	public void dropTable(String tableName) throws ClassNotFoundException, SQLException {
		Connection con = DBConnection.getDBConnection();
		Statement stm = con.createStatement();
		String sql_stmt = "drop table if exists " + tableName;
		stm.execute(sql_stmt);
		closeConnections(stm, con);
	}

	public String insertPhoneNumber(String phoneNumber) throws ClassNotFoundException, SQLException {
		System.out.println("insertPhoneNumber started.");
		String phoneNumberId = "";
		String sql_stmt = "INSERT INTO AlphaNumericPhoneDB.AlphaNumericMap( phoneNumber) values('" + phoneNumber + "')";

		System.out.println(sql_stmt);

		Connection con = DBConnection.getDBConnection();
		Statement stm = con.createStatement();
		stm.executeUpdate(sql_stmt);
		System.out.println("insertPhoneNumber ended.");

		phoneNumberId = getPhoneNumberId(phoneNumber);

		System.out.println("phoneNumberID: " + phoneNumberId);
		System.out.println("insertPhoneNumber ended.");
		closeConnections(stm, con);
		return phoneNumberId;
	}

	public void insertData(ArrayList<String> results, String phoneNumberId)
			throws ClassNotFoundException, SQLException {
		System.out.println("insertData started.");
		String sql_stmt = "INSERT INTO AlphaNumericPhoneDB.AlphaNumericPhoneDetails( phoneNumber, alphanumericid) values";
		Connection con = DBConnection.getDBConnection();
		Statement stm = con.createStatement();

		if (results.size() > 10000) {
			String tempBulk = "";
			int counter = 0, j=1;
			for (int i = 0; i < results.size(); i++) {
				tempBulk += "('" + results.get(i) + "', '" + phoneNumberId + "'),";
				counter++;
				if (counter == 10000 || i == results.size() - 1) {
					tempBulk = sql_stmt + tempBulk.substring(0, tempBulk.length() - 1);
					stm.executeUpdate(tempBulk);
					tempBulk = "";
					counter = 0;
					System.out.println(j * 10000); j++;
				}
			}
		} else {
			for (int i = 0; i < results.size(); i++) {
				sql_stmt += "('" + results.get(i) + "', '" + phoneNumberId + "'),";
			}

			sql_stmt = sql_stmt.substring(0, sql_stmt.length() - 1);
			stm.executeUpdate(sql_stmt);
		}
		closeConnections(stm, con);
		System.out.println("insertData ended.");
	}

	public ArrayList<String> getData(String phoneNumberId, int offset, int end)
			throws ClassNotFoundException, SQLException {
		System.out.println("getData started.");
		ArrayList<String> results = new ArrayList<String>();
		String sql_stmt = "SELECT phoneNumber FROM AlphaNumericPhoneDB.AlphaNumericPhoneDetails WHERE alphanumericid="
				+ phoneNumberId + " LIMIT " + offset + ", " + end;

		System.out.println(sql_stmt);
		Connection con = DBConnection.getDBConnection();
		Statement stm = con.createStatement();
		ResultSet rs = stm.executeQuery(sql_stmt);
		while (rs.next()) {
			results.add(rs.getString(1));
		}

		System.out.println(results);
		System.out.println("getData ended.");
		closeConnections(rs, stm, con);

		return results;
	}

	public String getPhoneNumberId(String phoneNumber) throws ClassNotFoundException, SQLException {
		System.out.println("getPhoneNumberId started.");
		System.out.println("Input PhoneNumber: " + phoneNumber);
		String phoneNumberId = "";
		Connection con = DBConnection.getDBConnection();
		Statement stm = con.createStatement();
		String sql_stmt = "SELECT id FROM AlphaNumericPhoneDB.AlphaNumericMap WHERE phoneNumber='" + phoneNumber + "';";
		System.out.println(sql_stmt);
		ResultSet rs = stm.executeQuery(sql_stmt);
		if (rs.next()) {
			phoneNumberId = rs.getString("id");
		}
		System.out.println(phoneNumberId);
		System.out.println("getPhoneNumberId ended.");
		closeConnections(rs, stm, con);
		return phoneNumberId;
	}

	public void closeConnections(Statement stm, Connection con) throws SQLException {
		stm.close();
		con.close();
	}

	public void closeConnections(ResultSet rs, Statement stm, Connection con) throws SQLException {
		rs.close();
		stm.close();
		con.close();
	}

	public int getTotalCount(String phoneNumberId) throws ClassNotFoundException, SQLException {
		System.out.println("getTotalCount started.");
		System.out.println("Input phoneNumberId: " + phoneNumberId);
		int totalCount = 0;
		Connection con = DBConnection.getDBConnection();
		Statement stm = con.createStatement();
		String sql_stmt = "SELECT COUNT(phoneNumber) FROM AlphaNumericPhoneDB.AlphaNumericPhoneDetails WHERE alphanumericid="
				+ phoneNumberId + ";";
		System.out.println(sql_stmt);
		ResultSet rs = stm.executeQuery(sql_stmt);
		if (rs.next()) {
			totalCount = rs.getInt(1);
		}
		System.out.println(phoneNumberId);
		System.out.println("getTotalCount ended.");

		closeConnections(rs, stm, con);
		return totalCount;
	}
}
