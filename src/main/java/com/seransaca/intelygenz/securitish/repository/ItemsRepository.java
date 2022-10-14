package com.seransaca.intelygenz.securitish.repository;

import com.seransaca.intelygenz.securitish.entity.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemsRepository extends R2dbcRepository<Items, Integer> {

    @Query("SELECT * FROM items  WHERE uuid = $1 ORDER BY id DESC")
    Flux<Items> findByUuid(String uuid);

}
