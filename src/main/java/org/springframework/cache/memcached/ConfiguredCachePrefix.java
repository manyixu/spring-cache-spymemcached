package org.springframework.cache.memcached;

import java.util.HashMap;
import java.util.Map;

public class ConfiguredCachePrefix implements CachePrefix {
    private Map<String, String> prefixes;
    /**
     * Use cache name as prefix if not configured.
     */
    private boolean cacheNameFallback = true;

    public ConfiguredCachePrefix() {
        prefixes = new HashMap<>(0);
    }

    public ConfiguredCachePrefix(Map<String, String> prefixes) {
        this.prefixes = prefixes;
    }

    public void setPrefixes(Map<String, String> prefixes) {
        this.prefixes = prefixes;
    }

    public Map<String, String> getPrefixes() {
        return prefixes;
    }

    @Override
    public String prefix(String cacheName) {
        String prefix = prefixes.get(cacheName);
        if (prefix == null) {
            return cacheNameFallback ? cacheName : null;
        } else {
            return prefix;
        }
    }
}
