package com.seransaca.intelygenz.securitish.web.converter;

import com.seransaca.intelygenz.securitish.entity.Items;
import com.seransaca.intelygenz.securitish.security.Cypher;
import com.seransaca.intelygenz.securitish.service.exceptions.MalformedDataException;
import com.seransaca.intelygenz.securitish.service.request.PutItemsRequest;
import com.seransaca.intelygenz.securitish.web.dto.ItemDTO;
import com.seransaca.intelygenz.securitish.web.dto.ItemsDTO;
import com.seransaca.intelygenz.securitish.web.dto.ItemsRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is in charge of converting the Items entity to DTO.
 *
 * @author Sergio A. Sánchez Camarero
 */

@Mapper
public interface ItemsConverter {

    @Mapping(target = "uuid", source = "safeboxId")
    @Mapping(target = "items", expression = "java(items.getItems())")
    PutItemsRequest toRequest(String safeboxId, ItemsRequestDTO items);

    default ItemsDTO itemsToDto(List<Items> items) throws Exception {

        ItemsDTO dto = new ItemsDTO(new ArrayList<>());
        items.stream().forEach(item -> dto.getItemList().add(new ItemDTO(item.getId(),item.getItem())));
        return dto;

        /*for(Items item1: items){
            item = new ItemDTO();
            item.setItemId(item1.getId());
            item.setItemName(Cypher.decrypt(item1.getItem()));
            dto.getItemList().add(item);
        }
        return dto;*/
    }
}
