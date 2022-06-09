package com.seransaca.intelygenz.securitish.web.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static com.seransaca.intelygenz.securitish.web.dto.ValidationMessageErrorConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.seransaca.intelygenz.securitish.support.AssertionUtilities.assertExistsValidationMessage;

class SafeboxRequestDTOTest {

    private static final String SAFEBOX_NAME = "NAME";

    private static final String SAFEBOX_PASSWORD = "Pa55Word$.";

    public static Validator validator;

    public SafeBoxRequestDTO safeBoxRequestDTO;

    @BeforeAll
    public static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @BeforeEach
    public void setUpTest(){
        safeBoxRequestDTO = new SafeBoxRequestDTO();
        safeBoxRequestDTO.setName(SAFEBOX_NAME);
        safeBoxRequestDTO.setPassword(SAFEBOX_PASSWORD);
    }

    @Test
    @DisplayName("Valid request validation")
    public void testValidRequest_thenCorrect(){
        Set<ConstraintViolation<SafeBoxRequestDTO>> validations = validator.validate(safeBoxRequestDTO);

        assertTrue(validations.isEmpty());
        assertEquals(SAFEBOX_NAME, safeBoxRequestDTO.getName());
        assertEquals(SAFEBOX_PASSWORD, safeBoxRequestDTO.getPassword());
    }

    @Test
    @DisplayName("Safebox name request is null")
    public void testSafeboxName_whenNull_thenError(){
        safeBoxRequestDTO.setName(null);

        Set<ConstraintViolation<SafeBoxRequestDTO>> validations = validator.validate(safeBoxRequestDTO);

        assertExistsValidationMessage(validations, ERROR_SAFEBOX_NAME);
    }

    @Test
    @DisplayName("Safebox password request is null")
    public void testSafeboxPassword_whenNull_thenError(){
        safeBoxRequestDTO.setPassword("");

        Set<ConstraintViolation<SafeBoxRequestDTO>> validations = validator.validate(safeBoxRequestDTO);

        assertExistsValidationMessage(validations, ERROR_SAFEBOX_PASSWORD);
    }
}
