package com.seransaca.intelygenz.securitish.web.dto.error;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a validation error. From the error it is interesting to know the object in which
 * the error occurred, the field, the invalid value and the error message.
 *
 * @author Sergio A. Sánchez Camarero
 */

@Data
@Builder
public class ApiValidationError implements ApiSubError {

	private String object;
	private String field;
	private Object rejectedValue;
	private String message;

}
