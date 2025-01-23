package com.truf.common.exception;

import com.truf.common.dto.ResultInfo;
import lombok.Getter;
import lombok.Setter;

@lombok.Data
@Getter
@Setter
public class ValidationException extends RuntimeException {
   private final ResultInfo resultInfo;

    public ValidationException(ResultInfo errorInfo) {
        this.resultInfo = errorInfo;
    }

}