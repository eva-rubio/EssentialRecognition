/**
 * 
 */
package views;

import models.Address;
import models.Human;

/**
 * Generic Controller Class.
 * Used by SceneChanger to force controllers to call 'preloadData(X x)' to allow a scene to pre-load data when chaged.
 * Handles which Controller class is needed for each .fxml file view. 
 * Handles how to pass in the correct Controller class instance between scenes.
 * 
 * Forces ALL instances of the Controller class, have the preloadData() method.
 * By having access to the Controller we are able to call its methods to pre-load data. 
 * 
 * @author Eva Rubio
 *
 *Forces any class that implements this Interface, to have a method called 'preloadData()
 */
public interface ControllerClass {
	
	/**
	 * Updates the view with a Human object's data, it pre-loades it for an edit.
	 * @param Human
	 */
	public abstract void preloadData(Human human);
	public abstract void preloadData(Address address);
}
