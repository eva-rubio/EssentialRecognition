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

		Course planning

		Time calculation


 * @author Eva Rubio
 *
 */
public class Course {


	private int c_ID;
	private String course_code;
	private String title;
	private LocalDate startDate;
	private LocalDate endDate;
	private String instructor_name;
	private String status;
	private String semester;
	private int curr_capacity;
	private int max_capacity;
	
	
	private boolean coreRequirement;
	private int creditHours;
	private int id_school;
	
	

	public Course( String course_code, String title, LocalDate startDate, LocalDate endDate,
			String instructor_name, String status, String semester, int curr_capacity, int max_capacity, boolean coreRequirement, int creditHours, int id_school) {
		this.course_code = course_code;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.instructor_name = instructor_name;
		this.status = status;
		this.semester = semester;
		this.curr_capacity = curr_capacity;
		this.max_capacity = max_capacity;
		this.coreRequirement = coreRequirement;
		this.creditHours = creditHours;
		this.id_school = id_school;
	}

	


	/**		PK - Primary Key 	*/
	/*
	 * private int courseID; private String schoolDepartment; private String
	 * courseDescription; private int courseSection; // 1-4 hours private int
	 * creditHours; private Classroom classroom; private Semester semester;
	 * 
	 * private LocalDate meetingTimes; private Faculty instructor;
	 */

	//	
	//	public Course(int courseID, String courseTitle, String courseDescription, int courseSection, int creditHours,
	//			int enrollmentCapacity, Classroom classroom, Semester semester, LocalDate startDate, LocalDate endDate,
	//			LocalDate meetingTimes, Faculty instructor) {
	//		this.courseID = courseID;
	//		this.courseTitle = courseTitle;
	//		this.courseDescription = courseDescription;
	//		this.courseSection = courseSection;
	//		this.creditHours = creditHours;
	//		this.enrollmentCapacity = enrollmentCapacity;
	//		this.classroom = classroom;
	//		this.semester = semester;
	//		this.startDate = startDate;
	//		this.endDate = endDate;
	//		this.meetingTimes = meetingTimes;
	//		this.instructor = instructor;
	//	}


	/*		TODO 
	 * 
	 * 	addStudent(Student)
	 * removeStudent(Student)
	 * 
	 * */


	/**
	 * @return the c_ID
	 */
	public int getC_ID() {
		return c_ID;
	}

	/**
	 * @param c_ID the c_ID to set
	 */
	public void setC_ID(int c_ID) {
		this.c_ID = c_ID;
	}

	/**
	 * @return the course_code
	 */
	public String getCourse_code() {
		return course_code;
	}

	/**
	 * @param course_code the course_code to set
	 */
	public void setCourse_code(String course_code) {
		this.course_code = course_code;
	}

	/**
	 * @return the instruct_name
	 */
	public String getInstructor_name() {
		return instructor_name;
	}

	/**
	 * @param instruct_name the instruct_name to set
	 */
	public void setInstructor_name(String instruct_name) {
		this.instructor_name = instruct_name;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the semest
	 */
	public String getSemester() {
		return semester;
	}

	/**
	 * @param semest the semest to set
	 */
	public void setSemester(String semest) {
		this.semester = semest;
	}

	/**
	 * @return the curr_capacity
	 */
	public int getCurr_capacity() {
		return curr_capacity;
	}

	/**
	 * @param curr_capacity the curr_capacity to set
	 */
	public void setCurr_capacity(int curr_capacity) {
		this.curr_capacity = curr_capacity;
	}

	/**
	 * @return the max_capacity
	 */
	public int getMax_capacity() {
		return max_capacity;
	}

	/**
	 * @param max_capacity the max_capacity to set
	 */
	public void setMax_capacity(int max_capacity) {
		this.max_capacity = max_capacity;
	}
	/**
	 * @return the courseTitle
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param courseTitle the courseTitle to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}




	/**
	 * @return the startDate
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
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

	public int getId_school() {
		return id_school;
	}




	public void setId_school(int id_school) {
		this.id_school = id_school;
	}




	/**
	 * Creates/insterst the instance of the Course into the correct db.
	 * */
	public void insertCourseIntoDB() throws SQLException{

		Connection conn = null;
		PreparedStatement preparedStatement = null;


		try {
			// (1) Connect to the database. (our connection)
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");
			System.out.println("Connection to courses established successfully!");

			// (2) Create a String that holds the query with ? as user inputs.
			String sql = "INSERT INTO courses (course_code, title, startDate, endDate, instructor_name, status, semester, curr_capacity, max_capacity, coreRequirement, creditHours)"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			// (3) Prepare the query (by sanitizing the inputs.)
			preparedStatement = conn.prepareStatement(sql);

			// (4) Convert the birthday into a SQL date
			Date strt = Date.valueOf(startDate);
			Date endd = Date.valueOf(endDate);

			//5. Bind the values to the parameters
			preparedStatement.setString(1, course_code);
			preparedStatement.setString(2, title);
			preparedStatement.setDate(3, strt);
			preparedStatement.setDate(4, endd);
			preparedStatement.setString(5, instructor_name);
			preparedStatement.setString(6, status);
			preparedStatement.setString(7, semester);
			preparedStatement.setInt(8, curr_capacity);
			preparedStatement.setInt(9, max_capacity);
			preparedStatement.setBoolean(10, coreRequirement);
			preparedStatement.setInt(11, creditHours);

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








}

/* REFERENCES
 * 
 * https://stackoverflow.com/questions/47181969/javafx-tableview-propertyvaluefactory-error
 * 
 * 
 * */
