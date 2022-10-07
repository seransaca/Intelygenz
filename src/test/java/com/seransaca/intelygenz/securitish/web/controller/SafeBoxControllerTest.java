package com.seransaca.intelygenz.securitish.web.controller;

import com.seransaca.intelygenz.securitish.service.SafeBoxService;
import com.seransaca.intelygenz.securitish.service.exceptions.CypherException;
import com.seransaca.intelygenz.securitish.service.request.Constants;
import com.seransaca.intelygenz.securitish.web.controller.impl.SafeBoxControllerImpl;
import com.seransaca.intelygenz.securitish.web.converter.SafeBoxConverter;
import com.seransaca.intelygenz.securitish.web.dto.SafeBoxDTO;
import com.seransaca.intelygenz.securitish.web.dto.SafeBoxRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.seransaca.intelygenz.securitish.ConstantsTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class SafeBoxControllerTest {

    @MockBean
    private SafeBoxService createSafeBoxService;

    @MockBean
    private SafeBoxConverter safeBoxConverter;

    @Mock
    private SafeBoxController safeBoxController;

    @TestConfiguration
    public static class SafeBoxControllerTestConfig {
        @Bean
        public SafeBoxController safeBoxController() {
            return new SafeBoxControllerImpl();
        }
    }

    @Test
    void testCreateSafeBox_returnOK() {
        SafeBoxRequestDTO request = getRequestDto();
        when(safeBoxController.createSafeBox(any(SafeBoxRequestDTO.class)))
                .thenReturn(ResponseEntity.of(Optional.of(getResponseDto())));

        ResponseEntity<SafeBoxDTO> result = safeBoxController.createSafeBox(request);

        assertNotNull(result);
        assertTrue(result.hasBody());
        assertEquals(UUID, result.getBody().getId());
    }

    @Test
    void testCreateSafeBox_returnKO() {
        SafeBoxRequestDTO request = getRequestDto();
        when(safeBoxController.createSafeBox(any(SafeBoxRequestDTO.class)))
                .thenThrow(new CypherException(SAFEBOX_PASSWORD,
                        Constants.ERROR_PASSWORD_ENCRYPT));

        assertThrows(CypherException.class,() -> {
            safeBoxController.createSafeBox(request);
        });
    }

    private SafeBoxRequestDTO getRequestDto(){
        SafeBoxRequestDTO request = new SafeBoxRequestDTO();
        request.setName(SAFEBOX_NAME);
        request.setPassword(SAFEBOX_PASSWORD);
        return request;
    }

    private SafeBoxDTO getResponseDto(){
        SafeBoxDTO response = new SafeBoxDTO();
        response.setId(UUID);
        return response;
    }
}
