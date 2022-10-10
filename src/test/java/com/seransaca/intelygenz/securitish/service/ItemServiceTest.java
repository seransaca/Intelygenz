package com.seransaca.intelygenz.securitish.service;

import com.seransaca.intelygenz.securitish.entity.Items;
import com.seransaca.intelygenz.securitish.entity.SafeBox;
import com.seransaca.intelygenz.securitish.repository.ItemsRepository;
import com.seransaca.intelygenz.securitish.repository.SafeBoxRepository;
import com.seransaca.intelygenz.securitish.service.impl.ItemsServiceImpl;
import com.seransaca.intelygenz.securitish.service.request.PutItemsRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.seransaca.intelygenz.securitish.ConstantsTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ItemServiceTest {

    @Autowired
    private ItemsService itemsService;

    @MockBean
    private ItemsRepository itemsRepository;

    @MockBean
    private SafeBoxRepository safeBoxRepository;

    @TestConfiguration
    public static class ItemServiceTestConfig {
        @Bean
        public ItemsService itemsService() {
            return new ItemsServiceImpl();
        }
    }

    @BeforeEach
    public void setup() {
        reset(itemsRepository);
        reset(safeBoxRepository);
    }

    @Test
    void testCreateItems() throws Exception {
        PutItemsRequest request = getPutItemsRequest();
        when(itemsRepository.save(any(Items.class))).thenReturn(getItemsDB());

        List<Items> result = itemsService.createItems(request);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(UUID, result.get(0).getUuid());
        assertEquals(ITEM1, result.get(0).getItem());
    }

    @Test
    void testFindItems() {
        when(itemsRepository.findByUuid(Mockito.anyString())).thenReturn(getListItemsDB());
        when(safeBoxRepository.findByUuid(Mockito.anyString())).thenReturn(getSafeBox());

        List<Items> result = itemsService.findItems(UUID);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(UUID, result.get(0).getUuid());
        assertEquals(ITEM1, result.get(0).getItem());
        assertEquals(UUID, result.get(1).getUuid());
        assertEquals(ITEM2, result.get(1).getItem());
    }

    private PutItemsRequest getPutItemsRequest(){
        PutItemsRequest request = new PutItemsRequest();
        request.setUuid(UUID);
        request.setItems(getItems());
        return request;
    }

    private List<String> getItems(){
        List<String> list = new ArrayList<>();
        list.add(ITEM1);
        return list;
    }

    private Items getItemsDB(){
        return Items.builder().uuid(UUID).item(ITEM1).build();
    }

    private List<Items> getListItemsDB(){
        List<Items> list = new ArrayList<>();
        list.add(Items.builder().uuid(UUID).item(ITEM1).build());
        list.add(Items.builder().uuid(UUID).item(ITEM2).build());
        return list;
    }
    private Mono<SafeBox> getSafeBox(){
        SafeBox safebox = new SafeBox();
        safebox.setId(SAFEBOX_ID);
        safebox.setName(SAFEBOX_NAME);
        safebox.setUuid(UUID);
        safebox.setPassword(SAFEBOX_PASSWORD);
        safebox.setBlocked(0);
        return Mono.just(safebox);
    }
}
