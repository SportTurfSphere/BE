package com.turf.user.constants;

public final class APIEndpoints {
    private APIEndpoints() {
    }
    public static final String CREATE = "";
    public static final String UPDATE = "/{id}";
    public static final String DELETE = "/{id}";
    public static final String API_V1_AUTH = "/api/v1/auth";
    public static final String API_V1_AUDIT = "/api/v1/audit/log";
    public static final String LOGIN = "/login";
    public static final String VALIDATE_TOKEN = "/validate-token";
    public static final String REFRESH_TOKEN = "/refresh-token";
    public static final String LOGOUT = "/logout";
    public static final String PASSWORD_RESET = "/password-reset";
    public static final String RESET_PASSWORD = "/reset-password";
}
