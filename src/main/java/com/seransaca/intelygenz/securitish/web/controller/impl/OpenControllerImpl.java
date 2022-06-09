package com.seransaca.intelygenz.securitish.web.controller.impl;

import com.seransaca.intelygenz.securitish.entity.Items;
import com.seransaca.intelygenz.securitish.entity.SafeBox;
import com.seransaca.intelygenz.securitish.security.Cypher;
import com.seransaca.intelygenz.securitish.security.JWTUtils;
import com.seransaca.intelygenz.securitish.service.ItemsService;
import com.seransaca.intelygenz.securitish.service.SafeBoxService;
import com.seransaca.intelygenz.securitish.service.exceptions.LockedException;
import com.seransaca.intelygenz.securitish.service.exceptions.UnauthorizedException;
import com.seransaca.intelygenz.securitish.service.request.PutItemsRequest;
import com.seransaca.intelygenz.securitish.web.controller.ItemsController;
import com.seransaca.intelygenz.securitish.web.controller.OpenController;
import com.seransaca.intelygenz.securitish.web.converter.ItemsConverter;
import com.seransaca.intelygenz.securitish.web.dto.ItemsDTO;
import com.seransaca.intelygenz.securitish.web.dto.ItemsRequestDTO;
import com.seransaca.intelygenz.securitish.web.dto.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class OpenControllerImpl implements OpenController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SafeBoxService safeBoxService;

    @Override
    public ResponseEntity<TokenDTO> openSafebox(String safeboxId) throws Exception {

        TokenDTO token = new TokenDTO();
        SafeBox safeBox = safeBoxService.findSafeBox(safeboxId);

        String authorization = request.getHeader("Authorization");
        if(authorization == null || !authorization.toLowerCase().startsWith("basic"))
            throw new UnauthorizedException();
        if(safeBox.getBlocked() > 2){
            throw new LockedException(safeboxId);
        }
        String name = JWTUtils.getValue(authorization, 0);
        String pass = JWTUtils.getValue(authorization, 1);
        if(!safeBox.getName().equals(name)
            || !Cypher.decrypt(safeBox.getPassword()).equals(pass)){
            safeBox.setBlocked(safeBox.getBlocked()+1);
            safeBoxService.updateSafeBox(safeBox);
            throw new UnauthorizedException();
        }else{
            token.setToken(JWTUtils.createJwtToken(name, pass));
            safeBox.setBlocked(SafeBox.SAFEBOX_RETRIES_INITIANIZED);
            safeBoxService.updateSafeBox(safeBox);
        }

        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
