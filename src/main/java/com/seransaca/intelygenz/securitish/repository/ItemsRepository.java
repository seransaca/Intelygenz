package com.seransaca.intelygenz.securitish.repository;

import com.seransaca.intelygenz.securitish.entity.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Integer> {

    @Query("SELECT i FROM Items i WHERE i.uuid = :uuid ORDER BY id DESC")
    Flux<Items> findByUuid(String uuid);

}
