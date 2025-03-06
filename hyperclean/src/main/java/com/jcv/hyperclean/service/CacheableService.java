package com.jcv.hyperclean.service;

import com.jcv.hyperclean.cache.RedisItemCache;
import com.jcv.hyperclean.cache.RedisListCache;
import com.jcv.hyperclean.model.BasicModel;
import com.jcv.hyperclean.util.ListUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.redis.RedisSystemException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class CacheableService<T extends BasicModel> {
    private final RedisItemCache<T> cache;
    private final RedisListCache<T> listCache;

    protected CacheableService(RedisItemCache<T> cache, RedisListCache<T> listCache) {
        this.cache = cache;
        this.listCache = listCache;
    }

    protected void putInCache(String key, T value) {
        cache.put(key, value);
    }

    protected void putInCache(Long key, T value) {
        putInCache(String.valueOf(key), value);
    }

    protected void setListCache(String key, List<T> values) {
        listCache.set(key, values);
    }

    protected void setListCache(Long key, List<T> values) {
        setListCache(String.valueOf(key), values);
    }

    protected Optional<T> getCached(String key) {
        T cachedElement;

        try {
            cachedElement = cache.get(key);
        } catch (RedisSystemException e) {
            cachedElement = null;
        }
        return Optional.ofNullable(cachedElement);
    }

    protected Optional<T> getCached(Long key) {
        return getCached(String.valueOf(key));
    }

    protected Optional<List<T>> getCachedList(String key) {
        List<T> cachedList;
        try {
            cachedList = listCache.get(key);
        } catch(RedisSystemException e) {
            cachedList = Collections.emptyList();
        }

        if (ListUtils.isEmpty(cachedList)) {
            return Optional.empty();
        }
        return Optional.ofNullable(cachedList);
    }

    protected Optional<List<T>> getCachedListList(Long key) {
        return getCachedList(String.valueOf(key));
    }

    protected void invalidateCache(String key) {
        cache.invalidate(key);
    }

    protected void invalidateCache(Long key) {
        invalidateCache(String.valueOf(key));
    }

    protected void invalidateListCache(String key) {
        listCache.invalidate(key);
    }

    protected void invalidateListCache(Long key) {
        invalidateListCache(String.valueOf(key));
    }

    protected void flushItemCache() {
        cache.flushAll();
    }

    protected void flushListCache() {
        listCache.flushAll();
    }

    protected void flushCaches() {
        flushItemCache();
        flushListCache();
    }

    /**
     * Searches for an element in the cache first. If not found, it uses the provided method
     * to fetch the element from the database and stores it in the cache for subsequent accesses.
     *
     * @param key              The cache key used to search for the element in the cache. This key is also used
     *                         to fetch the element from the database if it is not found in the cache.
     * @param repositoryMethod A function that defines how to retrieve the element from the database.
     *                         It should return an Optional of the element found in the repository.
     * @return The element of type T if found, either from the cache or the database.
     */
    protected T safeFindBy(String key, Function<String, Optional<T>> repositoryMethod) {
        return getCached(key).orElseGet(() -> {
            T item = repositoryMethod.apply(key)
                    .filter(t -> t.getId() != null) // Ensure entity exists
                    .orElse(null);
            if (item != null) {
                putInCache(key, item);
            }
            return item;
        });
    }

    protected T safeFindBy(Long key, Function<Long, Optional<T>> repositoryMethod) {
        return getCached(key).orElseGet(() -> {
            T item = repositoryMethod.apply(key)
                    .filter(t -> t.getId() != null) // Ensure entity exists
                    .orElse(null);
            if (item != null) {
                putInCache(key, item);
            }
            return item;
        });
    }

    /**
     * Same functionality as {@code safeFindBy} but it will throw an exception if the element was not found
     */
    protected T findBy(String key, Function<String, Optional<T>> repositoryMethod) throws EntityNotFoundException {
        T element = safeFindBy(key, repositoryMethod);
        if (element != null) {
            return element;
        }
        throw new EntityNotFoundException(key);
    }

    protected T findBy(Long key, Function<Long, Optional<T>> repositoryMethod) throws EntityNotFoundException {
        T element = safeFindBy(key, repositoryMethod);
        if (element != null) {
            return element;
        }
        throw new EntityNotFoundException(String.valueOf(key));
    }

    /**
     * Searches for a list of elements in the cache first. If not found, it fetches them from the database
     * and stores the result in the cache.
     *
     * @param key              The cache key used for lookup.
     * @param repositoryMethod A function to retrieve the list of elements from the database.
     * @return A list of found elements (could be empty).
     */
    protected List<T> safeFindListBy(String key, Function<Long, List<T>> repositoryMethod) {
        return getCachedList(key).orElseGet(() -> {
            List<T> items = repositoryMethod.apply(Long.valueOf(key));
            if (!items.isEmpty()) {
                setListCache(key, items);
            }
            return items;
        });
    }

    protected List<T> safeFindListBy(Long key, Function<Long, List<T>> repositoryMethod) {
        return safeFindListBy(String.valueOf(key), repositoryMethod);
    }

    /**
     * Searches for a list of elements in the cache first. If not found, it fetches them from the database.
     * If no elements are found, an exception is thrown.
     *
     * @param key              The cache key used for lookup.
     * @param repositoryMethod A function to retrieve the list of elements from the database.
     * @return A list of found elements.
     * @throws EntityNotFoundException if no elements are found.
     */
    protected List<T> findListBy(String key, Function<Long, List<T>> repositoryMethod) throws EntityNotFoundException {
        List<T> elements = safeFindListBy(key, repositoryMethod);
        if (!elements.isEmpty()) {
            return elements;
        }
        throw new EntityNotFoundException("No elements found for key: " + key);
    }

    protected List<T> findListBy(Long key, Function<Long, List<T>> repositoryMethod) throws EntityNotFoundException {
        return findListBy(String.valueOf(key), repositoryMethod);
    }

    /**
     * Retrieves all entities from the cache first. If not found, it fetches them from the database
     * and stores the result in the cache.
     *
     * @param cacheKey         The cache key used for lookup.
     * @param repositoryMethod A supplier that retrieves all entities from the database.
     * @return A list of found elements (could be empty).
     */
    protected List<T> safeFindAll(String cacheKey, Supplier<List<T>> repositoryMethod) {
        return getCachedList(cacheKey).orElseGet(() -> {
            List<T> items = repositoryMethod.get();
            if (!items.isEmpty()) {
                setListCache(cacheKey, items);
            }
            return items;
        });
    }
}
