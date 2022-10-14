package com.seransaca.intelygenz.securitish.web.controller;

import com.seransaca.intelygenz.securitish.service.SafeBoxService;
import com.seransaca.intelygenz.securitish.service.exceptions.CypherException;
import com.seransaca.intelygenz.securitish.service.exceptions.UnauthorizedException;
import com.seransaca.intelygenz.securitish.service.request.Constants;
import com.seransaca.intelygenz.securitish.web.controller.impl.OpenControllerImpl;
import com.seransaca.intelygenz.securitish.web.dto.TokenDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.seransaca.intelygenz.securitish.ConstantsTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class OpenControllerTest {

    @MockBean
    private HttpServletRequest request;

    @MockBean
    private SafeBoxService safeBoxService;

    @Mock
    private OpenController openController;

    @TestConfiguration
    public static class OpenControllerTestConfig{
        @Bean
        public OpenController openController() {
            return new OpenControllerImpl();
        }
    }

    @Test
    void testOpenSafeBox_retunrOK(){
        when(openController.openSafebox(anyString()))
                .thenReturn(ResponseEntity.of(Optional.of(getTokenDto())));

        ResponseEntity<TokenDTO> result = openController.openSafebox(UUID);

        assertNotNull(result);
        assertTrue(result.hasBody());
        assertEquals(TOKEN, result.getBody().getToken());
    }

    @Test
    void testOpenSafeBox_retunrKO_cypherException(){
        when(openController.openSafebox(anyString()))
                .thenThrow(new CypherException(SAFEBOX_PASSWORD,
                        Constants.ERROR_PASSWORD_DECRYPT));

        assertThrows(CypherException.class,() -> {
            openController.openSafebox(UUID);
        });
    }

    @Test
    void testOpenSafeBox_retunrKO_unauthorizedException(){
        when(openController.openSafebox(anyString()))
                .thenThrow(new UnauthorizedException());

        assertThrows(UnauthorizedException.class,() -> {
            openController.openSafebox(UUID);
        });
    }

    private TokenDTO getTokenDto(){
        return new TokenDTO(TOKEN);
    }
}
