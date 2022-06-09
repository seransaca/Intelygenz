package com.seransaca.intelygenz.securitish.web.controller.impl;

import com.seransaca.intelygenz.securitish.entity.SafeBox;
import com.seransaca.intelygenz.securitish.service.SafeBoxService;
import com.seransaca.intelygenz.securitish.web.controller.SafeBoxController;
import com.seransaca.intelygenz.securitish.web.converter.SafeBoxConverter;
import com.seransaca.intelygenz.securitish.web.dto.SafeBoxDTO;
import com.seransaca.intelygenz.securitish.web.dto.SafeBoxRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SafeBoxControllerImpl implements SafeBoxController {

    @Autowired
    private SafeBoxService createSafeBoxService;

    @Autowired
    private SafeBoxConverter safeBoxConverter;

    @Override
    public ResponseEntity<SafeBoxDTO> createSafeBox(SafeBoxRequestDTO request) throws Exception {

        String name = request.getName();
        String password = request.getPassword();
        SafeBox safebox = createSafeBoxService.createNewSafeBox(name, password);
        SafeBoxDTO dto = safeBoxConverter.safeboxToDto(safebox);
        return new ResponseEntity<>(dto, HttpStatus.OK);

    }
}
