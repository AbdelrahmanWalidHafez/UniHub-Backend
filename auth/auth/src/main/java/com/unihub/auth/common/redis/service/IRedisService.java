package com.unihub.auth.common.redis.service;

import java.time.Duration;


public interface IRedisService {

     void setValue(String key, String value, Duration ttl);

     String getValue(String key);

     void deleteKey(String key);

     boolean exists(String key);
}
