/**
 * 
 */
package views;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.PasswordGenerator;
import models.Volunteer;

/**
 * @author Eva Rubio
 *
 */
public class LoginViewController implements Initializable  {

	@FXML private TextField volunteerIDTextField;
	@FXML private PasswordField pwField;
	@FXML private Label errMsgLabel;



	/**
	 * When a person logs in, we get their password and salt.
	 * we create a temp volunteer
	 * @param event
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public void loginButtonPushed(ActionEvent event) throws IOException, NoSuchAlgorithmException {
		//query the database with the volunteerID provided, get the salt
		//and encrypted password stored in the database
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;

		//convert to int , same as the type in the database. 
		int volunteerNum = Integer.parseInt(volunteerIDTextField.getText());

		try{
			//1.  connect to the DB
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");

			//2.  create a query string with ? used instead of the values given by the user
			String sql = "SELECT * FROM volunteers WHERE volunteerID = ?";
			//String sql = "SELECT password, salt, admin FROM volunteers WHERE volunteerID = ?";

			//3.  prepare the statement
			ps = conn.prepareStatement(sql);

			//4.  bind the volunteerID to the ?
			ps.setInt(1, volunteerNum);

			//5. execute the query
			resultSet = ps.executeQuery();

			//6.  extract the password and salt from the resultSet
			String dbPassword = null;

			byte[] salt = null;

			boolean admin = false;

			Volunteer volunteer = null;

			while (resultSet.next()) {


				//our encrypted password
				dbPassword = resultSet.getString("password");

				Blob blob = resultSet.getBlob("salt");

				//convert blob into a byte array
				int blobLength = (int) blob.length();
				salt = blob.getBytes(1, blobLength);


				admin = resultSet.getBoolean("admin");

				// Create an instance of the Volunteer class from the database:
				//getting all of this Volunteer's information from the database
				// not needed if it
				volunteer = new Volunteer(resultSet.getString("firstName"),
						resultSet.getString("lastName"),
						resultSet.getString("phoneNumber"),
						resultSet.getDate("birthday").toLocalDate(),
						resultSet.getString("password"),
                        resultSet.getBoolean("admin"));
				
				volunteer.setVolunteerID(resultSet.getInt("VolunteerID"));
				volunteer.setImageFile(new File(resultSet.getString("imageFile")));  
			}

			//convert the password given by the user into an encrypted password
			//using the salt from the database
			String userPW = PasswordGenerator.getSHA512Password(pwField.getText(), salt);

			SceneChanger sc = new SceneChanger();
			
			if (userPW.equals(dbPassword))
                SceneChanger.setLoggedInUser(volunteer);

			// if the passwords match (if the password is valid) AND it is an 'admin' user 
			//		change the Scene to the VolunteerTableView.

			if (userPW.equals(dbPassword) && admin) {
				//SceneChanger.setLoggedInUser(volunteer);
				sc.changeScenes(event, "VolunteerTableView.fxml", "All Volunteers");

				// if it is a valid user but NOT an administrative user
				// 		change the Scene to the LogHoursView.
			} else if (userPW.equals(dbPassword)) {
				// create an instance of the controller class for log hours view (LogHoursViewController.java):
				LogHoursViewController controllerClass = new LogHoursViewController();
				//we need to pass in the Volunteer info
				sc.changeScenes(event, "LogHoursView.fxml", "Log Hours", volunteer, controllerClass);

			} else {
				errMsgLabel.setText("The volunteer ID and password do not match.");
			}

		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}

	}




	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		errMsgLabel.setText("");

	}

}
