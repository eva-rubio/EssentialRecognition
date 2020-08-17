/**
 * https://www.programiz.com/java-programming/enum-constructor
 */
package student;

/**
 * @author Eva Rubio
 *
 */
public enum EnrollmentTypeStatus {
	// enum constants calling the enum constructors
	PARTTIME("Part-Time"), 
	FULLTIME("Full-Time");

	private final String enrollStatus;



	/**
	 * Private enum constructor.
	 * The constructor takes a string value as a parameter and assigns value to the variable 'enrollStatus'.
	 * @param string
	 */
	private EnrollmentTypeStatus(String enrollStatus) {
		this.enrollStatus = enrollStatus;
	}



	public String getEnrollmentStatus() {
		return enrollStatus;
	}

}
