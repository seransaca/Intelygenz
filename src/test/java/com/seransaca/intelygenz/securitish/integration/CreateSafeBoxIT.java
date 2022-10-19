package com.seransaca.intelygenz.securitish.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seransaca.intelygenz.securitish.web.dto.SafeBoxRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class CreateSafeBoxIT {

    private static final String GOOD_PASSWORD = "Sergio8$Mola";
    private static final String NAME = "nombre";
    private static final String URL_PATH_CREATE_SAFEBOX = "/safebox";
    private static final String WRONG_PASSWORD = "sergio8";
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testCreateSafeBox_returnOK() throws Exception{
        mockMvc.perform(post(URL_PATH_CREATE_SAFEBOX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(getRequestDTO())))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testCreateSafeBox_wrongPassword_returnKO() throws Exception{
        SafeBoxRequestDTO requestDTO = getRequestDTO();
        requestDTO.setPassword(WRONG_PASSWORD);
        mockMvc.perform(post(URL_PATH_CREATE_SAFEBOX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(requestDTO)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testCreateSafeBox_emptyPassword_returnKO() throws Exception{
        SafeBoxRequestDTO requestDTO = getRequestDTO();
        requestDTO.setPassword(null);
        mockMvc.perform(post(URL_PATH_CREATE_SAFEBOX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(requestDTO)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testCreateSafeBox_emptyName_returnKO() throws Exception{
        SafeBoxRequestDTO requestDTO = getRequestDTO();
        requestDTO.setName(null);
        mockMvc.perform(post(URL_PATH_CREATE_SAFEBOX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(requestDTO)))
                .andExpect(status().is4xxClientError());
    }

    private SafeBoxRequestDTO getRequestDTO(){
        SafeBoxRequestDTO request = new SafeBoxRequestDTO();
        request.setName(NAME);
        request.setPassword(GOOD_PASSWORD);
        return request;
    }

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}
