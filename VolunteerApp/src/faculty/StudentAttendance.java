/**
 * 
 */
package faculty;

import java.time.LocalDate;

import models.Classroom;
import models.Course;
import student.Student;

/**
 * 
 * The student_presence table is used to store data about student presence for a specific lecture. 
 * We can assume that for each lecture the instructor will note the presence and/or absence for all students.
 * 
 * @author Eva Rubio
 *
 */
public class StudentAttendance {
	
	private Student theStudent;
	private Course theCourse;
	private Faculty theInstructor;
	private Classroom theClassroom;
	
	private LocalDate todaysDate;
	
	private boolean present;

}
