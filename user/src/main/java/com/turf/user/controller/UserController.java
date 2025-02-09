package com.turf.user.controller;

import com.turf.common.advice.TrackExecutionTime;
import com.turf.user.application.UserApplication;
import com.turf.user.controller.request.AuthRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.turf.user.constants.APIEndpoints.*;
import static com.turf.user.swagger.AuthExample.LOGIN_REQ;
import static com.turf.user.swagger.OpenAPITags.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Log4j2
@RestController
@RequestMapping(API_V1_AUTH)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserApplication userApplication;

    @TrackExecutionTime
    @Operation(summary = SUMMARY_LOGIN, tags = {TAG_AUTHORIZATION},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
                    @ExampleObject(value = LOGIN_REQ)
            })))
    @PostMapping(value = LOGIN,
            produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity login(@RequestBody @Valid AuthRequest authRequest) {
        log.info("User Login {}", authRequest.getUserName());
        return userApplication.login(authRequest);
    }

    @TrackExecutionTime
    @Operation(summary = SUMMARY_TOKEN_VALIDATE, tags = {TAG_AUTHORIZATION})
    @GetMapping(value = VALIDATE_TOKEN, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity validateToken() {
        log.info("Validating token");
        return userApplication.validateToken();
    }

    @TrackExecutionTime
    @Operation(summary = SUMMARY_REFRESH_TOKEN, tags = {TAG_AUTHORIZATION})
    @GetMapping(value = REFRESH_TOKEN, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity generateRefreshToken(@RequestHeader(AUTHORIZATION) String refreshToken) {
        log.info("Generating refresh token");
        return userApplication.generateRefreshToken(refreshToken);
    }

    @TrackExecutionTime
    @Operation(summary = SUMMARY_LOGOUT, tags = {TAG_AUTHORIZATION})
    @PostMapping(value = LOGOUT)
    public ResponseEntity userLogout() {
        log.info("User Logout");
        return userApplication.userLogout();
    }

}
