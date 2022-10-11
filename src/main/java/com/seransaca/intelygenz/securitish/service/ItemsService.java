package com.seransaca.intelygenz.securitish.service;

import com.seransaca.intelygenz.securitish.entity.Items;
import com.seransaca.intelygenz.securitish.entity.SafeBox;
import com.seransaca.intelygenz.securitish.service.request.PutItemsRequest;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ItemsService {

    /**
     * Return a list of items when is created.
     *
     * @return A list of Items
     */
    public Flux<Items> createItems(PutItemsRequest request);

    /**
     * Return a list of items of safebox.
     *
     * @return A list of Items
     */
    public Flux<Items> findItems(String uuid);
}
