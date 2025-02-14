package com.turf.user.entity;


import com.turf.common.entity.BaseEntity;
import com.turf.user.entity.enums.DeviceType;
import com.turf.user.entity.enums.LoginType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.turf.user.constants.SchemaNameConstants.TABLE_USER_SESSION;

@Entity
@Table(name = TABLE_USER_SESSION)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class UserSession extends BaseEntity {

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "device_type")
    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "login_type")
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column(name = "access_token", nullable = false, unique = true)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false, unique = true)
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
