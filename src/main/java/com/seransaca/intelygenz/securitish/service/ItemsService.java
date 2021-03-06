package com.seransaca.intelygenz.securitish.service;

import com.seransaca.intelygenz.securitish.entity.Items;
import com.seransaca.intelygenz.securitish.entity.SafeBox;
import com.seransaca.intelygenz.securitish.service.request.PutItemsRequest;

import java.util.List;

public interface ItemsService {

    /**
     * Return a list of items when is created.
     *
     * @return A list of Items
     */
    public List<Items> createItems(PutItemsRequest request) throws Exception;

    /**
     * Return a list of items of safebox.
     *
     * @return A list of Items
     */
    public List<Items> findItems(String uuid);
}
