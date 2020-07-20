/**
 * 
 */
package models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Period;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 
 * https://www.youtube.com/watch?v=_ySyOq-Hnpw
 * https://www.youtube.com/watch?v=jSfMTVk-8ys&list=PLoodc-fmtJNZuldAE53bGuOwJ6BhUG7mA&index=4
 * 
 * 
 * @author Eva Rubio
 *
 */
//
//enum Gender {
//	FEMALE,
//	MALE,
//	THEY,
//	NA
//}
//
//enum HumanCategory {
//	STUDENT,
//	INSTRUCTOR,
//	ADMIN
//}
public class Volunteer {

	/**		PK - Primary Key 	*/
	private int volunteerID;

	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String password;
	private LocalDate birthday;

	private File imageFile;

	private byte[] salt;

	private boolean admin;

	private Address address;

	private Gender gender;
	private PersonGroupType humanCategory;
	private String emailContact;

	/**
	 * The DEFAULT Volunteer Constructor - Sets default image. 
	 * 
	 * 
	 * @throws NoSuchAlgorithmException 
	 */
	public Volunteer(String firstName, String lastName, String phoneNumber, LocalDate birthday, String password, boolean admin) throws NoSuchAlgorithmException {
		setFirstName(firstName);
		setLastName(lastName);
		setPhoneNumber(phoneNumber);
		setBirthday(birthday);
		setImageFile(new File("./src/images/defaultPerson.png"));
		salt = PasswordGenerator.getSalt();
		this.password = PasswordGenerator.getSHA512Password(password, salt);
		this.setAdmin(admin);
	}

	public Volunteer(String firstName, String lastName, String phoneNumber, LocalDate birthday, File imageFile, String password, boolean admin) throws IOException, NoSuchAlgorithmException {
		this(firstName, lastName, phoneNumber, birthday, password, admin);
		setImageFile(imageFile);
		copyImageFile();
	}


	
	
	
	public String getEmailContact() {
		return emailContact;
	}



	//https://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method
	public boolean isValidEmailAddress(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}
	public void setEmailContact(String emailContact) {
		if(isValidEmailAddress(emailContact)) {
			this.emailContact = emailContact;
		} else {
			throw new IllegalArgumentException("Invalid email format.");
		}

	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * validate before setting.
	 * 
	 * area code    city    house 
	 * NXX          -XXX    -XXXX
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		if (phoneNumber.matches("[2-9]\\d{2}[-.]?\\d{3}[-.]\\d{4}"))
			this.phoneNumber = phoneNumber;
		else
			throw new IllegalArgumentException("Phone numbers must be in the pattern NXX-XXX-XXXX");
	}
	/**
	 * @return the birthday
	 */
	public LocalDate getBirthday() {
		return birthday;
	}
	/**
	 * validate that the volunteer is between the ages of 10 and 100.
	 * 
	 * @param birthday the birthday to set
	 */
	public void setBirthday(LocalDate birthday) {
		int age = Period.between(birthday, LocalDate.now()).getYears();

		if (age >= 10 && age <= 100) {
			this.birthday = birthday;
		} else {
			throw new IllegalArgumentException("Volunteers must be 10-100 years of age.");
		}
	}
	public String getPassword() {
		return password;
	}

	public byte[] getSalt() {
		return salt;
	}
	/**
	 * @return the admin
	 */
	public boolean isAdmin() {
		return admin;
	}

	/**
	 * @param admin the admin to set
	 */
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	/**
	 * @return the imageFile
	 */
	public File getImageFile() {
		return imageFile;
	}
	/**
	 * Receives a 'File', but it is NOT the actual file
	 * @param imageFile the imageFile to set
	 */
	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;
	}
	/**
	 * Copies the photo ("uploads it") into the server that is actually hosting the application. (local server for me).
	 * Copies the file specified to the 'images' directory on this server and gives it a unique name.
	 * 
	 * We do not want to override our default image. 
	 * 		we do not want to create many instances of the same image.
	 * 
	 * It takes whatever our current image is, and moves it. 
	 */
	public void copyImageFile() throws IOException {

		//create a new Path to copy the image into a local directory
		Path sourcePath = imageFile.toPath();

		String uniqueFileName = getUniqueFileName(imageFile.getName());

		//here is where we are going to copy the file at. 
		Path targetPath = Paths.get("./src/images/" + uniqueFileName);

		//copy the file to the new directory
		Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

		//update the imageFile to point to the new File
		imageFile = new File(targetPath.toString());
	}

	/**
	 * @return
	 */
	public int getVolunteerID() {
		return volunteerID;
	}

	/**
	 * Set the Volunteer ID number. Making sure it is a positive value. 
	 * @param volunteerID
	 */
	public void setVolunteerID(int volunteerID) {
		if (volunteerID >= 0) {
			this.volunteerID = volunteerID;
		} else {
			throw new IllegalArgumentException("VolunteerID must be >= 0");
		}
	}

	/**
	 * Receives a String that represents a fileName and returns it with a random, unique set of letters prefixed to it.
	 * 
	 * We want to keep the original fileName intact, but with a guaranteed UNIQUE Sting in front of it.
	 * 
	 * 	(1) create a new string, a new name for the file.
	 * 	(2) checks if it is unique in our 'images' directory.
	 * 	(3) if yes, we stop looping. 
	 * 		if not, we start from scratch & re-do it again.
	 * 
	 * @param oldFileName 	the fileName to modify.
	 * @return the oldFileName with a random, unique set of letters prefixed to it.
	 * 
	 */
	private String getUniqueFileName(String oldFileName) {
		String newName;

		/* create a Random Number Generator */
		SecureRandom rng = new SecureRandom();

		//loop until we have a UNIQUE fileName.
		do {
			newName = "";

			//generate 32 random characters to add at start of our fileName
			for (int count=1; count <=32; count++) {
				int nextChar;

				//loop until we find a valid character
				do {
					//ascii compliant?
					nextChar = rng.nextInt(123);
				} while(!validCharacterValue(nextChar));

				newName = String.format("%s%c", newName, nextChar);
			}
			//when valid found, append it to start of given fileName.
			newName += oldFileName;

		} while (!uniqueFileInDirectory(newName));
		//if name is unique, we are done,. 

		return newName;
	}

	/**
	 * Searches the images directory and ensure that the file name is unique.
	 * Checks our directory to make sure that our new name is unique.
	 * 	
	 * @param the fileName to check is it is unique
	 * @return true if fileName is unique or if it already exists in the directory. 
	 */
	public boolean uniqueFileInDirectory(String fileName) {
		// we pass in where our 'images' directory is.
		File directory = new File("./src/images/");

		// we look in the directory and get an array of the files inside the directory.
		// it is an array of File objects. 
		File[] dir_contents = directory.listFiles();

		//loop to see if any of the files has the same name
		for (File file: dir_contents) {
			if (file.getName().equals(fileName))
				return false;
		}
		return true;
	}

	/**
	 * Validates if the integer given corresponds to a valid
	 * ASCII character that could be used in a fileName.
	 */
	public boolean validCharacterValue(int asciiValue) {

		//0-9 = ASCII range 48 to 57
		if (asciiValue >= 48 && asciiValue <= 57)
			return true;

		//A-Z = ASCII range 65 to 90
		if (asciiValue >= 65 && asciiValue <= 90)
			return true;

		//a-z = ASCII range 97 to 122
		if (asciiValue >= 97 && asciiValue <= 122)
			return true;

		return false;
	}


	/**
	 * This method will return a formatted String with the persons' first name, last name and age
	 */
	public String toString() {
		return String.format("%s %s is %d years old", firstName, lastName, Period.between(birthday, LocalDate.now()).getYears());
	}




	/**
	 * Writes the instance of the Volunteer into the database.
	 * Lets a volunteer to write themselves to the database
	 * @throws SQLException 
	 * 
	 * */
	public void insertIntoDB() throws SQLException {

		Connection conn = null;
		/*
		 * to make sure no one can do a SQL injection attack, we use a PreparedStatement to satitize our inputs
		 * before we try to use them. 
		 *  */
		PreparedStatement preparedStatement = null;

		try {
			// (1) Connect to the database. (our connection)
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");
			System.out.println("Connection established successfully!");

			// (2) Create a String that holds the query with ? as user inputs.
			String sql = "INSERT INTO volunteers (firstName, lastName, phoneNumber, birthday, imageFile, password, salt, admin)"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

			// (3) Prepare the query (by sanitizing the inputs.)
			preparedStatement = conn.prepareStatement(sql);

			// (4) Convert the birthday into a SQL date
			Date db = Date.valueOf(birthday);

			//5. Bind the values to the parameters
			preparedStatement.setString(1, firstName);
			preparedStatement.setString(2, lastName);
			preparedStatement.setString(3, phoneNumber);
			preparedStatement.setDate(4, db);
			preparedStatement.setString(5, imageFile.getName());
			preparedStatement.setString(6, password);
			preparedStatement.setBlob(7, new javax.sql.rowset.serial.SerialBlob(salt));
			preparedStatement.setBoolean(8, admin);

			preparedStatement.executeUpdate();



			/*INSERT INTO volunteers(firstName, lastName, phoneNumber, birthday, imageFile) VALUES
('Fred', 'Flinstone', '651-555-1234', '2002-01-02', 'some file');*/

		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}

		// finally block to make sure that our database connection is closed.
		// it would be a security leak to keep it open. 
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
	 * Updates the Volunteer in our database.
	 * @throws SQLException 
	 */
	public void updateVolunteerInDB() throws SQLException {


		Connection conn = null;
		PreparedStatement preparedStatement = null;

		try{
			//1.  connect to the DB
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");

			//2.  create a String that holds our SQL update command with ? for user inputs
			String sql = "UPDATE volunteers SET firstName = ?, lastName = ?, "
					+"phoneNumber=?, birthday = ?, imageFile = ?, admin = ? "
					+ "WHERE volunteerID = ?";

			//3. prepare the query against SQL injection
			preparedStatement = conn.prepareStatement(sql);

			//4.  convert the birthday into a date object
			Date bd = Date.valueOf(birthday);

			//5. bind the parameters
			preparedStatement.setString(1, firstName);
			preparedStatement.setString(2, lastName);
			preparedStatement.setString(3, phoneNumber);
			preparedStatement.setDate(4, bd);
			preparedStatement.setString(5, imageFile.getName());
			preparedStatement.setBoolean(6, admin);
			preparedStatement.setInt(7, volunteerID);

			//6. run the command on the SQL server
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


	/**
	 * This method will record the hours worked for the volunteer.
	 * @param dateWorked - must be in the current year and previous the current date
	 * @param hoursWorked - must be less than 18 hours
	 */
	public void logHours(LocalDate dateWorked, int hoursWorked) throws SQLException  {

		//validate the date , check if it is today or earlier.
		if (dateWorked.isAfter(LocalDate.now()))
			throw new IllegalArgumentException("Date worked cannot be in the future");

		if (dateWorked.isBefore(LocalDate.now().minusYears(1)))
			throw new IllegalArgumentException("Date worked must be within the last 12 months");

		//validate the hours worked
		if (hoursWorked < 0 || hoursWorked > 18)
			throw new IllegalArgumentException("Hours worked must be in the range of 0-18");

		//ready to store in the database
		Connection conn = null;
		PreparedStatement ps = null;

		try{
			//1. connect to the database
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");

			//2. create a preparedStatement
			String sql = "INSERT INTO hoursWorked (volunteerID, dateWorked, hoursworked) VALUES (?,?,?);";

			//3.  prepare the query
			ps = conn.prepareStatement(sql);

			//4.  convert the localdate to sql date
			Date dw = Date.valueOf(dateWorked);

			//5.  bind the parameters
			ps.setInt(1, volunteerID);
			ps.setDate(2, dw);
			ps.setInt(3, hoursWorked);

			//6.  execute the update  
			ps.executeUpdate();


		} catch(SQLException e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}

		finally {
			if (conn != null)
				conn.close();
			if (ps != null)
				ps.close();
		}
	}


	/**
	 * Creates a new salt and encrypted password, and then stores it into the database.
	 * @param newPassword
	 * @throws NoSuchAlgorithmException 
	 * @throws SQLException 
	 */
	public void changePassword(String newPassword) throws NoSuchAlgorithmException, SQLException {

		salt = PasswordGenerator.getSalt();
		// update the password to be the new one
		password = PasswordGenerator.getSHA512Password(newPassword, salt);

		//store it in database
		Connection conn = null;
		PreparedStatement preparedStatement = null;

		try {
			//1.  connect to the DB
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");

			//2.  create a String that holds our SQL update command with ? for user inputs
			// we need to add the salt
			String sql = "UPDATE volunteers SET password = ?, salt = ?"
					+ "WHERE volunteerID = ?";

			//3. prepare the query against SQL injection
			preparedStatement = conn.prepareStatement(sql);



			//5. bind the parameters
			preparedStatement.setString(1, password);
			//add the salt - converting the byte array into a blob so that it can be stored into the database.
			preparedStatement.setBlob(2, new javax.sql.rowset.serial.SerialBlob(salt));
			preparedStatement.setInt(3, volunteerID);

			//6. run the command on the SQL server
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
