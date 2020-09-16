/**
 * 
 */
package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;

/**
 * @author Eva Rubio
 `timetableID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `weekday` INT NOT NULL,
  `startTime` TIME NOT NULL,
  `endTime` TIME NOT NULL,
  `sectionID` INT NOT NULL,
  `roomID` INT NOT NULL,
  `semesterID` INT
 */
public class Timetable {
	private int timetableID;
	private int weekday;
	private Time startTime; 
	private Time endTime;
	private int sectionID;
	private int roomID;
	private int semesterID;
	
	public Timetable(int weekday, Time startTime, Time endTime, int sectionID, int roomID, int semesterID) {
		setWeekday(weekday);
		setStartTime(startTime);
		setEndTime(endTime);
		setSectionID(sectionID);
		setRoomID(roomID);
		setSemesterID(semesterID);
		
	}
	public int getTimetableID() {
		return timetableID;
	}
	public void setTimetableID(int timetableID) {
		this.timetableID = timetableID;
	}
	public int getWeekday() {
		return weekday;
	}
	public void setWeekday(int weekday) {
		this.weekday = weekday;
	}
	public Time getStartTime() {
		return startTime;
	}
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}
	public Time getEndTime() {
		return endTime;
	}
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
	public int getSectionID() {
		return sectionID;
	}
	public void setSectionID(int sectionID) {
		this.sectionID = sectionID;
	}
	public int getRoomID() {
		return roomID;
	}
	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}
	public int getSemesterID() {
		return semesterID;
	}
	public void setSemesterID(int semesterID) {
		this.semesterID = semesterID;
	}
	/**
	 * Creates/inserts the instance of the Timetable into the correct db.
	 * */
	public void insertTimetableIntoDB() throws SQLException{

		Connection conn = null;
		PreparedStatement preparedStatement = null;


		try {
			// (1) Connect to the database. (our connection)
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");
			System.out.println("Connection to the Timetable established successfully!");

			// (2) Create a String that holds the query with ? as user inputs.
			//Timetable(int weekday, Time startTime, Time endTime, int sectionID, int roomID, int semesterID)
			String sql = "INSERT INTO timetable (weekday, startTime, endTime, sectionID, roomID, semesterID)"
					+ "VALUES (?, ?, ?, ?, ?, ? )";

			// (3) Prepare the query (by sanitizing the inputs.)
			preparedStatement = conn.prepareStatement(sql);
			
			//4. Bind the values to the parameters
			preparedStatement.setInt(1, weekday);
			preparedStatement.setTime(2, startTime);
			preparedStatement.setTime(3, endTime);
			preparedStatement.setInt(4, sectionID);
			preparedStatement.setInt(5, roomID);
			preparedStatement.setInt(6, semesterID);

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
	 * Updates the Timetable in our database.
	 * @throws SQLException 
	 */
	public void updateTimetableInDB() throws SQLException {


		Connection conn = null;
		PreparedStatement preparedStatement = null;

		try{
			//1.  connect to the DB
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");

			//2.  create a String that holds our SQL update command with ? for user inputs
			//timetable (weekday, startTime, endTime, sectionID, roomID, semesterID)
			String sql = "UPDATE timetable SET weekday = ?, startTime = ?, endTime = ?, sectionID = ? roomID = ?, semesterID = ? "
					+ "WHERE timetableID = ?";

			//3. prepare the query against SQL injection
			preparedStatement = conn.prepareStatement(sql);

		
			//4. Bind the values to the parameters
			preparedStatement.setInt(1, weekday);
			preparedStatement.setTime(2, startTime);
			preparedStatement.setTime(3, endTime);
			preparedStatement.setInt(4, sectionID);
			preparedStatement.setInt(5, roomID);
			preparedStatement.setInt(6, semesterID);

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
