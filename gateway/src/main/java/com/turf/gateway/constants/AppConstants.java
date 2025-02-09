package com.turf.gateway.constants;

public class AppConstants {
    private AppConstants(){}

    public static final String DOCS_API = "/docs-api/";
    public static final String BEARER = "Bearer ";
    public static final String BEARER_TOKEN = "Bearer";
    public static final String AUTHORISATION_TOKEN = "Authorization";
    public static final String REFERER_POLICY = "Referrer-Policy";
    public static final String REFERER_POLICY_VALUE = "same-origin";
    public static final String X_FRAME_OPTIONS = "X-Frame-Options";
    public static final String X_FRAME_OPTIONS_VALUE = "SAMEORIGIN";
    public static final String LOGGEDIN_USER_NAME = "loggedInUserName";
    public static final String LOGGEDIN_USER_ID = "loggedInUserId";
    public static final String LOGGEDIN_USER_TYPE = "loggedInUserType";
    public static final String LOGGEDIN_USER_UUID = "loggedInUserUUID";
    public static final String LOGGEDIN_NAME = "loggedInName";
    public static final String UNAUTHORIZED_MESSAGE = "UNAUTHORIZED_ACCESS_IN_APPLICATIONS";
    public static final String UNAUTHORIZED_CODE = "88000100";
    public static final String UNAUTHORIZED_STATUS = "F";
    public static final String AUTH_HEADER_MISSING = "Authorization header is missing in request";
    public static final String AUTH_HEADER_INVALID = "Authorization header is invalid";

    public static final String  REGISTER = "/api/v1/user/guest/register";
    public static final String  VERIFY_REGISTER = "/api/v1/user/register/guest/verify";
    public static final String  PASSWORD_RESET_VERIFY = "/api/v1/user/password-reset/verify";
    public static final String  REQUEST_PASSWORD_RESET = "/api/v1/user/request-password-reset";
    public static final String  LOGIN = "/api/v1/auth/login";
    public static final String  REFRESH_TOKEN = "/api/v1/auth/refresh-token";
    public static final String  VALIDATE_LINK = "/api/v1/auth/validate-link";
    public static final String  RSA_KEY = "/api/v1/auth/rsa-key";
}
