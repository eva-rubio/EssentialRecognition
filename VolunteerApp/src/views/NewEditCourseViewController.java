/**
 * 
 */
package views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import models.Course;
import models.Human;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.DatePicker;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;

/**
 * @author Eva Rubio
 *
 */
public class NewEditCourseViewController implements Initializable, ControllerClass{

	@FXML TextField codeTextField;
	@FXML TextField titleTextField;
	@FXML TextField instructorTextField;
	@FXML TextField semesterTextField;
	@FXML DatePicker startDateDatePicker;
	@FXML DatePicker endDateDatePicker;
	@FXML CheckBox coreReqCheckBox;

	@FXML RadioButton zeroCreditsRadioButton;
	@FXML RadioButton oneCreditsRadioButton;
	@FXML RadioButton twoCreditsRadioButton;
	@FXML RadioButton threeCreditsRadioButton;
	@FXML RadioButton fourCreditsRadioButton;
	private ToggleGroup creditHoursToggleGroup;

	@FXML Spinner<Integer> currCapacitySpinner;
	@FXML Spinner<Integer> maxCapacitySpinner;
	@FXML ChoiceBox<String> statusChoiceBox;
	@FXML Button saveCourseButton;
	@FXML Button cancelButton;

	private Course course;
	@FXML Spinner<Integer> idSchoolSpinner;



	/**
	 * This method will read from the scene and try to create a new instance of a Human.
	 * If a Human was successfully created, it is updated in the database.
	 * 
	 * @param event The event triggered when the button was clicked.
	 */
	public void saveCourseButtonPushed(ActionEvent event) {

		try {
			// if we are updating
			if(course != null) {

			} else {
				course = new Course(codeTextField.getText(), titleTextField.getText(), startDateDatePicker.getValue(), endDateDatePicker.getValue(),
						instructorTextField.getText(), statusChoiceBox.getValue().toString(), semesterTextField.getText(), (int)currCapacitySpinner.getValue(), (int)maxCapacitySpinner.getValue(), coreReqCheckBox.isSelected(), radioButtonChanged(),
						(int)idSchoolSpinner.getValue());
			}

			course.insertCourseIntoDB();

			// if NO error occurred  ->  Change the Scene to the TableView. 
			SceneChanger sc = new SceneChanger();
			sc.changeScenes(event, "DetailsCourseView.fxml", "Course Catalog");
			
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
		sc.changeScenes(event, "DetailsCourseView.fxml", "Course Catalog");
	}








	/**
	 * This method will update the radioButtonLabel when ever a different
	 * radio button is pushed
	 */
	@FXML
	public int radioButtonChanged() {
		if (this.creditHoursToggleGroup.getSelectedToggle().equals(this.zeroCreditsRadioButton)) {
			return 0;
		}        
		if (this.creditHoursToggleGroup.getSelectedToggle().equals(this.oneCreditsRadioButton)) {
			return 1;
		}

		if (this.creditHoursToggleGroup.getSelectedToggle().equals(this.twoCreditsRadioButton)) {
			return 2;
		}

		if (this.creditHoursToggleGroup.getSelectedToggle().equals(this.threeCreditsRadioButton)) {
			return 3;
		}

		if (this.creditHoursToggleGroup.getSelectedToggle().equals(this.fourCreditsRadioButton)) {
			return 4;
		}

		return 0;
	}





	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {


		//These items are for configuring the RadioButtons

		creditHoursToggleGroup = new ToggleGroup();
		this.zeroCreditsRadioButton.setToggleGroup(creditHoursToggleGroup);
		this.oneCreditsRadioButton.setToggleGroup(creditHoursToggleGroup);
		this.twoCreditsRadioButton.setToggleGroup(creditHoursToggleGroup);
		this.threeCreditsRadioButton.setToggleGroup(creditHoursToggleGroup);
		this.fourCreditsRadioButton.setToggleGroup(creditHoursToggleGroup);

		// 8 is the regular work day
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100,0);
		SpinnerValueFactory<Integer> valueFactMax = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100,50);
		SpinnerValueFactory<Integer> idSchoolValueFact = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,9,1);

		currCapacitySpinner.setValueFactory(valueFactory);
		maxCapacitySpinner.setValueFactory(valueFactMax);
		idSchoolSpinner.setValueFactory(idSchoolValueFact);
		
		statusChoiceBox.getItems().addAll("Open", "Closed", "Reopened");
		statusChoiceBox.setValue("Open");

	}


	@Override
	public void preloadData(Human human) {
		// TODO Auto-generated method stub
		
	}

}
