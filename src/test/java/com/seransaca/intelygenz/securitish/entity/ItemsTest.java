package com.seransaca.intelygenz.securitish.entity;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static com.seransaca.intelygenz.securitish.support.AssertionUtilities.assertExistsValidationMessage;
import static org.junit.jupiter.api.Assertions.*;

class ItemsTest {
    private static final String ITEM = "Item 1";

    private static final String ITEM_TOO_LONG = "Item 1 Item 1 Item 1 Item 1 Item 1 Item 1 Item 1 Item 1 Item 1";

    private static final String UUID = "03b17bd6-62af-4bcb-ab52-15c9f85a6a07";

    private static final String UUID_TOO_LONG = "03b17bd6-62af-4bcb-ab52-15c9f85a6a07-03b17bd6-62af-4bcb-ab52-15c9f85a6a07";

    public static Validator validator;

    private Items items;

    @BeforeAll
    public static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("Valid Item")
    void testItem_thenCorrect() {
        Items items = Items.builder().id(1).item(ITEM).uuid(UUID).build();

        Set<ConstraintViolation<Items>> validate = validator.validate(items);

        assertTrue(validate.isEmpty());
        assertEquals(1, items.getId());
        assertEquals(ITEM, items.getItem());
        assertEquals(UUID, items.getUuid());
    }

    @Test
    @DisplayName("Uuid is null")
    void testUUID_whenNull_thenCorrect() {
        Items items = Items.builder().id(1).item(ITEM).uuid(null).build();

        Set<ConstraintViolation<Items>> validate = validator.validate(items);

        assertFalse(validate.isEmpty());
        assertEquals(1, validate.size());
        assertExistsValidationMessage(validate, EntityValidationConstants.ERROR_SAFEBOX_UUID);
    }

    @Test
    @DisplayName("Uuid too long")
    void testUUID_whenTooLong_thenCorrect() {
        Items items = Items.builder().id(1).item(ITEM).uuid(UUID_TOO_LONG).build();

        Set<ConstraintViolation<Items>> validate = validator.validate(items);

        assertFalse(validate.isEmpty());
        assertEquals(1, validate.size());
        assertExistsValidationMessage(validate, EntityValidationConstants.ERROR_SAFEBOX_UUID_SIZE);
    }

    @Test
    @DisplayName("Item name is null")
    void testItemName_whenNull_thenCorrect() {
        Items items = Items.builder().id(1).item(null).uuid(UUID).build();

        Set<ConstraintViolation<Items>> validate = validator.validate(items);

        assertFalse(validate.isEmpty());
        assertEquals(1, validate.size());
        assertExistsValidationMessage(validate, EntityValidationConstants.ERROR_ITEM_NAME);
    }

    @Test
    @DisplayName("Item name too long")
    void testItemName_whenTooLong_thenCorrect() {
        Items items = Items.builder().id(1).item(ITEM_TOO_LONG).uuid(UUID).build();

        Set<ConstraintViolation<Items>> validate = validator.validate(items);

        assertFalse(validate.isEmpty());
        assertEquals(1, validate.size());
        assertExistsValidationMessage(validate, EntityValidationConstants.ERROR_ITEM_NAME_SIZE);
    }

    @Test
    @DisplayName("Item name and uuid are null")
    void testItemNameAndUuid_whenNull_thenCorrect() {
        Items items = Items.builder().id(1).item(null).uuid(null).build();

        Set<ConstraintViolation<Items>> validate = validator.validate(items);

        assertFalse(validate.isEmpty());
        assertEquals(2, validate.size());
    }

}
