/**
 * 
 */
package student;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

import models.Human;

/**
 * 
 * public class Student extends Volunteer {
 * @author Eva Rubio
 *
 */
public class Student extends Human {

	// listOfRegisteredCourses

	//private boolean undergrad;
	
	private int studentID;
	private int stMascotID; 

	private int stHumanID; 
	private int majorID;
	private boolean isCurrentEnrolled;
	private double currRegCredHrs;		// number of hours of credits registered for THIS current semester.
	private double career_total_earnedCreds;
	private String st_type;
	private double term_GPA;
	private double career_total_GPA;
	private int facultyAdvisorID;

	

	

	public Student(int personType, String firstName, String lastName, String phoneNumber, LocalDate dob,
			String password, String email, int gender, int addressID, int mascotID, int stMascotID, int stHumanID,
			int majorID, boolean isCurrentEnrolled, double currRegCredHrs, double career_total_earnedCreds,
			String st_type, double term_GPA, double career_total_GPA, int facultyAdvisorID)
			throws NoSuchAlgorithmException {
		super(personType, firstName, lastName, phoneNumber, dob, password, email, gender, addressID, mascotID);
		this.stMascotID = stMascotID;
		this.stHumanID = stHumanID;
		this.majorID = majorID;
		this.isCurrentEnrolled = isCurrentEnrolled;
		this.currRegCredHrs = currRegCredHrs;
		this.career_total_earnedCreds = career_total_earnedCreds;
		this.st_type = st_type;
		this.term_GPA = term_GPA;
		this.career_total_GPA = career_total_GPA;
		this.facultyAdvisorID = facultyAdvisorID;
	}

	public double getCurrRegCredHrs() {
		return currRegCredHrs;
	}

	public void setCurrRegCredHrs(double currRegCredHrs) {
		if( currRegCredHrs <= 18) {
			this.currRegCredHrs = currRegCredHrs;
		} else {
			throw new IllegalArgumentException("Cannot register for more than 18 credit hours per semester.");
		}

	}

	/**
	 * A college career for an undergraduate student is usually divided by 4 academic standings known as:
	 *  freshman, sophomore, junior and senior. 
	 *  
	 *  The following numbers of earned credits determine the student’s class designation.
	 * */
	public String getStudentClassStanding() {
		if(career_total_earnedCreds <= 29.9) {
			return "Freshman";
		} else if(career_total_earnedCreds <= 59.9) {
			return "Sophomore";
		} else if(career_total_earnedCreds <= 89.9) {
			return "Junior";
		} else if(career_total_earnedCreds >= 90) {
			return "Senior";
		} else {
			return "";
		}

	}


	public int getStudentID() {
		return studentID;
	}

	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}

	public int getStMascotID() {
		return stMascotID;
	}

	public void setStMascotID(int stMascotID) {
		this.stMascotID = stMascotID;
	}

	public int getStHumanID() {
		return stHumanID;
	}

	public void setStHumanID(int stHumanID) {
		this.stHumanID = stHumanID;
	}

	public int getMajorID() {
		return majorID;
	}

	public void setMajorID(int majorID) {
		this.majorID = majorID;
	}

	public boolean isCurrentEnrolled() {
		return isCurrentEnrolled;
	}

	public void setCurrentEnrolled(boolean isCurrentEnrolled) {
		this.isCurrentEnrolled = isCurrentEnrolled;
	}

	public double getCareer_total_earnedCreds() {
		return career_total_earnedCreds;
	}

	public void setCareer_total_earnedCreds(double career_total_earnedCreds) {
		this.career_total_earnedCreds = career_total_earnedCreds;
	}

	public String getSt_type() {
		return st_type;
	}

	public void setSt_type(String st_type) {
		if(currRegCredHrs >= 12) {
			this.st_type = "F";
		} else if(currRegCredHrs == 0){
			this.st_type = "N";
		} else {
			this.st_type = "P";
		}
		
	}

	public double getTerm_GPA() {
		return term_GPA;
	}

	public void setTerm_GPA(double term_GPA) {
		this.term_GPA = term_GPA;
	}

	public double getCareer_total_GPA() {
		return career_total_GPA;
	}

	public void setCareer_total_GPA(double career_total_GPA) {
		this.career_total_GPA = career_total_GPA;
	}

	public int getFacultyAdvisorID() {
		return facultyAdvisorID;
	}

	public void setFacultyAdvisorID(int facultyAdvisorID) {
		this.facultyAdvisorID = facultyAdvisorID;
	}


}
