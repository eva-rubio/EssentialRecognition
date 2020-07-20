/**
 * 
 */
package models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Eva Rubio
 *
 */
public class Address {

	/**		PK - Primary Key 	*/
	private Volunteer theHumansID;

	private String street;

	private String city;
	private String state;
	private int zipCode;
	private String country;

	private boolean onCampusHousing;



	//US ZIP code (U.S. postal code) allow both the five-digit and nine-digit (called ZIP + 4) formats.

	/**
	 * validate the united states 5 and 9 digit zip codes.
	 * Regular Expression to match the US Zip-Code.
	 * 		^d{5}(-d{4})?$
	 * 		^[0-9]{5}(-[0-9]{4})?$
	 * 		^[0-9]{5}(?:-[0-9]{4})?$
	 *  First five letters should be digits.
	 *  It can be followed by a dash and optional 4 digits
	 *  
	 *  https://www.youtube.com/watch?v=TsiM3qmVCAA
	 *  https://www.oreilly.com/library/view/regular-expressions-cookbook/9781449327453/ch03s07.html		
	 */
	//static final String regex = "^\d{5}(-\d{4})?$";

	public static boolean zipCodeUSvalid(String postCode) {
		
		String regex = "^[0-9]{5}(?:-[0-9]{4})?$";
		// object that just holds our pattern
		Pattern patternObj = Pattern.compile(regex);
		
		Matcher regexMatcher = patternObj.matcher(postCode);

		boolean valResult = regexMatcher.find();
		return valResult;
	}


}
