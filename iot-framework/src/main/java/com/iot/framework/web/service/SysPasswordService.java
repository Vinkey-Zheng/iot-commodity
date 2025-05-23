package com.iot.framework.web.service;

import com.iot.common.constant.CacheConstants;
import com.iot.common.core.domain.entity.SysUser;
import com.iot.common.core.redis.RedisCache;
import com.iot.common.exception.user.UserPasswordNotMatchException;
import com.iot.common.exception.user.UserPasswordRetryLimitExceedException;
import com.iot.common.utils.SecurityUtils;
import com.iot.framework.security.context.AuthenticationContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 登录密码方法
 *
 * @author ruoyi
 */
@Component
public class SysPasswordService
{
    @Autowired
    private RedisCache redisCache;

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username)
    {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    /**
     * 验证用户登录信息
     * 本方法主要功能是验证传入的用户对象的密码是否正确，同时处理密码重试逻辑
     * 如果密码错误，会增加重试计数并在达到最大重试次数后锁定用户
     * 如果密码正确，则清除之前的登录记录缓存
     *
     * @param user 待验证的用户对象，包含用户信息和密码
     * @throws UserPasswordRetryLimitExceedException 如果用户密码重试次数超过限定次数，则抛出此异常
     * @throws UserPasswordNotMatchException 如果用户密码不匹配，则抛出此异常
     */
    public void validate(SysUser user)
    {
        // 获取当前的认证信息
        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        // 从认证信息中提取用户名
        String username = usernamePasswordAuthenticationToken.getName();
        // 从认证信息中提取密码
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();

        // 获取缓存键
        String cacheKey = getCacheKey(username);
        //  获取锁定键
        String lockKey = cacheKey + "_lock";
        boolean locked = redisCache.setIfAbsent(lockKey, "locked", 5);// 设置5秒锁

        try {
            // 检查账户是否已被锁定
            if (locked) {
                // 从缓存中获取当前的重试次数
                Integer retryCount = redisCache.getCacheObject(cacheKey);
                // 如果重试次数为空，初始化为0
                if (retryCount == null) {
                    retryCount = 0;
                }

                // 如果重试次数达到最大限制，抛出异常，阻止登录
                if (retryCount >= maxRetryCount) {
                    throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
                }

                // 如果密码不匹配，增加重试次数并更新缓存，然后抛出异常
                if (!matches(user, password)) {
                    retryCount++;
                    // 固定过期时间，无需随机
                    redisCache.setCacheObject(cacheKey, retryCount, lockTime * 60, TimeUnit.SECONDS);
                    throw new UserPasswordNotMatchException();
                } else {
                    // 如果密码匹配，清除登录记录缓存
                    clearLoginRecordCache(username);
                }
            } else {
                // 如果账户未被锁定，短暂休眠后再次检查重试次数
                Thread.sleep(100);
                Integer retryCount = redisCache.getCacheObject(cacheKey);
                // 如果重试次数达到最大限制，抛出异常，阻止登录
                if (retryCount != null && retryCount >= maxRetryCount) {
                    throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
                }
            }
        } catch (InterruptedException e) {
            // 捕获中断异常，恢复中断状态，并抛出运行时异常
            Thread.currentThread().interrupt();
            throw new RuntimeException("密码验证中断", e);
        } finally {
            // 释放锁，确保公平性
            if (locked) {
                redisCache.deleteObject(lockKey);
            }
        }

    }

    public boolean matches(SysUser user, String rawPassword)
    {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName)
    {
        if (redisCache.hasKey(getCacheKey(loginName)))
        {
            redisCache.deleteObject(getCacheKey(loginName));
        }
    }
}
