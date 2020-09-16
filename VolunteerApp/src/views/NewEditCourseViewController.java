/**
 * 
 */
package views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import models.Address;
import models.Catalog;
import models.Human;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.DatePicker;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/**
 * @author Eva Rubio
 *
 */
public class NewEditCourseViewController implements Initializable, ControllerClass{

	@FXML TextField codeTextField;
	@FXML TextField titleTextField;
	@FXML CheckBox coreReqCheckBox;

	@FXML RadioButton oneCredHrsRadioButton;
	@FXML RadioButton twoCredHrsRadioButton;
	@FXML RadioButton threeCredHrsRadioButton;
	@FXML RadioButton fourCredHrsRadioButton;
	private ToggleGroup creditHoursToggleGroup;

	@FXML Button saveCourseButton;
	@FXML Button cancelButton;

	private Catalog catalog;
	@FXML Spinner<Integer> idSchoolSpinner;
	@FXML TextArea descriptionTextArea;
	/**
	 * This method will read from the scene and try to create a new instance of a Human.
	 * If a Human was successfully created, it is updated in the database.
	 * 
	 * @param event The event triggered when the button was clicked.
	 */
	public void saveCourseButtonPushed(ActionEvent event) {

		int schoolIDvalue =  idSchoolSpinner.getValue();
		
		try {
			// if we are updating
			if(catalog != null) {

			} else {
				catalog = new Catalog(codeTextField.getText(), titleTextField.getText(), 
						credHrsRadioButtonChanged(), coreReqCheckBox.isSelected(),
						descriptionTextArea.getText(), schoolIDvalue);
				
				System.out.println(codeTextField.getText());
				System.out.println(titleTextField.getText());
				System.out.println(credHrsRadioButtonChanged());
				System.out.println(coreReqCheckBox.isSelected());
				System.out.println(descriptionTextArea.getText());
				System.out.println(schoolIDvalue);
				
			}

			catalog.insertCatalogIntoDB();

			// if NO error occurred  ->  Change the Scene to the TableView. 
			SceneChanger sc = new SceneChanger();
			sc.changeScenes(event, "CatalogListView.fxml", "Course Catalog");

		} catch(Exception e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());

		}

	}


	/**
	 * This method will change back to the TableView of humans without adding a user. 
	 * 
	 * ALL data in the form will be LOST.
	 */
	public void cancelButtonPushed(ActionEvent event) throws IOException {
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "CatalogListView.fxml", "Course Catalog");
	}








	/**
	 * This method will update the radioButtonLabel when ever a different
	 * radio button is pushed
	 */
	@FXML
	public int credHrsRadioButtonChanged() {
	    
		if (this.creditHoursToggleGroup.getSelectedToggle().equals(this.oneCredHrsRadioButton)) {
			return 1;
		}

		if (this.creditHoursToggleGroup.getSelectedToggle().equals(this.twoCredHrsRadioButton)) {
			return 2;
		}

		if (this.creditHoursToggleGroup.getSelectedToggle().equals(this.threeCredHrsRadioButton)) {
			return 3;
		}

		if (this.creditHoursToggleGroup.getSelectedToggle().equals(this.fourCredHrsRadioButton)) {
			return 4;
		}

		return 1;
	}





	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {


		
		//These items are for configuring the RadioButtons

		creditHoursToggleGroup = new ToggleGroup();
		this.oneCredHrsRadioButton.setToggleGroup(creditHoursToggleGroup);
		this.twoCredHrsRadioButton.setToggleGroup(creditHoursToggleGroup);
		this.threeCredHrsRadioButton.setToggleGroup(creditHoursToggleGroup);
		this.fourCredHrsRadioButton.setToggleGroup(creditHoursToggleGroup);




		SpinnerValueFactory<Integer> idSchoolValueFact = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,9,1);

		idSchoolSpinner.setValueFactory(idSchoolValueFact);


	}


	@Override
	public void preloadData(Human human) {
		// TODO Auto-generated method stub

		human = null;
	}


	@Override
	public void preloadData(Address address) {
		// TODO Auto-generated method stub
		
	}

}
