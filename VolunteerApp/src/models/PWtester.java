/**
 * 
 */
package models;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Tester class for the PasswordGenerator Class.
 * Retuns the word 'simple' in 10 different passwords, EACH one with a different salt.
 * 
 * @author Eva Rubio
 *
 */
public class PWtester {

	public static void main(String[] args) throws NoSuchAlgorithmException {



		String password = "simple"; 
		/*
		 * for(int i=1; i<=10; i++) { //System.out.printf("Salt %d: %s%n", i,
		 * Arrays.toString(PasswordGenerator.getSalt()));
		 * 
		 * 
		 * //byte[] salt = PasswordGenerator.getSalt();
		 * 
		 * System.out.printf("password %d: %s%n", i,
		 * PasswordGenerator.getSHA512Password(password, PasswordGenerator.getSalt()));
		 * 
		 * //System.out.printf("password: %s%n",
		 * PasswordGenerator.getSHA512Password(password, salt));
		 * 
		 * }
		 */

		byte[] salt = PasswordGenerator.getSalt();

		System.out.printf("password: %s%n", PasswordGenerator.getSHA512Password(password, salt));
		System.out.printf("password: %s%n", PasswordGenerator.getSHA512Password(password, salt));

	}

}
