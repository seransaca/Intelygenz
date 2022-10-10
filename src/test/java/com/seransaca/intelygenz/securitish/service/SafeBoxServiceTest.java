package com.seransaca.intelygenz.securitish.service;

import com.seransaca.intelygenz.securitish.entity.SafeBox;
import com.seransaca.intelygenz.securitish.repository.SafeBoxRepository;
import com.seransaca.intelygenz.securitish.security.Cypher;
import com.seransaca.intelygenz.securitish.service.exceptions.SafeboxNotFoundException;
import com.seransaca.intelygenz.securitish.service.impl.SafeBoxServiceImpl;
import com.seransaca.intelygenz.securitish.utils.UuidGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static com.seransaca.intelygenz.securitish.ConstantsTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class SafeBoxServiceTest {



    @Autowired
    private SafeBoxService safeBoxService;

    @MockBean
    private SafeBoxRepository safeBoxRepository;

    @TestConfiguration
    public static class SafeBoxServiceTestConfig {
        @Bean
        public SafeBoxService safeBoxService() {
            return new SafeBoxServiceImpl();
        }
    }

    @BeforeEach
    public void setup() {
        reset(safeBoxRepository);
    }

    @Test
    void testCreateSafeBox_whenNotCreated() {
        try (MockedStatic<Cypher> cypher = Mockito.mockStatic(Cypher.class);
             MockedStatic<UuidGenerator> uuidGenerated = Mockito.mockStatic(UuidGenerator.class)) {
            uuidGenerated.when(UuidGenerator::createUuid).thenReturn(UUID);
            cypher.when(() -> Cypher.encrypt(anyString(), anyInt())).thenReturn(SAFEBOX_PASSWORD);
            when(safeBoxRepository.findSafeBoxByNameAndPassword(anyString(), anyString())).thenReturn(Mono.empty());
            when(safeBoxRepository.save(any(SafeBox.class))).thenReturn(getSafeBox());

            SafeBox result = safeBoxService.createNewSafeBox(SAFEBOX_NAME, SAFEBOX_PASSWORD).block();

            assertNotNull(result);
            assertEquals(SAFEBOX_ID, result.getId());
            assertEquals(SAFEBOX_NAME, result.getName());
            assertEquals(UUID, result.getUuid());
            assertEquals(SAFEBOX_PASSWORD, result.getPassword());
            assertEquals(0, result.getBlocked());
            verify(safeBoxRepository, times(1)).findSafeBoxByNameAndPassword(SAFEBOX_NAME, SAFEBOX_PASSWORD);
            verify(safeBoxRepository, times(1)).save(getSafeBoxWithoutId());
        }

    }

    @Test
    void testCreateSafeBox_whenCreated() {
        try(MockedStatic<Cypher> cypher = Mockito.mockStatic(Cypher.class);
            MockedStatic<UuidGenerator> uuidGenerated = Mockito.mockStatic(UuidGenerator.class)) {
            uuidGenerated.when(UuidGenerator::createUuid).thenReturn(UUID);
            cypher.when(() -> Cypher.encrypt(anyString(), anyInt())).thenReturn(SAFEBOX_PASSWORD);
            when(safeBoxRepository.findSafeBoxByNameAndPassword(anyString(), anyString())).thenReturn(getMonoSafeBox());

            SafeBox result = safeBoxService.createNewSafeBox(SAFEBOX_NAME, SAFEBOX_PASSWORD).block();

            assertNotNull(result);
            assertEquals(SAFEBOX_ID, result.getId());
            assertEquals(SAFEBOX_NAME, result.getName());
            assertEquals(UUID, result.getUuid());
            assertEquals(SAFEBOX_PASSWORD, result.getPassword());
            assertEquals(0, result.getBlocked());
            verify(safeBoxRepository, times(1)).findSafeBoxByNameAndPassword(SAFEBOX_NAME, SAFEBOX_PASSWORD);
        }
    }

    @Test
    void testUpdateSafebox(){
        safeBoxService.updateSafeBox(getMonoSafeBox().block());

        verify(safeBoxRepository,times(1)).save(getMonoSafeBox().block());
        verifyNoMoreInteractions(safeBoxRepository);
    }

    @Test
    void testFindSafeBoWithCorrectParams(){
        when(safeBoxRepository.findSafeBoxByNameAndPassword(anyString(), anyString())).thenReturn(getMonoSafeBox());

        SafeBox result = safeBoxService.findSafeBox(SAFEBOX_NAME, SAFEBOX_PASSWORD).block();

        assertNotNull(result);
        assertEquals(SAFEBOX_NAME, result.getName());
        assertEquals(SAFEBOX_ID, result.getId());
        assertEquals(UUID, result.getUuid());
        assertEquals(SAFEBOX_PASSWORD, result.getPassword());
        assertEquals(0, result.getBlocked());
        verify(safeBoxRepository,times(1)).findSafeBoxByNameAndPassword(SAFEBOX_NAME, SAFEBOX_PASSWORD);
        verifyNoMoreInteractions(safeBoxRepository);
    }

    @Test
    void testFindSafeBoWithIncorrectParams(){
        when(safeBoxRepository.findSafeBoxByNameAndPassword(anyString(), anyString())).thenReturn(Mono.empty());

        SafeBox result = safeBoxService.findSafeBox(SAFEBOX_NAME, SAFEBOX_PASSWORD).block();

        assertNull(result);
        verify(safeBoxRepository,times(1)).findSafeBoxByNameAndPassword(SAFEBOX_NAME, SAFEBOX_PASSWORD);
        verifyNoMoreInteractions(safeBoxRepository);
    }

    @Test
    void testFindSafeBoxByUuid(){
        when(safeBoxRepository.findByUuid(anyString())).thenReturn(getMonoSafeBox());

        SafeBox result = safeBoxRepository.findByUuid(UUID).block();

        assertNotNull(result);
        assertEquals(SAFEBOX_NAME, result.getName());
        assertEquals(SAFEBOX_ID, result.getId());
        assertEquals(UUID, result.getUuid());
        assertEquals(SAFEBOX_PASSWORD, result.getPassword());
        assertEquals(0, result.getBlocked());
        verify(safeBoxRepository,times(1)).findByUuid(UUID);
        verifyNoMoreInteractions(safeBoxRepository);
    }

    @Test
    void testFindSafeBoxByUuid_returnException(){
        when(safeBoxRepository.findByUuid(anyString())).thenThrow(new SafeboxNotFoundException(UUID));

        assertThrows(SafeboxNotFoundException.class,() -> {
            safeBoxService.findSafeBox(UUID);
        });
    }

    private Mono<SafeBox> getMonoSafeBox() {
        SafeBox safeBox = new SafeBox();
        safeBox.setId(SAFEBOX_ID);
        safeBox.setUuid(UUID);
        safeBox.setName(SAFEBOX_NAME);
        safeBox.setPassword(SAFEBOX_PASSWORD);
        safeBox.setBlocked(0);
        return Mono.just(safeBox);
    }

    private SafeBox getSafeBox() {
        SafeBox safeBox = new SafeBox();
        safeBox.setId(SAFEBOX_ID);
        safeBox.setUuid(UUID);
        safeBox.setName(SAFEBOX_NAME);
        safeBox.setPassword(SAFEBOX_PASSWORD);
        safeBox.setBlocked(0);
        return safeBox;
    }

    private SafeBox getSafeBoxWithoutId(){
        SafeBox safebox = getSafeBox();
        safebox.setId(null);
        return safebox;
    }

}
