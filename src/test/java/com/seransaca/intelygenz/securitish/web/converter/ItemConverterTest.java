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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
            when(itemsConverter.itemsToDto(any(Flux.class))).thenReturn(getItemsDTO());

            ItemsDTO result = itemsConverter.itemsToDto(getListItems()).block();

            assertNotNull(result);
            assertEquals(2, result.getItemList().size());
            assertEquals(ITEM1_ID, result.getItemList().get(0).getItemId());
            assertEquals(ITEM_NAME, result.getItemList().get(0).getItemName());
            assertEquals(ITEM2_ID, result.getItemList().get(1).getItemId());
            assertEquals(ITEM_NAME, result.getItemList().get(1).getItemName());
        }
    }

    private Flux<Items> getListItems(){
        return Flux.just(Items.builder().id(ITEM1_ID).item(ITEM_NAME).build(),
                Items.builder().id(ITEM2_ID).item(ITEM_NAME).build());
    }

    private Mono<ItemsDTO> getItemsDTO(){
        ItemsDTO dto = new ItemsDTO(new ArrayList<>());
        dto.getItemList().add(new ItemDTO(ITEM1_ID, ITEM_NAME));
        dto.getItemList().add(new ItemDTO(ITEM2_ID, ITEM_NAME));
        return Mono.just(dto);
    }
}
