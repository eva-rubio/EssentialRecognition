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
import java.util.ResourceBundle;

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

/**
 * 
 * CourseListViewController is designed to show details of all the Courses offered by the University
 *  
 * 
 * @author Eva Rubio
 *
 */
public class CourseListViewController implements Initializable{

	@FXML
	private TableView<Course> courseListTable;

	@FXML
	private TableColumn<Course, String> schoolColumn;

	@FXML
	private Button createCourseButton;

	@FXML
	private Button editCourseButton;

	@FXML private TextField textFieldCountry;

	@FXML private TextField textFieldArea;

	@FXML private TextField textFieldText;

	@FXML private TextArea textAreaResults;

	@FXML private TableColumn<Course, String> courseIDtableColumn;

	@FXML private TableColumn<Course, String> titleTableColumn;

	@FXML private TableColumn<Course, String> facultyTableColumn;

	@FXML private TableColumn<Course, String> semesterTableColumn;

	@FXML private TableColumn<Course, String> scheduleTableColumn;

	@FXML private TableColumn<Course, String> capacityTableColumn;

	/**
	 * This method will load/get/retrieve the courses from the database and load them into 
	 * the TableView object
	 */
	public void loadVolunteers() throws SQLException {

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
						resultSet.getInt("max_capacity"));
	

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

	/**
     * The method of initializing the visual components described in the FXML document
     *
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Write an empty string instead of "No content in table":
		courseListTable.setPlaceholder(new Label(""));
		
		
		courseIDtableColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("firstName"));
		
		
		
		
		
		
		

	}

	@FXML public void createCourseButtonPushed(ActionEvent event) throws IOException {
		
		// we create a SceneChanger object.
				SceneChanger sc = new SceneChanger();
				sc.changeScenes(event, "NewCourseView.fxml", "New Course ");
		
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

	@FXML public void doSearchByWord(ActionEvent event) {}

	@FXML public void doSearchBySubstring(ActionEvent event) {}
	
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
