package com.seransaca.intelygenz.securitish.web.controller.impl;

import com.seransaca.intelygenz.securitish.entity.SafeBox;
import com.seransaca.intelygenz.securitish.security.Cypher;
import com.seransaca.intelygenz.securitish.security.JWTUtils;
import com.seransaca.intelygenz.securitish.service.SafeBoxService;
import com.seransaca.intelygenz.securitish.service.exceptions.CypherException;
import com.seransaca.intelygenz.securitish.service.exceptions.UnauthorizedException;
import com.seransaca.intelygenz.securitish.service.request.Constants;
import com.seransaca.intelygenz.securitish.web.controller.OpenController;
import com.seransaca.intelygenz.securitish.web.dto.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
public class OpenControllerImpl implements OpenController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SafeBoxService safeBoxService;

    @Override
    public ResponseEntity<TokenDTO> openSafebox(String safeboxId) {
        return new ResponseEntity<>(Mono.just(safeboxId)
                .map(id -> safeBoxService.findSafeBox(id))
                .zipWith(Mono.just(request.getHeader("Authorization")))
                .filter(tuple -> Objects.nonNull(tuple.getT2()) && tuple.getT2().toLowerCase().startsWith("basic"))
                .flatMap(tuple -> processData(tuple.getT1(), tuple.getT2()))
                .switchIfEmpty(Mono.error(new UnauthorizedException()))
                .block(),
                HttpStatus.OK);
    }

    private Mono<TokenDTO> processData(Mono<SafeBox> safeBox, String authorization) {
        return safeBox.filter(Objects::nonNull).flatMap(safeBox1 -> Mono.just(Cypher.decrypt(safeBox1.getPassword(), Cypher.TYPE_PASSWORD)))
                .filter(decrypted -> decrypted.equals(JWTUtils.getValue(authorization, 1)))
                .flatMap(validPassword -> checkNameAndPassword(safeBox,JWTUtils.getValue(authorization, 0), JWTUtils.getValue(authorization, 1)))
                .switchIfEmpty(Mono.error(new CypherException(JWTUtils.getValue(authorization, 1), Constants.ERROR_PASSWORD_DECRYPT)));
    }

    private Mono<TokenDTO> checkNameAndPassword(Mono<SafeBox> safeBox, String name, String pass) {
        return safeBox.filter(Objects::nonNull)
                .flatMap(result -> {
                    if(result.getName().equals(name)){
                        result.setBlocked(SafeBox.SAFEBOX_RETRIES_INITIANIZED);
                        safeBoxService.updateSafeBox(result);
                        return Mono.just(new TokenDTO(JWTUtils.createJwtToken(name, pass)));
                    }else{
                        result.setBlocked(result.getBlocked()+1);
                        safeBoxService.updateSafeBox(result);
                        return Mono.empty();
                    }
                })
                .switchIfEmpty(Mono.error(new UnauthorizedException()));
    }
}
