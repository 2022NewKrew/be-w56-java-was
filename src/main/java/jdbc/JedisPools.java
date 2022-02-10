package jdbc;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPools {
    private final JedisPool pool;

    public JedisPools() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        pool = new JedisPool(jedisPoolConfig, "finn-krane.ay1.krane.9rum.cc", 6379, 1000, "finnian");
    }

    /**
     * @return Jedis Instance. You must close it after use.
     */
    public Jedis get() {
        return pool.getResource();
    }
}
