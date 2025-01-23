package com.truf.common.util.cache.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.truf.common.dto.CacheKeyResponse;
import com.truf.common.dto.CacheResponse;
import com.truf.common.exception.ValidationException;
import com.truf.common.util.ObjectMapperUtil;
import com.truf.common.util.cache.CustomCacheService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.truf.common.constants.ResultInfoConstants.SYSTEM_ERROR;


@Service
@Log4j2
public class CustomCacheServiceImpl implements CustomCacheService {

    @Autowired
    private CacheManager cacheManager;

    @Override
    public List<Object> findAllData(String cacheName,String key) {
        Cache allItemsCache = cacheManager.getCache(cacheName);
        return allItemsCache.get(key, List.class);
    }

    @Override
    public List<Object> addListDataToCache(String cacheName, String key, List<Object> dataList) {
        Cache allItemsCache = cacheManager.getCache(cacheName);
        allItemsCache.put(key, dataList);
        return allItemsCache.get(key, List.class);
    }

    @Override
    public void addDataToCache(String cacheName,String key, Object data) {
        Cache allItemsCache = this.cacheManager.getCache(cacheName);
        allItemsCache.put(key, data);
    }

    @Override
    public void removeDataFromCache(String cacheName,String key, Integer id) {
        Cache allItemsCache = cacheManager.getCache(cacheName);
        assert allItemsCache != null;
        Cache.ValueWrapper valueWrapper = allItemsCache.get(key);
        List<Object> dataList;
        if (valueWrapper != null) {
            Object cachedValue = valueWrapper.get();
            if (cachedValue instanceof List) {
                dataList = (List<Object>) cachedValue;
            } else {
                // Handle the case where the cached value is not a List
                dataList = new ArrayList<>();
            }
        } else {
            dataList = new ArrayList<>();
        }

        Object itemToRemove = dataList.stream()
                .filter(data -> {
                    JsonElement element = null;
                    try {
                        element = new Gson().fromJson(ObjectMapperUtil.mapObjectToJson(data), JsonElement.class);
                    } catch (IOException e) {
                        throw new ValidationException(SYSTEM_ERROR);
                    }
                    Integer elementId = element.getAsJsonObject().get("id").getAsInt();
                    return elementId.equals(id);
                })
                .findFirst()
                .orElse(null);

        if (itemToRemove != null) {
            dataList.remove(itemToRemove);
            allItemsCache.put(key, dataList);
        }
    }

    @Override
    public void updateDataInCache(String cacheName, String key, Integer id, Object data) throws IOException {
        Cache allItemsCache = cacheManager.getCache(cacheName);
        assert allItemsCache != null;
        Cache.ValueWrapper valueWrapper = allItemsCache.get(key);
        List<Object> dataList;
        if (valueWrapper != null) {
            Object cachedValue = valueWrapper.get();
            if (cachedValue instanceof List) {
                dataList = (List<Object>) cachedValue;
            } else {
                // Handle the case where the cached value is not a List
                dataList = new ArrayList<>();
            }
        } else {
            dataList = new ArrayList<>();
        }

        Optional<Object> dataToUpdate = dataList.stream()
                .filter(item -> {
                    try {
                        JsonObject jsonObject = new Gson().fromJson(ObjectMapperUtil.mapObjectToJson(item), JsonElement.class)
                                .getAsJsonObject();
                        return jsonObject.get("id").getAsInt() == id;
                    } catch (IOException e) {
                        log.info("save in redis failed : {}", e.getMessage());
                        throw new ValidationException(SYSTEM_ERROR);
                    }
                })
                .findFirst();

        if (dataToUpdate.isPresent()) {
            int index = dataList.indexOf(dataToUpdate.get());
            dataList.set(index, data);
        } else {
            dataList.add(data);
        }

        allItemsCache.put(key, dataList);
    }
    @Override
    public Object findByIdFromCache(String cacheName,String key, Integer id) throws IOException {
        var allItemsCache = cacheManager.getCache(cacheName);
        if (allItemsCache != null) {
            var dataList = allItemsCache.get(key, List.class);
            if (dataList != null) {
                for (var data : dataList) {
                    var element = new Gson().fromJson(ObjectMapperUtil.mapObjectToJson(data), JsonElement.class);
                    var elementId = element.getAsJsonObject().get("id").getAsInt();
                    if (elementId == id) {
                        return data;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<CacheResponse> viewCache() {
        return cacheManager.getCacheNames().stream()
                .map(cacheName -> {
                    Cache cache = cacheManager.getCache(cacheName);
                    if (Objects.nonNull(cache)) {
                        Set<?> keys = getCachedKeys(cache);
                        List<CacheKeyResponse> cacheKeyResponses = keys.stream()
                                .map(key -> {
                                    var value = cache.get(key);
                                    Object data = (value != null) ? value.get() : null;
                                    return CacheKeyResponse.builder()
                                            .key(key)
                                            .data(data)
                                            .build();
                                })
                                .collect(Collectors.toList());

                        return CacheResponse.builder()
                                .cacheName(cacheName)
                                .keys(cacheKeyResponses)
                                .build();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void clearAllCaches() {
        cacheManager.getCacheNames().forEach(cacheName ->
                Objects.requireNonNull(cacheManager.getCache(cacheName)).clear()
        );
    }

    @Override
    public void clearCache(String cacheName) {
        Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
    }

    @Override
    public long estimatedSize() {
        return cacheManager.getCacheNames().size();
    }

    public Set<?> getCachedKeys(Cache cache) {
        if (cache.getNativeCache() instanceof com.github.benmanes.caffeine.cache.Cache<?, ?> nativeCache) {
            return nativeCache.asMap().keySet();
        }
        return Set.of();
    }

}
