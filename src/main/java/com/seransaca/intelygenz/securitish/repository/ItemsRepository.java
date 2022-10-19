package com.seransaca.intelygenz.securitish.repository;

import com.seransaca.intelygenz.securitish.entity.Items;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ItemsRepository extends R2dbcRepository<Items, Integer> {

    @Query("SELECT * FROM items  WHERE uuid = $1 ORDER BY id ASC")
    Flux<Items> findByUuid(String uuid);

}
