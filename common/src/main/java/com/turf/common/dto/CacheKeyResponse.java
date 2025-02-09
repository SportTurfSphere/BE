package com.turf.common.dto;

import lombok.Builder;

@lombok.Data
@Builder
public class CacheKeyResponse {
    private Object key;
    private Object data;
}
