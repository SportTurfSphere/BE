package com.turf.common.exception;

import com.turf.common.dto.ResultInfo;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuthenticationException extends RuntimeException{
    final ResultInfo resultInfo;

    public AuthenticationException(ResultInfo errorInfo) {
        this.resultInfo = errorInfo;
    }
}
