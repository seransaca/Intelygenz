package com.seransaca.intelygenz.securitish.service.exceptions;

import com.seransaca.intelygenz.securitish.service.request.Constants;

public class UnauthorizedException extends SafeishRuntimeException{

    public UnauthorizedException() {
        super(Constants.ERROR_AUTHORIZATION_NOT_FOUNT);
    }
}
