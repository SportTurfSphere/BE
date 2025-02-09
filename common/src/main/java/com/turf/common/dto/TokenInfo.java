package com.turf.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TokenInfo {
    private Integer loggedInUserId;
    private String loggedInUserUUID;
    private String loggedInUserName;
    private String accessToken;
    private String refreshToken;
    private String name;
    private Date tokenExpiry;
    private boolean accessAllowed;
    private String registrationStatus;
    private String userType;
}
