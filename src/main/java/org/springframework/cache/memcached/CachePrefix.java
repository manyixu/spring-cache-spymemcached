package org.springframework.cache.memcached;

public interface CachePrefix {

    /**
     * Returns the prefix for the given cache
     *
     * @param cacheName name of the cache
     * @return the corresponding prefix to a cache
     */
    String prefix(String cacheName);
}
