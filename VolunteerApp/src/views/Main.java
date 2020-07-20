/**
 * 
 */
package views;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class to lauch our GUI view.
 * It is a customized extension of the Application Class.
 * 
 * @author erubi
 *
 */
public class Main extends Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		/*
		 * 	'launch()' method - inside of Application class. 
		 * 			Takes in the String array &
		 * 			calls the 'start()' method 
		 * */
		launch(args);

	}

	/**
	 * Launches the view that we want to launch/display.
	 *@param primaryStage  the Stage object (the window) where Scenes are added to it. 
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		//Parent root = FXMLLoader.load(getClass().getResource("NewUserView.fxml"));
		//Parent root = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
		Parent root = FXMLLoader.load(getClass().getResource("DetailsCourseView.fxml"));

		Scene scene = new Scene(root);

//		primaryStage.setTitle("New Volunteer");
		primaryStage.setTitle("Login!");

		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
