package com.seransaca.intelygenz.securitish.service.exceptions;

import com.seransaca.intelygenz.securitish.service.request.Constants;

public class MalformedDataException extends SafeishRuntimeException{

    public MalformedDataException(String msg) {
        super(Constants.ERROR_MALFORMED_DATA);
    }
}
