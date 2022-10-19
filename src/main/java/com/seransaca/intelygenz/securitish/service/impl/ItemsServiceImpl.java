package com.seransaca.intelygenz.securitish.service.impl;

import com.seransaca.intelygenz.securitish.entity.Items;
import com.seransaca.intelygenz.securitish.entity.SafeBox;
import com.seransaca.intelygenz.securitish.repository.ItemsRepository;
import com.seransaca.intelygenz.securitish.repository.SafeBoxRepository;
import com.seransaca.intelygenz.securitish.security.Cypher;
import com.seransaca.intelygenz.securitish.service.ItemsService;
import com.seransaca.intelygenz.securitish.service.exceptions.CypherException;
import com.seransaca.intelygenz.securitish.service.exceptions.ItemNotFoundException;
import com.seransaca.intelygenz.securitish.service.exceptions.SafeboxNotFoundException;
import com.seransaca.intelygenz.securitish.service.request.Constants;
import com.seransaca.intelygenz.securitish.service.request.PutItemsRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

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
        Flux<Items> list = Flux.fromIterable(request.getItems())
                .map(item -> Items.builder().uuid(request.getUuid()).item(Cypher.encrypt(item, Cypher.TYPE_ITEM)).build())
                .switchIfEmpty(Flux.error(new CypherException("Create items error", Constants.ERROR_ITEM_DECRYPT)));
        return itemsRepository.saveAll(list);
    }

    @Override
    public Flux<Items> findItems(String uuid) {
        return safeBoxRepository.findByUuid(uuid)
                .filter(Objects::nonNull)
                .flatMapMany(safeBox -> findItemsBBDD(uuid))
                .switchIfEmpty(Flux.error(new SafeboxNotFoundException(uuid)));
    }

    private Flux<Items> findItemsBBDD(String uuid){
        return itemsRepository.findByUuid(uuid)
                .switchIfEmpty(Flux.error(new ItemNotFoundException(uuid)));
    }
}
