/**
 * 
 */
package faculty;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

import models.Human;

/**
 * @author Eva Rubio
 *
 */
/*
 * enum Rank { INSTRUCTOR, ASSISTANTPROFESSOR, ASSOCIATEPROFESSOR, PROFESSOR }
 */

public class Faculty extends Human {
	
	

	/**		PK - Primary Key 	*/
	private int facultyID;
	private int facMascotID; 
	private int facHumanID; 
	private String rank;
	private int officeID; 
	private int facSchoolID; 
 
	

	

	public Faculty(int personType, String firstName, String lastName, String phoneNumber, LocalDate dob,
			String password, String email, int gender, int addressID, int facHumanID,
			String rank, int officeID, int facSchoolID) throws NoSuchAlgorithmException {
		super(personType, firstName, lastName, phoneNumber, dob, password, email, gender, addressID);
		this.facHumanID = facHumanID;
		this.rank = rank;
		this.officeID = officeID;
		this.facSchoolID = facSchoolID;
	}



	/*
	 * 
	 * public Faculty(int personType, String firstName, String lastName, String
	 * phoneNumber, LocalDate dob, String password, String email, int gender, int
	 * addressID, int mascotID, int mascotID2, int humanID, String rank, int
	 * officeID, int schoolID) throws NoSuchAlgorithmException { super(personType,
	 * firstName, lastName, phoneNumber, dob, password, email, gender, addressID,
	 * mascotID); mascotID = mascotID2; this.humanID = humanID; this.rank = rank;
	 * this.officeID = officeID; this.schoolID = schoolID; }
	 * 
	 */
	


	public int getFacultyID() {
		return facultyID;
	}

	public void setFacultyID(int facultyID) {
		this.facultyID = facultyID;
	}

	public int getFacMascotID() {
		return facMascotID;
	}
	public void setFacMascotID(int facMascotID) {
		this.facMascotID = facMascotID;
	}

	public int getFacHumanID() {
		return facHumanID;
	}

	public void setFacHumanID(int facHumanID) {
		this.facHumanID = facHumanID;
	}

	public int getOfficeID() {
		return officeID;
	}

	public void setOfficeID(int officeID) {
		this.officeID = officeID;
	}

	public int getFacSchoolID() {
		return facSchoolID;
	}


	public void setFacSchoolID(int facSchoolID) {
		this.facSchoolID = facSchoolID;
	}

	public String getRank() {
		return rank;
	}


	public void setRank(String rank) {
		this.rank = rank;
	}



}
