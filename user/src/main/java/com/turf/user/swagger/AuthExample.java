package com.turf.user.swagger;

public final class AuthExample {
    private AuthExample() {
    }

    public static final String LOGIN_REQ = """
            {
               "userName": "test_user@gmail.com",
               "password": "password"
             }""";
}