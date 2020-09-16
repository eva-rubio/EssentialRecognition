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
 `sectionID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `sect_name` varchar(10) NOT NULL,
  `catalogID` int NOT NULL,
  `facultyID` int NOT NULL,
  `sect_format` int DEFAULT NULL,			-- [ 1 = In-Person, 	2 = Online, 	3 = Hybrid ]
  `semesterOfferedID` int NOT NULL,
  `sect_status` int DEFAULT NULL,			-- [ 1 = Open, 	2 = Closed, 	3 = Reopened ]
  `curr_capacity` int DEFAULT 0,
  `max_capacity` int DEFAULT 0,
 */
public class Section {
	private int sectionID;
	private String sect_name;
	private int catalogID;
	private int facultyID;
	private int sect_format;
	private int semesterOfferedID;
	private int sect_status;
	private int curr_capacity;
	private int max_capacity;
	
	public Section(String sect_name, int catalogID, int facultyID, 
			int sect_format, int semesterOfferedID, int sect_status, int curr_capacity, int max_capacity) {
		setSect_name(sect_name);
		setCatalogID(catalogID);
		setFacultyID(facultyID);
		setSect_format(sect_format);
		setSemesterOfferedID(semesterOfferedID);
		setSect_status(sect_status);
		setCurr_capacity(curr_capacity);
		setMax_capacity(max_capacity);
		
	}
	
	public int getSectionID() {
		return sectionID;
	}
	public void setSectionID(int sectionID) {
		this.sectionID = sectionID;
	}
	public String getSect_name() {
		return sect_name;
	}
	public void setSect_name(String sect_name) {
		this.sect_name = sect_name;
	}
	public int getCatalogID() {
		return catalogID;
	}
	public void setCatalogID(int catalogID) {
		this.catalogID = catalogID;
	}
	public int getFacultyID() {
		return facultyID;
	}
	public void setFacultyID(int facultyID) {
		this.facultyID = facultyID;
	}
	public int getSect_format() {
		return sect_format;
	}
	public void setSect_format(int sect_format) {
		this.sect_format = sect_format;
	}
	public int getSemesterOfferedID() {
		return semesterOfferedID;
	}
	public void setSemesterOfferedID(int semesterOfferedID) {
		this.semesterOfferedID = semesterOfferedID;
	}
	public int getSect_status() {
		return sect_status;
	}
	public void setSect_status(int sect_status) {
		this.sect_status = sect_status;
	}
	public int getCurr_capacity() {
		return curr_capacity;
	}
	public void setCurr_capacity(int curr_capacity) {
		this.curr_capacity = curr_capacity;
	}
	public int getMax_capacity() {
		return max_capacity;
	}
	public void setMax_capacity(int max_capacity) {
		this.max_capacity = max_capacity;
	}
	
	/**
	 * Creates/inserts the instance of the Section into the correct db.
	 * */
	public void insertSectionIntoDB() throws SQLException{

		Connection conn = null;
		PreparedStatement preparedStatement = null;


		try {
			// (1) Connect to the database. (our connection)
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");
			System.out.println("Connection to the Section established successfully!");

			// (2) Create a String that holds the query with ? as user inputs.
			//Section( sect_name,  catalogID,  facultyID, 
				//	 sect_format, semesterOfferedID, sect_status, curr_capacity, max_capacity)
			String sql = "INSERT INTO section (sect_name, catalogID, facultyID, sect_format, semesterOfferedID, sect_status, curr_capacity, max_capacity )"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ? )";

			// (3) Prepare the query (by sanitizing the inputs.)
			preparedStatement = conn.prepareStatement(sql);
			
			//4. Bind the values to the parameters
			preparedStatement.setString(1, sect_name);
			preparedStatement.setInt(2, catalogID);
			preparedStatement.setInt(3, facultyID);
			preparedStatement.setInt(4, sect_format);
			preparedStatement.setInt(5, semesterOfferedID);
			preparedStatement.setInt(6, sect_status);
			preparedStatement.setInt(7, curr_capacity);
			preparedStatement.setInt(8, max_capacity);

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
	 * Updates the Section in our database.
	 * @throws SQLException 
	 */
	public void updateSectionInDB() throws SQLException {


		Connection conn = null;
		PreparedStatement preparedStatement = null;

		try{
			//1.  connect to the DB
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");

			//2.  create a String that holds our SQL update command with ? for user inputs
			//section (sect_name, catalogID, facultyID, sect_format, semesterOfferedID, sect_status, curr_capacity, max_capacity )
			String sql = "UPDATE section SET sect_name = ?, catalogID = ?, facultyID = ?, sect_format = ?, semesterOfferedID = ?, sect_status = ?, curr_capacity = ?, max_capacity = ? "
					+ "WHERE sectionID = ?";

			//3. prepare the query against SQL injection
			preparedStatement = conn.prepareStatement(sql);

		
			//4. Bind the values to the parameters
			preparedStatement.setString(1, sect_name);
			preparedStatement.setInt(2, catalogID);
			preparedStatement.setInt(3, facultyID);
			preparedStatement.setInt(4, sect_format);
			preparedStatement.setInt(5, semesterOfferedID);
			preparedStatement.setInt(6, sect_status);
			preparedStatement.setInt(7, curr_capacity);
			preparedStatement.setInt(8, max_capacity);

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
