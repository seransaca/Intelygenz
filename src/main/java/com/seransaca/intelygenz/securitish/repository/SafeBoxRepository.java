package com.seransaca.intelygenz.securitish.repository;

import com.seransaca.intelygenz.securitish.entity.SafeBox;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SafeBoxRepository extends R2dbcRepository<SafeBox, Integer> {

    @Query("SELECT * FROM safebox  WHERE name = $1 AND password = $2")
    Mono<SafeBox> findSafeBoxByNameAndPassword(String name, String password);

    @Query("SELECT * FROM safebox WHERE uuid = $1")
    Mono<SafeBox> findByUuid(String uuid);

}
