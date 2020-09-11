/**
 * 
 */
package views;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

//import application.face.detection.Person;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Catalog;
import models.Volunteer;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.scene.control.TextArea;
import javafx.scene.control.CheckBox;

/**
 * 
 * CatalogListViewController is designed to show details of all the Courses offered by the University
 *  
 * 
 * @author Eva Rubio
 *
 */
public class CatalogListViewController implements Initializable{

	@FXML private TableView<Catalog> catalogListTable;

	@FXML private TableColumn<Catalog, String> c_codeTableColumn;

	@FXML private TableColumn<Catalog, String> c_titleTableColumn;
	@FXML private TableColumn<Catalog, String> c_descriptionTableColumn;

	

	@FXML private Button newCatalogButton;
	@FXML private TableColumn<Catalog, Boolean> coreReqTableColumn;

	@FXML private TableColumn<Catalog, Integer> creditHoursTableColumn;

	@FXML private Button editCatalogButton;

	@FXML private TableColumn<Catalog, Integer> schoolIDTableColumn;



	/**
	 * The method of initializing the visual components described in the FXML document
	 *
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Write an empty string instead of "No content in table":
		//catalogListTable.setPlaceholder(new Label(""));


		c_codeTableColumn.setCellValueFactory(new PropertyValueFactory<Catalog, String>("c_code"));
		c_titleTableColumn.setCellValueFactory(new PropertyValueFactory<Catalog, String>("c_title"));
		creditHoursTableColumn.setCellValueFactory(new PropertyValueFactory<Catalog, Integer>("creditHours"));
		coreReqTableColumn.setCellValueFactory(new PropertyValueFactory<Catalog, Boolean>("coreRequirement"));
		c_descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<Catalog, String>("c_description"));


		try {

			loadCatalogList();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}		
	}




	/**
	 * This method will load/get/retrieve the courses from the database and load them into 
	 * the TableView object
	 */
	public void loadCatalogList() throws SQLException {

		// we create a Volunteer list, but it is empty, 
		// thus, we need to connect it to the database
		// List whose contents will be displayed in the table:
		ObservableList<Catalog> catalogList = FXCollections.observableArrayList();

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
			resultSet = statement.executeQuery("SELECT * FROM catalog");

			/*
			 * (4)  Create volunteer objects from EACH record.
			 * 			Loop over the list of records obtained, 
			 * 				and, for each item/record we create a Volunteer object. 
			 * */

			while (resultSet.next()) {
				//			String sql = "INSERT INTO courses 
				//(c_code, c_title, creditHours, coreRequirement, c_description, schoolID)

				Catalog newCatalog = new Catalog(resultSet.getString("c_code"),
						resultSet.getString("c_title"),
						resultSet.getInt("creditHours"),
						resultSet.getBoolean("coreRequirement"),
						resultSet.getString("c_description"),
						resultSet.getInt("schoolID"));
				
				newCatalog.setCatalogID(resultSet.getInt("catalogID"));


				// Add the newly created Catalog into our ObservableList.
				catalogList.add(newCatalog);
			}

			// 
			catalogListTable.getItems().addAll(catalogList);

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



	@FXML public void newCatalogButtonPushed(ActionEvent event) throws IOException {

		// we create a SceneChanger object.
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "NewEditCourseView.fxml", "New Catalog ");

	}

	@FXML public void editCatalogButtonPushed(ActionEvent event) throws IOException{
		// we create a SceneChanger object.
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "NewEditCourseView.fxml", "New Catalog ");
	}

	/**
	 * Create a file selection dialog
	 *
	 * @param title window title
	 */
	public static FileChooser getFileChooser(String title) {
		FileChooser fileChooser = new FileChooser();
		// We are starting to search from the current folder:
		fileChooser.setInitialDirectory(new File("."));
		// Set filters to find files:
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("All files (*.*)", "*.*"));
		// Specify the title of the window:
		fileChooser.setTitle(title);
		return fileChooser;
	}

	/**
	 * Dialog box for an arbitrary message
	 *
	 * @param message message text
	 */
	public static void showMessage(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("");
		alert.setHeaderText(message);
		alert.showAndWait();
	}

public void logoutButttonPushed(ActionEvent event) throws IOException {
		
		SceneChanger.setLoggedInUser(null);
		
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "LoginView.fxml", "Login");	
	}


}
