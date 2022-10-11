package com.seransaca.intelygenz.securitish.web.converter;

import com.seransaca.intelygenz.securitish.entity.Items;
import com.seransaca.intelygenz.securitish.security.Cypher;
import com.seransaca.intelygenz.securitish.service.exceptions.CypherException;
import com.seransaca.intelygenz.securitish.service.exceptions.MalformedDataException;
import com.seransaca.intelygenz.securitish.service.request.Constants;
import com.seransaca.intelygenz.securitish.service.request.PutItemsRequest;
import com.seransaca.intelygenz.securitish.web.dto.ItemDTO;
import com.seransaca.intelygenz.securitish.web.dto.ItemsDTO;
import com.seransaca.intelygenz.securitish.web.dto.ItemsRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    default Mono<ItemsDTO> itemsToDto(Flux<Items> items){
        return items.collectList()
                .filter(Objects::nonNull)
                .flatMap(items2 -> {
                    ItemsDTO dto = new ItemsDTO(new ArrayList<>());
                    items2.forEach(item -> Mono.just(item)
                            .zipWith(Mono.just(Cypher.decrypt(item.getItem(), Cypher.TYPE_ITEM)))
                            .map(tuples -> dto.getItemList().add(new ItemDTO(tuples.getT1().getId(), tuples.getT2())))
                            .switchIfEmpty(Mono.error(new CypherException(item.getItem(), Constants.ERROR_ITEM_DECRYPT))));
                    return Mono.just(dto);
                });
    }
}
