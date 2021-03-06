/**
 * 
 */
package views;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class to launch our GUI view.
 * It is a customized extension of the Application Class.
 * This launches our view. 
 * 
 * @author erubi
 *
 */
public class Main extends Application {

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, SQLException {

		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.loadLibrary("libopencv_java440");

		/*
		 * 	'launch()' method - inside of Application class. 
		 * 			Takes in the String array &
		 * 			calls the 'start()' method 
		 * */
		launch(args);
		
		// 	public Human(int h_type, String firstName, String lastName, String phoneNumber, LocalDate dob, 
		// String password, String email, int gender, int addressID, int mascotID) 

		//Human humanTest = new Human(1, "Antonio", "Rubio", "555-555-5555", LocalDate.of(1999, Month.JUNE, 06), "calima", "arubio@overlap.net", 2, 102, 202020);

		//humanTest.insertIntoDB();
	}

	/**
	 * Launches the view that we want to launch/display.
	 *@param primaryStage  the Stage object (the window) where Scenes are added to it. 
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
		
		//Parent root = FXMLLoader.load(getClass().getResource("DetailsCourseView.fxml"));  
		//(1)Parent root = FXMLLoader.load(getClass().getResource("NewUserView.fxml"));
		//Parent root = FXMLLoader.load(getClass().getResource("CatalogListView.fxml"));  
		//CatalogListViewController
		
		//Parent root = FXMLLoader.load(getClass().getResource("NewEditCourseView.fxml"));  


		//Parent root = FXMLLoader.load(getClass().getResource("HumanTableView.fxml"));
		///VolunteerApp/src/views/adminarea/NewSectionView.fxml
		
		//Parent root = FXMLLoader.load(getClass().getResource("adminarea/NewSectionView.fxml"));
		//Parent root = FXMLLoader.load(getClass().getResource("adminarea/ScheduleTableView.fxml"));
		//Parent root = FXMLLoader.load(getClass().getResource("adminarea/TimetableView.fxml"));
	//Parent root = FXMLLoader.load(getClass().getResource("adminarea/SectionTableView.fxml"));

		
		
		//Parent root = FXMLLoader.load(getClass().getResource("AttendanceTrackerView.fxml"));
		//Parent root = FXMLLoader.load(getClass().getResource("AddressTableView.fxml"));  
		


		Scene scene = new Scene(root);

//		primaryStage.setTitle("Register New to College Form.");
		primaryStage.setTitle("Login!");

		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
