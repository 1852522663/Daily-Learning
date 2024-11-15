package com.learning.demo.lock;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * @author yuehewei <yuehewei@kuaishou.com>
 * Created on 2024-11-15
 */
public class RedisLockTool {

    private static final Logger log = LoggerFactory.getLogger(RedisLockTool.class);
    private static Jedis jedis;
    private static final String Lock_SUCCESS="OK";
    private static final Long RELEASE_SUCCESS = 1L;
    private static final String SET_IF_NO_EXIST="NX";  // 意思是SET IF NOT EXIST，即当key不存在时，我们进行set操作；若key已经存在，则不做任何操作；
    private static final String SET_WITH_EXPIRE_TIME="PX";  //意思是我们要给这个key加一个过期的设置，具体时间由第五个参数决定


    /**
     * 尝试获取分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超时时间
     * @return
     */
    public static boolean tryGetLDistributedLock(String lockKey, String requestId, int expireTime) {
        //通过给value赋值为requestId，我们就知道这把锁是哪个请求加的了，在解锁的时候就可以有依据。requestId可以使用UUID.randomUUID().toString()方法生成。
        String result = jedis.set(lockKey, requestId, SET_IF_NO_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        return !Lock_SUCCESS.equals(result);
    }


    /**
     * 错误样例
     * @param lockKey
     * @param requestId
     * @param expireTime
     * @return
     */
    public static void tryGetLDistributedLockV1(String lockKey, String requestId, int expireTime){
        Long result = jedis.setnx(lockKey, requestId);
        if(result==1){
            // 设置过期时间 此时程序崩溃，则锁无法释放形成死锁
           jedis.expire(lockKey, expireTime);
        }
    }

    /**
     * 错误样例
     * 1. 由于是客户端自己生成过期时间，所以需要强制要求分布式下每个客户端的时间必须同步。
     * 2. 当锁过期的时候，如果多个客户端同时执行jedis.getSet()方法，那么虽然最终只有一个客户端可以加锁，但是这个客户端的锁的过期时间可能被其他客户端覆盖。
     * 3. 锁不具备拥有者标识，即任何客户端都可以解锁。
     * @param lockKey
     * @param expireTime
     * @return
     */
    public static boolean tryGetDistributedLockV3(String lockKey, int expireTime){
        long expire = System.currentTimeMillis() + expireTime;
        String expireStr = String.valueOf(expire);
        //如果缓存不存在就加锁
        if (jedis.setnx(lockKey, expireStr) == 1){
            return true;
        }
        //如果锁已经存在，检查是否超时
        String currentValueStr = jedis.get(lockKey);
        if (currentValueStr!=null && Long.parseLong(currentValueStr)<System.currentTimeMillis()){
            //锁已经过期，获取上一个锁的过期时间，并设置现在锁的过期时间
            String oldValueStr = jedis.getSet(lockKey, expireStr);
            if (oldValueStr!=null && oldValueStr.equals(currentValueStr)){
                // 考虑多线程并发的情况，只有一个线程的设置值和当前值相同，它才有权利加锁
                return true;
            }
        }
        // 其他情况，一律返回加锁失败
        return false;
    }

    /**
     * 释放分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return
     */
    public static boolean releaseDistributedLock(String lockKey,String requestId){
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        //Lua代码传到jedis.eval()方法里，并使参数KEYS[1]赋值为lockKey，ARGV[1]赋值为requestId。eval()方法是将Lua代码交给Redis服务端执行。简单来说，就是在eval命令执行Lua代码的时候，Lua代码将被当成一个命令去执行，并且直到eval命令执行完成，Redis才会执行其他命令。保证加锁和解锁是一个原子操作。
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        return RELEASE_SUCCESS.equals(result);
    }

    /**
     * 错误样例
     * @param lockKey
     * @param requestId
     */
    public static void wrongReleaseLock2(String lockKey, String requestId) {
        // 判断加锁与解锁是不是同一个客户端
        if (requestId.equals(jedis.get(lockKey))) {
            // 若在此时，这把锁突然不是这个客户端的，则会误解锁
            jedis.del(lockKey);
        }
    }


}
