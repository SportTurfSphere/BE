package com.turf.user.entity;

import com.turf.common.entity.BaseEntity;
import com.turf.user.entity.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.turf.user.constants.SchemaNameConstants.TABLE_USER;

@Entity
@Table(name = TABLE_USER)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "login_attempt")
    private Integer loginAttemptCount;

    @Column(name = "last_failed_login")
    private Long lastFailedLogin;

    @Column(name = "is_default_user")
    private Boolean isDefaultUser;

}
