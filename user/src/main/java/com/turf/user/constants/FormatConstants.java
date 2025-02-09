package com.turf.user.constants;

public final class FormatConstants {
    private FormatConstants() {
    }
    public static final String BEARER = "Bearer ";
    public static final String EMAIL_FORMAT = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}";
    public static final String P_PATTERN = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    public static final String LAST_LOGIN_FORMAT = "yyyy-MM-dd";
    public static final String USERNAME = "userName";
    public static final String LINK = "link";
    public static final String LOGGED_IN_USER_NAME = "loggedInUserName";
    public static final String LOGGED_IN_USER_ID = "loggedInUserId";
}
