/**
 * 
 */
package models;

/**
 * 
 * https://www.programiz.com/java-programming/enums
 * 
 * @author Eva Rubio
 *
 */
public enum PersonType {
	STUDENT,
	INSTRUCTOR,
	ADMIN;
	
	private PersonType() {}
	
	/**
	 * The name() method returns the defined name of an enum constant in string form. 
	 * The returned value from the name() method is final. 
	 * */
	public String value() {
		return name();
	}
	
	public static PersonType fromValue(String theValue) {
		/*
		 * The valueOf() method takes a string and returns an enum constant having the same string name.
		 * */
		return valueOf(theValue);
		
	}

}
