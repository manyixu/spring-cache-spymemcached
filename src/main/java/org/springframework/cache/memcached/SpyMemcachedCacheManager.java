package org.springframework.cache.memcached;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.util.CollectionUtils;

import net.spy.memcached.MemcachedClient;

public class SpyMemcachedCacheManager extends AbstractCacheManager {
    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * Never expire by default
     */
    private static final int DEFAULT_EXPIRE = 0;

    private Collection<? extends Cache> initialCaches;

    private Map<String, Integer> expires;

    private MemcachedClient client;

    public SpyMemcachedCacheManager() {
        initialCaches = Collections.emptyList();
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return initialCaches;
    }

    public void setInitialCaches(
            Collection<? extends Cache> initialCaches) {
        this.initialCaches = initialCaches;
    }

    public Map<String, Integer> getExpires() {
        return expires;
    }

    public void setExpires(Map<String, Integer> expires) {
        this.expires = expires;
    }

    @Autowired
    public void setClient(MemcachedClient client) {
        this.client = client;
    }

    @Override
    protected Cache getMissingCache(String name) {
        int expire = checkExpire(name);
        return new SpyMemcachedCache(name, client, false, expire);
    }

    private int checkExpire(String name) {
        Integer expire = expires.get(name);
        return (expire == null) ? DEFAULT_EXPIRE : expire;
    }

    @Override
    public void afterPropertiesSet() {
        if (CollectionUtils.isEmpty(expires)) {
            expires = new HashMap<>(0);
            if (logger.isTraceEnabled()) {
                logger.trace("No expire configured");
            }
        }

        super.afterPropertiesSet();
    }
}
