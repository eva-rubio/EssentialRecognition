/**
 * 
 */
package views;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import models.Volunteer;

/**
 * @author Eva Rubio
 *
 */
public class NewUserViewController implements Initializable, ControllerClass {

	@FXML private TextField firstNameTextField;
	@FXML private TextField lastNameTextField;
	@FXML private TextField phoneTextField;
	@FXML private DatePicker birthday;
	@FXML private Label errMsgLabel;
	@FXML private ImageView imageView;
	@FXML private Label headerLabel;
	//used to determine if the user is an Admin or not.
	@FXML private CheckBox adminCheckBox;
	@FXML private Label adminLabel;

	/**
	 * The actual File that will be loaded.
	 */
	private File imageFile;
	private boolean imageFileChanged;

	private Volunteer volunteer;

	// for the passwords
	@FXML private PasswordField pwField;
	@FXML private PasswordField confirmPwField;






	/**
	 * This method will read from the scene and try to create a new instance of a Volunteer.
	 * If a volunteer was successfully created, it is updated in the database.
	 * 
	 * @param event The event triggered when the button was clicked.
	 */
	public void saveVolunteerButtonPushed(ActionEvent event) {

		//if we have a valid password, OR the volunteer is NOT null (already exists)
		// if its new user, volunteer == null. and it will force them to validate the password.
		if (validPassword() || volunteer != null) {

			try {

				// Edit/update an EXISTING volunteer's information
				if (volunteer != null) {

					updateVolunteer();

					volunteer.updateVolunteerInDB();
					
					// Update the password IF it was changed.
					// if the passwordField is NOT empty
					if(!pwField.getText().isEmpty()) {
						//check if it is a Valid password
						if(validPassword()) {
							// change it to whatever is currently in the passField. 
							volunteer.changePassword(pwField.getText());
						}
						
					}

				} else {

					//create a Volunteer with a CUSTOM image.
					if (imageFileChanged) {

						volunteer = new Volunteer(firstNameTextField.getText(),lastNameTextField.getText(),
								phoneTextField.getText(), birthday.getValue(), imageFile, pwField.getText(),
								adminCheckBox.isSelected());

					} else {

						//the imageFile has NOT been changed. USE THE DEFAULT IMG FOR AVATAR.
						volunteer = new Volunteer(firstNameTextField.getText(),lastNameTextField.getText(),
								phoneTextField.getText(), birthday.getValue(), pwField.getText(),
								adminCheckBox.isSelected());
					}


					//do NOT show errors if creating Volunteer was successful
					errMsgLabel.setText("");    
					volunteer.insertIntoDB(); 

				}


				// if NO error occurred  ->  Change the Scene to the TableView. 
				SceneChanger sc = new SceneChanger();
				sc.changeScenes(event, "VolunteerTableView.fxml", "All Volunteers");



			} catch (Exception e) {

				errMsgLabel.setText(e.getMessage());
				System.err.println(e.getMessage());
				System.out.println(e);
				StackTraceElement l = new Exception().getStackTrace()[0];
				System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
			}
		}
	}

	/**
	 * Validates that the passwords match.
	 * 
	 */
	public boolean validPassword() {

		if (pwField.getText().length() < 5) {

			errMsgLabel.setText("Passwords must be greater than 5 characters in length");

			return false;
		}

		if (pwField.getText().equals(confirmPwField.getText())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Reads from the GUI fields and updates all the Volunteer object's fields with the new values .
	 * Changes everything except for their ID. 
	 * Takes the Volunteer that is pre-loaded, and reads the data in the textFields
	 * 
	 */
	public void updateVolunteer() throws IOException {

		volunteer.setFirstName(firstNameTextField.getText());
		volunteer.setLastName(lastNameTextField.getText());
		volunteer.setPhoneNumber(phoneTextField.getText());
		volunteer.setBirthday(birthday.getValue());
		volunteer.setImageFile(imageFile);
		
		volunteer.setAdmin(adminCheckBox.isSelected());


		/* if the imageFileChanged is TRUE, lets copy it and put it in the correct folder.
		 * we do not do it unless it has changed.*/
		if(imageFileChanged) {
			volunteer.copyImageFile();
		}
	}

	/**
	 * When this button is pushed, a FileChooser object is launched to allow the user
	 * to browse for a new image file.  When that is complete, it will update the 
	 * view with a new image
	 */
	public void chooseImageButtonPushed(ActionEvent event) {

		//get the Stage to open a new window/Stage
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

		//Instantiate a FileChooser object
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Image");

		/*
		 * To make sure the user only selects the correct image format we
		 *  filter to only select be allowed to select .jpg and .png formats*/
		FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("Image File (*.jpg)", "*.jpg");
		FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("Image File (*.png)", "*.png");
		// the list is initially empty so we add them all to it. 
		fileChooser.getExtensionFilters().addAll(jpgFilter, pngFilter);

		//Set to the user's picture directory or user directory if not available
		String userDirectoryString = System.getProperty("user.home")+"\\Pictures";
		File userDirectory = new File(userDirectoryString);

		//if you cannot navigate to the pictures directory, go to the user home
		if (!userDirectory.canRead())
			userDirectory = new File(System.getProperty("user.home"));

		fileChooser.setInitialDirectory(userDirectory);

		//open the file dialog window
		File tmpImageFile = fileChooser.showOpenDialog(stage);

		if (tmpImageFile != null) {
			imageFile = tmpImageFile;

			//update the ImageView with the new image
			if (imageFile.isFile()) {

				try {

					BufferedImage bufferedImage = ImageIO.read(imageFile);
					Image img = SwingFXUtils.toFXImage(bufferedImage, null);
					imageView.setImage(img);

					imageFileChanged = true;

				} catch (IOException e) {

					System.err.println(e.getMessage());
					System.out.println(e);
					StackTraceElement l = new Exception().getStackTrace()[0];
					System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
				}
			}
		}
	}
	

	/**
	 * This method will change back to the TableView of volunteers without adding a user. 
	 * 
	 * ALL data in the form will be LOST.
	 */
	public void cancelButtonPushed(ActionEvent event) throws IOException {

		SceneChanger sc = new SceneChanger();

		//check if it is an admin user and go to the table view

		if (SceneChanger.getLoggedInUser().isAdmin()) {
			sc.changeScenes(event,"VolunteerTableView.fxml", "All Volunteers"); 

		} else { 
			LogHoursViewController controller = new LogHoursViewController(); 
			sc.changeScenes(event, "LogHoursView.fxml", "Log Hours", volunteer, controller); 
		}
	}
	/**
	 * Logs the current user out of the application and returns them to the LoginView.
	 * @param event
	 * @throws IOException 
	 */
	public void logoutButttonPushed(ActionEvent event) throws IOException {
		
		SceneChanger.setLoggedInUser(null);
		
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "LoginView.fxml", "Login");	
	}




	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		//assume our Volunteer is atleast 18 years old. 
		birthday.setValue(LocalDate.now().minusYears(18));
		
		// if the current user is NOT an Admin, do NOT display the adminCheckBox.
		if(!SceneChanger.getLoggedInUser().isAdmin()) {
			adminCheckBox.setVisible(false);
			adminLabel.setVisible(false);

		}

		//initially the image has not changed, use the default
		imageFileChanged = false; 

		errMsgLabel.setText("");

		//load the default image for the user avatar
		try {
			// the 'imageFile' points to the default img. (to access the file)
			imageFile = new File("./src/images/defaultPerson.png");
			BufferedImage bufferedImage = ImageIO.read(imageFile);
			// now we can create an Image 
			Image image = SwingFXUtils.toFXImage(bufferedImage, null);
			// we now set the created image into our ImageView.
			imageView.setImage(image);

		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}

	}

	/**
	 * update the view with a Volunteer object preloaded for an edit.
	 * Pre-loads all of the fields in the table with our user information. 
	 * @param volunteer the volunteer to edit. 
	 */
	@Override
	public void preloadData(Volunteer volunteer) {
		this.volunteer = volunteer;
		// to update the view.
		this.firstNameTextField.setText(volunteer.getFirstName());
		this.lastNameTextField.setText(volunteer.getLastName());
		this.birthday.setValue(volunteer.getBirthday());
		this.phoneTextField.setText(volunteer.getPhoneNumber());
		this.headerLabel.setText("Edit Volunteer");
		
		if(volunteer.isAdmin()) {
			adminCheckBox.setSelected(true);
		}

		//load the image 
		try {

			String imgLocation = ".\\src\\images\\" + volunteer.getImageFile().getName();

			imageFile = new File(imgLocation);

			BufferedImage bufferedImage = ImageIO.read(imageFile);

			Image img = SwingFXUtils.toFXImage(bufferedImage, null);
			imageView.setImage(img);

		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}

	}

}
