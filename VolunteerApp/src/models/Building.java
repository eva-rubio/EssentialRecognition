/**
 * 
 */
package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Eva Rubio
 *
 */
/**
 * @author Eva Rubio
 *
 */
public class Building {
	
	
	// from 200 thru 212
	private int buildingID;
	private String buildingName;
	private int b_type;		//	[1= Academic, 2= Admin, 3= Recreational, 4= Mix]
	
	
	
	
	
	public Building(int buildingID, String buildingName, int b_type) {
		this.buildingID = buildingID;
		this.buildingName = buildingName;
		this.b_type = b_type;
	}
	public int getBuildingID() {
		return buildingID;
	}
	public void setBuildingID(int buildingID) {
		this.buildingID = buildingID;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public int getB_type() {
		return b_type;
	}
	public String getB_typeStr() {
		return Integer.toString(b_type);
	}
	public void setB_type(int b_type) {
		if((b_type >= 1)&&(b_type <= 4)) {
			this.b_type = b_type;
		}else {
			throw new IllegalArgumentException("Building type must be one of the following: 1 (Academic), 2 (Admin), 3 (Recreational), 4(Mix)");
		}
		
	}
	


	/**
	 * Writes the instance of a Building into the database.
	 * Lets a human person to write themselves to the database
	 * @throws SQLException 
	 * 
	 * */
	public void insertBuildingIntoDB() throws SQLException {

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
			// INSERT INTO `building` (`buildingID`, `buildingName`, `b_type`) VALUES ('d', 'd', 'd');
			String sql = "INSERT INTO building (buildingID, buildingName, b_type)"
					+ "VALUES (?, ?, ? )";

			// (3) Prepare the query (by sanitizing the inputs.)
			preparedStatement = conn.prepareStatement(sql);
		

			// (4) Bind the values to the parameters
			preparedStatement.setInt(1, buildingID);
			preparedStatement.setString(2, buildingName);
			preparedStatement.setInt(3, b_type);

			preparedStatement.executeUpdate();


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
	 * Updates a Building record in our database.
	 * Allows us to modify entries in the database.
	 * @throws SQLException 
	 */
	public void updateBuildingInDB() throws SQLException {

		Connection conn = null;
		PreparedStatement preparedStatement = null;

		try{
			//1.  connect to the DB
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");

			//2.  create a String that holds our SQL update command with ? for user inputs
			String sql = "UPDATE building SET buildingName = ?, b_type = ? "
					+ "WHERE buildingID = ?";

			//3. prepare the query against SQL injection
			preparedStatement = conn.prepareStatement(sql);


			//4. bind the parameters
			preparedStatement.setString(1, buildingName);
			preparedStatement.setInt(2, b_type);
			preparedStatement.setInt(3, buildingID);
			
			//5. Execute the UPDATE command on the SQL server.
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
	
	@Override
    public String toString() {
        return  buildingID + " - " + buildingName + " [" + b_type + "]";
    }

}
