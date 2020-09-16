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
import views.SceneChanger;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.RadioButton;

/**
 * @author Eva Rubio
 *
 */
public class NewSectionViewController implements Initializable{


	@FXML Button resetFormButton;
	@FXML Button createNewCourseButton;

	@FXML TextField courseIDTextField;
	@FXML TextField titleCourseTextField;

	@FXML Spinner<Integer> capacitySpinner;
	@FXML Spinner<Integer> startMeetingTimeSpinner;
	@FXML Spinner<Integer> endMeetingTimeSpinner;
	@FXML TextField nameSectionTextField;
	@FXML ChoiceBox sectFormatChoiceBox;
	@FXML TextField facultyIDTextField;
	@FXML RadioButton inPersonRadioButton;
	@FXML RadioButton onlineRadioButton;
	@FXML RadioButton hybridRadioButton;
	@FXML TextField scheduleIDTextField;
	@FXML TextField roomIDTextField;
	@FXML Button cancelButton;
	@FXML Button createNewSectionButton;
	@FXML ChoiceBox semesterChoiceBox;


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




		//SpinnerValueFactory<Integer> capacityCourseValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 50);

		//this.capacitySpinner.setValueFactory(capacityCourseValueFactory);
		//capacitySpinner.setEditable(true);	
	}


	@FXML public void cancelButtonPushed(ActionEvent event) throws IOException {
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "adminarea/SectionTableView.fxml", "Section List");
		}

	@FXML public void createNewSectionButtonPushed(ActionEvent event)  throws IOException {
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "adminarea/SectionTableView.fxml", "Section List");
		}


}
