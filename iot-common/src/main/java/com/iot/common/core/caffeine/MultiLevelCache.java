package com.iot.common.core.caffeine;
//
//import com.github.benmanes.caffeine.cache.Cache;
//import com.github.benmanes.caffeine.cache.Caffeine;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.TimeUnit;
//import java.util.function.Supplier;
//
//@Component
//public class MultiLevelCache {
//    private final RedisTemplate<Object, Object> redisTemplate;
//    private final Cache<String, Object> localCache;
//
//    // 改为构造器注入（推荐）
//    public MultiLevelCache(RedisTemplate<Object, Object> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//        this.localCache = Caffeine.newBuilder()
//                .expireAfterWrite(5, TimeUnit.MINUTES)
//                .maximumSize(1000)
//                .build();
//    }
//
//    public <T> T get(String key, Class<T> clazz, Supplier<T> dbFallback, long expireTime, TimeUnit timeUnit) {
//        // 方法逻辑保持不变
//        T value = (T) localCache.getIfPresent(key);
//        if (value != null) {
//            return value;
//        }
//
//        value = (T) redisTemplate.opsForValue().get(key);
//        if (value != null) {
//            localCache.put(key, value);
//            return value;
//        }
//
//        value = dbFallback.get();
//        if (value != null) {
//            redisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
//            localCache.put(key, value);
//        }
//        return value;
//    }
//}
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public class MultiLevelCache {
    public static void main(String[] args) {
        // 创建一个 LoadingCache
        LoadingCache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(100) // 最大缓存条目数
                .expireAfterWrite(10, TimeUnit.MINUTES) // 写入后10分钟失效
                .build(key -> fetchDataFromFeignApi(key)); // 加载数据的方法

        // 使用缓存
        String result = cache.get("user:123"); // 若不存在，则自动加载
        System.out.println(result);

        // 获取已存在的值
        String cachedResult = cache.getIfPresent("user:123");
        System.out.println("获取" + cachedResult);
    }

    // 模拟从某个API获取数据
    private static String fetchDataFromFeignApi(String key) {
        // 实际的远程调用查询逻辑
        return "Data for " + key; // 示例返回数据
    }
}

