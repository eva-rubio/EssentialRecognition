/**
 * 
 */
package views;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import models.Volunteer;
import javafx.fxml.Initializable;

/**
 * @author Eva Rubio
 *
 */
public class VolunteerTableViewController implements Initializable {

	@FXML private TableView<Volunteer> volunteerTable;
	// need to use the wrapper class Integer
	@FXML private TableColumn<Volunteer, Integer> volunterIDColumn;
	@FXML private TableColumn<Volunteer, String> firstNameColumn;
	@FXML private TableColumn<Volunteer, String> lastNameColumn;
	@FXML private TableColumn<Volunteer, String> phoneColumn;
	@FXML private TableColumn<Volunteer, LocalDate> birthdayColumn;

	@FXML private Button editVolunteerButton;

	@FXML private Button logHoursButton;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		//disable the edit button until a volunteer has been selected from the table
		editVolunteerButton.setDisable(true);
		logHoursButton.setDisable(true);


		/*
		 * Configure the table columns.
		 * The factories tell the table where it can get information from. 
		 * */
		volunterIDColumn.setCellValueFactory(new PropertyValueFactory<Volunteer, Integer>("volunteerID"));
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<Volunteer, String>("firstName"));
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<Volunteer, String>("lastName"));
		phoneColumn.setCellValueFactory(new PropertyValueFactory<Volunteer, String>("phoneNumber"));
		birthdayColumn.setCellValueFactory(new PropertyValueFactory<Volunteer, LocalDate>("birthday"));

		try{
			loadVolunteers();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}

	}
	
	/**
     * This method will call up the loghours view
     */
    public void logHoursButtonPushed(ActionEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        
        //this gets the volunteer from the table
        Volunteer volunteer = this.volunteerTable.getSelectionModel().getSelectedItem();
        if (volunteer == null)
            return;
        
        LogHoursViewController lhvc = new LogHoursViewController();
        sc.changeScenes(event, "LogHoursView.fxml", "Log Hours", volunteer, lhvc);
    }


	/**
	 * This method will load/get/retrieve the courses from the database and load them into 
	 * the TableView object
	 */
	public void loadVolunteers() throws SQLException {

		// we create a Volunteer list, but it is empty, 
		// thus, we need to connect it to the database
		 // List whose contents will be displayed in the table:
		ObservableList<Volunteer> volunteers = FXCollections.observableArrayList();

		Connection conn = null;
		Statement statement = null;
		// this is what gets returned from the database.The LIST of records. 
		ResultSet resultSet = null;

		try {

			// (1) Establish a connection to our database.
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");
			//System.out.println("loadVolunteers() - Connection established successfully!");

			// (2)  Create a Statement object.
			statement = conn.createStatement();

			// (3)  Create the SQL query.
			resultSet = statement.executeQuery("SELECT * FROM volunteers");

			/*
			 * (4)  Create volunteer objects from EACH record.
			 * 			Loop over the list of records obtained, 
			 * 				and, for each item/record we create a Volunteer object. 
			 * */

			while (resultSet.next()) {
				Volunteer newVolunteer = new Volunteer(resultSet.getString("firstName"),
						resultSet.getString("lastName"),
						resultSet.getString("phoneNumber"),
						resultSet.getDate("birthday").toLocalDate(),
						resultSet.getString("password"),
                        resultSet.getBoolean("admin"));
				newVolunteer.setVolunteerID(resultSet.getInt("VolunteerID"));
				newVolunteer.setImageFile(new File(resultSet.getString("imageFile")));

				// Add the newly created Volunteer into our ObservableList.
				volunteers.add(newVolunteer);
			}

			// 
			volunteerTable.getItems().addAll(volunteers);

		} catch (Exception e) {

			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}

		/*
		 * The finally block gets executed EVEN IF there is an exception.
		 * Close everything up so there is nothing lingering. 
		 * */
		finally {
			if (conn != null) {
				conn.close();
			}
			if(statement != null) {
				statement.close();
			}
			if(resultSet != null) {
				resultSet.close();
			}
		}
	}

	/**
	 * This method will switch to the NewUserView scene when the button is pushed
	 */
	public void newVolunteerButtonPushed(ActionEvent event) throws IOException {
		// we create a SceneChanger object.
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "NewUserView.fxml", "Create New Volunteer");
	}

	/**
	 * If a user has been selected in the table, enable the edit button
	 */
	public void volunteerSelected() {

		editVolunteerButton.setDisable(false);

		logHoursButton.setDisable(false);
	}

	/**
	 * If the edit button is pushed, pass the selected Volunteer to the NewUserView 
	 * and pre-loads it with the data
	 */
	public void editButtonPushed(ActionEvent event) throws IOException {

		SceneChanger sc = new SceneChanger();

		//get the Volunteer object that has been selected. 
		Volunteer volunteer = this.volunteerTable.getSelectionModel().getSelectedItem();

		NewUserViewController nuvc = new NewUserViewController();

		//System.out.printf("The image file is in %s%n", volunteer.getImageFile().getCanonicalPath());

		sc.changeScenes(event, "NewUserView.fxml", "Edit Volunteer", volunteer, nuvc );

	}
	
	
	
	/**
     * Change scenes to the monthly report view when pushed
     */
    public void monthlyHoursButtonPushed(ActionEvent event) throws IOException {
    	
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "MonthlyHoursView.fxml", "View Hours");
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

}
