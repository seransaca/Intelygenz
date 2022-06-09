package com.seransaca.intelygenz.securitish.web.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.seransaca.intelygenz.securitish.web.dto.ValidationMessageErrorConstants.ERROR_ITEMS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.seransaca.intelygenz.securitish.support.AssertionUtilities.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class ItemsRequestDTOTest {

    private static final List<String> ITEMS = new ArrayList<>(List.of("Item1", "Item2"));

    public static Validator validator;

    public ItemsRequestDTO itemsRequestDTO;

    @BeforeAll
    public static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @BeforeEach
    public void setUpTest(){
        itemsRequestDTO = new ItemsRequestDTO();
        itemsRequestDTO.setItems(ITEMS);
    }

    @Test
    @DisplayName("Valid request validation")
    public void testValidRequest_thenCorrect(){
        Set<ConstraintViolation<ItemsRequestDTO>> validations = validator.validate(itemsRequestDTO);

        assertTrue(validations.isEmpty());
        assertEquals(ITEMS, itemsRequestDTO.getItems());
    }

    @Test
    @DisplayName("Items request is null")
    public void testItems_whenNull_thenError(){
        itemsRequestDTO.setItems(new ArrayList<>());

        Set<ConstraintViolation<ItemsRequestDTO>> validations = validator.validate(itemsRequestDTO);

        assertExistsValidationMessage(validations, ERROR_ITEMS);
    }
}
