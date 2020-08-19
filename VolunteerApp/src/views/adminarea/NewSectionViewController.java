/**
 * 
 */
package views.adminarea;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Building;
import models.Human;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

/**
 * @author Eva Rubio
 *
 */
public class NewSectionViewController implements Initializable{


	@FXML Button resetFormButton;
	@FXML Button createNewCourseButton;


	@FXML CheckBox mondayCheckBox;
	@FXML CheckBox tuesdayCheckBox;
	@FXML CheckBox wednesdayCheckBox;
	@FXML CheckBox thursdayCheckBox;
	@FXML CheckBox fridayCheckBox;
	@FXML CheckBox saturdayCheckBox;
	@FXML CheckBox sundayCheckBox;

	@FXML TextField courseIDTextField;
	@FXML TextField titleCourseTextField;

	/*
	 * @FXML RadioButton oneCreditRadioButton;
	 * 
	 * @FXML RadioButton twoCreditRadioButton;
	 * 
	 * @FXML RadioButton threeCreditRadioButton;
	 * 
	 * @FXML RadioButton fourCreditRadioButton;
	 * 
	 * @FXML private Label radioButtonLabel;
	 * 
	 * @FXML TextArea longDescriptionTextArea;
	 */


	@FXML ChoiceBox<String> semesterChoiceBox;
	@FXML ChoiceBox<String> departmentChoiceBox;
	@FXML ChoiceBox<String> facultyChoiceBox;
	@FXML ChoiceBox<String> statusChoiceBox;

	@FXML Label semesterLabel;
	@FXML Label departmentLabel;
	@FXML Label facultyLabel;
	@FXML Label statusLabel;
	@FXML Label descriptionLabel;

	@FXML Spinner<Integer> capacitySpinner;
	@FXML Spinner<Integer> startMeetingTimeSpinner;
	@FXML Spinner<Integer> endMeetingTimeSpinner;
	@FXML TextField nameSectionTextField;

	@FXML ChoiceBox sectFormatChoiceBox;
	@FXML TextField facultyIDTextField;
	@FXML Button selectCatalogBtn;
	@FXML Label courseIDLabel;


	//@FXML ChoiceBox<String> buildingChoiceBox;
	//@FXML ChoiceBox roomNumChoiceBox;
	@FXML Button createNewSectionButton;
	@FXML ComboBox buildingComboBox;
	@FXML ChoiceBox roomNumChoiceBox;


	@FXML TableView<Building> buildingTableView;
	@FXML TableColumn<Building, Integer> buildingIDColumn;
	@FXML TableColumn<Building, String> buildingNameColumn;
	@FXML TableColumn<Building, Integer> b_typeColumn;

	/*
	 * @FXML public void radioButtonChanged() { if
	 * (this.creditHoursToggleGroup.getSelectedToggle().equals(this.
	 * oneCreditRadioButton)) radioButtonLabel.setText("( 1 )");
	 * 
	 * if (this.creditHoursToggleGroup.getSelectedToggle().equals(this.
	 * twoCreditRadioButton)) radioButtonLabel.setText("( 2 )");
	 * 
	 * if (this.creditHoursToggleGroup.getSelectedToggle().equals(this.
	 * threeCreditRadioButton)) radioButtonLabel.setText("( 3 )");
	 * 
	 * if (this.creditHoursToggleGroup.getSelectedToggle().equals(this.
	 * fourCreditRadioButton)) radioButtonLabel.setText("( 4 )");
	 * 
	 * }
	 */

	/**
	 * https://www.youtube.com/watch?v=0aZy81etURE
	 * 
	 * */
	@FXML
	public int statusButtonPushed() {
		statusLabel.setText(statusChoiceBox.getValue().toString());

		//get the human object that has been selected from the table. 
		Building building = this.buildingTableView.getSelectionModel().getSelectedItem();
		System.out.println(building.getBuildingID());
		return building.getBuildingID();

	}

	/**
	 * Checks if any of our CheckBoxes have been selected or not.
	 * Creates a string with the appropiate selected topping. 
	 */
	@FXML
	public void pizzaOrderButtonPushed() {
		String order = "Toppings are:";

		if (mondayCheckBox.isSelected())
			order += "\nPineapple";

		if (tuesdayCheckBox.isSelected())
			order += "\nPepperoni";

		if (wednesdayCheckBox.isSelected())
			order += "\nBacon";

		// set its text to the string that we have just been creating. 
		//this.pizzaOrderLabel.setText(order);
	}

	@FXML public void createNewCourseButtonPushed(ActionEvent event) {

	}

	@FXML public void resetFormButtonPushed(ActionEvent event) {

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//These are for configuring the RadioButtons
		// toggle group is for grouping them together.
		/*
		 * radioButtonLabel.setText(""); creditHoursToggleGroup = new ToggleGroup();
		 * this.oneCreditRadioButton.setToggleGroup(creditHoursToggleGroup);
		 * this.twoCreditRadioButton.setToggleGroup(creditHoursToggleGroup);
		 * this.threeCreditRadioButton.setToggleGroup(creditHoursToggleGroup);
		 * this.fourCreditRadioButton.setToggleGroup(creditHoursToggleGroup);
		 */

		statusLabel.setText("");
		statusChoiceBox.getItems().addAll("Open","Closed","Reopened");
		statusChoiceBox.setValue("Open");



		SpinnerValueFactory<Integer> capacityCourseValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 50);

		this.capacitySpinner.setValueFactory(capacityCourseValueFactory);
		capacitySpinner.setEditable(true);



		/*
		 * Configures the table columns.
		 * The factories tell the table where it can get the information from. 
		 * */
		buildingIDColumn.setCellValueFactory(new PropertyValueFactory<Building, Integer>("buildingID"));
		buildingNameColumn.setCellValueFactory(new PropertyValueFactory<Building, String>("buildingName"));
		b_typeColumn.setCellValueFactory(new PropertyValueFactory<Building, Integer>("b_type"));

		try{
			//Updates the table with the modifications we just inserted.
			loadAllBuildings();


		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}
	}



	/**
	 * This method will load/get/retrieve the humans from the database and load them into 
	 * the TableView object.
	 * 
	 * A TableView Object gets populated by an ObservableList.
	 */
	public void loadAllBuildings() throws SQLException {

		// we create a Human (observable)list, but it is empty rn, 
		// thus, we need to connect it to the database to populate it.
		// List whose contents will be displayed in the table:
		ObservableList<Building> buildinglist = FXCollections.observableArrayList();

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
			resultSet = statement.executeQuery("SELECT * FROM building");

			/*
			 * (4)  Create human objects from EACH record.
			 * 			Loop over the list of records obtained, 
			 * 				and, for each item/record we create a new Human object. 
			 * */

			// public Human(int h_type, String firstName, String lastName, String phoneNumber, LocalDate dob, File imageFile, String password, String email, int gender, int addressID) 


			while (resultSet.next()) {
				Building newBuilding = new Building(resultSet.getInt("buildingID"),
						resultSet.getString("buildingName"),
						resultSet.getInt("b_type"));
				// Add the newly created Human object into our (Human) ObservableList.
				buildinglist.add(newBuilding);
			}

			/*
			 * Once our list has been fully populated, we add it to our TableView.
			 * 	humanTable.getItems() - Gets all the existing items that are in our table.
			 * 			Initially, the humanTable is EMPTY, thus, it first returns an Empty ObservableList.
			 * 	humanTable.getItems().addAll(humanlist) - Adds to this list all of our human records.
			 * 			 
			 * */
			buildingTableView.getItems().addAll(buildinglist);

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


}
