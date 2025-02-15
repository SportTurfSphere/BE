package com.turf.common.entity.listener;

import com.turf.common.entity.BaseEntity;
import com.turf.common.util.CommonEntityUtils;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseEntityListener {

    private final HttpServletRequest request;

    @PrePersist
    public void prePersist(BaseEntity entity) {
        String userUuid = getUserUuidFromRequest();
        entity.setCreatedBy(userUuid);
        entity.setUpdatedBy(userUuid);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(entity.getCreatedAt());
        entity.setUuid(CommonEntityUtils.generateUniqueId());
    }

    @PreUpdate
    public void preUpdate(BaseEntity entity) {
        entity.setUpdatedBy(getUserUuidFromRequest());
        entity.setUpdatedAt(LocalDateTime.now());
    }

    private String getUserUuidFromRequest() {
        if (request != null) {
            return request.getHeader("userUuid");
        }
        return "SYSTEM";
    }

}
