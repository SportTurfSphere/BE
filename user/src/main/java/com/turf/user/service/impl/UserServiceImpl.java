package com.turf.user.service.impl;

import com.turf.common.dto.TokenInfo;
import com.turf.common.exception.AuthenticationException;
import com.turf.common.util.BcryptUtil;
import com.turf.common.util.JwtUtil;
import com.turf.user.controller.request.AuthRequest;
import com.turf.user.dataaccess.rds.readonly.FetchUserRepo;
import com.turf.user.dataaccess.rds.readwrite.PersistUserRepo;
import com.turf.user.entity.User;
import com.turf.user.entity.enums.UserStatus;
import com.turf.user.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.turf.user.constants.FormatConstants.BEARER;
import static com.turf.user.constants.FormatConstants.LOGGED_IN_USER_NAME;
import static com.turf.user.constants.ResultInfoConstants.*;

@Service
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    @Value("${max.login.attempt}")
    int maxLoginAttempt;

    @Value("${default.user.emails}")
    List<String> defaultUserEmails;

    @Value("${default.user.password}")
    String defaultUserPassword;

    @Value("${allowed.login.attempt.interval}")
    long allowedLoginAttemptInterval;

    private final HttpServletRequest httpServletRequest;
    private final JwtUtil jwtUtil;
    private final FetchUserRepo fetchUserRepo;
    private final PersistUserRepo persistUserRepo;
    public static final String TOKEN = "?token=";
    private ConcurrentHashMap<String, TokenInfo> loggedInUserToken = new ConcurrentHashMap<>();

    @Override
    public TokenInfo login(AuthRequest authRequest) {
        Optional<User> user = fetchUserRepo.findByEmail(authRequest.getUserName());
        if (user.isEmpty()) {
            log.error("Invalid User Name/Email:{}", authRequest.getUserName());
            throw new AuthenticationException(ACCOUNT_NOT_FOUND);
        }

        var userDetails = user.get();
        checkUserUserStatus(userDetails, true);
        if (!BcryptUtil.matches(authRequest.getPassword(), userDetails.getPassword())) {
            int attempt = Optional.ofNullable(userDetails.getLoginAttemptCount()).isEmpty() ? 0 :
                    userDetails.getLoginAttemptCount();
            userDetails.setLoginAttemptCount(attempt + 1);
            userDetails.setLastFailedLogin(System.currentTimeMillis());
            log.error("{} failed to login with the attempt:{}",
                    authRequest.getUserName(), userDetails.getLoginAttemptCount());
            persistUserRepo.saveUser(userDetails);
            throw new AuthenticationException(UNAUTHORIZED_ERROR);
        }
        userDetails.setLoginAttemptCount(0);
        userDetails.setLastLogin(LocalDateTime.now());
        persistUserRepo.saveUser(userDetails);
        TokenInfo tokenInfo = jwtUtil.generateTokens(
                userDetails.getEmail(), userDetails.getId(), userDetails.getName());
        loggedInUserToken.put(userDetails.getEmail(), tokenInfo);
        return tokenInfo;
    }

    @Override
    public TokenInfo validateToken() {
        String token = getTokenFromServlet();
        String userName = extractUsernameFromToken(token);
        TokenInfo tokenInfo = loggedInUserToken.get(userName);
        if (Optional.ofNullable(tokenInfo).isEmpty() ||
                (!tokenInfo.getAccessToken().equals(token) && !tokenInfo.getRefreshToken().equals(token))
                || !jwtUtil.isTokenValid(token)) {
            log.error("invalid logged in user token");
            throw new AuthenticationException(INVALID_ACCESS_TOKEN);
        }
        return tokenInfo;
    }

    @Override
    public TokenInfo generateRefreshToken(String refreshTokenWithBearer) {
        val refreshToken = removeBearerFromToken(refreshTokenWithBearer);
        String userName = extractUsernameFromToken(refreshToken);
        val tokenInfo = loggedInUserToken.get(userName);
        if (tokenInfo == null ||
                (!tokenInfo.getAccessToken().equals(refreshToken) &&
                        !tokenInfo.getRefreshToken().equals(refreshToken))) {
            log.error("invalid logged in user token");
            throw new AuthenticationException(SESSION_EXPIRY_ERROR);
        }
        val newToken = jwtUtil.generateTokens(
                tokenInfo.getLoggedInUserName(), tokenInfo.getLoggedInUserId(), tokenInfo.getName());

        loggedInUserToken.put(tokenInfo.getLoggedInUserName(), newToken);
        return newToken;
    }

    @Override
    public void logout() {
        String userName = getUserNameFromServlet();
        loggedInUserToken.remove(userName);
        log.info("user {} log out successful", userName);
    }


    public void checkUserUserStatus(User user, boolean login) {
        if (login && user.getLoginAttemptCount() != null && user.getLoginAttemptCount() >= maxLoginAttempt &&
                System.currentTimeMillis() - user.getLastFailedLogin() < allowedLoginAttemptInterval) {
            log.error("{} failed to login due to too many attempts", user.getName());
            throw new AuthenticationException(ACCOUNT_LOCKED);
        }
        switch (user.getStatus()) {
            case INACTIVE -> throw new AuthenticationException(ACCOUNT_INACTIVE);
            case SUSPENDED -> throw new AuthenticationException(ACCOUNT_SUSPENDED);
            default -> {
            }
        }
    }

    private String getTokenFromServlet() {
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isEmpty(token)) {
            log.error("failed to fetch token from header");
            throw new AuthenticationException(UNAUTHORIZED_ERROR);
        }
        token = removeBearerFromToken(token);
        return token;
    }

    private String removeBearerFromToken(String token) {
        if (token.startsWith(BEARER)) {
            return token.substring(BEARER.length());
        }
        return token;
    }

    private String extractUsernameFromToken(String token) {
        try {
            if (!jwtUtil.isTokenValid(token)) {
                throw new AuthenticationException(SESSION_EXPIRY_ERROR);
            }
            return jwtUtil.extractUsername(token);
        } catch (AuthenticationException ex) {
            log.error("failed to extract username from token");
            throw new AuthenticationException(ex.getResultInfo());
        }
    }


    @PostConstruct
    private void insertUserData() {
        defaultUserEmails.forEach(userName -> {
            if (fetchUserRepo.findByEmail(userName).isEmpty()) {
                val newUser = new User();
                newUser.setName(userName);
                newUser.setEmail(userName);
                newUser.setIsDefaultUser(true);
                newUser.setPassword(BcryptUtil.encodePassword(defaultUserPassword));
                newUser.setStatus(UserStatus.ACTIVE);
                persistUserRepo.saveUser(newUser);
            }
        });
    }

    private String getUserNameFromServlet() {
        String userName = httpServletRequest.getHeader(LOGGED_IN_USER_NAME);
        if (StringUtils.isEmpty(userName)) {
            throw new AuthenticationException(UNAUTHORIZED_ERROR);
        }
        return userName;
    }
}