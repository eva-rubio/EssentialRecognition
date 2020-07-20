/**
 * 
 */
package models;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

/**
 * 
 * public class Student extends Volunteer {
 * @author Eva Rubio
 *
 */
public class Student extends Human {
	
	// listOfRegisteredCourses
	
	private boolean undergrad;
	private boolean currentlyEnrolled;
	private String majorOfStudent;
	private int numCoursesEnrolledIn;   // number of courses the student is enrolled in. 

	/**
	 * @param firstName
	 * @param lastName
	 * @param phoneNumber
	 * @param birthday
	 * @param imageFile
	 * @param password
	 * @param admin
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public Student(String firstName, String lastName, String phoneNumber, LocalDate birthday, File imageFile,
			String password, boolean admin) throws IOException, NoSuchAlgorithmException {
		
		super(firstName, lastName, phoneNumber, birthday, imageFile, password, admin);
		
		// TODO Auto-generated constructor stub
	}

}
