/**
 * 
 */
package models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * It is NOT possible to ever convert the encryption back to the password, but:
 * Given the password and the salt I can re-create the exact same combination,
 * and thus be able to validate a password. 
 * 
 * @author Eva Rubio
 *
 */
public class PasswordGenerator {
	static int minPassLength = 5;

	/**
	 * SHA512 libraries we are using to encrypt the passwords. 
	 * we need the salt/seed to protect from maliciousness. another method to apply randomness. 
	 * 
	 * @param passwordToEncrypt
	 * @param salt		
	 * @return
	 */
	public static String getSHA512Password(String passwordToEncrypt, byte[] salt) {

		String generatedPassword = null;

		try {

			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt);

			//converts our password into a string of bytes
			byte[] bytes = md.digest(passwordToEncrypt.getBytes());

			//convert the string of bytes back into characters. 
			StringBuilder sb = new StringBuilder();

			// for each byte that is return3ed in our array of bytes we convert it to a string and append them together. 
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}

			generatedPassword = sb.toString();


		} catch (NoSuchAlgorithmException e) {
			
			System.err.println(e.getMessage());
		}

		return generatedPassword;
	}

	/**
	 * Create a salt that is as random as possible. 
	 * @return	a random set of bytes. 
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] getSalt() throws NoSuchAlgorithmException {

		SecureRandom sr = SecureRandom.getInstanceStrong();
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
	}
	
	public static boolean isLengthValid(String pass) {
		if(minPassLength <= pass.length()) {
			return true;
		} else {
			throw new IllegalArgumentException("Password must be at least 5 characters long.");
		}
	}

}
