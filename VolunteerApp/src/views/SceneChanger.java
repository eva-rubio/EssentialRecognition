/**
 * 
 */
package views;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Volunteer;

/**
 * 
 * Utility class that handles Scene changes
 * We create this separately to avoid having to re-type this code over and over. 
 * @author Eva Rubio
 *
 */
public class SceneChanger {
	
	/*
	 * a static loggedinUser. 
	 * Any time we open an instance of our SceneChanger, we can access this loggedinUser.
	 * */
	private static Volunteer loggedInUser;
	
	

    /**
     * This method will accept the title of the new scene, the .fxml file name for
     * the view and the ActionEvent that triggered the change.
     * 
     * @param event		the event triggered by the mouse clicked. 
     * @param viewName	the .fxml file name for the view we want to load. 
     * @param title		the title of the new scene.
     * @throws IOException
     */
    public void changeScenes(ActionEvent event, String viewName, String title) throws IOException {
    	
        FXMLLoader loader = new FXMLLoader();
        // the file we want to load. 
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        //get the stage from the event that was passed in
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
    

    /**
     * Changes scenes and pre-loads the next scene with a Volunteer object (that was selected).
     * To change Scenes and pass information between them, we need to know the controller class to be used.
     *  
     * @param event				the event triggered by the mouse clicked. 
     * @param viewName			the .fxml file name for the view we want to load. 
     * @param title				the title of the new scene.
     * @param volunteer			the Volunteer object that was selected from the table.
     * @param controllerClass	the controller class that handles the view we desire. 
     * @throws IOException
     */
    public void changeScenes(ActionEvent event, String viewName, String title, Volunteer volunteer, ControllerClass controllerClass) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        //access the controller class and preloaded the volunteer data
        controllerClass = loader.getController();
        controllerClass.preloadData(volunteer);
        
        //get the stage from the event that was passed in
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
    
    public static Volunteer getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(Volunteer loggedInUser) {
        SceneChanger.loggedInUser = loggedInUser;
    }

}
