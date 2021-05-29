package org.server.core;

import redis.clients.jedis.Jedis;

public class RedisModule {

    private RedisModule() {
        try {
            build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void build() throws Exception {
        redisClient = new Jedis("localhost", 6379);
    }

    public static final Jedis module()
    {
        return module_.redisClient;
    }
    private Jedis redisClient;
    private static final RedisModule module_ = new RedisModule();
}
