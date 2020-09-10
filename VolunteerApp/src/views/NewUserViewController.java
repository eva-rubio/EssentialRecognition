/**
 * 
 */
package views;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import models.Address;
import models.Human;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;

/**
 * Admin view. Adds, updates the db.
 * @author Eva Rubio
 *
 */
public class NewUserViewController implements Initializable, ControllerClass {

	@FXML private TextField firstNameTextField;
	@FXML private TextField lastNameTextField;
	@FXML private TextField phoneTextField;
	@FXML private Label errMsgLabel;
	@FXML private ImageView imageView;
	@FXML private Label headerLabel;
	//used to determine if the user is an Admin or not.
	@FXML private CheckBox adminCheckBox;
	@FXML private Label adminLabel;

	/**
	 * The actual File that will be loaded. This instance variable gives me access to the actual file. */
	private File imageFile;
	private boolean imageFileChanged;

	private Human human;

	// for the passwords
	@FXML private PasswordField pwField;
	@FXML private PasswordField confirmPwField;
	@FXML private DatePicker dobDatePicker;
	@FXML private TextField emailTextField;
	//These items are for the human type RadioButton
	@FXML private RadioButton studentRadioButton;
	@FXML private RadioButton facultyRadioButton;
	@FXML private RadioButton adminRadioButton;
	private ToggleGroup h_typeToggleGroup;


	@FXML Button newAddressButton;
	@FXML Button selectAddressButton;

	//These for the Gender RadioButton
	@FXML RadioButton femaleRadioButton;
	@FXML RadioButton maleRadioButton;
	@FXML RadioButton otherRadioButton;
	private ToggleGroup genderToggleGroup;
	@FXML Label genderSelectedLabel;
	@FXML Label h_typeSelectedLabel;
	@FXML TextField addressIDTextField;
	@FXML Button saveHumanButton;






	/**
	 * Reads/gets the values in the fields from the Scene, then,
	 *  attempts/tries to create a new instance of a Human.
	 * If a person was successfully created, it is updated (or inserted if new) in the database.
	 * 
	 * @param event The event that was triggered when the button was clicked.
	 */
	public void saveHumanButtonPushed(ActionEvent event) {

		// parse() returns type int or Integer
		int addressIDValue = Integer.parseInt(addressIDTextField.getText());

		//if we have a valid password, OR the human is NOT null (already exists)
		// if its new user, human == null. and it will force them to validate the password.
		if (validPassword() || human != null) {

			try {
				// Edit/update an EXISTING human record information.
				if (human != null) {

					//before updating the human in the db directly, we must first update the Human object.
					updateHumanObj();

					human.updateHumanInDB();

					// Update the password IF it was changed.
					// if the passwordField is NOT empty
					if(!pwField.getText().isEmpty()) {
						//check if it is a Valid password
						if(validPassword()) {
							// change it to whatever is currently in the passField. 
							human.changePassword(pwField.getText());
						}

					}

				} else {

					// Human(int h_type, String firstName, String lastName, String phoneNumber, LocalDate dob, File imageFile, String password, String email, int gender, int addressID, int mascotID) throws IOException, NoSuchAlgorithmException {
					// this(h_type, firstName, lastName, phoneNumber, dob, password, email, gender, addressID, mascotID);

					//create a Human with a CUSTOM image. 
					if (imageFileChanged) {


						human = new Human(h_typeRadioButtonChanged(), firstNameTextField.getText(), lastNameTextField.getText(),
								phoneTextField.getText(), dobDatePicker.getValue(), imageFile, pwField.getText(), emailTextField.getText(),
								genderRadioButtonChanged(), addressIDValue);

					} else {

						//the imageFile has NOT been changed. USE THE DEFAULT IMG FOR AVATAR.

						// (1) public Human( int h_type, String firstName, String lastName, String phoneNumber, LocalDate dob, String password, String email, int gender, int addressID)

						human = new Human(h_typeRadioButtonChanged(), firstNameTextField.getText(), lastNameTextField.getText(),
								phoneTextField.getText(), dobDatePicker.getValue(), pwField.getText(), emailTextField.getText(),
								genderRadioButtonChanged(), addressIDValue);
					}


					//do NOT show errors if creating Human was successful
					errMsgLabel.setText("");    
					human.insertIntoDB(); 

				}


				// if NO error occurred  ->  Change the Scene to the TableView. 
				SceneChanger sc = new SceneChanger();
				sc.changeScenes(event, "HumanTableView.fxml", "All Humans");



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
	 * Validates that the passwords match and that it is of appropriate length.
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
	 * Reads from the GUI fields and updates all the Human object's fields with the new values .
	 * Changes everything except for their ID. 
	 * Takes the Human that is pre-loaded, and reads the data in the textFields
	 * 
	 */
	public void updateHumanObj() throws IOException {

		// parse() returns type int or Integer
		int addressIDValue = Integer.parseInt(addressIDTextField.getText());
		

		human.setFirstName(firstNameTextField.getText());
		human.setLastName(lastNameTextField.getText());
		human.setPhoneNumber(phoneTextField.getText());
		human.setDob(dobDatePicker.getValue());
		human.setEmail(emailTextField.getText());
		human.setImageFile(imageFile);
		human.setGender(genderRadioButtonChanged());
		human.setAddressID(addressIDValue);

		/* if the imageFileChanged is TRUE, lets copy it and put it in the correct folder.
		 * we do NOT do it unless it has CHANGED.*/
		if(imageFileChanged) {
			human.copyImageFile();
		}
	}

	/**
	 * When this button is pushed, a FileChooser object is launched to allow the user
	 * to browse for a new image file.  When that is complete, it will update the 
	 * view with a new image.
	 */
	public void chooseImageButtonPushed(ActionEvent event) {

		//get the Stage to open a new window/Stage    --> this gives us access to the actual window.
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

		//Instantiate a FileChooser object
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Image");

		/*
		 * To make sure the user only selects the correct image format we
		 *  filter to only select be allowed to select .jpg and .png formats*/
		FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("image File (*.jpg)", "*.jpg");
		FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("image File (*.png)", "*.png");
		// the list is initially empty so we add them all to it. 
		fileChooser.getExtensionFilters().addAll(jpgFilter, pngFilter);

		//Set to the user's picture directory or user directory if not available
		String userDirectoryString = System.getProperty("user.home")+"\\Pictures";
		File userDirectory = new File(userDirectoryString);

		//if you cannot navigate to the pictures directory, go to the user home
		if (!userDirectory.canRead())
			userDirectory = new File(System.getProperty("user.home"));

		fileChooser.setInitialDirectory(userDirectory);

		//open the file dialog window - giving the user the ability to select their desired one. 
		File tmpImageFile = fileChooser.showOpenDialog(stage);

		if (tmpImageFile != null) {
			imageFile = tmpImageFile;

			//update the ImageView with the new user-selected image
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
	 * This method will change back to the TableView of humans WITHOUT adding a user. 
	 * 
	 * ALL data in the form will be LOST.
	 */
	public void cancelButtonPushed(ActionEvent event) throws IOException {

		SceneChanger sc = new SceneChanger();

		//check if it is an admin user and if so, go to the table view
		
//TODO : REMOVE TRUEEEE
		
		
		if ((SceneChanger.getLoggedInUser().getH_type() == 3)|| true) {
			sc.changeScenes(event,"HumanTableView.fxml", "All Humans"); 

		} else { 
			
			//TODO: Go to my individual student schedule/timetable. 
			
		//	LogHoursViewController controller = new LogHoursViewController(); 
			//sc.changeScenes(event, "LogHoursView.fxml", "Log Hours", human, controller); 
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




	/**
	 * This method will update the radioButtonLabel when ever a different
	 * radio button is pushed
	 */
	@FXML
	public int genderRadioButtonChanged() {
		if (this.genderToggleGroup.getSelectedToggle().equals(this.femaleRadioButton)) {
			genderSelectedLabel.setText("Female.");
			return 1;
		}        
		if (this.genderToggleGroup.getSelectedToggle().equals(this.maleRadioButton)) {
			genderSelectedLabel.setText("Male.");
			return 2;
		}
		if (this.genderToggleGroup.getSelectedToggle().equals(this.otherRadioButton)) {
			genderSelectedLabel.setText("Other.");
			return 3;
		}
		return 1;  
	}
	/**
	 * This method will update the radioButtonLabel when ever a different radio button is pushed.
	 */
	@FXML
	public int h_typeRadioButtonChanged() {
		if (this.h_typeToggleGroup.getSelectedToggle().equals(this.studentRadioButton)) {
			h_typeSelectedLabel.setText("Student.");
			return 1;
		}        
		if (this.h_typeToggleGroup.getSelectedToggle().equals(this.facultyRadioButton)) {
			h_typeSelectedLabel.setText("Faculty.");
			return 2;
		}
		if (this.h_typeToggleGroup.getSelectedToggle().equals(this.adminRadioButton)) {
			h_typeSelectedLabel.setText("Admin.");
			return 3;
		}
		return 1;  
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// configuring the RadioButtons
		genderSelectedLabel.setText("");
		genderToggleGroup = new ToggleGroup();
		this.femaleRadioButton.setToggleGroup(genderToggleGroup);
		this.maleRadioButton.setToggleGroup(genderToggleGroup);
		this.otherRadioButton.setToggleGroup(genderToggleGroup);

		h_typeSelectedLabel.setText("");
		h_typeToggleGroup = new ToggleGroup();
		this.studentRadioButton.setToggleGroup(h_typeToggleGroup);
		this.facultyRadioButton.setToggleGroup(h_typeToggleGroup);
		this.adminRadioButton.setToggleGroup(h_typeToggleGroup);




		//assume our Human is at least 16 years old. 
		dobDatePicker.setValue(LocalDate.now().minusYears(16));

		// if the current user is NOT an Admin, do NOT display the adminCheckBox.
		/*
		 * if(!(SceneChanger.getLoggedInUser().getH_type() == 3)) {
		 * adminCheckBox.setVisible(false); adminLabel.setVisible(false);
		 * 
		 * }
		 */

		//initially the image has not changed, use the default
		imageFileChanged = false; 

		errMsgLabel.setText("");

		//'try to' load the default image for the user avatar
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
	 * (Pre)Load the current view with the Human object that was passed in for editting it.
	 * Pre-loads all of the fields in the table with our user/human information. 
	 * 
	 * Required for scene changes and preloading data.
	 * @param Human the Human to edit. 
	 */
	@Override
	public void preloadData(Human human) {
		//the human that we passed in:
		this.human = human;
		// to update the view.
		this.firstNameTextField.setText(human.getFirstName());
		this.lastNameTextField.setText(human.getLastName());
		this.dobDatePicker.setValue(human.getDob());
		this.phoneTextField.setText(human.getPhoneNumber());
		this.emailTextField.setText(human.getEmail());
		this.addressIDTextField.setText(human.getAddressIDString());
		
		if(human.getH_type()==1) {
			this.studentRadioButton.setSelected(true);}
		if(human.getH_type()==2) {
			this.facultyRadioButton.setSelected(true);}
		if(human.getH_type()==3) {
			this.adminRadioButton.setSelected(true);}
		if(human.getGender()==1) {
			this.femaleRadioButton.setSelected(true);}
		if(human.getGender()==2) {
			this.maleRadioButton.setSelected(true);	}
		if(human.getGender()==3) {
			this.otherRadioButton.setSelected(true);}
		
		this.headerLabel.setText("Edit Person Details");
		this.saveHumanButton.setText("Save");

//		if(human.isAdmin()) {
//			adminCheckBox.setSelected(true);
//		}

		//load the image (that was previously selected by the person in question).
		//we obtain the image file from the human object passed in. 
		try {

			String imgLocation = ".\\src\\images\\" + human.getImageFile().getName();

			imageFile = new File(imgLocation);
			// ImageIO - throws IOException as it is possible that the image is not at the location given, permissions issue... 
			BufferedImage bufferedImage = ImageIO.read(imageFile);
			//create an Image and then set the imageView to contain it inside.
			Image img = SwingFXUtils.toFXImage(bufferedImage, null);
			imageView.setImage(img);

		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}

	}

	@FXML public void createAddressButtonPushed(ActionEvent event) {}

	@FXML public void selectAddressButtonPushed(ActionEvent event) {
		SceneChanger sc = new SceneChanger();

		//get the human object that has been selected from the table. 
		//Address address = this.addressTable.getSelectionModel().getSelectedItem();

		// the Controller class to pass in
		//AddressTableViewController atvc = new AddressTableViewController(); 

		//System.out.printf("The image file is in %s%n", human.getImageFile().getCanonicalPath());

		try {
			sc.changeScenes(event, "AddressTableView.fxml", "Select Address" );
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}
	}


}
