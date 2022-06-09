package com.seransaca.intelygenz.securitish.support;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Support class containing functionalities for checking
 *  * in the tests.
 *   
 * @author Sergio A. Sánchez Camarero
 */

public final class AssertionUtilities {
	
	private AssertionUtilities() {
		throw new IllegalStateException("It should not be instantiated, it is a support class");
	}

	/**
	 * Checks for a message in the reset violation set. If you can't find the message
	 * * would force a JUnit crash.
	 * 
	 * @param setConstraintViolations Set of errors after a validation.
	 * @param expectedMessage	The message sought within the error set.
	 */

	public static <T> void assertExistsValidationMessage(Set<ConstraintViolation<T>> setConstraintViolations, String expectedMessage) {
		boolean exists = setConstraintViolations.stream()
			.anyMatch((v) -> v.getMessage().equals(expectedMessage));
		
		if (!exists) 
			fail(String.format("Expected message validation : \"%s\"", expectedMessage));
	}
	
}
