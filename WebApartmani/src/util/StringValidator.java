package util;

public class StringValidator {

	public static Boolean isNullOrEmpty(String string) {
		return string == null || string.isEmpty();
	}
	
	public static Boolean isAlphaWithSpaceDash(String string) {
		return string.matches("[\\p{IsAlphabetic} -]*");
	}
	
	public static Boolean isAlphanumeric(String string) {
		return string.matches("[\\p{IsAlphabetic}0-9]*");
	}

}