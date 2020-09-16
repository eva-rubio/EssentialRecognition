/**
 * 
 */
package views;


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
public class AddressTableViewController implements Initializable {

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
	@FXML Button clearButton;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		saveButton.setDisable(true);
		streetTextField.setDisable(true);
		cityTextField.setDisable(true);
		stateTextField.setDisable(true);
		zipTextField.setDisable(true);
		countryTextField.setDisable(true);
		onCampusCheckBox.setDisable(true);
		
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
		ObservableList<Address> theaddresslist = FXCollections.observableArrayList();

		Connection conn = null;
		Statement statement = null;
		// ResultSet - this is what gets returned from the database. The LIST of records. 
		ResultSet resultSet = null;

		try {

			// (1) Establish a connection to our database.
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");
			System.out.println("loadAllAddresses() - Connection established successfully!");

			// (2)  Create a Statement object.
			statement = conn.createStatement();

			// (3)  Create the SQL query to execute. - Returns the complete list of address records.
			resultSet = statement.executeQuery("SELECT * FROM addresslist");

			/*
			 * (4)  Create address objects from EACH record.
			 * 			Loop over the list of records obtained, 
			 * 				and, for each item/record we create a new Address object. 
			 * */
			// public Address(String street, String city, String state, String zipCode, String country, boolean onCampusHousing) 
			System.out.println("SIZE IS: "+resultSet.getFetchSize());
			
			while (resultSet.next()) {
				Address newAddress = new Address(resultSet.getString("street"),
						resultSet.getString("city"),
						resultSet.getString("state"),
						resultSet.getString("zipCode"),
						resultSet.getString("country"),
						resultSet.getBoolean("onCampusHousing"));
				// As we do NOT have the addressID associated to each record:
				newAddress.setAddressID(resultSet.getInt("addressID"));

				// Add the newly created Addrsess object into our (Address) ObservableList.
				theaddresslist.add(newAddress);
			}

			/*
			 * Once our list has been fully populated, we add it to our TableView.
			 * 	addressTable.getItems() - Gets all the existing items that are in our table.
			 * 			Initially, the addressTable is EMPTY, thus, it first returns an Empty ObservableList.
			 * 	addressTable.getItems().addAll(addresslist) - Adds to this list all of our address records.
			 * 			 
			 * */
			addressTable.getItems().addAll(theaddresslist);

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
	 * If a human record has been selected in the table, enable the edit button.
	 */
	public void addressSelected() {

		saveButton.setDisable(false);

	}

	/**
	 * If the edit button is pushed, pass the selected Human and all its info to the NewUserView. 
	 * Then, pre-load the Scene with the data passed.
	 */
	public void saveAddressButtonPushed(ActionEvent event) throws IOException {

		SceneChanger sc = new SceneChanger();

		//get the human object that has been selected from the table. 
		Address address = this.addressTable.getSelectionModel().getSelectedItem();

		// the Controller class to pass in
		NewUserViewController nuvc = new NewUserViewController();


		sc.changeScenes(event, "NewUserView.fxml", "Edit Person Details", address, nuvc );

	}
	/**
	 * This method will change back to the TableView of humans WITHOUT adding a user. 
	 * 
	 * ALL data in the form will be LOST.
	 */
	public void cancelButtonPushed(ActionEvent event) throws IOException {

		SceneChanger sc = new SceneChanger();
		
		sc.changeScenes(event,"HumanTableView.fxml", "All Humans"); 
	}
}
