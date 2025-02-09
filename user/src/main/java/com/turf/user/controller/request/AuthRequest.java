package com.turf.user.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank(message = "The user name is required.")
    private String userName;
    @NotBlank(message = "The password is required.")
    private String password;
}
