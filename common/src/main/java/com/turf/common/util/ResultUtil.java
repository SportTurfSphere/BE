package com.turf.common.util;

import com.turf.common.dto.GenericResponse;
import com.turf.common.dto.ResultInfo;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class ResultUtil<T> {
    public <T> ResponseEntity<GenericResponse> generateSuccessResponse(T data, ResultInfo successInfo) {
        return ResponseEntity.ok(GenericResponse.builder().resultInfo(successInfo).data(data).build());
    }

    public <T> ResponseEntity<GenericResponse> generateErrorResponse(ResultInfo errorInfo, HttpStatusCode httpStatusCode) {
        return ResponseEntity.status(httpStatusCode).body(GenericResponse.builder().resultInfo(errorInfo).build());
    }

    public ResponseEntity generateNoContentResponse() {
        return ResponseEntity.noContent().build();
    }
}