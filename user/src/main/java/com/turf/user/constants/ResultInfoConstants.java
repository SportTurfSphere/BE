package com.turf.user.constants;

import com.turf.common.dto.ResultInfo;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public final class ResultInfoConstants {
    private ResultInfoConstants() {
    }
    public static final String STATUS_SUCCESS = "S";
    public static final String STATUS_FAILED = "F";

    //Error Code Format - XXXXYYYY where,
    //    XXXX -> Service Port E.g.: 7003
    //    YYYY -> Sequence number should start from 0001
    public static final ResultInfo SUCCESS = new ResultInfo("70030001", "SUCCESS", "Successful Request", STATUS_SUCCESS);
    public static final ResultInfo SYSTEM_ERROR = new ResultInfo("70030002", "SYSTEM_ERROR", "Internal Server Error", STATUS_FAILED);
    public static final ResultInfo BAD_REQUEST = new ResultInfo("70030003", "BAD_REQUEST", "Bad Request", STATUS_FAILED);
    public static final ResultInfo GENERIC_FORBIDDEN = new ResultInfo("70030004", FORBIDDEN.name(), "Forbidden", STATUS_FAILED);
    public static final ResultInfo UNAUTHORIZED_ERROR = new ResultInfo("70030005", UNAUTHORIZED.name(), "Invalid username or password", STATUS_FAILED);
    public static final ResultInfo INVALID_ACCESS_TOKEN = new ResultInfo("70030006", UNAUTHORIZED.name(), "Invalid user token", STATUS_FAILED);
    public static final ResultInfo DUPLICATE_USER = new ResultInfo("70030007", CONFLICT.name(), "User Email already Exist", STATUS_FAILED);
    public static final ResultInfo ACCOUNT_LOCKED = new ResultInfo("70030008", FORBIDDEN.name(), "Too many failed attempts. Try again later or reset your password.", STATUS_FAILED);
    public static final ResultInfo ACCOUNT_INACTIVE = new ResultInfo("70030009", FORBIDDEN.name(), "Your account is inactive. Please contact support.", STATUS_FAILED);
    public static final ResultInfo ACCOUNT_SUSPENDED = new ResultInfo("70030010", FORBIDDEN.name(), "Your account has been suspended. Contact support for assistance.", STATUS_FAILED);
    public static final ResultInfo ACCOUNT_NOT_FOUND = new ResultInfo("70030011", NOT_FOUND.name(), "Email address not found. Please try again.", STATUS_FAILED);
    public static final ResultInfo INVALID_LINK = new ResultInfo("70030012", "LINK_EXPIRED","This link has been expired. Please request a new password reset.",STATUS_FAILED);
    public static final ResultInfo P_RESET_ATTEMPT_ERROR = new ResultInfo("70030013", "PASSWORD_RESET_ATTEMPT_ERROR", "Too many attempts. Please try again later.", STATUS_FAILED);
    public static final ResultInfo SELF_SUSPEND_ERROR = new ResultInfo("70030014", "SELF_UPDATE_ERROR", "You cannot suspend your own account.", STATUS_FAILED);
    public static final ResultInfo SESSION_EXPIRY_ERROR = new ResultInfo("70030015", "SESSION_EXPIRY_ERROR", "Session expired. Please login again.", STATUS_FAILED);
    public static final ResultInfo DEFAULT_ADMIN_UPDATE_ERROR = new ResultInfo("70030016", "DEFAULT_ADMIN_UPDATE_ERROR", "You cannot update default ", STATUS_FAILED);
    public static final ResultInfo SAME_PASSWORD_ERROR = new ResultInfo("70030017", "SAME_PASSWORD_ERROR", "New password cannot be same as old password.", STATUS_FAILED);
    public static final ResultInfo DELETED_USER_ERROR = new ResultInfo("70030018", "DELETED_USER_ERROR", "The selected user is no longer available.", STATUS_FAILED);
}
