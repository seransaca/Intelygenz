package com.seransaca.intelygenz.securitish.service.exceptions;

import com.seransaca.intelygenz.securitish.service.request.Constants;

public class CypherException extends SafeishRuntimeException{

    public CypherException(String msg) {
        super(String.format(Constants.ERROR_ITEM_ENCRYPT,msg));
    }
}
