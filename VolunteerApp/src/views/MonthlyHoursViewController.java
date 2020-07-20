/**
 * 
 */
package views;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * @author Eva Rubio
 *
 */
public class MonthlyHoursViewController implements Initializable {

	@FXML    private BarChart<?, ?> barChart;
	@FXML    private CategoryAxis months;
	@FXML    private NumberAxis hoursWorked;

	// so that the chart will be populated with a Series. 
	private XYChart.Series currentYearSeries, previousYearSeries;
	//private XYChart.Series series;

	/**
	 * This method will return the scene to the VolunteerTableView
	 */
	public void backButtonPushed(ActionEvent event) throws IOException {

		SceneChanger sc = new SceneChanger();
		sc.changeScenes(event, "VolunteerTableView.fxml", "All Volunteers");
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

		currentYearSeries = new XYChart.Series<>();
		previousYearSeries = new XYChart.Series<>();

		//series = new XYChart.Series<>();

		//barChart.setTitle("Hours Worked");
		months.setLabel("Months");
		hoursWorked.setLabel("Hours worked");

		//dummy example - data added to the chart. 
		//        series.getData().add(new XYChart.Data("phones", 10));
		//        series.getData().add(new XYChart.Data("computers", 14));

		currentYearSeries.setName(Integer.toString(LocalDate.now().getYear()));
		previousYearSeries.setName(Integer.toString(LocalDate.now().getYear()-1));

		try{

			populateSeriesFromDB();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}

		//barChart.getData().addAll(series);

		barChart.getData().addAll(previousYearSeries);
		barChart.getData().addAll(currentYearSeries);

	}

	/**
	 * This will read the user data from the database and populate the series.
	 * every month should gve us something new. 
	 */
	public void populateSeriesFromDB() throws SQLException {
		//get the results from the database
		Connection conn=null;
		Statement statement=null;
		ResultSet resultSet=null;

		try {
			//1.  connect to the database
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC", "root", "Sudafrica1$");

			//2.  create the statement
			statement = conn.createStatement();

			//3.  create a string with the sql statement
			String sql = "SELECT YEAR(dateWorked), MONTHNAME(dateWorked), SUM(hoursworked) " +
					"FROM hoursworked " +
					"GROUP BY YEAR(dateWorked), MONTH(dateWorked)" +
					"ORDER BY YEAR(dateWorked), MONTH(dateWorked);";

			//4. execute the query statement
			resultSet = statement.executeQuery(sql);

			//5.  loop over the result set and add to our series
			while (resultSet.next()) {

				///series.getData().add(new XYChart.Data(resultSet.getString(2), resultSet.getInt(3)));

				if (resultSet.getInt(1) == LocalDate.now().getYear()) {
					currentYearSeries.getData().add(new XYChart.Data(resultSet.getString(2), resultSet.getInt(3)));
					
				} else if (resultSet.getInt(1) == LocalDate.now().getYear()-1) {
					previousYearSeries.getData().add(new XYChart.Data(resultSet.getString(2), resultSet.getInt(3)));  
				}

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

}
