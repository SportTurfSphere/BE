package com.turf.common.exception;

import com.turf.common.dto.ResultInfo;
import lombok.Data;

@Data
public class ResourceAlreadyExistsException extends RuntimeException {
    private final ResultInfo resultInfo;

    public ResourceAlreadyExistsException(ResultInfo resultInfo) {
        this.resultInfo = resultInfo;
    }
}