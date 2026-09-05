package com.slockey.dreamlightlegend.gameobjects.map;

import java.util.UUID;

public class IdGenerator {

    private static final String NULL_ID_STRING = "NULL_ID";

    public static String getRandomIdString() {
        return UUID.randomUUID().toString();
    }

    public static String getNamedIdString(String name) {
        return UUID.fromString(name).toString();
    }

    public static String getNullIdString() {
        return NULL_ID_STRING;
    }
}
