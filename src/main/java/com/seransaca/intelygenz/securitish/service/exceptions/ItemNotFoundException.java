package com.seransaca.intelygenz.securitish.service.exceptions;

import com.seransaca.intelygenz.securitish.service.request.Constants;

public class ItemNotFoundException extends SafeishRuntimeException{

    public ItemNotFoundException(String msg) {
        super(String.format(Constants.ERROR_ITEMS_NOT_FOUND,msg));
    }
}
