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
import java.util.regex.Pattern;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**	(Inheritance)
 * 
 * 		THE SUPER CLASS - BASE/PARENT 
 * 
 * 	A subclass inherits all the variables and methods from its superclasses, including its immediate parent as well as all the ancestors. 
 * 
 * pulling out all the common variables and methods into the superclasses, 
 * and leave the specialized variables and methods in the subclasses.
 * 
 * 
 * https://www.youtube.com/watch?v=_ySyOq-Hnpw
 * https://www.youtube.com/watch?v=jSfMTVk-8ys&list=PLoodc-fmtJNZuldAE53bGuOwJ6BhUG7mA&index=4
 * 
 * 
 * @author Eva Rubio
 *
 */

/*
 * 
 * enum PersonGroupType { STUDENT, INSTRUCTOR, ADMIN }
 */
public class Human {

	/**		PK - Primary Key 	
	 * We cant access/get the humanID UNLESS it is directly coming from our database.
	 *  */
	private int humanID;

	private int h_type;

	private String firstName;
	private String lastName;
	private String phoneNumber;
	private LocalDate dob;

	private File imageFile;

	private String password;	
	private byte[] salt;

	private String email;
	private int gender;
	//private boolean admin;
	private int addressID;
	private int mascotID;

	private String defaultPass = "default";

	//private PersonGroupType humanCategory;

	public Human(int h_type, String firstName, String lastName, String phoneNumber, LocalDate dob, String email, int gender, int addressID, int mascotID) throws NoSuchAlgorithmException {
		setH_type(h_type);
		setFirstName(firstName);
		setLastName(lastName);
		setPhoneNumber(phoneNumber);
		setDob(dob);
		setImageFile(new File("./src/images/defaultPerson.png"));

		password = defaultPass;
		salt = PasswordGenerator.getSalt();
		this.password = PasswordGenerator.getSHA512Password(password, salt);

		setEmail(email);
		setGender(gender);
		setAddressID(addressID);
		setMascotID(mascotID);

		//this.setAdmin(admin);
	}
	/**
	 * (1) Base constructor - sets default image. 
	 * @param firstName
	 * @param lastName
	 * @param phoneNumber
	 * @param dob
	 * @throws NoSuchAlgorithmException 
	 */
	public Human(int h_type, String firstName, String lastName, String phoneNumber, LocalDate dob, String password, String email, int gender, int addressID) throws NoSuchAlgorithmException {
		setH_type(h_type);
		setFirstName(firstName);
		setLastName(lastName);
		setPhoneNumber(phoneNumber);
		setDob(dob);
		setImageFile(new File("./src/images/defaultPerson.png"));

		salt = PasswordGenerator.getSalt();
		this.password = PasswordGenerator.getSHA512Password(password, salt);

		setEmail(email);
		setGender(gender);
		setAddressID(addressID);
		setMascotID(mascotID);

		//this.setAdmin(admin);
	}


	/**
	 * (2) Largest Constructor.
	 * Adds personalized photo to human. */
	public Human(int h_type, String firstName, String lastName, String phoneNumber, LocalDate dob, File imageFile, String password, String email, int gender, int addressID) throws IOException, NoSuchAlgorithmException {
		this(h_type, firstName, lastName, phoneNumber, dob, password, email, gender, addressID);
		setImageFile(imageFile);
		copyImageFile();
	}


	public String setDefaultPassword() {
		return this.password = "default";
	}
	public static boolean isNameValid(String nameToCheck) {

		return Pattern.matches("[a-zA-Z]+", nameToCheck);	
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
	 * Validates before setting thr phone number.
	 * Following the North American dialing planning.
	 * 	NXX is the pattern used. 
	 * 		NXX-XXX-XXXX
	 * 
	 * N	--	can only be numbers 2-9
	 * 					0 calls the operator
	 * 					1 significa long distance
	 * X 	--	any number between 0-9
	 * 
	 * area code    city    house 
	 * NXX          -XXX    -XXXX
	 *              
	 * @param phoneNumber the phoneNumber to set if valid.
	 */
	public void setPhoneNumber(String phoneNumber) {
		/*
		 * [2-9]	First number has to be 2 through 9.
		 * \\d		Any digits
		 * {2}		Need 2 of those digits ^
		 * 
		 * [-.]?	Then either a dash or a dot, optional. 
		 * 
		 * \\d{3}	Then 3 digits
		 * */


		if (phoneNumber.matches("[2-9]\\d{2}[-.]?\\d{3}[-.]\\d{4}"))
			this.phoneNumber = phoneNumber;
		else
			throw new IllegalArgumentException("Phone numbers must be in the pattern NXX-XXX-XXXX");
	}
	/**
	 * @return the birthday
	 */
	public LocalDate getDob() {
		return dob;
	}
	/**
	 * Validates that the human is between the ages of 16 and 150.
	 * i.e. That the person is within a certain age range.
	 * 
	 * @param birthday the birthday to set
	 */
	public void setDob(LocalDate dob) {
		int age = Period.between(dob, LocalDate.now()).getYears();

		if (age >= 16 && age <= 150) {
			this.dob = dob;
		} else {
			throw new IllegalArgumentException("Must be 16-150 years of age.");
		}
	}
	public String getPassword() {
		return password;
	}

	public byte[] getSalt() {
		return salt;
	}

	public int getH_type() {
		return h_type;
	}

	/**
	 * @param admin the admin to set
	 */
	public void setH_type(int h_type) {
		this.h_type = h_type;
		if(h_type == 1 || h_type==2 || h_type==3) {
			this.h_type = h_type;
		} else {
			throw new IllegalArgumentException("Type of Person must be one of the following: 1 (Student), 2 (Faculty), 3 (Admin)");
		}
	}
	/**
	 * @return the imageFile
	 */
	public File getImageFile() {
		return imageFile;
	}
	/**
	 * @param imageFile the imageFile to set
	 */
	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;
	}
	/**
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

	public String getEmail() {
		return email;
	}
	/**
	 * Checks that the Email is valid. 
	 * The following regex will match 99.99% of all email addresses in actual use today. 
	 * 
	 * 
			\A[a-z0-9!#$%&'*+/=?^_‘{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_‘{|}~-]+)*@
			(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\z

https://www.regular-expressions.info/email.html */
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setGender(int gender) {
		if(gender == 1 || gender==2 || gender==3) {
			this.gender = gender;
		} else {
			throw new IllegalArgumentException("Gender must be one of the following: 1 (Female), 2 (Male), 3 (Other)");
		}
	}
	public void setAddressID(int addressID) {
		if(addressID > 0) {
			this.addressID = addressID;
		} else {
			throw new IllegalArgumentException("The Address ID must be > 0.");
		}
	}
	public int getAddressID() {
		return addressID;
	}
	public String getAddressIDString() {
		return Integer.toString(addressID);
	}

	public int getGender() {
		return gender;
	}
	public int getHumanID() {
		return humanID;
	}

	/**
	 * Set the Human ID number. Making sure it is a positive value. 
	 * @param humanID
	 */
	public void setHumanID(int humanID) {
		if (humanID >= 0) {
			this.humanID = humanID;
		} else {
			throw new IllegalArgumentException("HumanID must be >= 0");
		}
	}
	/**
	 * @param mascotID
	 */
	private void setMascotID(int mascotID) {
		if (mascotID >= 0) {
			this.mascotID = mascotID;
		} else {
			throw new IllegalArgumentException("MascotID must be >= 0");
		}

	}
	public int getMascotID() {
		return mascotID;
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
		return String.format("%s %s is %d years old", firstName, lastName, Period.between(dob, LocalDate.now()).getYears());
	}




	/**
	 * Writes the instance of the Human into the database.
	 * Lets a human person to write themselves to the database
	 * @throws SQLException 
	 * 
	 * */
	public void insertIntoDB() throws SQLException {

		Connection conn = null;
		/*
		 * PreparedStatement
		 * To make sure no one can do a SQL injection attack, we use a PreparedStatement to sanitize our inputs
		 * before we try to use them. 
		 *  */
		PreparedStatement preparedStatement = null;

		try {
			// (1) Connect to the database. (our connection)
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");
			System.out.println("Connection established successfully!");

			// (2) Create a String that holds the query with ? as user inputs.
			String sql = "INSERT INTO humanperson (h_type, firstName, lastName, phoneNumber, dob, imageFile, password, salt, email, gender, addressID)"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

			// (3) Prepare the query (by sanitizing the inputs.)
			preparedStatement = conn.prepareStatement(sql);

			// (4) Convert the birthday into a SQL date
			Date db = Date.valueOf(dob);

			//5. Bind the values to the parameters
			preparedStatement.setInt(1, h_type);
			preparedStatement.setString(2, firstName);
			preparedStatement.setString(3, lastName);
			preparedStatement.setString(4, phoneNumber);
			preparedStatement.setDate(5, db);
			preparedStatement.setString(6, imageFile.getName());	//returns the name of our file. 
			preparedStatement.setString(7, password);
			preparedStatement.setBlob(8, new javax.sql.rowset.serial.SerialBlob(salt));
			preparedStatement.setString(9, email);
			preparedStatement.setInt(10, gender);
			preparedStatement.setInt(11, addressID);
			//preparedStatement.setInt(11, mascotID);
			//preparedStatement.setBoolean(8, admin);

			preparedStatement.executeUpdate();



			/*INSERT INTO humanperson(firstName, lastName, phoneNumber, birthday, imageFile) VALUES
('Fred', 'Flinstone', '651-555-1234', '2002-01-02', 'some file');*/

		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}

		/*
		 * finally block - 
		 * 	to make sure that our database connection is closed.
		 * It would be a security leak to keep it open.*/  
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
	 * Updates the Human in our database.
	 * Allows us to modify entries in the database.
	 * @throws SQLException 
	 */
	public void updateHumanInDB() throws SQLException {


		Connection conn = null;
		PreparedStatement preparedStatement = null;

		try{
			//1.  connect to the DB
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");

			//2.  create a String that holds our SQL update command with ? for user inputs
			String sql = "UPDATE humanperson SET h_type = ?, firstName = ?, lastName = ?, "
					+"phoneNumber=?, dob = ?, imageFile = ?, email = ?, gender = ? , addressID = ?  "
					+ "WHERE humanID = ?";

			//3. prepare the query against SQL injection
			preparedStatement = conn.prepareStatement(sql);

			//4.  convert the birthday into a date object
			Date bd = Date.valueOf(dob);

			//5. bind the parameters
			preparedStatement.setInt(1, h_type);
			preparedStatement.setString(2, firstName);
			preparedStatement.setString(3, lastName);
			preparedStatement.setString(4, phoneNumber);
			preparedStatement.setDate(5, bd);
			preparedStatement.setString(6, imageFile.getName());
			preparedStatement.setString(7, email);
			preparedStatement.setInt(8, gender);
			preparedStatement.setInt(9, addressID);
			preparedStatement.setInt(10, humanID);

			//6. Execute the UPDATE command on the SQL server.
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
	 * This method will record the hours worked for the human.
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
			String sql = "INSERT INTO hoursWorked (humanID, dateWorked, hoursworked) VALUES (?,?,?);";

			//3.  prepare the query
			ps = conn.prepareStatement(sql);

			//4.  convert the localdate to sql date
			Date dw = Date.valueOf(dateWorked);

			//5.  bind the parameters
			ps.setInt(1, humanID);
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
			String sql = "UPDATE humanperson SET password = ?, salt = ?"
					+ "WHERE humanID = ?";

			//3. prepare the query against SQL injection
			preparedStatement = conn.prepareStatement(sql);



			//5. bind the parameters
			preparedStatement.setString(1, password);
			//add the salt - converting the byte array into a blob so that it can be stored into the database.
			preparedStatement.setBlob(2, new javax.sql.rowset.serial.SerialBlob(salt));
			preparedStatement.setInt(3, humanID);

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
	 * 
	 * NRRXXX
	 * N [1-6	Student, 	7-8	Faculty, 	9	Admin] 
	 * RR 	are 2 random ints*/
	public int generateUniqueMascotID() {
		int uniqueMasID = 0;
		if(this.h_type == 1) {
			uniqueMasID += 200000;
			uniqueMasID += this.humanID;
		}
		if(this.h_type == 2) {
			uniqueMasID += 700000;
			uniqueMasID += this.humanID;
		}
		if(this.h_type == 3) {
			uniqueMasID += 900000;
			uniqueMasID += this.humanID;
		}
		return uniqueMasID; 
	}





}
