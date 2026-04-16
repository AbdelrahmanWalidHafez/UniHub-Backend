package com.unihub.auth.common.redis.concerns;

public interface RedisKeys {
    String REFRESH_PREFIX = "refresh:";
    String VERIFICATION_PREFIX = "verification:";
    String ACTIVATION_PREFIX = "activation:";
    String BLACKLIST_PREFIX = "blacklist:access:";
    String VERIFICATION_TOKEN_PREFIX = "verification_token:";
}
