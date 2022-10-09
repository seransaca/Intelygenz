package com.seransaca.intelygenz.securitish.web.controller.impl;

import com.seransaca.intelygenz.securitish.service.ItemsService;
import com.seransaca.intelygenz.securitish.web.controller.ItemsController;
import com.seransaca.intelygenz.securitish.web.converter.ItemsConverter;
import com.seransaca.intelygenz.securitish.web.dto.ItemsDTO;
import com.seransaca.intelygenz.securitish.web.dto.ItemsRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ItemsControllerImpl implements ItemsController {

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private ItemsConverter itemsConverter;

    @Override
    public ResponseEntity<ItemsDTO> getItems(String safeboxId) {
        return new ResponseEntity<>(Mono.just(itemsService.findItems(safeboxId))
                .map(items -> itemsConverter.itemsToDto(items))
                .block(),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ItemsDTO> putItems(String safeboxId, ItemsRequestDTO request) {
        return new ResponseEntity<>(Mono.just(itemsConverter.toRequest(safeboxId, request))
                .map(putRequest -> itemsService.createItems(putRequest))
                .map(items -> itemsConverter.itemsToDto(items))
                .block(),
                HttpStatus.OK);
    }
}
