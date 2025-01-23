package com.truf.common.entity.listener;

import com.truf.common.entity.BaseEntity;
import com.truf.common.util.CommonEntityUtils;
import org.springframework.util.ObjectUtils;

import javax.persistence.PrePersist;

public class CommonEntityListener {

    @PrePersist
    private void beforeInsert(BaseEntity abstractEntity) {

        if (ObjectUtils.isEmpty(abstractEntity.getUuid())) {
            abstractEntity.setUuid(CommonEntityUtils.generateUniqueId());
        }
    }
}
