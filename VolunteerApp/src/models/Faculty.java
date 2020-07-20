/**
 * 
 */
package models;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

/**
 * @author Eva Rubio
 *
 */

enum Rank {
	INSTRUCTOR,
	ASSISTANTPROFESSOR,
	ASSOCIATEPROFESSOR,
	PROFESSOR
}

public class Faculty extends Volunteer {
	
	

	/**		PK - Primary Key 	*/
	private int facultyID;
	
	private String officeBuildingName;
	private int roomOfficeNumber; 
	
	private Rank rank;
	

	/**
	 * @return the rank
	 */
	public Rank getRank() {
		return rank;
	}


	/**
	 * @param rank the rank to set
	 */
	public void setRank(Rank rank) {
		this.rank = rank;
	}


	public Faculty(String firstName, String lastName, String phoneNumber, LocalDate birthday, File imageFile,
			String password, boolean admin) throws IOException, NoSuchAlgorithmException {
		
		super(firstName, lastName, phoneNumber, birthday, imageFile, password, admin);
		// TODO Auto-generated constructor stub
		
	}

}
