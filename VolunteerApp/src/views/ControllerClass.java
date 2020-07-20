/**
 * 
 */
package views;

import models.Volunteer;

/**
 * Handles which controller class is needed for each fxml file view. 
 * Handles how to pass in the correct Controller class.
 * Forces all controller classes to contain preloadData method.
 * 
 * @author Eva Rubio
 *
 */
public interface ControllerClass {
	
	/**
	 * update the view with a Volunteer object preloaded for an edit.
	 * @param volunteer
	 */
	public abstract void preloadData(Volunteer volunteer);

}
