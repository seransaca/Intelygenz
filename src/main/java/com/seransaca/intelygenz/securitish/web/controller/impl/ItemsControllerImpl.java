package com.seransaca.intelygenz.securitish.web.controller.impl;

import com.seransaca.intelygenz.securitish.entity.Items;
import com.seransaca.intelygenz.securitish.service.ItemsService;
import com.seransaca.intelygenz.securitish.service.request.PutItemsRequest;
import com.seransaca.intelygenz.securitish.web.controller.ItemsController;
import com.seransaca.intelygenz.securitish.web.converter.ItemsConverter;
import com.seransaca.intelygenz.securitish.web.dto.ItemsDTO;
import com.seransaca.intelygenz.securitish.web.dto.ItemsRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemsControllerImpl implements ItemsController {

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private ItemsConverter itemsConverter;

    @Override
    public ResponseEntity<ItemsDTO> getItems(String safeboxId) {

        List<Items> items = itemsService.findItems(safeboxId);
        ItemsDTO dto = itemsConverter.itemsToDto(items);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ItemsDTO> putItems(String safeboxId, ItemsRequestDTO request) {

        PutItemsRequest putItemsRequest = itemsConverter.toRequest(safeboxId, request);
        List<Items> items = itemsService.createItems(putItemsRequest);
        ItemsDTO dto = itemsConverter.itemsToDto(items);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
