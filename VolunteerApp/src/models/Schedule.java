/**
 * 
 */
package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;

/**
 * @author Eva Rubio
 *
 */


public class Schedule {
	
	
	/**	
  `scheduleID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `startTime` TIME NOT NULL,
  `endTime` TIME NOT NULL,
  `dayLetter`
  
  */
	
	private int scheduleID;
	private Time startTime; 
	private Time endTime; 
	private String dayLetter;
	
	public Schedule(Time startTime, Time endTime, String dayLetter) {
		setStartTime(startTime);
		setEndTime(endTime);
		setDayLetter(dayLetter);
	}

	
	public int getScheduleID() {
		return scheduleID;
	}
	public void setScheduleID(int scheduleID) {
		this.scheduleID = scheduleID;
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
	public String getDayLetter() {
		return dayLetter;
	}
	public void setDayLetter(String dayLetter) {
		this.dayLetter = dayLetter;
	}
	
	/**
	 * Creates/inserts the instance of the Schedule into the correct db.
	 * */
	public void insertScheduleIntoDB() throws SQLException{

		Connection conn = null;
		PreparedStatement preparedStatement = null;


		try {
			// (1) Connect to the database. (our connection)
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");
			System.out.println("Connection to the schedule established successfully!");

			// (2) Create a String that holds the query with ? as user inputs.
			String sql = "INSERT INTO schedule (startTime, endTime, dayLetter)"
					+ "VALUES (?, ?, ? )";

			// (3) Prepare the query (by sanitizing the inputs.)
			preparedStatement = conn.prepareStatement(sql);
			
			//4. Bind the values to the parameters
			preparedStatement.setTime(1, startTime);
			preparedStatement.setTime(2, endTime);
			preparedStatement.setString(3, dayLetter);

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
	 * Updates the Schedule in our database.
	 * @throws SQLException 
	 */
	public void updateScheduleInDB() throws SQLException {


		Connection conn = null;
		PreparedStatement preparedStatement = null;

		try{
			//1.  connect to the DB
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");

			//2.  create a String that holds our SQL update command with ? for user inputs
			//schedule (scheduleID, startTime, endTime, dayLetter)
			String sql = "UPDATE schedule SET startTime = ?, endTime = ?, dayLetter = ? "
					+ "WHERE scheduleID = ?";

			//3. prepare the query against SQL injection
			preparedStatement = conn.prepareStatement(sql);

		
			//4. Bind the values to the parameters
			preparedStatement.setTime(1, startTime);
			preparedStatement.setTime(2, endTime);
			preparedStatement.setString(3, dayLetter);

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
