package com.seransaca.intelygenz.securitish.web.converter;

import com.seransaca.intelygenz.securitish.entity.Items;
import com.seransaca.intelygenz.securitish.security.Cypher;
import com.seransaca.intelygenz.securitish.service.exceptions.CypherException;
import com.seransaca.intelygenz.securitish.service.request.Constants;
import com.seransaca.intelygenz.securitish.web.dto.ItemDTO;
import com.seransaca.intelygenz.securitish.web.dto.ItemsDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static com.seransaca.intelygenz.securitish.ConstantsTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ItemConverterTest {

    @Mock
    ItemsConverter itemsConverter;

    @Test
    void testItemsToDto(){
        try (MockedStatic<Cypher> cypher = Mockito.mockStatic(Cypher.class)){
            cypher.when(() -> Cypher.encrypt(anyString(), anyInt())).thenReturn(ITEM_NAME);
            when(itemsConverter.itemsToDto(any(List.class))).thenReturn(getItemsDTO());

            ItemsDTO result = itemsConverter.itemsToDto(getListItems());

            assertNotNull(result);
            assertEquals(2, result.getItemList().size());
            assertEquals(ITEM1_ID, result.getItemList().get(0).getItemId());
            assertEquals(ITEM_NAME, result.getItemList().get(0).getItemName());
            assertEquals(ITEM2_ID, result.getItemList().get(1).getItemId());
            assertEquals(ITEM_NAME, result.getItemList().get(1).getItemName());
        }
    }

    @Test
    void testItemsToDto_throwException(){
        when(itemsConverter.itemsToDto(getListItems()))
                .thenThrow(new CypherException(Items.builder().id(ITEM1_ID).item(ITEM_NAME).build().toString(),
                        Constants.ERROR_ITEM_DECRYPT));

        assertThrows(CypherException.class,() -> {
            itemsConverter.itemsToDto(getListItems());
        });
    }

    private List<Items> getListItems(){
        List<Items> itemsList = new ArrayList<>();
        Items items1 = Items.builder().id(ITEM1_ID).item(ITEM_NAME).build();
        Items items2 = Items.builder().id(ITEM2_ID).item(ITEM_NAME).build();
        itemsList.add(items1);
        itemsList.add(items2);
        return itemsList;
    }

    private ItemsDTO getItemsDTO(){
        ItemsDTO dto = new ItemsDTO(new ArrayList<>());
        dto.getItemList().add(new ItemDTO(ITEM1_ID, ITEM_NAME));
        dto.getItemList().add(new ItemDTO(ITEM2_ID, ITEM_NAME));
        return dto;
    }
}
