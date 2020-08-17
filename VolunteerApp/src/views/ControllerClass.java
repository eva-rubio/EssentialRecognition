/**
 * 
 */
package views;

import models.Human;

/**
 * Handles which Controller class is needed for each .fxml file view. 
 * Handles how to pass in the correct Controller class instance between scenes.
 * Forces all Controller classes to contain preloadData() method.
 * By having access to the Controller we are able to call its methods to pre-load data. 
 * 
 * @author Eva Rubio
 *
 */
public interface ControllerClass {
	
	/**
	 * Updates the view with a Human object's data, it preloades it for an edit.
	 * @param Human
	 */
	public abstract void preloadData(Human human);
}
