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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Address;
import models.Human;

/**
 * @author Eva Rubio
 *
 */
public class AddressTableViewController implements Initializable, ControllerClass {

	@FXML private TextField streetTextField;
	@FXML private TextField cityTextField;
	@FXML private TextField stateTextField;
	@FXML private TextField zipTextField;
	@FXML private TextField countryTextField;
	@FXML private CheckBox onCampusCheckBox;
	@FXML private Button saveButton;
	// need to use the wrapper class Integer
	@FXML private TableView<Address> addressTable;
	@FXML private TableColumn<Address, Integer> addressIDColumn;
	@FXML private TableColumn<Address, String> streetColumn;
	@FXML private TableColumn<Address, String> cityColumn;
	@FXML private TableColumn<Address, String> stateColumn;
	@FXML private TableColumn<Address, String> zipColumn;
	@FXML private TableColumn<Address, String> countryColumn;
	@FXML private TableColumn<Address, Boolean> onCampusColumn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		/*
		 * Configures the table columns.
		 * The factories tell the table where it can get the information from. 
		 * */
		addressIDColumn.setCellValueFactory(new PropertyValueFactory<Address, Integer>("addressID"));
		streetColumn.setCellValueFactory(new PropertyValueFactory<Address, String>("street"));
		cityColumn.setCellValueFactory(new PropertyValueFactory<Address, String>("city"));
		stateColumn.setCellValueFactory(new PropertyValueFactory<Address, String>("state"));
		zipColumn.setCellValueFactory(new PropertyValueFactory<Address, String>("zipCode"));
		countryColumn.setCellValueFactory(new PropertyValueFactory<Address, String>("country"));
		onCampusColumn.setCellValueFactory(new PropertyValueFactory<Address, Boolean>("onCampusHousing"));
		
		try{
			//Updates the table with the modifications we just inserted.
			loadAllAddresses();
			
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}
	}
	/**
	 * This method will load/get/retrieve the addresses from the database and load them into 
	 * the TableView object.
	 * 
	 * A TableView Object gets populated by an ObservableList.
	 */
	public void loadAllAddresses() throws SQLException {

		// we create an Address (observable)list, but it is empty rn, 
		// thus, we need to connect it to the database to populate it.
		 // List whose contents will be displayed in the table:
		ObservableList<Address> addresslist = FXCollections.observableArrayList();

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
			resultSet = statement.executeQuery("SELECT * FROM addresslist");

			/*
			 * (4)  Create human objects from EACH record.
			 * 			Loop over the list of records obtained, 
			 * 				and, for each item/record we create a new Address object. 
			 * */
// public Address(String street, String city, String state, String zipCode, String country, boolean onCampusHousing) 
			while (resultSet.next()) {
				Address newAddress = new Address(resultSet.getString("street"),
						resultSet.getString("city"),
						resultSet.getString("state"),
						resultSet.getString("zipCode"),
						resultSet.getString("country"),
						resultSet.getBoolean("onCampusHousing"));
				// As we do NOT have the humanID associated to each record:
				newAddress.setAddressID(resultSet.getInt("addressID"));

				// Add the newly created Human object into our (Human) ObservableList.
				addresslist.add(newAddress);
			}

			/*
			 * Once our list has been fully populated, we add it to our TableView.
			 * 	humanTable.getItems() - Gets all the existing items that are in our table.
			 * 			Initially, the humanTable is EMPTY, thus, it first returns an Empty ObservableList.
			 * 	humanTable.getItems().addAll(addresslist) - Adds to this list all of our human records.
			 * 			 
			 * */
			addressTable.getItems().addAll(addresslist);

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
	
	@Override
	public void preloadData(Human human) {
		// TODO Auto-generated method stub
		
	}

}
