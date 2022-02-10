package jdbc;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Objects;

public class JedisPools {
    private final JedisPool pool;

    public JedisPools() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        pool = new JedisPool(jedisPoolConfig, "finn-krane.ay1.krane.9rum.cc", 6379, 1000, "finnian");
    }

    /**
     * @return Jedis Instance. You must call release this after use.
     */
    public Jedis get() {
        return pool.getResource();
    }

    public void release(final Jedis jedis) {
        pool.returnResource(Objects.requireNonNull(jedis));
    }
}
