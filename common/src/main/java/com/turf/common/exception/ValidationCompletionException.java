package com.turf.common.exception;

import com.turf.common.dto.ResultInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.CompletionException;

@Getter
@Setter
public class ValidationCompletionException extends CompletionException {
   private final ResultInfo resultInfo;

    public ValidationCompletionException(ResultInfo errorInfo) {
        this.resultInfo = errorInfo;
    }

}