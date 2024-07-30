package com.fisa.wooriarte.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisDao {

    private final RedisTemplate<String, String> redisTemplate; // 수정된 부분

    public RedisDao(RedisTemplate<String, String> redisTemplate) { // 생성자 수정
        this.redisTemplate = redisTemplate;
    }

    public void setValues(String key, String data) {
        ValueOperations<String, String> values = redisTemplate.opsForValue(); // 수정된 부분
        values.set(key, data);
    }

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue(); // 수정된 부분
        values.set(key, data, duration);
    }

    public String getValues(String key) { // 반환 타입 수정
        ValueOperations<String, String> values = redisTemplate.opsForValue(); // 수정된 부분
        return values.get(key);
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}
