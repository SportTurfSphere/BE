package com.turf.common.entity.listener;

import com.turf.common.entity.BaseEntity;
import com.turf.common.util.CommonEntityUtils;
import jakarta.persistence.PrePersist;
import org.springframework.util.ObjectUtils;

public class CommonEntityListener {

    @PrePersist
    private void beforeInsert(BaseEntity abstractEntity) {

        if (ObjectUtils.isEmpty(abstractEntity.getUuid())) {
            abstractEntity.setUuid(CommonEntityUtils.generateUniqueId());
        }
    }
}
