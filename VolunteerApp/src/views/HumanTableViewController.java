/**
 * 
 */
package views;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
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
import models.Human;
import javafx.fxml.Initializable;

/**
 * @author Eva Rubio
 *
 */
public class HumanTableViewController implements Initializable {

	@FXML private TableView<Human> humanTable;
	// need to use the wrapper class Integer
	@FXML private TableColumn<Human, Integer> humanIDColumn;
	@FXML private TableColumn<Human, String> firstNameColumn;
	@FXML private TableColumn<Human, String> lastNameColumn;
	@FXML private TableColumn<Human, String> phoneColumn;
	@FXML private TableColumn<Human, LocalDate> dobColumn;

	@FXML private TableColumn<Human, Integer> h_typeColumn;
	@FXML private TableColumn<Human, String> emailColumn;
	@FXML private TableColumn<Human, String> addressNameColumn;


	@FXML private Button editHumanButton;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		//disable the edit button until a human has been selected from the table.
		editHumanButton.setDisable(true);


		/*
		 * Configures the table columns.
		 * The factories tell the table where it can get the information from. 
		 * */
		humanIDColumn.setCellValueFactory(new PropertyValueFactory<Human, Integer>("humanID"));
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<Human, String>("firstName"));
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<Human, String>("lastName"));
		phoneColumn.setCellValueFactory(new PropertyValueFactory<Human, String>("phoneNumber"));
		dobColumn.setCellValueFactory(new PropertyValueFactory<Human, LocalDate>("dob"));
		h_typeColumn.setCellValueFactory(new PropertyValueFactory<Human, Integer>("h_type"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<Human, String>("email"));
		// addressNameColumn

		try{
			//Updates the table with the modifications we just inserted.
			loadAllHumans();
			
			
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
        
        //this gets the human from the table
        Human human = this.humanTable.getSelectionModel().getSelectedItem();
        if (human == null)
            return;
        
        LogHoursViewController lhvc = new LogHoursViewController();
        sc.changeScenes(event, "LogHoursView.fxml", "Log Hours", human, lhvc);
    }


	/**
	 * This method will load/get/retrieve the humans from the database and load them into 
	 * the TableView object.
	 * 
	 * A TableView Object gets populated by an ObservableList.
	 */
	public void loadAllHumans() throws SQLException {

		// we create a Human (observable)list, but it is empty rn, 
		// thus, we need to connect it to the database to populate it.
		 // List whose contents will be displayed in the table:
		ObservableList<Human> humanlist = FXCollections.observableArrayList();

		Connection conn = null;
		Statement statement = null;
		// ResultSet - this is what gets returned from the database. The LIST of records. 
		ResultSet resultSet = null;

		try {

			// (1) Establish a connection to our database.
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");
			//System.out.println("loadAllHumans() - Connection established successfully!");

			// (2)  Create a Statement object.
			statement = conn.createStatement();

			// (3)  Create the SQL query to execute. - Returns the complete list of human records.
			resultSet = statement.executeQuery("SELECT * FROM humanperson");

			/*
			 * (4)  Create human objects from EACH record.
			 * 			Loop over the list of records obtained, 
			 * 				and, for each item/record we create a new Human object. 
			 * */

// public Human(int h_type, String firstName, String lastName, String phoneNumber, LocalDate dob, File imageFile, String password, String email, int gender, int addressID) 
			
			
			while (resultSet.next()) {
				Human newHuman = new Human(resultSet.getInt("h_type"),
						resultSet.getString("firstName"),
						resultSet.getString("lastName"),
						resultSet.getString("phoneNumber"),
						resultSet.getDate("dob").toLocalDate(),
						resultSet.getString("password"),
						resultSet.getString("email"),
						resultSet.getInt("gender"),
						resultSet.getInt("addressID"));
				// As we do NOT have the humanID associated to each record:
				newHuman.setHumanID(resultSet.getInt("humanID"));
				// Also, must set the File for each record's image.
				newHuman.setImageFile(new File(resultSet.getString("imageFile")));

				// Add the newly created Human object into our (Human) ObservableList.
				humanlist.add(newHuman);
			}

			/*
			 * Once our list has been fully populated, we add it to our TableView.
			 * 	humanTable.getItems() - Gets all the existing items that are in our table.
			 * 			Initially, the humanTable is EMPTY, thus, it first returns an Empty ObservableList.
			 * 	humanTable.getItems().addAll(humanlist) - Adds to this list all of our human records.
			 * 			 
			 * */
			humanTable.getItems().addAll(humanlist);

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
	 * Changes the scene to the NewUserView scene when the button is pushed.
	 * @param event
	 * @throws IOException		When it is unable to locate the fxml file to load.
	 */
	public void newHumanButtonPushed(ActionEvent event) throws IOException {
		// we create a SceneChanger object.
		SceneChanger sc = new SceneChanger();
		// we change the Scene
		sc.changeScenes(event, "NewUserView.fxml", "Create New Human");
	}

	/**
	 * If a human record has been selected in the table, enable the edit button.
	 */
	public void humanSelected() {

		editHumanButton.setDisable(false);

	}

	/**
	 * If the edit button is pushed, pass the selected Human and all its info to the NewUserView. 
	 * Then, pre-load the Scene with the data passed.
	 */
	public void editButtonPushed(ActionEvent event) throws IOException {

		SceneChanger sc = new SceneChanger();

		//get the human object that has been selected from the table. 
		Human human = this.humanTable.getSelectionModel().getSelectedItem();

		// the Controller class to pass in
		NewUserViewController nuvc = new NewUserViewController();

		//System.out.printf("The image file is in %s%n", human.getImageFile().getCanonicalPath());

		sc.changeScenes(event, "NewUserView.fxml", "Edit Person Details", human, nuvc );

	}
	
	
	
	/**
     * Change scenes to the monthly report view when pushed
     */
    public void monthlyHoursButtonPushed(ActionEvent event) throws IOException {
    	
    	SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "adminarea/AdminView.fxml", "Admin Main Menu");
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
