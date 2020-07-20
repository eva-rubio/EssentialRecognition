/**
 * 
 */
package views;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import models.Volunteer;

/**
 * @author Eva Rubio
 *
 */
public class LogHoursViewController implements Initializable, ControllerClass {


	@FXML private DatePicker datePicker;
	@FXML private Spinner<Integer> hoursWorkedSpinner;
	@FXML private Label volunteerIDLabel;
	@FXML private Label firstNameLabel;
	@FXML private Label lastNameLabel;
	@FXML private Label errMsgLabel;

	@FXML private Button backButton;

	@FXML private LineChart<?, ?> lineChart;
	@FXML private CategoryAxis monthAxis;
	@FXML private NumberAxis hoursAxis;
	private XYChart.Series hoursLoggedSeries;

	private Volunteer volunteer;

	@Override
	public void preloadData(Volunteer volunteer) {
		this.volunteer = volunteer;
		volunteerIDLabel.setText(Integer.toString(volunteer.getVolunteerID()));
		firstNameLabel.setText(volunteer.getFirstName());
		lastNameLabel.setText(volunteer.getLastName());
		datePicker.setValue(LocalDate.now());
		errMsgLabel.setText("");

		updateLineChart();

	}

	/**
	 * Updates the lineChart with the latest information that is stored in the database.
	 * each time we call this method it clears the data and load it up FRESHHH.
	 */
	public void updateLineChart() {
		//initialize the instance variables for the chart:
		hoursLoggedSeries = new XYChart.Series<>();
		hoursLoggedSeries.setName(Integer.toString(LocalDate.now().getYear()));
		// clears out the observableList
		lineChart.getData().clear();

		try {
			populateSeriesFromDB();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}
		lineChart.getData().addAll(hoursLoggedSeries);
	}

	/**
	 * Populates the hoursLoggedSeries with the latest information from our database.
	 * Yet another database query..
	 * @throws SQLException 
	 * 
	 */
	public void populateSeriesFromDB() throws SQLException {
		//get the results from the database
		Connection conn=null;
		PreparedStatement statement=null;
		ResultSet resultSet=null;

		try {
			//1.  connect to the database
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");


			//2.  create a string with the sql statement
			String sql = "SELECT MONTHNAME(dateWorked), SUM(hoursworked) " +
					"FROM hoursWorked " +
					"WHERE volunteerID=? AND YEAR(dateWorked)=? " +
					"GROUP BY MONTH(dateWorked);";

			// 3 create statement
			statement = conn.prepareCall(sql);

			//4 bind params
			statement.setInt(1, volunteer.getVolunteerID());
			statement.setInt(2, LocalDate.now().getYear());

			//5. execute the query statement
			resultSet = statement.executeQuery();

			//6.  loop over the result set and build our series
			while (resultSet.next()) {
				hoursLoggedSeries.getData().add(new XYChart.Data(resultSet.getString(1), resultSet.getInt(2)));

			}

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}
		finally {
			if (conn != null)
				conn.close();
			if (statement != null)
				statement.close();
			if (resultSet != null)
				resultSet.close();
		}

	}

	/**
	 * This method will read/validate the inputs and store the information
	 * in the hoursWorked table
	 */
	public void saveButtonPushed(ActionEvent event) {

		try {

			volunteer.logHours(datePicker.getValue(), (int) hoursWorkedSpinner.getValue());
			errMsgLabel.setText("Hours logged!");
			updateLineChart();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		} catch (IllegalArgumentException e) {
			errMsgLabel.setText(e.getMessage());
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}
	}


	/**
	 * This will return the user to the table of all volunteers
	 */ 
	public void cancelButttonPushed(ActionEvent event) throws IOException {

		//if this is an admin user, go back to the table of volunteers
		SceneChanger sc = new SceneChanger();

		if (SceneChanger.getLoggedInUser().isAdmin()) {
			sc.changeScenes(event, "VolunteerTableView.fxml", "All Volunteers");

		} else {
			NewUserViewController controller = new NewUserViewController();
			sc.changeScenes(event, "NewUserView.fxml", "Edit Profile", volunteer, controller);
		}
	}

	/**
	 * Logs the current user out of the application and returns them to the LoginView.
	 * @param event
	 * @throws IOException 
	 */
	public void logoutButttonPushed(ActionEvent event) throws IOException {

		SceneChanger.setLoggedInUser(null);

		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "LoginView.fxml", "Login");	
	}



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 8 is the regular work day
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,18,8);

		hoursWorkedSpinner.setValueFactory(valueFactory);

		//change the text on the button if its NOT an administrative user
		if (!SceneChanger.getLoggedInUser().isAdmin()) {
			backButton.setText("Edit");
		}

	}

}
