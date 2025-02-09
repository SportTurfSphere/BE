package com.turf.user.application;


import com.turf.user.controller.request.AuthRequest;
import com.turf.user.service.UserService;
import com.turf.common.dto.TokenInfo;
import com.turf.common.util.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.turf.user.constants.ResultInfoConstants.SUCCESS;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserApplication {

    private final UserService userService;

    public ResponseEntity login(AuthRequest authRequest) {
        TokenInfo tokenInfo = userService.login(authRequest);
        return ResultUtil.generateSuccessResponse(tokenInfo, SUCCESS);
    }

    public ResponseEntity validateToken() {
        TokenInfo tokenInfo = userService.validateToken();
        return ResultUtil.generateSuccessResponse(tokenInfo, SUCCESS);
    }

    public ResponseEntity generateRefreshToken(String refreshToken) {
        TokenInfo tokenInfo = userService.generateRefreshToken(refreshToken);
        return ResultUtil.generateSuccessResponse(tokenInfo, SUCCESS);
    }

    public ResponseEntity userLogout() {
        userService.logout();
        return ResultUtil.generateSuccessResponse(null, SUCCESS);
    }
}
