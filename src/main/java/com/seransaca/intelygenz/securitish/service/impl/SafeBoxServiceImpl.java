package com.seransaca.intelygenz.securitish.service.impl;

import com.seransaca.intelygenz.securitish.entity.SafeBox;
import com.seransaca.intelygenz.securitish.repository.SafeBoxRepository;
import com.seransaca.intelygenz.securitish.security.Cypher;
import com.seransaca.intelygenz.securitish.service.SafeBoxService;
import com.seransaca.intelygenz.securitish.service.exceptions.SafeboxNotFoundException;
import com.seransaca.intelygenz.securitish.utils.UuidGenerator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
public class SafeBoxServiceImpl implements SafeBoxService {

    @Autowired
    private SafeBoxRepository safeBoxRepository;

    @Override
    @Transactional
    public SafeBox createNewSafeBox(String name, String password) throws Exception {
        log.info("Creating SafeBox...");
        List<SafeBox> list = null;
        SafeBox safeBox = null;

        list = findSafeBox(name, password);

        if(list.isEmpty()){
            safeBox = new SafeBox();
            safeBox.setUuid(UuidGenerator.createUuid());
            safeBox.setName(name);
            safeBox.setPassword(Cypher.encrypt(password));
            safeBox.setBlocked(SafeBox.SAFEBOX_RETRIES_INITIANIZED);
            safeBox = safeBoxRepository.save(safeBox);
        }else{
            safeBox = list.get(0);
        }

        return safeBox;
    }

    @Override
    public List<SafeBox> findSafeBox(String name, String password) {
        return safeBoxRepository.findSafeBoxByNameAndPassword(name, password);
    }

    @Override
    public SafeBox findSafeBox(String uuid) {
        return safeBoxRepository.findByUuid(uuid)
                .orElseThrow(() -> new SafeboxNotFoundException(uuid));
    }

    @Override
    public void updateSafeBox(SafeBox safeBox) {
        safeBoxRepository.save(safeBox);
    }
}
