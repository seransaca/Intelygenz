package com.seransaca.intelygenz.securitish.repository;

import com.seransaca.intelygenz.securitish.entity.SafeBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SafeBoxRepository extends JpaRepository<SafeBox, Integer> {

    @Query("SELECT sb FROM SafeBox sb WHERE sb.name = :name AND sb.password = :password")
    List<SafeBox> findSafeBoxByNameAndPassword(String name, String password);

    @Query("SELECT sb FROM SafeBox sb WHERE sb.uuid = :uuid")
    Optional<SafeBox> findByUuid(String uuid);

}
