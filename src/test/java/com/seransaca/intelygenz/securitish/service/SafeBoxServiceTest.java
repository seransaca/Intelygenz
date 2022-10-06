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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class SafeBoxServiceTest {

    private final static String UUID = "1aaf1523-e07c-4394-9ffb-ff8b0fdbf1ff";

    private final static Integer SAFEBOX_ID = 1;

    private final static String SAFEBOX_NAME = "safeboxName";

    private final static String SAFEBOX_PASSWORD = "safeboxPassword";

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
    void testCreateSafeBox_whenNotCreated() throws Exception {
        try (MockedStatic<Cypher> cypher = Mockito.mockStatic(Cypher.class);
             MockedStatic<UuidGenerator> uuidGenerated = Mockito.mockStatic(UuidGenerator.class)) {
            uuidGenerated.when(UuidGenerator::createUuid).thenReturn(UUID);
            cypher.when(() -> Cypher.encrypt(anyString())).thenReturn(SAFEBOX_PASSWORD);
            when(safeBoxRepository.findSafeBoxByNameAndPassword(anyString(), anyString())).thenReturn(new ArrayList<>());
            when(safeBoxRepository.save(any(SafeBox.class))).thenReturn(getSafeBox());

            SafeBox result = safeBoxService.createNewSafeBox(SAFEBOX_NAME, SAFEBOX_PASSWORD);

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
    void testCreateSafeBox_whenCreated() throws Exception {
        try(MockedStatic<Cypher> cypher = Mockito.mockStatic(Cypher.class);
            MockedStatic<UuidGenerator> uuidGenerated = Mockito.mockStatic(UuidGenerator.class)) {
            uuidGenerated.when(UuidGenerator::createUuid).thenReturn(UUID);
            cypher.when(() -> Cypher.encrypt(anyString())).thenReturn(SAFEBOX_PASSWORD);
            when(safeBoxRepository.findSafeBoxByNameAndPassword(anyString(), anyString())).thenReturn(getListSafeBox());

            SafeBox result = safeBoxService.createNewSafeBox(SAFEBOX_NAME, SAFEBOX_PASSWORD);

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
        safeBoxRepository.save(getSafeBox());

        verify(safeBoxRepository,times(1)).save(getSafeBox());
        verifyNoMoreInteractions(safeBoxRepository);
    }

    @Test
    void testFindSafeBoWithCorrectParams(){
        when(safeBoxRepository.findSafeBoxByNameAndPassword(anyString(), anyString())).thenReturn(getListSafeBox());

        List<SafeBox> result = safeBoxRepository.findSafeBoxByNameAndPassword(SAFEBOX_NAME, SAFEBOX_PASSWORD);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(SAFEBOX_NAME, result.get(0).getName());
        assertEquals(SAFEBOX_ID, result.get(0).getId());
        assertEquals(UUID, result.get(0).getUuid());
        assertEquals(SAFEBOX_PASSWORD, result.get(0).getPassword());
        assertEquals(0, result.get(0).getBlocked());
        verify(safeBoxRepository,times(1)).findSafeBoxByNameAndPassword(SAFEBOX_NAME, SAFEBOX_PASSWORD);
        verifyNoMoreInteractions(safeBoxRepository);
    }

    @Test
    void testFindSafeBoWithIncorrectParams(){
        when(safeBoxRepository.findSafeBoxByNameAndPassword(anyString(), anyString())).thenReturn(new ArrayList<>());

        List<SafeBox> result = safeBoxRepository.findSafeBoxByNameAndPassword(SAFEBOX_NAME, SAFEBOX_PASSWORD);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(safeBoxRepository,times(1)).findSafeBoxByNameAndPassword(SAFEBOX_NAME, SAFEBOX_PASSWORD);
        verifyNoMoreInteractions(safeBoxRepository);
    }

    @Test
    void testFindSafeBoxByUuid(){
        when(safeBoxRepository.findByUuid(anyString())).thenReturn(Optional.of(getSafeBox()));

        Optional<SafeBox> result = safeBoxRepository.findByUuid(UUID);

        assertNotNull(result);
        assertEquals(SAFEBOX_NAME, result.get().getName());
        assertEquals(SAFEBOX_ID, result.get().getId());
        assertEquals(UUID, result.get().getUuid());
        assertEquals(SAFEBOX_PASSWORD, result.get().getPassword());
        assertEquals(0, result.get().getBlocked());
        verify(safeBoxRepository,times(1)).findByUuid(UUID);
        verifyNoMoreInteractions(safeBoxRepository);
    }

    @Test
    void testFindSafeBoxByUuid_returnException(){
        when(safeBoxRepository.findByUuid(anyString())).thenThrow(new SafeboxNotFoundException(UUID));

        assertThrows(SafeboxNotFoundException.class,() -> {
            safeBoxRepository.findByUuid(UUID);
        });
    }

    private List<SafeBox> getListSafeBox(){
        List<SafeBox> list = new ArrayList<>();
        SafeBox safeBox = getSafeBox();
        list.add(safeBox);
        return list;
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
