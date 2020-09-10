/**
 * 
 */
package models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * 

		Adding courses

		Registering teacher

		Section registration

		Class registration

		User registration

		View all Information

		Catalog planning

		Time calculation


 * @author Eva Rubio
 *
 */
public class Catalog {


	private int catalogID;
	private String c_code;
	private String c_title;
	private int creditHours;
	private boolean coreRequirement;
	private String c_description;
	private int schoolID;

	
	

	public Catalog(String c_code, String c_title, int creditHours, boolean coreRequirement, String c_description,  int schoolID) {
		setC_code(c_code);
		setC_title(c_title);
		setCreditHours(creditHours);
		setCoreRequirement(coreRequirement);
		setC_description(c_description);
		setSchoolID(schoolID);
	}

	


	/**
	 * @param schoolID
	 */
	private void setSchoolID(int schoolID) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param c_description
	 */
	private void setC_description(String c_description) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * @param c_title
	 */
	private void setC_title(String c_title) {
		this.c_title = c_title;
	}

	/**
	 * @return the catalogID
	 */
	public int getC_ID() {
		return catalogID;
	}

	/**
	 * @param catalogID the catalogID to set
	 */
	public void setCatalogID(int catalogID) {
		this.catalogID = catalogID;
	}

	/**
	 * @return the catalog_code
	 */
	public String getC_code() {
		return c_code;
	}

	/**
	 * @param catalog_code the catalog_code to set
	 */
	public void setC_code(String c_code) {
		this.c_code = c_code;
	}
	public boolean isCoreRequirement() {
		return coreRequirement;
	}
	public void setCoreRequirement(boolean coreRequirement) {
		this.coreRequirement = coreRequirement;
	}

	public int getCreditHours() {
		return creditHours;
	}

	public void setCreditHours(int creditHours) {
		this.creditHours = creditHours;
	}

	/**
	 * Creates/inserts the instance of the Catalog into the correct db.
	 * */
	public void insertCatalogIntoDB() throws SQLException{

		Connection conn = null;
		PreparedStatement preparedStatement = null;


		try {
			// (1) Connect to the database. (our connection)
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");
			System.out.println("Connection to the catalog established successfully!");

			// (2) Create a String that holds the query with ? as user inputs.
			String sql = "INSERT INTO catalog (c_code, c_title, creditHours, coreRequirement, c_description, schoolID)"
					+ "VALUES (?, ?, ?, ?, ?, ? )";

			// (3) Prepare the query (by sanitizing the inputs.)
			preparedStatement = conn.prepareStatement(sql);
			// (4) Convert the birthday into a SQL date
			//5. Bind the values to the parameters
			preparedStatement.setString(1, c_code);
			preparedStatement.setString(2, c_title);
			preparedStatement.setDouble(3, creditHours);
			preparedStatement.setBoolean(4, coreRequirement);
			preparedStatement.setString(5, c_description);
			preparedStatement.setInt(6, schoolID);

			preparedStatement.executeUpdate();


		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}
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
	 * Updates the Catalog in our database.
	 * @throws SQLException 
	 */
	public void updateCatalogInDB() throws SQLException {


		Connection conn = null;
		PreparedStatement preparedStatement = null;

		try{
			//1.  connect to the DB
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");

			//2.  create a String that holds our SQL update command with ? for user inputs
			String sql = "UPDATE catalog SET c_code = ?, c_title = ?, "
			+"creditHours=?, coreRequirement = ?, c_description = ?, schoolID = ? "
					+ "WHERE catalogID = ?";

			//3. prepare the query against SQL injection
			preparedStatement = conn.prepareStatement(sql);

		
			//4. bind the parameters
			preparedStatement.setString(1, c_code);
			preparedStatement.setString(2, c_title);
			preparedStatement.setDouble(3, creditHours);
			preparedStatement.setBoolean(4, coreRequirement);
			preparedStatement.setString(5, c_description);
			preparedStatement.setInt(6, schoolID);

			//5. run the command on the SQL server
			preparedStatement.executeUpdate();


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

/* REFERENCES
 * 
 * https://stackoverflow.com/questions/47181969/javafx-tableview-propertyvaluefactory-error
 * 
 * 
 * */
