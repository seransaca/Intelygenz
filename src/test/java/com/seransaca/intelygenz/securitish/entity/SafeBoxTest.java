package com.seransaca.intelygenz.securitish.entity;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static com.seransaca.intelygenz.securitish.entity.EntityValidationConstants.*;
import static com.seransaca.intelygenz.securitish.support.AssertionUtilities.assertExistsValidationMessage;
import static org.junit.jupiter.api.Assertions.*;

class SafeBoxTest {

    private static final String NAME = "NAME";

    private static final String NAME_TOO_LONG = "NAMENAMENAMENAMENAMENAMENAMENAM";

    private static final String PASSWORD = "PASSWORD";

    private static final String PASSWORD_TOO_LONG = "PASSWORDPASSWORDPASSWORDPASSWORDPASSWORDPASSWORDPASSWORD";

    private static final String UUID = "03b17bd6-62af-4bcb-ab52-15c9f85a6a07";

    private static final String UUID_TOO_LONG = "03b17bd6-62af-4bcb-ab52-15c9f85a6a07-03b17bd6-62af-4bcb-ab52-15c9f85a6a07";

    public static Validator validator;

    private SafeBox safeBox;

    @BeforeAll
    public static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("Valid Safebox")
    void testSafeBox_thenCorrect() {
        SafeBox safeBox = new SafeBox();
        safeBox.setId(1);
        safeBox.setUuid(UUID);
        safeBox.setName(NAME);
        safeBox.setPassword(PASSWORD);
        safeBox.setBlocked(0);

        Set<ConstraintViolation<SafeBox>> validate = validator.validate(safeBox);

        assertTrue(validate.isEmpty());
        assertEquals(1, safeBox.getId());
        assertEquals(UUID, safeBox.getUuid());
        assertEquals(NAME, safeBox.getName());
        assertEquals(PASSWORD, safeBox.getPassword());
        assertEquals(0, safeBox.getBlocked());

    }

    @Test
    @DisplayName("Uuid is null")
    void testUUID_whenNull_thenCorrect() {
        SafeBox safeBox = new SafeBox();
        safeBox.setId(1);
        safeBox.setUuid(null);
        safeBox.setName(NAME);
        safeBox.setPassword(PASSWORD);
        safeBox.setBlocked(0);

        Set<ConstraintViolation<SafeBox>> validate = validator.validate(safeBox);

        assertFalse(validate.isEmpty());
        assertEquals(1, validate.size());
        assertExistsValidationMessage(validate, ERROR_SAFEBOX_UUID);
    }

    @Test
    @DisplayName("Uuid too long")
    void testUUID_whenTooLong_thenCorrect() {
        SafeBox safeBox = new SafeBox();
        safeBox.setId(1);
        safeBox.setUuid(UUID_TOO_LONG);
        safeBox.setName(NAME);
        safeBox.setPassword(PASSWORD);
        safeBox.setBlocked(0);

        Set<ConstraintViolation<SafeBox>> validate = validator.validate(safeBox);

        assertFalse(validate.isEmpty());
        assertEquals(1, validate.size());
        assertExistsValidationMessage(validate, ERROR_SAFEBOX_UUID_SIZE);
    }

    @Test
    @DisplayName("Safebox name is null")
    void testSafeboxName_whenNull_thenCorrect() {
        SafeBox safeBox = new SafeBox();
        safeBox.setId(1);
        safeBox.setUuid(UUID);
        safeBox.setName(null);
        safeBox.setPassword(PASSWORD);
        safeBox.setBlocked(0);

        Set<ConstraintViolation<SafeBox>> validate = validator.validate(safeBox);

        assertFalse(validate.isEmpty());
        assertEquals(1, validate.size());
        assertExistsValidationMessage(validate, ERROR_SAFEBOX_NAME);
    }

    @Test
    @DisplayName("Safebox name too long")
    void testSafeboxName_whenTooLong_thenCorrect() {
        SafeBox safeBox = new SafeBox();
        safeBox.setId(1);
        safeBox.setUuid(UUID);
        safeBox.setName(NAME_TOO_LONG);
        safeBox.setPassword(PASSWORD);
        safeBox.setBlocked(0);

        Set<ConstraintViolation<SafeBox>> validate = validator.validate(safeBox);

        assertFalse(validate.isEmpty());
        assertEquals(1, validate.size());
        assertExistsValidationMessage(validate, ERROR_SAFEBOX_NAME_SIZE);
    }

    @Test
    @DisplayName("Safebox password is null")
    void testSafeboxPassword_whenNull_thenCorrect() {
        SafeBox safeBox = new SafeBox();
        safeBox.setId(1);
        safeBox.setUuid(UUID);
        safeBox.setName(NAME);
        safeBox.setPassword(null);
        safeBox.setBlocked(0);

        Set<ConstraintViolation<SafeBox>> validate = validator.validate(safeBox);

        assertFalse(validate.isEmpty());
        assertEquals(1, validate.size());
        assertExistsValidationMessage(validate, ERROR_SAFEBOX_PASSWORD);
    }

    @Test
    @DisplayName("Safebox password too long")
    void testSafeboxPassword_whenTooLong_thenCorrect() {
        SafeBox safeBox = new SafeBox();
        safeBox.setId(1);
        safeBox.setUuid(UUID);
        safeBox.setName(NAME);
        safeBox.setPassword(PASSWORD_TOO_LONG);
        safeBox.setBlocked(0);

        Set<ConstraintViolation<SafeBox>> validate = validator.validate(safeBox);

        assertFalse(validate.isEmpty());
        assertEquals(1, validate.size());
        assertExistsValidationMessage(validate, ERROR_SAFEBOX_PASSWORD_SIZE);
    }

    @Test
    @DisplayName("All param of safebox are null")
    void testSafeboxAll_whenNull_thenCorrect() {
        SafeBox safeBox = new SafeBox();
        safeBox.setId(1);
        safeBox.setUuid(null);
        safeBox.setName(null);
        safeBox.setPassword(null);
        safeBox.setBlocked(0);

        Set<ConstraintViolation<SafeBox>> validate = validator.validate(safeBox);

        assertFalse(validate.isEmpty());
        assertEquals(3, validate.size());
    }

}
