/**
 * 
 */
package models;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.HashMap;


enum job{
	
}
/**
 * 
 * Registrars Office 
 * 
 * 
 Student Forms:
 Admin's job to complete them and make them happen.
 
		Add/Drop Course 
		Change of Address/Name
		Change of Major/Concentration
		Change of Enrollment Status
		Course Withdrawal

 * @author Eva Rubio
 *
 */
public class Admin extends Volunteer {
	
	
	


	public Admin(String firstName, String lastName, String phoneNumber, LocalDate birthday, File imageFile,
			String password, boolean admin) throws IOException, NoSuchAlgorithmException {
		super(firstName, lastName, phoneNumber, birthday, imageFile, password, admin);
		// TODO Auto-generated constructor stub
	}


    
}
