package com.unihub.auth.common.redis.service.impl;

import com.unihub.auth.common.redis.service.IRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements IRedisService {

    private final StringRedisTemplate redisTemplate;

    public void setValue(String key, String value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }
}
