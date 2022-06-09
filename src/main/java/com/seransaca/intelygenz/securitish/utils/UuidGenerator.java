package com.seransaca.intelygenz.securitish.utils;

import java.util.UUID;

public class UuidGenerator {

    public static String createUuid(){
        UUID uuid = UUID.randomUUID();

        return uuid.toString();
    }
}
