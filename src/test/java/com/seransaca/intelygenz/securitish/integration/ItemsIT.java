package com.seransaca.intelygenz.securitish.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seransaca.intelygenz.securitish.security.JWTUtils;
import com.seransaca.intelygenz.securitish.web.controller.ItemsController;
import com.seransaca.intelygenz.securitish.web.controller.SafeBoxController;
import com.seransaca.intelygenz.securitish.web.dto.ItemsRequestDTO;
import com.seransaca.intelygenz.securitish.web.dto.SafeBoxRequestDTO;
import com.seransaca.intelygenz.securitish.web.dto.TokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class ItemsIT {

    private static final String ITEM1 = "Item 1";
    private static final String ITEM2 = "Item 2";
    private static final String ITEM_PUT_1 = "Item Put 1";
    private static final String ITEM_PUT_2 = "Item Put 2";
    private static final String PASSWORD = "Sergio8$Mola";
    private static final String NAME = "nombre";

    private static final String URL_PATH_OPEN_ITEMS = "/safebox/{id}/items";

    private static final String UUID_NOT_FOUND = "9d7c01b9-2692-4ec3-b0f6-0fc09d546218";

    private static final String TOKEN_EXPIRED = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJub21icmUiLCJzdWIiOiJTZXJnaW84JE1vbGEiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjY1NzczNDcyLCJleHAiOjE2NjU3NzUyNzJ9.OLZB1Jeh5Nx1YMfZBFkMckoQFuoVd1Fdqr1eRpsZC708HwZRwpxj0FmABmwwhOi8Vea-5urvCE3xNT_jMwDodA";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SafeBoxController safeBoxController;

    @Autowired
    private ItemsController itemsController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetItems_retunrOK_emptyList() throws Exception{
        String uuid = getUuidSafeBox();

        String token = getToken(uuid);

        mockMvc.perform(get(replaceInPath("{id}", uuid))
                .header("Authorization", createBearerToken(token)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json("{'itemList':[]}"));
    }

    @Test
    public void testGetItems_returnOK_notEmptyList() throws Exception{
        String uuid = getUuidSafeBox();

        String token = getToken(uuid);

        putItem(uuid);

        mockMvc.perform(get(replaceInPath("{id}", uuid))
                        .header("Authorization", createBearerToken(token)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json("{'itemList':[{'itemId':1,'itemName':'Item 1'},{'itemId':2,'itemName':'Item 2'}]}"));
    }

    @Test
    public void testGetItems_notSafeBoxCreated_returnKO() throws Exception{
        String token = getToken(UUID_NOT_FOUND);

        mockMvc.perform(get(replaceInPath("{id}", UUID_NOT_FOUND))
                        .header("Authorization", createBearerToken(token)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetItems_expiredToken_returnKO() throws Exception{
        String uuid = getUuidSafeBox();

        mockMvc.perform(get(replaceInPath("{id}", uuid))
                        .header("Authorization", createBearerToken(null)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testPutItems_returnOK_notEmptyList() throws Exception{
        String uuid = getUuidSafeBox();

        String token = getToken(uuid);

        mockMvc.perform(put(replaceInPath("{id}", uuid))
                        .header("Authorization", createBearerToken(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createPutRequest())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json("{'itemList':[{'itemId':3,'itemName':'Item Put 1'},{'itemId':4,'itemName':'Item Put 2'}]}"));
    }

    private String replaceInPath(String cad, String replaced){
        return URL_PATH_OPEN_ITEMS.replace(cad, replaced);
    }

    private String getUuidSafeBox(){
        SafeBoxRequestDTO request = new SafeBoxRequestDTO();
        request.setName(NAME);
        request.setPassword(PASSWORD);
        return safeBoxController.createSafeBox(request).getBody().getId();
    }

    private String getToken(String uuid){
        return (new TokenDTO(JWTUtils.createJwtToken(NAME, PASSWORD))).getToken();
    }

    private String createBearerToken(String token){
        return "Bearer " + (token == null ? TOKEN_EXPIRED : token);
    }

    private void putItem(String uuid){
        ItemsRequestDTO request = new ItemsRequestDTO();
        request.setItems(new ArrayList<>());
        request.getItems().add(ITEM1);
        request.getItems().add(ITEM2);

        itemsController.putItems(uuid, request);
    }

    private ItemsRequestDTO createPutRequest(){
        ItemsRequestDTO request = new ItemsRequestDTO();
        request.setItems(new ArrayList<>());
        request.getItems().add(ITEM_PUT_1);
        request.getItems().add(ITEM_PUT_2);
        return request;
    }

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}
