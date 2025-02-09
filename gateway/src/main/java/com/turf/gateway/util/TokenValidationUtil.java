package com.turf.gateway.util;

import com.turf.common.dto.TokenInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Log4j2
@Service
public class TokenValidationUtil {

    public boolean isAccessTokenPresent(String token) {
        TokenInfo tokenInfo = fetchTokenInfo(token);
        if (Objects.nonNull(tokenInfo)) {
            return new Date().before(tokenInfo.getTokenExpiry());
        }
        return false;
    }

    public TokenInfo fetchTokenInfo(String token) {
        /*Object redisToken = redisBaseService.fetch(redisConstants.accessToken, token, TokenInfo.class);
        if (Objects.nonNull(redisToken)) {
            return (TokenInfo) redisToken;
        }*/
        return null;
    }
}
