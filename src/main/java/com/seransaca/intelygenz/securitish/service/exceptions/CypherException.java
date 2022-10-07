package com.seransaca.intelygenz.securitish.service.exceptions;

import com.seransaca.intelygenz.securitish.service.request.Constants;

public class CypherException extends SafeishRuntimeException{

    public final static Integer TYPE_ITEM_CYPHER_EXCEPTION = 1;

    public final static Integer TYPE_PASSSWORD_CYPHER_EXCEPTION = 2;

    public CypherException(String msg, Integer type) {
        super(String.format(type.equals(TYPE_ITEM_CYPHER_EXCEPTION) ?
                        Constants.ERROR_ITEM_ENCRYPT :
                        Constants.ERROR_PASSWORD_ENCRYPT,
                msg));
    }
}
