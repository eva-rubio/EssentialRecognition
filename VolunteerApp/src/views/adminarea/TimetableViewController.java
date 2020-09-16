/**
 * 
 */
package views.adminarea;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Timetable;
import views.SceneChanger;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

/**
 * @author Eva Rubio
 *
 */
public class TimetableViewController implements Initializable {

	@FXML TableView<Timetable> timetableTable;
	@FXML TableColumn<Timetable, Integer> timetableIDTableColumn;
	@FXML TableColumn<Timetable, Integer> weekdayTableColumn;
	@FXML TableColumn<Timetable, Time> startTimeTableColumn;
	@FXML TableColumn<Timetable, Time> endTimeTableColumn;
	@FXML TableColumn<Timetable, Integer> sectionIDTableColumn;
	@FXML TableColumn<Timetable, Integer> roomIDTableColumn;
	@FXML TableColumn<Timetable, Integer> semesterIDTableColumn;
	@FXML Button newTimetableButton;
	@FXML Button editTimetableButton;
	@FXML Button mainMenuButton;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		/* Configures the table columns.
		 * The factories tell the table where it can get the information from. 
		 * */
		timetableIDTableColumn.setCellValueFactory(new PropertyValueFactory<Timetable, Integer>("timetableID"));
		weekdayTableColumn.setCellValueFactory(new PropertyValueFactory<Timetable, Integer>("weekday"));
		startTimeTableColumn.setCellValueFactory(new PropertyValueFactory<Timetable, Time>("startTime"));
		endTimeTableColumn.setCellValueFactory(new PropertyValueFactory<Timetable, Time>("endTime"));
		sectionIDTableColumn.setCellValueFactory(new PropertyValueFactory<Timetable, Integer>("sectionID"));
		roomIDTableColumn.setCellValueFactory(new PropertyValueFactory<Timetable, Integer>("roomID"));
		semesterIDTableColumn.setCellValueFactory(new PropertyValueFactory<Timetable, Integer>("semesterID"));
		try{
			//Updates the table with the modifications we just inserted.
			loadAllTimetables();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}
	}
	/**
	 * This method will load/get/retrieve the Timetables from the database and load them into 
	 * the TableView object.
	 * A TableView Object gets populated by an ObservableList.
	 */
	public void loadAllTimetables() throws SQLException {

		// we create a Timetable (observable)list, but it is empty rn, 
		// thus, we need to connect it to the database to populate it.
		// List whose contents will be displayed in the table:
		ObservableList<Timetable> timetablelist = FXCollections.observableArrayList();

		Connection conn = null;
		Statement statement = null;
		// ResultSet - this is what gets returned from the database. The LIST of records. 
		ResultSet resultSet = null;

		try {

			// (1) Establish a connection to our database.
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");
			System.out.println("loadAllTimetables() - Connection established successfully!");

			// (2)  Create a Statement object.
			statement = conn.createStatement();

			// (3)  Create the SQL query to execute. - Returns the complete list of address records.
			resultSet = statement.executeQuery("SELECT * FROM timetable");

			/*(4)  Create address objects from EACH record.
			 * 			Loop over the list of records obtained, 
			 * 				and, for each item/record we create a new Timetable object. 
			 * timetable (weekday, startTime, endTime, sectionID, roomID, semesterID)
			 * */
			while (resultSet.next()) {				
				Timetable newTimetable = new Timetable(resultSet.getInt("weekday"),
						resultSet.getTime("startTime"),
						resultSet.getTime("endTime"),
						resultSet.getInt("sectionID"),
						resultSet.getInt("roomID"),
						resultSet.getInt("semesterID"));
				// As we do NOT have the timetableID associated to each record:
				newTimetable.setTimetableID(resultSet.getInt("timetableID"));

				// Add the newly created Schedule object into our (Schedule) ObservableList.
				timetablelist.add(newTimetable);
			}

			/*
			 * Once our list has been fully populated, we add it to our TableView.
			 * 	TimetableTable.getItems() - Gets all the existing items that are in our table.
			 * 			Initially, the addressTable is EMPTY, thus, it first returns an Empty ObservableList.
			 * 	TimetableTable.getItems().addAll(Timetablelist) - Adds to this list all of our address records.*/
			timetableTable.getItems().addAll(timetablelist);

		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			System.out.println(e.getCause());
			StackTraceElement l = new Exception().getStackTrace()[0];
			StackTraceElement ll = new Exception().getStackTrace()[1];
			StackTraceElement a = new Exception().getStackTrace()[2];

			e.printStackTrace();
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
			System.out.println(ll.getClassName()+"/"+ll.getMethodName()+":"+ll.getLineNumber());
			System.out.println(a.getClassName()+"/"+a.getMethodName()+":"+a.getLineNumber());
		}
		/* The finally block gets executed EVEN IF there is an exception.
		 * Close everything up so there is nothing lingering.  * */
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

	@FXML public void newTimetableButtonPushed(ActionEvent event)  throws IOException {
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "adminarea/AdminView.fxml", "Admin Main Menu");}

	@FXML public void editTimetableButtonPushed(ActionEvent event)  throws IOException {
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "adminarea/AdminView.fxml", "Admin Main Menu");
		}

	@FXML public void mainMenuButtonPushed(ActionEvent event)  throws IOException {
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "adminarea/AdminView.fxml", "Admin Main Menu");
		}

	@FXML public void logoutButttonPushed(ActionEvent event) throws IOException {

		SceneChanger.setLoggedInUser(null);

		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "LoginView.fxml", "Login");	}

}
