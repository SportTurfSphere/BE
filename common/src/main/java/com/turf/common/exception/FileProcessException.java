package com.turf.common.exception;

import com.turf.common.dto.ResultInfo;
import lombok.Getter;
import lombok.Setter;

@lombok.Data
@Getter
@Setter
public class FileProcessException extends RuntimeException {
    private final ResultInfo resultInfo;

    public FileProcessException(ResultInfo errorInfo) {
        this.resultInfo = errorInfo;
    }

}
