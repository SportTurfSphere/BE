package com.turf.common.util;


import java.util.UUID;

@lombok.experimental.UtilityClass
public class CommonEntityUtils {

    public static String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
}
