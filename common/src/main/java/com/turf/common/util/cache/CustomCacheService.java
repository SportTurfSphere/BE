package com.turf.common.util.cache;


import com.turf.common.dto.CacheResponse;

import java.io.IOException;
import java.util.List;

public interface CustomCacheService {

    List<Object> addListDataToCache(String cacheName, String key, List<Object> dataList);

    List<Object> findAllData(String cacheName,String key);

    void addDataToCache(String cacheName,String key, Object data);

    void removeDataFromCache(String cacheName,String key, Integer id) throws IOException;

    void updateDataInCache(String cacheName,String key,Integer id,Object data) throws IOException;

    Object findByIdFromCache(String cacheName,String key,Integer id) throws IOException;

    List<CacheResponse> viewCache();

    void clearAllCaches();

    void clearCache(String cacheName);

    long estimatedSize();

}
