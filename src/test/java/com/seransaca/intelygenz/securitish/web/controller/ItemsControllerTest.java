package com.seransaca.intelygenz.securitish.web.controller;

import com.seransaca.intelygenz.securitish.service.ItemsService;
import com.seransaca.intelygenz.securitish.service.exceptions.CypherException;
import com.seransaca.intelygenz.securitish.service.exceptions.SafeboxNotFoundException;
import com.seransaca.intelygenz.securitish.service.request.Constants;
import com.seransaca.intelygenz.securitish.web.controller.impl.ItemsControllerImpl;
import com.seransaca.intelygenz.securitish.web.converter.ItemsConverter;
import com.seransaca.intelygenz.securitish.web.dto.ItemDTO;
import com.seransaca.intelygenz.securitish.web.dto.ItemsDTO;
import com.seransaca.intelygenz.securitish.web.dto.ItemsRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.seransaca.intelygenz.securitish.ConstantsTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ItemsControllerTest {

    @MockBean
    private ItemsService itemsService;

    @MockBean
    private ItemsConverter itemsConverter;

    @Mock
    private ItemsController itemsController;

    @TestConfiguration
    public static class ItemsControllerTestConfig{
        @Bean
        public ItemsController itemsController(){
            return new ItemsControllerImpl();
        }
    }

    @Test
    void testGetItems_returnOK(){
        when(itemsController.getItems(anyString()))
                .thenReturn(ResponseEntity.of(Optional.of(getItemDto())));

        ResponseEntity<ItemsDTO> result = itemsController.getItems(UUID);

        assertNotNull(result);
        assertTrue(result.hasBody());
        assertEquals(2, result.getBody().getItemList().size());
        assertEquals(ITEM1, result.getBody().getItemList().get(0).getItemName());
        assertEquals(ITEM1_ID, result.getBody().getItemList().get(0).getItemId());
        assertEquals(ITEM2, result.getBody().getItemList().get(1).getItemName());
        assertEquals(ITEM2_ID, result.getBody().getItemList().get(1).getItemId());
    }

    @Test
    void testGetItems_returnKO_safeBoxNotFoundException(){
        when(itemsController.getItems(anyString()))
                .thenThrow(new SafeboxNotFoundException(UUID));

        assertThrows(SafeboxNotFoundException.class,() -> {
            itemsController.getItems(UUID);
        });
    }

    @Test
    void testGetItems_returnKO_cypherException(){
        when(itemsController.getItems(anyString()))
                .thenThrow(new CypherException(getItemDto().getItemList().get(0).toString(), Constants.ERROR_ITEM_DECRYPT));

        assertThrows(CypherException.class,() -> {
            itemsController.getItems(UUID);
        });
    }

    @Test
    void testPutItems_returnOK(){
        when(itemsController.putItems(anyString(), any(ItemsRequestDTO.class)))
                .thenReturn(ResponseEntity.of(Optional.of(getItemDto())));

        ResponseEntity<ItemsDTO> result = itemsController.putItems(UUID, getItemsRequestDto());

        assertNotNull(result);
        assertTrue(result.hasBody());
        assertEquals(2, result.getBody().getItemList().size());
        assertEquals(ITEM1, result.getBody().getItemList().get(0).getItemName());
        assertEquals(ITEM1_ID, result.getBody().getItemList().get(0).getItemId());
        assertEquals(ITEM2, result.getBody().getItemList().get(1).getItemName());
        assertEquals(ITEM2_ID, result.getBody().getItemList().get(1).getItemId());
    }

    @Test
    void testPutItems_returnKO_cypherEncryptException(){
        when(itemsController.getItems(anyString()))
                .thenThrow(new CypherException(getItemDto().getItemList().get(0).toString(), Constants.ERROR_ITEM_ENCRYPT));

        assertThrows(CypherException.class,() -> {
            itemsController.getItems(UUID);
        });
    }

    @Test
    void testPutItems_returnKO_cypherDecryptException(){
        when(itemsController.getItems(anyString()))
                .thenThrow(new CypherException(getItemDto().getItemList().get(0).toString(), Constants.ERROR_ITEM_DECRYPT));

        assertThrows(CypherException.class,() -> {
            itemsController.getItems(UUID);
        });
    }

    private ItemsDTO getItemDto(){
        ItemsDTO items = new ItemsDTO(new ArrayList<>());
        items.getItemList().add(new ItemDTO(ITEM1_ID, ITEM1));
        items.getItemList().add(new ItemDTO(ITEM2_ID, ITEM2));
        return items;
    }

    private ItemsRequestDTO getItemsRequestDto(){
        ItemsRequestDTO request = new ItemsRequestDTO();
        request.setItems(List.of(ITEM1,ITEM2));
        return request;
    }
}
