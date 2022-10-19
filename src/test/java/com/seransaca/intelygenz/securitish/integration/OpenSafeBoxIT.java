package com.seransaca.intelygenz.securitish.integration;

import com.seransaca.intelygenz.securitish.web.controller.SafeBoxController;
import com.seransaca.intelygenz.securitish.web.dto.SafeBoxRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class OpenSafeBoxIT {

    private static final String PASSWORD = "Sergio8$Mola";
    private static final String NAME = "nombre";

    private static final String URL_PATH_OPEN_SAFEBOX = "/safebox/{id}/open";

    private static final String UUID_NOT_FOUND = "9d7c01b9-2692-4ec3-b0f6-0fc09d546218";

    private static final String WRONG_PASSWORD = "sergio8";

    private static final String WRONG_NAME = "name";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SafeBoxController safeBoxController;

    @Test
    public void testOpenSafeBox_returnOK() throws Exception{
        String uuid = getUuidSafeBox();

        mockMvc.perform(get(replaceInPath("{id}", uuid))
                        .header("Authorization", "Basic "+createBasic(NAME, PASSWORD)))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void testOpenSafeBox_uuidNotFound_returnKO() throws Exception{
        mockMvc.perform(get(replaceInPath("{id}", UUID_NOT_FOUND))
                        .header("Authorization", "Basic "+createBasic(NAME, PASSWORD)))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void testOpenSafeBox_wrongPassword_returnKO() throws Exception{
        String uuid = getUuidSafeBox();

        mockMvc.perform(get(replaceInPath("{id}", uuid))
                        .header("Authorization", "Basic "+createBasic(NAME, WRONG_PASSWORD)))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void testOpenSafeBox_wrongName_returnKO() throws Exception{
        String uuid = getUuidSafeBox();

        mockMvc.perform(get(replaceInPath("{id}", uuid))
                        .header("Authorization", "Basic "+createBasic(WRONG_NAME, PASSWORD)))
                .andExpect(status().is4xxClientError());

    }

    private String createBasic(String name, String password){
        return new String(Base64.getEncoder().encode((name+":"+password).getBytes()));
    }

    private String replaceInPath(String cad, String replaced){
        return URL_PATH_OPEN_SAFEBOX.replace(cad, replaced);
    }

    private String getUuidSafeBox(){
        SafeBoxRequestDTO request = new SafeBoxRequestDTO();
        request.setName(NAME);
        request.setPassword(PASSWORD);
        return safeBoxController.createSafeBox(request).getBody().getId();
    }
}
