package org.springframework.cache.memcached;

import org.springframework.cache.support.AbstractValueAdaptingCache;

import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;

public class SpyMemcachedCache extends AbstractValueAdaptingCache {
    private String name;
    private MemcachedClient client;
    private int expire;

    protected SpyMemcachedCache(String name, MemcachedClient client,
            boolean allowNullValues, int expire) {
        super(allowNullValues);
        this.name = name;
        this.client = client;
        this.expire = expire;
    }

    @Override
    protected Object lookup(Object key) {
        return client.get(cacheKey(key));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MemcachedClient getNativeCache() {
        return client;
    }

    @Override
    public void put(Object key, Object value) {
        client.set(cacheKey(key), expire, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        String ck = cacheKey(key);
        CASValue<Object> cached = client.gets(ck);
        if (cached.getValue() == null) {
            CASResponse casResp = client.cas(ck, cached.getCas(), expire, value);
            return (casResp == CASResponse.OK) ? null : get(key);
        } else {
            return toValueWrapper(cached.getValue());
        }
    }

    @Override
    public void evict(Object key) {
        client.delete(cacheKey(key));
    }

    @Override
    public void clear() {
        client.flush();
    }

    public String cacheKey(Object key) {
        return String.valueOf(key);
    }
}

