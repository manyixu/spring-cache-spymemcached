package org.springframework.cache.memcached;

/**
 * Default implementation of {@link CachePrefix} which use the cache name as prefix.
 */
public class DefaultCachePrefix implements CachePrefix {
    private static final String DEFAULT_DELIMITER = ":";
    private String delimiter;

    public DefaultCachePrefix() {
        this(DEFAULT_DELIMITER);
    }

    public DefaultCachePrefix(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public String prefix(String cacheName) {
        return (delimiter == null) ? cacheName.concat(DEFAULT_DELIMITER)
                                   : cacheName.concat(delimiter);
    }
}
