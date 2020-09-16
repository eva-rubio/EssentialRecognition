/**
 * 
 */
package views.adminarea;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import views.SceneChanger;
import javafx.event.ActionEvent;

/**
 * @author Eva Rubio
 *
 */
public class AdminViewController implements Initializable {


	@FXML Button humanListButton;
	@FXML Button catalogListButton;
	@FXML Button sectionListButton;
	@FXML Button addressListButton;
	@FXML Button scheduleListButton;
	@FXML Button attendanceListButton;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	@FXML public void humanListButtonPushed(ActionEvent event) throws IOException {
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event,"HumanTableView.fxml", "All Humans"); 
	}

	@FXML public void catalogListButtonPushed(ActionEvent event) throws IOException {
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "CatalogListView.fxml", "Course Catalog");
	}

	@FXML public void sectionListButtonPushed(ActionEvent event) throws IOException {
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "CatalogListView.fxml", "Course Catalog");
	}

	@FXML public void addressListButtonPushed(ActionEvent event) throws IOException {
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "AddressTableView.fxml", "Course Catalog");
	}

	@FXML public void scheduleListButtonPushed(ActionEvent event) throws IOException {
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "adminarea/ScheduleTableView.fxml", "Schedule Listing.");
	}

	@FXML public void attendanceListButtonPushed(ActionEvent event) throws IOException {
		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "CatalogListView.fxml", "Course Catalog");
	}

	@FXML public void logoutButtonPushed(ActionEvent event) throws IOException {

		SceneChanger.setLoggedInUser(null);

		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "LoginView.fxml", "Login");	
	}

}
