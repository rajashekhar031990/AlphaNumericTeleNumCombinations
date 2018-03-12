package businessLayer;

import java.sql.SQLException;
import java.util.ArrayList;

public class GenerateCombinations {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		new GenerateCombinations().generateCombinations("11", 1, 10);

	}

	public Results generateCombinations(String phoneNumber, int offset, int step)
			throws ClassNotFoundException, SQLException {
		Results results = new Results();
		System.out.println("initial generateCombinations method started.");
		DBQueries dbQueries = new DBQueries();
		// dbQueries.createDB();

		// Generate combinations if not exist already in database
		String id = dbQueries.getPhoneNumberId(phoneNumber);
		if (id == "") {
			id = generateCombinations(phoneNumber);
		}

		results.phoneNumbers = dbQueries.getData(id, offset, step);
		results.phoneNumber = phoneNumber;
		results.phoneNumberId = id;
		results.totalCount = dbQueries.getTotalCount(id);

		System.out.println("initial generateCombinations method ended.");

		return results;
	}

	@SuppressWarnings("unchecked")
	public String generateCombinations(String phoneNumber) throws ClassNotFoundException, SQLException {
		System.out.println("main generateCombinations method started.");
		ArrayList<String> results = new ArrayList<String>();
		ArrayList<String> tempResults = new ArrayList<String>();
		int i, j, k;
		String currText = "", temp = "", id = "";

		String[] arr = { "", "", "ABC", "DEF", "GHI", "JKL", "MNO", "PQRS", "TUV", "WXYZ" };

		results.add(phoneNumber);
		for (i = 0; i < phoneNumber.length(); i++) {
			if (Character.isDigit(phoneNumber.charAt(i)) == false) {
				continue;
			} else {
				tempResults = (ArrayList<String>) results.clone();
				for (j = 0; j < tempResults.size(); j++) {
					temp = tempResults.get(j);
					System.out.println("before");
					currText = arr[Integer.parseInt(phoneNumber.charAt(i) + "")];
					System.out.println(currText);
					for (k = 0; k < currText.length(); k++) {
						temp = temp.substring(0, i) + currText.charAt(k) + temp.substring(i + 1);
						results.add(temp);
					}
				}
			}
		}
System.out.println("results : "+ results);
		DBQueries dbQueries = new DBQueries();
		id = dbQueries.insertPhoneNumber(phoneNumber);
		dbQueries.insertData(results, id);

		System.out.println("main generateCombinations method ended and phoneNumberId: " + id);
		return id;
	}
}
