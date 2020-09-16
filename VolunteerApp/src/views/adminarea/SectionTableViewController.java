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
import models.Schedule;
import models.Section;
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
public class SectionTableViewController implements Initializable {

	@FXML TableView<Section> sectionTable;
	@FXML TableColumn<Section, String> sect_nameTableColumn;
	@FXML TableColumn<Section, Integer> catalogIDTableColumn;
	@FXML TableColumn<Section, Integer> facultyIDTableColumn;
	@FXML TableColumn<Section, Integer> sect_formatTableColumn;
	@FXML TableColumn<Section, Integer> semesterOfferedIDTableColumn;
	@FXML TableColumn<Section, Integer> sect_statusTableColumn;
	@FXML TableColumn<Section, Integer> curr_capacityTableColumn;
	@FXML TableColumn<Section, Integer> max_capacityTableColumn;
	@FXML Button newSectionButton;
	@FXML Button editSectionButton;
	@FXML Button mainMenuButton;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		/* Configures the table columns.
		 * The factories tell the table where it can get the information from. */
		sect_nameTableColumn.setCellValueFactory(new PropertyValueFactory<Section, String>("sect_name"));
		catalogIDTableColumn.setCellValueFactory(new PropertyValueFactory<Section, Integer>("catalogID"));
		facultyIDTableColumn.setCellValueFactory(new PropertyValueFactory<Section, Integer>("facultyID"));
		sect_formatTableColumn.setCellValueFactory(new PropertyValueFactory<Section, Integer>("sect_format"));
		semesterOfferedIDTableColumn.setCellValueFactory(new PropertyValueFactory<Section, Integer>("semesterOfferedID"));
		sect_statusTableColumn.setCellValueFactory(new PropertyValueFactory<Section, Integer>("sect_status"));
		curr_capacityTableColumn.setCellValueFactory(new PropertyValueFactory<Section, Integer>("curr_capacity"));
		max_capacityTableColumn.setCellValueFactory(new PropertyValueFactory<Section, Integer>("max_capacity"));
		try{
			//Updates the table with the modifications we just inserted.
			loadAllSections();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}
	}

	/**
	 * This method will load/get/retrieve the sections from the database and load them into 
	 * the TableView object.
	 * A TableView Object gets populated by an ObservableList.
	 */
	public void loadAllSections() throws SQLException {

		// we create a section (observable)list, but it is empty rn, 
		// thus, we need to connect it to the database to populate it.
		// List whose contents will be displayed in the table:
		ObservableList<Section> sectionlist = FXCollections.observableArrayList();

		Connection conn = null;
		Statement statement = null;
		// ResultSet - this is what gets returned from the database. The LIST of records. 
		ResultSet resultSet = null;

		try {

			// (1) Establish a connection to our database.
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");
			System.out.println("loadAllSections() - Connection established successfully!");

			// (2)  Create a Statement object.
			statement = conn.createStatement();

			// (3)  Create the SQL query to execute. - Returns the complete list of address records.
			resultSet = statement.executeQuery("SELECT * FROM section");

			/*(4)  Create Section objects from EACH record.
			 * 			Loop over the list of records obtained, 
			 * 				and, for each item/record we create a new Section object.  
			 * section (sect_name, catalogID, facultyID, sect_format, semesterOfferedID, sect_status, curr_capacity, max_capacity */
			while (resultSet.next()) {				
				Section newSect = new Section(resultSet.getString("sect_name"),
						resultSet.getInt("catalogID"),
						resultSet.getInt("facultyID"),
						resultSet.getInt("sect_format"),
						resultSet.getInt("semesterOfferedID"),
						resultSet.getInt("sect_status"),
						resultSet.getInt("curr_capacity"),
						resultSet.getInt("max_capacity"));
				// As we do NOT have the sectionID associated to each record:
				newSect.setSectionID(resultSet.getInt("sectionID"));

				// Add the newly created section object into our (section) ObservableList.
				sectionlist.add(newSect);
			}

			sectionTable.getItems().addAll(sectionlist);

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
		/*The finally block gets executed EVEN IF there is an exception.
		 * Close everything up so there is nothing lingering. */
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

	@FXML public void newSectionButtonPushed(ActionEvent event) throws IOException {
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "adminarea/NewSectionView.fxml", "Create Section");
	}

	@FXML public void editSectionButtonPushed(ActionEvent event)  throws IOException {
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "adminarea/NewSectionView.fxml", "Edit Section");
	}

	@FXML public void mainMenuButtonPushed(ActionEvent event) throws IOException {
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "adminarea/AdminView.fxml", "Admin Main Menu");
	}

	@FXML public void logoutButttonPushed(ActionEvent event) throws IOException {
		SceneChanger.setLoggedInUser(null);
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "LoginView.fxml", "Login");	
	}


}
