package com.seransaca.intelygenz.securitish.repository;

import com.seransaca.intelygenz.securitish.entity.SafeBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Repository
public interface SafeBoxRepository extends JpaRepository<SafeBox, Integer> {

    @Query("SELECT sb FROM SafeBox sb WHERE sb.name = :name AND sb.password = :password")
    Mono<SafeBox> findSafeBoxByNameAndPassword(String name, String password);

    @Query("SELECT sb FROM SafeBox sb WHERE sb.uuid = :uuid")
    Mono<SafeBox> findByUuid(String uuid);

}
