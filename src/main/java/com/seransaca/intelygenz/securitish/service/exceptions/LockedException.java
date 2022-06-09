package com.seransaca.intelygenz.securitish.service.exceptions;

import com.seransaca.intelygenz.securitish.service.request.Constants;

public class LockedException extends SafeishRuntimeException{

    public LockedException(String msg) {
        super(String.format(Constants.ERROR_SAFEBOX_LOCKED,msg));
    }
}
