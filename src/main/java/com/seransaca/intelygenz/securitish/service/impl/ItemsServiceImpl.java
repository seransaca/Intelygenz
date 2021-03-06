package com.seransaca.intelygenz.securitish.service.impl;

import com.seransaca.intelygenz.securitish.entity.Items;
import com.seransaca.intelygenz.securitish.entity.SafeBox;
import com.seransaca.intelygenz.securitish.repository.ItemsRepository;
import com.seransaca.intelygenz.securitish.repository.SafeBoxRepository;
import com.seransaca.intelygenz.securitish.security.Cypher;
import com.seransaca.intelygenz.securitish.service.ItemsService;
import com.seransaca.intelygenz.securitish.service.exceptions.SafeboxNotFoundException;
import com.seransaca.intelygenz.securitish.service.request.PutItemsRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class ItemsServiceImpl implements ItemsService {

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private SafeBoxRepository safeBoxRepository;


    @Override
    public List<Items> createItems(PutItemsRequest request) throws Exception {

        List<Items> list = new ArrayList<>();
        Items items = null;

        for(String item : request.getItems()){
            items = new Items();
            items.setUuid(request.getUuid());
            items.setItem(Cypher.encrypt(item));

            items = itemsRepository.save(items);
            list.add(items);
        }

        return list;
    }

    @Override
    public List<Items> findItems(String uuid) {

        safeBoxRepository.findByUuid(uuid)
                .orElseThrow(() -> new SafeboxNotFoundException(uuid));

        return itemsRepository.findByUuid(uuid);
    }
}
