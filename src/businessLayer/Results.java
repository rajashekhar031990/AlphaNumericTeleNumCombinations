package businessLayer;

import java.sql.SQLException;
import java.util.ArrayList;

public class Results {
	public ArrayList<String> phoneNumbers = new ArrayList<String>();
	public String phoneNumber = "";
	public String phoneNumberId = "";
	public int totalCount;

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Results rs = new GenerateCombinations().generateCombinations("1234", 1, 10);
		System.out.println("phoneNumber: "+ rs.phoneNumber + ", phoneNumberId: " + rs.phoneNumberId + ", totalCount: " + rs.totalCount);
	}

}
