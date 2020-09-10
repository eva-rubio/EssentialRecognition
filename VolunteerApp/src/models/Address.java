/**
 * 
 */
package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * https://www.tutorialspoint.com/How-can-we-add-a-FOREIGN-KEY-constraint-to-the-field-of-an-existing-MySQL-table
 * 
 * public String getAddressIDString() {
		return Integer.toString(addressID);
	}
 * @author Eva Rubio
 *
 */
public class Address {

	/**		PK - Primary Key 	*/
	private int addressID;
	private String street;
	private String city;
	private String state;
	private String zipCode;
	private String country;
	private boolean onCampusHousing;



	public Address(String street, String city, String state, String zipCode, String country, boolean onCampusHousing) {
		setStreet(street);
		setCity(city);
		setState(state);
		setZipCode(zipCode);
		setCountry(country);
		setOnCampusHousing(onCampusHousing);
	}


	/*
	 * public Address(int addressID, String street, String city, String state,
	 * String zipCode, String country, boolean onCampusHousing) {
	 * setAddressID(addressID); setStreet(street); setCity(city); setState(state);
	 * setZipCode(zipCode); setCountry(country);
	 * setOnCampusHousing(onCampusHousing); }
	 */

	public int getAddressID() {
		return addressID;
	}

	public void setAddressID(int addressID) {
		this.addressID = addressID;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		if(isAllLetters(state)) {
			this.state = state;
		} else {
			throw new IllegalArgumentException("State must be 2 letters.");
		}
	}

	public String getZipCode() {
		return zipCode;
	}


	/**		US ZIP code (U.S. postal code) allow both the five-digit and nine-digit (called ZIP + 4) formats. 
	 * 
	 * 
	 * 5 digits optionally followed by 4 more digits.
	 * 
	 * 		[0-9]{5}([0-9]{4})?
	 * 
	 * US ZIP code (U.S. postal code) allow both the five-digit and nine-digit (called ZIP + 4) formats. 
	 * 	 * */
	public void setZipCode(String zipCode) {

		if(zipCode.matches("[0-9]{5}([0-9]{4})?")) {
			if((country.equalsIgnoreCase("US") || country.equalsIgnoreCase("USA") ) && zipCodeUSvalid(zipCode)) {
				this.zipCode = zipCode;
			} else {
				throw new IllegalArgumentException("US Zip codes must be between 5 and 9 digits long, inclusive.");
			}
		} else {
			throw new IllegalArgumentException("Zip codes must be either the five-digit and/or nine-digit (called ZIP + 4) formats.");
		}

	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean isOnCampusHousing() {
		return onCampusHousing;
	}

	public void setOnCampusHousing(boolean onCampusHousing) {
		this.onCampusHousing = onCampusHousing;
	}



	/**
	 * validate the united states 5 and 9 digit zip codes.
	 * Regular Expression to match the US Zip-Code.
	 * 		^d{5}(-d{4})?$
	 * 		^[0-9]{5}(-[0-9]{4})?$
	 * 		^[0-9]{5}(?:-[0-9]{4})?$
	 *  First five letters should be digits.
	 *  It can be followed by a dash and optional 4 digits
	 *  
	 *  https://www.youtube.com/watch?v=TsiM3qmVCAA
	 *  https://www.oreilly.com/library/view/regular-expressions-cookbook/9781449327453/ch03s07.html		
	 */
	//static final String regex = "^\d{5}(-\d{4})?$";

	public static boolean zipCodeUSvalid(String postCode) {

		String regex = "^[0-9]{5}(?:-[0-9]{4})?$";
		// object that just holds our pattern
		Pattern patternObj = Pattern.compile(regex);

		Matcher regexMatcher = patternObj.matcher(postCode);

		boolean valResult = regexMatcher.find();
		return valResult;
	}


	public static boolean isAllLetters(String nameToCheck) {

		return Pattern.matches("[a-zA-Z]+", nameToCheck);	
	}
	
	/**
	 * Writes the instance of an Address into the database.
	 * Lets a humanperson to write themselves to the database
	 * @throws SQLException 
	 * 
	 * */
	public void insertAddressIntoDB() throws SQLException {

		Connection conn = null;
		/*
		 * PreparedStatement
		 * To make sure no one can do a SQL injection attack, we use a PreparedStatement to sanitize our inputs
		 * before we try to use them. 
		 *  */
		PreparedStatement preparedStatement = null;

		try {
			// (1) Connect to the database. (our connection)
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");
			System.out.println("Connection established successfully!");

			// (2) Create a String that holds the query with ? as user inputs.
			String sql = "INSERT INTO addresslist (street, city, state, zipCode, country, onCampusHousing)"
					+ "VALUES (?, ?, ?, ?, ?, ? )";

			// (3) Prepare the query (by sanitizing the inputs.)
			preparedStatement = conn.prepareStatement(sql);



			// (4) Bind the values to the parameters
			preparedStatement.setString(1, street);
			preparedStatement.setString(2, city);
			preparedStatement.setString(3, state);
			preparedStatement.setString(4, zipCode);
			preparedStatement.setString(5, country);
			preparedStatement.setBoolean(6, onCampusHousing);


			preparedStatement.executeUpdate();



			/*INSERT INTO addresslist(firstName, lastName, phoneNumber, birthday, imageFile) VALUES
('Fred', 'Flinstone', '651-555-1234', '2002-01-02', 'some file');*/

		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}

		/*
		 * finally block - 
		 * 	to make sure that our database connection is closed.
		 * It would be a security leak to keep it open.*/  
		finally {
			//close the preparedStatement
			if(preparedStatement != null) {
				preparedStatement.close();
			}
			//close the connection
			if(conn != null) {
				conn.close();
			}

		}
	}
	
	

	/**
	 * Updates the Address in our database.
	 * @throws SQLException 
	 */
	public void updateAddressInDB() throws SQLException {


		Connection conn = null;
		PreparedStatement preparedStatement = null;

		try{
			//1.  connect to the DB
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");

			//2.  create a String that holds our SQL update command with ? for user inputs
			String sql = "UPDATE addresslist SET street = ?, city = ?, "
			+"state=?, zipCode = ?, country = ?, onCampusHousing = ? "
					+ "WHERE addressID = ?";

			//3. prepare the query against SQL injection
			preparedStatement = conn.prepareStatement(sql);

		
			//4. bind the parameters
			preparedStatement.setString(1, street);
			preparedStatement.setString(2, city);
			preparedStatement.setString(3, state);
			preparedStatement.setString(4, zipCode);
			preparedStatement.setString(5, country);
			preparedStatement.setBoolean(6, onCampusHousing);
			preparedStatement.setInt(7, addressID);

			//5. run the command on the SQL server
			preparedStatement.executeUpdate();
			preparedStatement.close();


		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}

		finally {
			if (conn != null) {
				conn.close();
			}
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		}

	}


}
