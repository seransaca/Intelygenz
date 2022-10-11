package com.seransaca.intelygenz.securitish.service.impl;

import com.seransaca.intelygenz.securitish.entity.Items;
import com.seransaca.intelygenz.securitish.entity.SafeBox;
import com.seransaca.intelygenz.securitish.repository.ItemsRepository;
import com.seransaca.intelygenz.securitish.repository.SafeBoxRepository;
import com.seransaca.intelygenz.securitish.security.Cypher;
import com.seransaca.intelygenz.securitish.service.ItemsService;
import com.seransaca.intelygenz.securitish.service.exceptions.CypherException;
import com.seransaca.intelygenz.securitish.service.exceptions.SafeboxNotFoundException;
import com.seransaca.intelygenz.securitish.service.request.Constants;
import com.seransaca.intelygenz.securitish.service.request.PutItemsRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Log4j2
public class ItemsServiceImpl implements ItemsService {

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private SafeBoxRepository safeBoxRepository;


    @Override
    public Flux<Items> createItems(PutItemsRequest request) {
        return Flux.fromStream(request.getItems().stream())
                .filter(Objects::nonNull)
                .map(item -> {
                    return itemsRepository.save(Items.builder().uuid(request.getUuid()).item(Cypher.encrypt(item, Cypher.TYPE_ITEM)).build());
                })
                .switchIfEmpty(Flux.error(new CypherException(request.getItems().toString(), Constants.ERROR_ITEM_ENCRYPT)));
    }

    @Override
    public Flux<Items> findItems(String uuid) {
        safeBoxRepository.findByUuid(uuid).onErrorMap(error -> new SafeboxNotFoundException(uuid));
        return itemsRepository.findByUuid(uuid);
    }
}
