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
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Human;
import models.PasswordGenerator;

/**
 * @author Eva Rubio
 *
 */
public class LoginViewController implements Initializable  {

	@FXML private TextField humIDTextField;
	@FXML private PasswordField pwField;
	@FXML private Label errMsgLabel;



	/**
	 * When a person logs in, we get their password and salt.
	 * we create a temp human
	 * @param event
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public void loginButtonPushed(ActionEvent event) throws IOException, NoSuchAlgorithmException {
		//query the database with the humanID provided, get the salt
		//and encrypted password stored in the database
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;

		//convert to int , same as the type in the database. 
		int userInput = Integer.parseInt(humIDTextField.getText());

		try{
			//1.  connect to the DB
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");

			//2.  create a query string with ? used instead of the values given by the user
			String sql = "SELECT * FROM humanperson WHERE humanID = ?";
			// este estaba antes -- String sql = "SELECT * FROM humanperson WHERE humanID = ?";
			//String sql = "SELECT password, salt, admin FROM humanperson WHERE humanID = ?";

			//3.  prepare the statement
			ps = conn.prepareStatement(sql);

			//4.  bind the humanID to the ?
			ps.setInt(1, userInput);

			//5. execute the query
			resultSet = ps.executeQuery();

			//6.  extract the password and salt from the resultSet
			String dbPassword = null;

			byte[] salt = null;

			
			Human human = null;

			int userType = 1;
			
			
			while (resultSet.next()) {


				//our encrypted password
				dbPassword = resultSet.getString("password");

				Blob blob = resultSet.getBlob("salt");

				//convert blob into a byte array
				int blobLength = (int) blob.length();
				salt = blob.getBytes(1, blobLength);

				userType = resultSet.getInt("h_type");
				

				// Create an instance of the Human class from the database:
				//getting all of this Human's information from the database
				// public Human(int h_type, String firstName, String lastName, String phoneNumber, LocalDate dob, 
				//String password, String email, int gender, int addressID, int mascotID)
				human = new Human(resultSet.getInt("h_type"),
						resultSet.getString("firstName"),
						resultSet.getString("lastName"),
						resultSet.getString("phoneNumber"),
						resultSet.getDate("dob").toLocalDate(),
						resultSet.getString("password"),
						resultSet.getString("email"),
                        resultSet.getInt("gender"),
                        resultSet.getInt("addressID"));
				
				human.setHumanID(resultSet.getInt("humanID"));
				human.setImageFile(new File(resultSet.getString("imageFile")));  
				
				//human = new Human();
				
			}

			//convert the password given by the user into an encrypted password
			//using the salt from the database
			String userPW = PasswordGenerator.getSHA512Password(pwField.getText(), salt);

			SceneChanger sc = new SceneChanger();
			
			if (userPW.equals(dbPassword)) {
                SceneChanger.setLoggedInUser(human);

			//TODO: BORRAR NO VA AQUI.
			//sc.changeScenes(event, "HumanTableView.fxml", "All People");
			// if the passwords match (if the password is valid) AND it is an 'admin' user 
			//		change the Scene to the HumanTableView.

			if (userPW.equals(dbPassword) && (userType == 2 || userType == 3)) {
				//SceneChanger.setLoggedInUser(human);
				sc.changeScenes(event, "adminarea/AdminView.fxml", "Admin Area");

				// if it is a valid user but NOT an administrative user
				// 		change the Scene to the LogHoursView.
				
			} else if (userPW.equals(dbPassword)) {
				sc.changeScenes(event, "HumanTableView.fxml", "All People");
			}
			
	/*			// create an instance of the controller class for log hours view (LogHoursViewController.java):
				LogHoursViewController controllerClass = new LogHoursViewController();
				//we need to pass in the Human info
				sc.changeScenes(event, "LogHoursView.fxml", "Log Hours", human, controllerClass);
*/
			} else {
				errMsgLabel.setText("The ID and password do not match. Please try again.");
				humIDTextField.clear();
				pwField.clear();
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
