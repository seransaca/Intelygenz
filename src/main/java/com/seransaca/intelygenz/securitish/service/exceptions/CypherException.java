package com.seransaca.intelygenz.securitish.service.exceptions;

import com.seransaca.intelygenz.securitish.service.request.Constants;

public class CypherException extends SafeishRuntimeException{

    public CypherException(String msg, String error){
        super(String.format(error, msg));
    }

}
