/**
 * 
 */
package views.adminarea;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;

/**
 * @author Eva Rubio
 *
 */
public class NewCourseViewController implements Initializable{


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


	@FXML RadioButton oneCreditRadioButton;
	@FXML RadioButton twoCreditRadioButton;
	@FXML RadioButton threeCreditRadioButton;
	@FXML RadioButton fourCreditRadioButton;
	@FXML private Label radioButtonLabel;
	@FXML TextArea longDescriptionTextArea;

//	@FXML ChoiceBox buildingChoiceBox;
	@FXML TextField roomNumberTextField;

	private ToggleGroup creditHoursToggleGroup;
	@FXML ChoiceBox<String> semesterChoiceBox;
	@FXML ChoiceBox<String> departmentChoiceBox;
	@FXML ChoiceBox<String> facultyChoiceBox;
	@FXML ChoiceBox<String> statusCourseChoiceBox;
	
	@FXML Label semesterLabel;
	@FXML Label departmentLabel;
	@FXML Label facultyLabel;
	@FXML Label statusLabel;
	@FXML Label descriptionLabel;
	
	@FXML Spinner<Integer> capacitySpinner;
	@FXML Spinner<Integer> startMeetingTimeSpinner;
	@FXML Spinner<Integer> endMeetingTimeSpinner;


	@FXML
    public void radioButtonChanged() {
        if (this.creditHoursToggleGroup.getSelectedToggle().equals(this.oneCreditRadioButton))
            radioButtonLabel.setText("( 1 )");
        
        if (this.creditHoursToggleGroup.getSelectedToggle().equals(this.twoCreditRadioButton))
            radioButtonLabel.setText("( 2 )");
        
        if (this.creditHoursToggleGroup.getSelectedToggle().equals(this.threeCreditRadioButton))
            radioButtonLabel.setText("( 3 )");
        
        if (this.creditHoursToggleGroup.getSelectedToggle().equals(this.fourCreditRadioButton))
            radioButtonLabel.setText("( 4 )");
        
    }
	
	/**
	 * https://www.youtube.com/watch?v=0aZy81etURE
	 * 
	 * */
	@FXML
	public void statusCourseButtonPushed() {
		statusLabel.setText(statusCourseChoiceBox.getValue().toString());
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
		radioButtonLabel.setText("");
		creditHoursToggleGroup = new ToggleGroup();
		this.oneCreditRadioButton.setToggleGroup(creditHoursToggleGroup);
		this.twoCreditRadioButton.setToggleGroup(creditHoursToggleGroup);
		this.threeCreditRadioButton.setToggleGroup(creditHoursToggleGroup);
		this.fourCreditRadioButton.setToggleGroup(creditHoursToggleGroup);
		
		statusLabel.setText("");
		statusCourseChoiceBox.getItems().addAll("Open","Closed","Reopened");
		statusCourseChoiceBox.setValue("Open");
		
		
		
		SpinnerValueFactory<Integer> capacityCourseValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 50);

		this.capacitySpinner.setValueFactory(capacityCourseValueFactory);
		capacitySpinner.setEditable(true);
	}
	
	
	
	
}
