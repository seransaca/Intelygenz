package com.seransaca.intelygenz.securitish.web.dto.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 	This class represents an API error. From the error, it is interesting to know the resulting HTTP status,
 *  the time it occurred, the message and if it has sub errors.
 *  <p>
 *  An example case of sub errors occurs when there is a validation error. In this case, include more
 *  fields to indicate the reason.
 *
 * @author Sergio A. Sánchez Camarero
 * @see ApiValidationError
 */
@Data
@JsonInclude(content = JsonInclude.Include.NON_NULL)
@Schema(description = "Information about the error that occurred")
public class ApiError {

    @Schema(description = "HTTP error returned", enumAsRef = true, example = "404")
    private HttpStatus httpStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Schema(description = "Date and time of the error", pattern = "dd-MM-yyyy hh:mm:ss", example = "2022-02-05 14:33:02")
    private LocalDateTime timestamp;

    @Schema(description = "Message of the error produced", example = "Message of error")
    private String message;

    @Schema(description = "Exact message of the error that occurred on the server, for debugging purposes", example = "DEBUG message error")
    private String debugMessage;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Schema(description = "Errors nested in the main that extend the information", subTypes = {ApiValidationError.class})
    private List<ApiSubError> subErrors;



    public ApiError(HttpStatus httpStatus, String message, String debugMessage) {
        super();
        this.httpStatus = httpStatus;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.subErrors = new ArrayList<>();
        this.debugMessage= debugMessage;
    }

    public ApiError(HttpStatus status, Throwable ex) {
        this(status, "Unexpected error", ex.getLocalizedMessage());
    }

    public ApiError(HttpStatus status, String message, Throwable ex) {
        this(status, message, ex.getLocalizedMessage());
    }

    public void addValidationErrors(BindingResult bindingResult) {

        for (FieldError error : bindingResult.getFieldErrors())
            addValidationErrors(error);

    }

    public void addValidationErrors(FieldError fieldError) {
        ApiValidationError apiValidationError = ApiValidationError.builder()
                .object(fieldError.getObjectName())
                .field(fieldError.getField())
                .rejectedValue(fieldError.getRejectedValue())
                .message(fieldError.getDefaultMessage())
                .build();
        subErrors.add(apiValidationError);
    }
}
