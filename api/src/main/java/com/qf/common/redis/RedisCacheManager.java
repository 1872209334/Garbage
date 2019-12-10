package com.qf.common.redis;



import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;



@Repository("redisCacheManager")
public class RedisCacheManager implements CacheManager {
    
    @SuppressWarnings("rawtypes") 
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisConfiguration redisConfiguration;

    @Override
    @SuppressWarnings("rawtypes") 
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        Cache c = caches.get(name);
        if(c == null){
            c =  new RedisShiroCache<K, V>(name, redisTemplate, redisConfiguration);
            caches.put(name, c);
        }
        return  c;
    }

}