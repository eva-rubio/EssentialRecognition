/**
 * 
 */
package admin;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * @author Eva Rubio
 *
 */
public class AdminViewController implements Initializable {

	@FXML TextField idTextField;
	@FXML TextField firstNameTextField;
	@FXML TextField lastNameTextField;
	@FXML TextField majorTextField;
	@FXML TextField emailTextField;
	@FXML TextField genderTextField;
	@FXML DatePicker dobDatePicker;
	@FXML Button addEntryButton;
	@FXML Button clearFormButton;
	@FXML Button loadDataButton;
	@FXML TableColumn sIDTableColumn;
	@FXML TableColumn sFirstNameTableColumn;
	@FXML TableColumn sLastNameTableColumn;
	@FXML TableColumn sDobTableColumn;
	@FXML TableColumn sMajorTableColumn;
	@FXML TableColumn sEmailTableColumn;
	@FXML TableColumn sGenderTableColumn;
	@FXML TableView studentsInfoTableView;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		
	}

}
