package com.binance.platform.redis.lock;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Cluster;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Sentinel;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.binance.platform.redis.lock.support.ClusterRedLockHelper;
import com.binance.platform.redis.lock.support.RedisLockThreadContext;

/**
 * redis 红锁
 * 
 */
public abstract class RedisMultipleLock extends RedisSingleLock {

    private Boolean isCluster = false;
    private Boolean isSentinel = false;

    public RedisMultipleLock(RedisProperties redisPropties, StringRedisTemplate redisTemplate, Environment env) {
        super(redisTemplate, env);
        Sentinel sentinel = redisPropties.getSentinel();
        Cluster cluster = redisPropties.getCluster();
        if (cluster != null) {
            this.isCluster = true;
        }
        if (sentinel != null) {
            this.isSentinel = true;
        }
    }

    @Override
    protected boolean lockInternal(String key, long waitTime, long expireTime) {
        // 如果是集群模式的话，需要在每一个slot都进行加锁，全部返回正确才算正确
        if (this.isCluster) {
            // 计算需要分散Key值
            final String threadId = RedisLockThreadContext.getContext().getCurrentThreadId();
            final ClusterRedLockHelper lockHelper = new ClusterRedLockHelper(RedisMultipleLock.this);
            // 并行向redis节点进行加锁申请
            Boolean result = lockHelper.lock(key, threadId, waitTime, expireTime);
            // 对所有节点返回结果进行判断
            if (!result) {
                lockHelper.unlock(key, threadId);
                return false;
            } else {
                return true;
            }
        }
        // 如果是主从模式的话
        else if (this.isSentinel) {
            return super.lockInternal(key, waitTime, expireTime);
        } else {
            return super.lockInternal(key, waitTime, expireTime);
        }
    }

    @Override
    protected boolean unlockInternal(String key) {
        // 如果是集群模式的话，需要在每一个slot都进行加锁，全部返回正确才算正确
        if (this.isCluster) {
            final String threadId = RedisLockThreadContext.getContext().removeCurrentThreadId();
            if (threadId != null) {
                // 并行向redis节点进行解锁申请
                return new ClusterRedLockHelper(RedisMultipleLock.this).unlock(key, threadId);
            } else {
                return true;
            }
        }
        // 如果是主从模式的话
        else if (this.isSentinel) {
            return super.unlockInternal(key);
        } else {
            return super.unlockInternal(key);
        }
    }

}
