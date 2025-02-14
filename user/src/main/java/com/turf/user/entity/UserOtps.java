package com.turf.user.entity;

import com.turf.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

import static com.turf.user.constants.SchemaNameConstants.TABLE_USER_SESSION;

@Entity
@Table(name = TABLE_USER_SESSION)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class UserOtps extends BaseEntity {

    @Column(name = "opt_hash", nullable = false)
    private String optHash;

    @CreatedDate
    @Column(name = "expiry_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime expiryAt;

    @Column(name = "attempts", nullable = false)
    private Integer attempts;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
