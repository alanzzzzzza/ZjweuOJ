package com.nextrt.acm.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    private final RedisTemplate<Object,Object> redisTemplate;
    private final static Integer expire = 3;

    public RedisUtil(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    //得到锁
    public boolean getLock(String key){
        while (checkLock(key)){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
        addLock(key,60);
        return true;
    }
    //对key加锁
    public void addLock(String key,Integer time){
        redisTemplate.opsForValue().setIfAbsent("Lock-"+key,"ok",time, TimeUnit.SECONDS);
    }
    //检查是否加锁
    public Boolean checkLock(String key){
        return redisTemplate.hasKey("Lock-"+key);
    }
    //对key解除锁定
    public void removeLock(String key){
        redisTemplate.delete("Lock-"+key);
    }

    Long incr(String key) {
        Long i = redisTemplate.boundValueOps(key).increment(1);
        redisTemplate.boundValueOps(key).expire(65,TimeUnit.SECONDS);
        return i;
    }
    public void set(String key, String value){
        redisTemplate.opsForValue().set(key, value ,expire, TimeUnit.MINUTES);
    }
    public String get(String key) {return String.valueOf(redisTemplate.opsForValue().get(key));}
    public void remove(String key){redisTemplate.delete(key);}
    public Boolean hasKey(String key){return redisTemplate.hasKey(key);}
}
