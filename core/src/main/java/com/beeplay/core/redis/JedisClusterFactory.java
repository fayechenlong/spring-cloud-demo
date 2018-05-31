package com.beeplay.core.redis;

import com.alibaba.fastjson.JSON;
import com.beeplay.core.log.LogExceptionStackTrace;
import com.beeplay.core.utils.GfJsonUtil;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.*;
import redis.clients.util.JedisClusterCRC16;
import java.text.ParseException;
import java.util.*;


public class JedisClusterFactory implements FactoryBean<JedisCluster>, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisClusterFactory.class);
    private GenericObjectPoolConfig genericObjectPoolConfig;
    private JedisCluster jedisCluster;
    private int connectionTimeout = 2000;
    private int soTimeout = 3000;
    private int maxRedirections = 5;
    private Set<String> jedisClusterNodes;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (jedisClusterNodes == null || jedisClusterNodes.size() == 0) {
            throw new NullPointerException("jedisClusterNodes is null.");
        }
        Set<HostAndPort> haps = new HashSet<HostAndPort>();
        for (String node : jedisClusterNodes) {
            String[] arr = node.split(":");
            if (arr.length != 2) {
                throw new ParseException("node address error !", node.length() - 1);
            }
            haps.add(new HostAndPort(arr[0], Integer.valueOf(arr[1])));
        }
        jedisCluster = new JedisCluster(haps, connectionTimeout, soTimeout, maxRedirections, genericObjectPoolConfig);
    }

    @Override
    public JedisCluster getObject() throws Exception {
        return jedisCluster;
    }

    @Override
    public Class<?> getObjectType() {
        return (this.jedisCluster != null ? this.jedisCluster.getClass() : JedisCluster.class);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public GenericObjectPoolConfig getGenericObjectPoolConfig() {
        return genericObjectPoolConfig;
    }

    public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
        this.genericObjectPoolConfig = genericObjectPoolConfig;
    }

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getMaxRedirections() {
        return maxRedirections;
    }

    public void setMaxRedirections(int maxRedirections) {
        this.maxRedirections = maxRedirections;
    }

    public Set<String> getJedisClusterNodes() {
        return jedisClusterNodes;
    }

    public void setJedisClusterNodes(Set<String> jedisClusterNodes) {
        this.jedisClusterNodes = jedisClusterNodes;
    }


    public Long incr(String key) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.incr(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("command：incr key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：incr key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long incr(String key, Long integer) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.incrBy(key, integer);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("command：incr key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：incr key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public String get(String key) {
        Long start = System.currentTimeMillis();
        try {
            String v = jedisCluster.get(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：get key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：get key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public String set(String key, String value) {
        Long start = System.currentTimeMillis();
        try {
            String v = jedisCluster.set(key, value);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：set key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：set key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    /**
     * The command is exactly equivalent to the following group of commands:
     * {@link #set(String, String) SET} + {@link #expire(String, int) EXPIRE}. The operation is atomic.
     *
     * @param key
     * @param seconds expire_time
     * @param value
     * @return
     */
    public String setex(String key, int seconds, String value) {
        long start = System.currentTimeMillis();
        try {
            String ret = jedisCluster.setex(key, seconds, value);
            long time = System.currentTimeMillis() - start;
            if (time > 500)
                logger.warn("command= setex, key={}, execute_time={}ms", key, time);
            return ret;
        } catch (Exception e) {
            logger.error("command= setex, key={}, error={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public String spop(String key) {
        Long start = System.currentTimeMillis();
        try {
            String v = jedisCluster.spop(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：spop key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：sadd key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long scard(String key) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.scard(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：scard key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：scard key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Set<String> sdiff(String... keys) {
        long start = System.currentTimeMillis();
        try {
            Set<String> ret = jedisCluster.sdiff(keys);
            long time = System.currentTimeMillis() - start;
            if (time > 500)
                logger.warn("command=sdiff, keys={}, execute_time={}", JSON.toJSONString(keys), time);
            return ret;
        } catch (Exception e) {
            logger.error("command=sdiff, keys={}, error={}", JSON.toJSONString(keys), LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Set<String> spop(String key, int count) {
        Long start = System.currentTimeMillis();
        try {
            Set<String> v = jedisCluster.spop(key, count);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：spop key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：sadd key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public void set(List<String> keys, List<String> values) {
        Long start = System.currentTimeMillis();
        try {
            for (int i = 0; i < keys.size(); i++) {
                jedisCluster.set(keys.get(i), values.get(i));
            }
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：set list key:{} execution time:{}ms", GfJsonUtil.toJSONString(keys), time);
            }
        } catch (Exception e) {
            logger.error("command：set key:{} ex={}", GfJsonUtil.toJSONString(keys), LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    public String setObject(String key, Object value) {
        Long start = System.currentTimeMillis();
        try {
            String objectValue = GfJsonUtil.toJSONString(value);
            String v = jedisCluster.set(key, objectValue);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：setObject key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：setObject key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public void set(String key, List<String> strings) {
        Long start = System.currentTimeMillis();
        try {
            for (String s : strings) {
                jedisCluster.set(key, s);
            }
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：set key:{} execution time:{}ms", key, time);
            }
        } catch (Exception e) {
            logger.error("command：jedisCluster set key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    /**
     * @param key
     * @param value
     * @param nxxx   NX 存在 XX不存在
     * @param expx   PX毫秒 EX秒
     * @param exTime
     * @return
     */
    public String set(String key, String value, String nxxx, String expx, long exTime) {
        Long start = System.currentTimeMillis();
        try {
            String result = jedisCluster.set(key, value, nxxx, expx, exTime);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：set key:{} execution time:{}ms", key, time);
            }
            return result;
        } catch (Exception e) {
            logger.error("command：jedisCluster set key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public String getSet(String key, String value) {
        Long start = System.currentTimeMillis();
        try {
            String v = jedisCluster.getSet(key, value);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：getSet key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：getSet key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long sadd(String key, String... members) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.sadd(key, members);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：sadd key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：sadd key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long srem(String key, String... members) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.srem(key, members);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：srem key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：srem key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Set<String> sunion(String... keys) {
        long start = System.currentTimeMillis();
        try {
            Set<String> ret = jedisCluster.sunion(keys);
            long time = System.currentTimeMillis() - start;
            if (time > 500)
                logger.warn("command= sunion, key={}, execute_time={}ms", JSON.toJSONString(keys), time);
            return ret;
        } catch (Exception e) {
            logger.error("command=sunion, keys={}, error={}", JSON.toJSONString(keys), LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long zadd(String key, Map<String, Double> map) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.zadd(key, map);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：zadd key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：zadd key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public void zadd(String key, List<Map<String, Double>> maps) {
        Long start = System.currentTimeMillis();
        try {
            for (Map<String, Double> map : maps) {
                jedisCluster.zadd(key, map);
            }
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：ShardedJedisPipeline zadd key:{} execution time:{}ms", key, time);
            }
        } catch (Exception e) {
            logger.error("command：ShardedJedisPipeline zadd key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    public Long zcard(String key) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.zcard(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：zcard key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：zcard key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long zcount(String key, String min, String max) {
        long start = System.currentTimeMillis();
        try {
            Long ret = jedisCluster.zcount(key, min, max);
            long time = System.currentTimeMillis() - start;
            if (time > 500) {
                logger.warn("command= zcount, key={}, execute_time={}ms", key, time);
            }
            return ret;
        } catch (Exception e) {
            logger.error("command：zcount key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Double zincrby(String key, Double score, String member) {
        long start = System.currentTimeMillis();
        try {
            Double ret = jedisCluster.zincrby(key, score, member);
            long time = System.currentTimeMillis() - start;
            if (time > 500)
                logger.warn("command= zincrby, key={}, execute_time={}ms", key, time);
            return ret;
        } catch (Exception e) {
            logger.error("command：zincrby key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public void sadd(String key, List<String> strings) {
        Long start = System.currentTimeMillis();
        try {
            for (String s : strings) {
                jedisCluster.sadd(key, s);
            }
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：ShardedJedisPipeline sadd key:{} execution time:{}ms", key, time);
            }
        } catch (Exception e) {
            logger.error("command：jedisCluster sadd key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    public String hget(String key, String field) {
        Long start = System.currentTimeMillis();
        try {
            String v = jedisCluster.hget(key, field);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：hget key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：hget key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public String hmset(String key, Map<String, String> hash) {
        Long start = System.currentTimeMillis();
        try {
            String v = jedisCluster.hmset(key, hash);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：hmset key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：hmset key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public void hmset(String key, List<Map<String, String>> hash) {
        Long start = System.currentTimeMillis();
        try {
            for (Map<String, String> h : hash) {
                jedisCluster.hmset(key, h);
            }
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：ShardedJedisPipeline hmset key:{} execution time:{}ms", key, time);
            }
        } catch (Exception e) {
            logger.error("command：jedisCluster hmset key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    public List<String> hmget(String key, String... fields) {
        Long start = System.currentTimeMillis();
        try {
            List<String> v = jedisCluster.hmget(key, fields);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：hmget key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：hmget key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long del(String key) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.del(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：del key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：del key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long decr(String key) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.decr(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：decr key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：decr key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long decr(String key, Long integer) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.decrBy(key, integer);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：decrBy key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：decrBy key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long append(String key, String value) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.append(key, value);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：append key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：append key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long bitcount(String key) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.bitcount(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：bitcount key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：bitcount key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long bitpos(String key, boolean bool) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.bitpos(key, bool);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：bitpos key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：bitpos key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long expire(String key, int seconds) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.expire(key, seconds);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：expire key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：expire key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long expireAt(String key, Long unixTime) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.expireAt(key, unixTime);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：expireAt key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：expireAt key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long hdel(String key, String... fields) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.hdel(key, fields);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：hdel key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：hdel key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Boolean hexists(String key, String field) {
        long start = System.currentTimeMillis();
        try {
            Boolean ret = jedisCluster.hexists(key, field);
            long time = System.currentTimeMillis() - start;
            if (time > 500)
                logger.warn("command= hexists, execute_time={}ms", key, time);
            return ret;
        } catch (Exception e) {
            logger.error("command= hexists, key={}, error={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long hincrBy(String key, String field, Long value) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.hincrBy(key, field, value);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：hincrBy key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：hincrBy key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Double hincrByFloat(String key, String field, Double value) {
        Long start = System.currentTimeMillis();
        try {
            Double ret = jedisCluster.hincrByFloat(key, field, value);
            Long time = System.currentTimeMillis() - start;
            if (time > 500) {
                logger.warn("command：hincrByFloat key:{} execution time:{}ms", key, time);
            }
            return ret;
        } catch (Exception e) {
            logger.error("command：hincrByFloat key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long hlen(String key) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.hlen(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：hlen key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：hlen key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long hset(String key, String field, String value) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.hset(key, field, value);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：hset key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：hset key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public void hset(String key, String field, List<String> strings) {
        Long start = System.currentTimeMillis();
        try {
            for (String s : strings) {
                jedisCluster.hset(key, field, s);
            }
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：ShardedJedisPipeline hset key:{} execution time:{}ms", key, time);
            }
        } catch (Exception e) {
            logger.error("command：ShardedJedisPipeline hset key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    private void hsetLoop(String key, Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        long start = System.currentTimeMillis();
        try {
            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                jedisCluster.hset(key, entry.getKey(), entry.getValue());
            }
            long time = System.currentTimeMillis() - start;
            if (time > 500)
                logger.warn("command= hset, key={}, map.len={}, execute_time={}ms", key, map.size(), time);
        } catch (Exception e) {
            logger.error("command= hset, key={}, map.size={}, error={}", key, map.size(), LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    public Long hsetnx(String key, String field, String value) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.hsetnx(key, field, value);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：hsetnx key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：hsetnx key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public void hsetnx(String key, String field, List<String> strings) {
        Long start = System.currentTimeMillis();
        try {
            for (String s : strings) {
                jedisCluster.hsetnx(key, field, s);
            }
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：ShardedJedisPipeline hsetnx key:{} execution time:{}ms", key, time);
            }
        } catch (Exception e) {
            logger.error("command：ShardedJedisPipeline hsetnx key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    public Long llen(String key) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.llen(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：llen key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：llen key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long lpush(String key, String... strings) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.lpush(key, strings);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：lpush key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：lpush key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public void lpush(String key, List<String> strings) {
        Long start = System.currentTimeMillis();
        try {
            for (String s : strings) {
                jedisCluster.lpush(key, s);
            }
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：ShardedJedisPipeline lpush key:{} execution time:{}ms", key, time);
            }
        } catch (Exception e) {
            logger.error("command：ShardedJedisPipeline lpush key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    public Long lpushx(String key, String string) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.lpushx(key, string);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：lpushx key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：lpushx key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public void lpushx(String key, List<String> strings) {
        Long start = System.currentTimeMillis();
        try {
            for (String s : strings) {
                jedisCluster.lpushx(key, s);
            }
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：ShardedJedisPipeline lpushx key:{} execution time:{}ms", key, time);
            }
        } catch (Exception e) {
            logger.error("command：ShardedJedisPipeline lpushx key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    public String lindex(String key, int index) {
        Long start = System.currentTimeMillis();
        try {
            String v = jedisCluster.lindex(key, index);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：lindex key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：lindex key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public String lpop(String key) {
        Long start = System.currentTimeMillis();
        try {
            String v = jedisCluster.lpop(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：lpop key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：lpop key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public List<String> lrange(String key, int start, int end) {
        Long startTime = System.currentTimeMillis();
        try {
            List<String> v = jedisCluster.lrange(key, start, end);
            Long endTime = System.currentTimeMillis();
            Long time = endTime - startTime;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：lpop key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：lpop key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public List<String> blpop(String key) {
        try {
            return jedisCluster.blpop(500, key);
        } catch (Exception e) {
            logger.error("blpop error", e);
        }
        return null;
    }


    public Long lrem(String key, int count, String value) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.lrem(key, count, value);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：lrem key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：lrem key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public String lset(String key, long index, String value) {
        long start = System.currentTimeMillis();

        try {
            String ret = jedisCluster.lset(key, index, value);
            long time = System.currentTimeMillis() - start;
            if (time > 500)
                logger.warn("command=lset, key={}, execute_time={}ms", key, time);
            return ret;
        } catch (Exception e) {
            logger.error("command=lset, key={}, error={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public String ltrim(String key, long start, long end) {
        long startTime = System.currentTimeMillis();
        try {
            String ret = jedisCluster.ltrim(key, start, end);
            long time = System.currentTimeMillis() - startTime;
            if (time > 500)
                logger.warn("command=ltrim, key={}, execute_time={}ms", key, time);
            return ret;
        } catch (Exception e) {
            logger.error("command=ltrim, key={}, error={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long move(String key, int dbIndex) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.move(key, dbIndex);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：move key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：move key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long persist(String key) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.persist(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：persist key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：persist key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long persist(String key, Long milliseconds) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.pexpire(key, milliseconds);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：persist key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：persist key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long pexpireAt(String key, Long milliseconds) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.pexpireAt(key, milliseconds);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：pexpireAt key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：pexpireAt key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long pfadd(String key, String... elements) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.pfadd(key, elements);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：pfadd key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：pfadd key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long pttl(String key) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.pttl(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：pttl key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：pttl key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
            return null;
        }
    }

    public Long pfcount(String key) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.pfcount(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：pfcount key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：pfcount key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long rpush(String key, String... strings) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.rpush(key, strings);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：rpush key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：rpush key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public void rpush(String key, List<String> strings) {
        Long start = System.currentTimeMillis();
        try {
            for (String s : strings) {
                jedisCluster.rpush(key, s);
            }
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：ShardedJedisPipeline rpush key:{} execution time:{}ms", key, time);
            }
        } catch (Exception e) {
            logger.error("command：ShardedJedisPipeline rpush key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    public void rpushx(String key, List<String> strings) {
        Long start = System.currentTimeMillis();
        try {
            for (String s : strings) {
                jedisCluster.rpushx(key, s);
            }
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：ShardedJedisPipeline rpushx key:{} execution time:{}ms", key, time);
            }
        } catch (Exception e) {
            logger.error("command：ShardedJedisPipeline rpushx key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
    }

    public Long rpushx(String key, String... strings) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.rpushx(key, strings);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：rpushx key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：rpushx key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public String rpop(String key) {
        Long start = System.currentTimeMillis();
        try {
            String v = jedisCluster.rpop(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：rpop key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：rpop key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Double zscore(String key, String member) {
        Long start = System.currentTimeMillis();
        try {
            Double v = jedisCluster.zscore(key, member);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：zscore key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：zscore key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Set<String> smembers(String key, String member) {
        Long start = System.currentTimeMillis();
        try {
            Set<String> v = jedisCluster.smembers(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：smembers key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：smembers key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long smove(String srckey, String dstkey, String member) {
        long start = System.currentTimeMillis();
        try {
            Long ret = jedisCluster.smove(srckey, dstkey, member);
            long time = System.currentTimeMillis() - start;
            if (time > 500)
                logger.warn("command= smove, srckey={}, dstkey={}, execute_time={}ms", srckey, dstkey, time);
            return ret;
        } catch (Exception e) {
            logger.error("command：smove srckey:{}, dstkey={}, ex={}", srckey, dstkey, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Boolean sismember(String key, String member) {
        Long start = System.currentTimeMillis();
        try {
            Boolean v = jedisCluster.sismember(key, member);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：sismember key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：sismember key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Boolean exists(String key) {
        Long start = System.currentTimeMillis();
        try {
            Boolean v = jedisCluster.exists(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：exists key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：exists key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public String echo(String key) {
        Long start = System.currentTimeMillis();
        try {
            String v = jedisCluster.echo(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：echo key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：echo key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Set<String> zrange(String key, Long start, Long end) {
        Long startTime = System.currentTimeMillis();
        try {
            Set<String> v = jedisCluster.zrange(key, start, end);
            Long endTime = System.currentTimeMillis();
            Long time = endTime - startTime;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：zrange key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：zrange key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long zrank(String key, String member) {
        long start = System.currentTimeMillis();
        try {
            Long ret = jedisCluster.zrank(key, member);
            long time = System.currentTimeMillis() - start;
            if (time > 500)
                logger.warn("command：zrank key:{} execution time:{}ms", key, time);
            return ret;
        } catch (Exception e) {
            logger.error("command：zrank key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long zrem(String key, String... members) {
        Long start = System.currentTimeMillis();
        try {
            Long v = jedisCluster.zrem(key, members);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：zrem key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：zrem key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Set<String> zrange(String key) {
        Long start = System.currentTimeMillis();
        try {
            Set<String> v = jedisCluster.zrange(key, 0, -1);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：zrange key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：zrange key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Set<String> zrevrange(String key, Long start, Long end) {
        Long startTime = System.currentTimeMillis();
        try {
            Set<String> v = jedisCluster.zrevrange(key, start, end);
            Long endTime = System.currentTimeMillis();
            Long time = endTime - startTime;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：zrevrange key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：zrevrange key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Set<String> zrevrange(String key) {
        Long start = System.currentTimeMillis();
        try {
            Set<String> v = jedisCluster.zrevrange(key, 0, -1);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：zrevrange key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：zrevrange key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Long zrevrank(String key, String member) {
        long start = System.currentTimeMillis();
        try {
            Long ret = jedisCluster.zrevrank(key, member);
            long time = System.currentTimeMillis() - start;
            if (time > 500)
                logger.warn("command：zrevrank key:{} execution time:{}ms", key, time);
            return ret;
        } catch (Exception e) {
            logger.error("command：zrevrank key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Map<String, String> hgetAll(String key) {
        Long start = System.currentTimeMillis();
        try {
            Map<String, String> v = jedisCluster.hgetAll(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：hgetAll key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：hgetAll key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public Set<String> hkeys(String key) {
        Long start = System.currentTimeMillis();
        try {
            Set<String> v = jedisCluster.hkeys(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：hkeys key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：hkeys key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public List<String> brpop(String arg) {
        Long start = System.currentTimeMillis();
        try {
            List<String> v = jedisCluster.brpop(arg);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：brpop key:{} execution time:{}ms", arg, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：brpop key:{} ex={}", arg, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public List<String> sort(String key) {
        Long start = System.currentTimeMillis();
        try {
            List<String> v = jedisCluster.sort(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：sort key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：sort key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public List<String> srandmember(String key, int count) {
        Long start = System.currentTimeMillis();
        try {
            List<String> v = jedisCluster.srandmember(key, count);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：srandmember key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：srandmember key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    public String type(String key) {
        Long start = System.currentTimeMillis();
        try {
            String v = jedisCluster.type(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：hget key:{} execution time:{}ms", key, time);
            }
            return v;
        } catch (Exception e) {
            logger.error("command：hget key:{} ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    /**
     * @return Set<String> 匹配到的结果集合
     * @Description 通配所有相关的redis key值
     * @params pattern 需要匹配的表达式
     */
    public Set<String> keys(String key) {
        Long start = System.currentTimeMillis();
        try {
            Set<String> keys = jedisCluster.hkeys(key);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            if (time > 500) {
                logger.warn("ip:{} port:{} command：keys key:{}  execution time:{}ms", key, time);
            }
            return keys;
        } catch (Exception e) {
            logger.error("command：keys key:{}  ex={}", key, LogExceptionStackTrace.erroStackTrace(e));
        }
        return null;
    }

    private TreeMap<Long, String> getSlotHostMap(String anyHostAndPortStr) {
        TreeMap<Long, String> tree = new TreeMap<>();
        String parts[] = anyHostAndPortStr.split(":");
        HostAndPort anyHostAndPort = new HostAndPort(parts[0], Integer.parseInt(parts[1]));
        try {
            Jedis jedisNode = new Jedis(anyHostAndPort.getHost(), anyHostAndPort.getPort());
            List<Object> list = jedisNode.clusterSlots();
            for (Object object : list) {
                List<Object> list1 = (List<Object>) object;
                List<Object> master = (List<Object>) list1.get(2);
                String hostAndPort = new String((byte[]) master.get(0)) + ":" + master.get(1);
                tree.put((Long) list1.get(0), hostAndPort);
                tree.put((Long) list1.get(1), hostAndPort);
            }
            jedisNode.close();
        } catch (Exception e) {

        }
        return tree;
    }

    public Jedis getJedisByKey(String key) {
        try {
            int slot = JedisClusterCRC16.getSlot(key);
            Set<String> set = jedisCluster.getClusterNodes().keySet();
            TreeMap<Long, String> slotHostMap = getSlotHostMap(set.iterator().next());
            Map.Entry<Long, String> entry = slotHostMap.lowerEntry(Long.valueOf(slot + 1));
            return jedisCluster.getClusterNodes().get(entry.getValue()).getResource();
        } catch (Exception e) {
            logger.error("get Jedis Node Failure! key={}", key);
        }
        return null;
    }
}
