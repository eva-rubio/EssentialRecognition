/**
 * 
 */
package views.adminarea;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * @author Eva Rubio
 *
 */
public class NewCatalogViewController implements Initializable {

	@FXML TextArea descriptionTextArea;
	@FXML Spinner<Integer> schoolSpinner;
	@FXML RadioButton yesCoreReqRadioButton;
	@FXML RadioButton noCoreReqRadioButton;
	@FXML RadioButton oneCredHrsRadioButton;
	@FXML RadioButton twoCredHrsRadioButton;
	@FXML RadioButton threeCredHrsRadioButton;
	@FXML RadioButton fourCredHrsRadioButton;
	@FXML TextField codeTextField;
	@FXML TextField titleTextField;
	@FXML Button createCatalogBtn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
