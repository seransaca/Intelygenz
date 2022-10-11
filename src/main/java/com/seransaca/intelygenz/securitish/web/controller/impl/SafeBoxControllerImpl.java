package com.seransaca.intelygenz.securitish.web.controller.impl;

import com.seransaca.intelygenz.securitish.service.SafeBoxService;
import com.seransaca.intelygenz.securitish.web.controller.SafeBoxController;
import com.seransaca.intelygenz.securitish.web.converter.SafeBoxConverter;
import com.seransaca.intelygenz.securitish.web.dto.SafeBoxDTO;
import com.seransaca.intelygenz.securitish.web.dto.SafeBoxRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class SafeBoxControllerImpl implements SafeBoxController {

    @Autowired
    private SafeBoxService createSafeBoxService;

    @Autowired
    private SafeBoxConverter safeBoxConverter;

    @Override
    public ResponseEntity<SafeBoxDTO> createSafeBox(SafeBoxRequestDTO request){
        return new ResponseEntity<>(createSafeBoxService.createNewSafeBox(request.getName(), request.getPassword())
                .map(safebox -> safeBoxConverter.safeboxToDto(safebox))
                .block(),
                HttpStatus.OK);
    }
}
