package com.seransaca.intelygenz.securitish.service.exceptions;

import com.seransaca.intelygenz.securitish.service.request.Constants;

public class TokenExpiredException extends SafeishRuntimeException{

    public TokenExpiredException(String msg) {
        super(String.format(Constants.ERROR_TOKEN_EXPIRED,msg));
    }
}
