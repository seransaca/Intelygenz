package com.seransaca.intelygenz.securitish.service.impl;

import com.seransaca.intelygenz.securitish.entity.Items;
import com.seransaca.intelygenz.securitish.repository.ItemsRepository;
import com.seransaca.intelygenz.securitish.repository.SafeBoxRepository;
import com.seransaca.intelygenz.securitish.security.Cypher;
import com.seransaca.intelygenz.securitish.service.ItemsService;
import com.seransaca.intelygenz.securitish.service.exceptions.CypherException;
import com.seransaca.intelygenz.securitish.service.exceptions.SafeboxNotFoundException;
import com.seransaca.intelygenz.securitish.service.request.Constants;
import com.seransaca.intelygenz.securitish.service.request.PutItemsRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Log4j2
public class ItemsServiceImpl implements ItemsService {

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private SafeBoxRepository safeBoxRepository;


    @Override
    public Flux<Items> createItems(PutItemsRequest request) {
        Flux<Items> list = Flux.fromIterable(request.getItems())
                .map(item -> Items.builder().uuid(request.getUuid()).item(Cypher.encrypt(item, Cypher.TYPE_ITEM)).build())
                .switchIfEmpty(Flux.error(new CypherException("Create items error", Constants.ERROR_ITEM_DECRYPT)));
        return itemsRepository.saveAll(list);
    }

    @Override
    public Flux<Items> findItems(String uuid) {
        safeBoxRepository.findByUuid(uuid).onErrorMap(error -> new SafeboxNotFoundException(uuid));
        return itemsRepository.findByUuid(uuid);
    }
}
