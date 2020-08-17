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
import models.Human;

/**
 * 
 * Utility class that handles Scene changes.
 * We create this separately to avoid having to re-type this code over and ooover.
 * @author Eva Rubio
 *
 *
 *https://www.youtube.com/watch?v=roY-gyE4UlA&list=PLoodc-fmtJNZuldAE53bGuOwJ6BhUG7mA&index=10
 */
public class SceneChanger {
	
	/*
	 * a static loggedinUser. 
	 * Any time we open an instance of our SceneChanger, we can access this loggedinUser.
	 * */
	private static Human loggedInUser;
	
	

    /**
     * (1/2) Simple Scene Changer. The title of the new scene, the .fxml file name for
     * the view and the ActionEvent that triggered the change.
     * In order to actually change the Scene, I NEED to use it to get the Parent window/stage.
     * 
     * @param event		the event triggered by the mouse clicked. 
     * @param viewName	the .fxml filename for the view that we want to load. 
     * @param title		the title of the new scene.
     * @throws IOException
     */
    public void changeScenes(ActionEvent event, String viewName, String title) throws IOException {
    	
        FXMLLoader loader = new FXMLLoader();
        // viewName - the file we want to load. (the resource to load)
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        //get the STAGE from the event that was just passed in.
        // this is why we had to pass in the 'event'
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
    

    /**
     * (2/2) Advanced Scene Changer. Changes scenes and pre-loads the next scene with a Human object (that was selected).
     * Passes information from Scene to Scene.
     * To change Scenes and pass information between them, we need to know the Controller class that has to be used.
     * 		We need an instance of the controller class so that we can call its methods to pre-load data.
     *  
     * @param event				the event triggered by the mouse clicked. 
     * @param viewName			the .fxml file name for the view we want to load. 
     * @param title				the title of the new scene.
     * @param human				the Human object that was selected from the table.
     * @param controllerClass	the controller class that handles the view we desire. An instance of the controller class. So we can call methods
     * @throws IOException
     */
    public void changeScenes(ActionEvent event, String viewName, String title, Human human, ControllerClass controllerClass) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        //Once we have the scene, we need to access the Controller class, and preload the human data.
        controllerClass = loader.getController();
        controllerClass.preloadData(human);
        
        //get the Stage from the event that was passed in
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
    
    public static Human getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(Human loggedInUser) {
        SceneChanger.loggedInUser = loggedInUser;
    }

}
