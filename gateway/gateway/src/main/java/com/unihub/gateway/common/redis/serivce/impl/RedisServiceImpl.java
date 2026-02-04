package com.unihub.gateway.common.redis.serivce.impl;

import com.unihub.gateway.common.redis.serivce.IRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements IRedisService {

    private final StringRedisTemplate redisTemplate;

    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }
}
