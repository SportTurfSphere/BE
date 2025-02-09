package com.turf.user.service;

import com.turf.common.dto.TokenInfo;
import com.turf.user.controller.request.AuthRequest;

public interface UserService {

    TokenInfo login(AuthRequest authRequest);

    TokenInfo validateToken();

    TokenInfo generateRefreshToken(String refreshToken);

    void logout();

}
