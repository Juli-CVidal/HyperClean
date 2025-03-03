package com.jcv.hyperclean.service;

import com.jcv.hyperclean.cache.RedisItemCache;
import com.jcv.hyperclean.cache.RedisListCache;

import java.util.List;
import java.util.Optional;

public abstract class CacheableService<T> {
    private final RedisItemCache<T> cache;
    private final RedisListCache<T> listCache;

    protected CacheableService(RedisItemCache<T> cache, RedisListCache<T> listCache) {
        this.cache = cache;
        this.listCache = listCache;
    }

    protected void putInCache(String key, T value) {
        cache.put(key, value);
    }

    protected void setListCache(String key, List<T> values) {
        listCache.set(key, values);
    }

    protected Optional<T> getCached(String key) {
        return Optional.ofNullable(cache.get(key));
    }

    protected Optional<List<T>> getCachedList(String key) {
        return Optional.ofNullable(listCache.get(key));
    }

    protected void invalidateCache(String key) {
        cache.invalidate(key);
    }

    protected void invalidateListCache(String key) {
        listCache.invalidate(key);
    }
}
