package com.seransaca.intelygenz.securitish.service.impl;

import com.seransaca.intelygenz.securitish.entity.SafeBox;
import com.seransaca.intelygenz.securitish.repository.SafeBoxRepository;
import com.seransaca.intelygenz.securitish.security.Cypher;
import com.seransaca.intelygenz.securitish.service.SafeBoxService;
import com.seransaca.intelygenz.securitish.service.exceptions.CypherException;
import com.seransaca.intelygenz.securitish.service.exceptions.SafeboxNotFoundException;
import com.seransaca.intelygenz.securitish.service.request.Constants;
import com.seransaca.intelygenz.securitish.utils.UuidGenerator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@Log4j2
public class SafeBoxServiceImpl implements SafeBoxService {

    @Autowired
    private SafeBoxRepository safeBoxRepository;

    @Override
    @Transactional
    public Mono<SafeBox> createNewSafeBox(String name, String password){
        log.info("Creating SafeBox...");
        return Mono.just(Cypher.encrypt(password, Cypher.TYPE_PASSWORD))
                .flatMap(pass -> checkSafeBoxExists(name, pass, password))
                .switchIfEmpty(Mono.error(new CypherException(password, Constants.ERROR_PASSWORD_ENCRYPT)));
    }

    @Override
    public Mono<SafeBox> findSafeBox(String name, String password) {
        return safeBoxRepository.findSafeBoxByNameAndPassword(name, password);
    }

    @Override
    public Mono<SafeBox> findSafeBox(String uuid) {
        return safeBoxRepository.findByUuid(uuid).switchIfEmpty(Mono.error(new SafeboxNotFoundException(uuid)));
    }

    @Override
    public void updateSafeBox(SafeBox safeBox) {
        safeBoxRepository.save(safeBox);
    }

    private Mono<SafeBox> createSafeBox(String name, String password){
        return Mono.just(Cypher.encrypt(password, Cypher.TYPE_PASSWORD))
                .zipWith(Mono.just(UuidGenerator.createUuid()))
                .flatMap(tuple -> {
                    SafeBox safeBox = new SafeBox();
                    safeBox.setName(name);
                    safeBox.setBlocked(SafeBox.SAFEBOX_RETRIES_INITIANIZED);
                    safeBox.setUuid(tuple.getT2());
                    safeBox.setPassword(tuple.getT1());
                    return safeBoxRepository.save(safeBox);
                })
                .switchIfEmpty(Mono.error(new CypherException(password, Constants.ERROR_PASSWORD_ENCRYPT)));

    }

    private Mono<SafeBox> checkSafeBoxExists(String name, String passEncripted, String password){
        return findSafeBox(name, passEncripted)
                .filter(Objects::nonNull)
                .map(safebox1 -> safebox1)
                .switchIfEmpty(createSafeBox(name, password));
    }
}
