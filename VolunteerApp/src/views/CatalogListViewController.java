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
import models.Course;
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

	@FXML private TableView<Course> courseListTable;
	
	@FXML private TableColumn<Course, String> courseCodeTableColumn;

	@FXML private TableColumn<Course, String> titleTableColumn;

	@FXML private TableColumn<Course, String> facultyTableColumn;

	@FXML private TableColumn<Course, String> semesterTableColumn;

	@FXML private TableColumn<Course, LocalDate> startDateTableColumn;

	@FXML private TableColumn<Course, LocalDate> endDateTableColumn;

	@FXML TableColumn<Course, Integer> maxCapacityTableColumn;

	@FXML TableColumn<Course, Integer> currCapacityTableColumn;

	@FXML private TableColumn<Course, String> statusTableColumn;

	@FXML private Button createCourseButton;

	@FXML private Button clearFormButton;

	@FXML private Button loadDataButton;

	@FXML private CheckBox coreReqCheckBox;

	@FXML private TextField creditHoursTextField;

	@FXML private TableColumn<Course, Boolean> coreReqTableColumn;

	@FXML private TableColumn<Course, Integer> creditHoursTableColumn;

	@FXML private Button editCourseButton;

	@FXML private TableColumn<Course, Integer> idSchoolTableColumn;


	
	/**
	 * The method of initializing the visual components described in the FXML document
	 *
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Write an empty string instead of "No content in table":
		//courseListTable.setPlaceholder(new Label(""));


		courseCodeTableColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("course_code"));
		titleTableColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("title"));

		facultyTableColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("instructor_name"));
		semesterTableColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("semester"));
		startDateTableColumn.setCellValueFactory(new PropertyValueFactory<Course, LocalDate>("startDate"));
		endDateTableColumn.setCellValueFactory(new PropertyValueFactory<Course, LocalDate>("endDate"));
		maxCapacityTableColumn.setCellValueFactory(new PropertyValueFactory<Course, Integer>("max_capacity"));
		currCapacityTableColumn.setCellValueFactory(new PropertyValueFactory<Course, Integer>("curr_capacity"));
		statusTableColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("status"));
		
		coreReqTableColumn.setCellValueFactory(new PropertyValueFactory<Course, Boolean>("coreRequirement"));
		creditHoursTableColumn.setCellValueFactory(new PropertyValueFactory<Course, Integer>("creditHours"));
		
		idSchoolTableColumn.setCellValueFactory(new PropertyValueFactory<Course, Integer>("creditHours"));


		try {

			loadCourses();

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
	public void loadCourses() throws SQLException {

		// we create a Volunteer list, but it is empty, 
		// thus, we need to connect it to the database
		// List whose contents will be displayed in the table:
		ObservableList<Course> coursesList = FXCollections.observableArrayList();

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
			resultSet = statement.executeQuery("SELECT * FROM courses");

			/*
			 * (4)  Create volunteer objects from EACH record.
			 * 			Loop over the list of records obtained, 
			 * 				and, for each item/record we create a Volunteer object. 
			 * */

			while (resultSet.next()) {
				//			String sql = "INSERT INTO courses 
				//(course_code, title, startDate, endDate, instructor_name, status, semester, curr_capacity, max_capacity)"


				Course newCourse = new Course(resultSet.getString("course_code"),
						resultSet.getString("title"),
						resultSet.getDate("startDate").toLocalDate(),
						resultSet.getDate("endDate").toLocalDate(),
						resultSet.getString("instructor_name"),
						resultSet.getString("status"),
						resultSet.getString("semester"),
						resultSet.getInt("curr_capacity"),
						resultSet.getInt("max_capacity"),
						resultSet.getBoolean("coreRequirement"),
						resultSet.getInt("creditHours"),
						resultSet.getInt("id_school"));


				// Add the newly created Volunteer into our ObservableList.
				coursesList.add(newCourse);
			}

			// 
			courseListTable.getItems().addAll(coursesList);

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

	

	@FXML public void createNewCourseButtonPushed(ActionEvent event) throws IOException {

		// we create a SceneChanger object.
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "NewEditCourseView.fxml", "New Course ");

	}

	@FXML public void editCourseButtonPushed(ActionEvent event) {}

	@FXML public void doNew(ActionEvent event) {}

	@FXML public void doOpen(ActionEvent event) {

	}

	@FXML public void doSave(ActionEvent event) {}

	@FXML public void doExit(ActionEvent event) {
		Platform.exit(); // correct completion of the JavaFX application
	}

	@FXML public void doAdd(ActionEvent event) {}

	@FXML public void doRemove(ActionEvent event) {}

	@FXML public void doAbout(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About...");
		alert.setHeaderText("Population censuses data");
		alert.setContentText("Version 1.0");
		alert.showAndWait();
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

	/**
	 * Error dialog box
	 *
	 * @param message message text
	 */
	public static void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(message);
		alert.showAndWait();
	}


}
