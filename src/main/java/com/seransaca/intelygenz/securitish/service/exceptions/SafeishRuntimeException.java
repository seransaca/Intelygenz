package com.seransaca.intelygenz.securitish.service.exceptions;

import org.springframework.core.NestedRuntimeException;

public class SafeishRuntimeException extends NestedRuntimeException {

    public SafeishRuntimeException(String msg) {
        super(msg);
    }
}
