package com.turf.common.dto;

import lombok.Builder;

import java.util.List;

@lombok.Data
@Builder
public class CacheResponse {
    private String cacheName;
    private List<CacheKeyResponse> keys;
}
