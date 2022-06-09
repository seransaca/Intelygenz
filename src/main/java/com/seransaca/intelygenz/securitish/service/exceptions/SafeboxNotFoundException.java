package com.seransaca.intelygenz.securitish.service.exceptions;

import com.seransaca.intelygenz.securitish.service.request.Constants;

public class SafeboxNotFoundException extends SafeishRuntimeException{

    public SafeboxNotFoundException(String msg) {
        super(String.format(Constants.ERROR_SAFEBOX_NOT_FOUND,msg));
    }
}
