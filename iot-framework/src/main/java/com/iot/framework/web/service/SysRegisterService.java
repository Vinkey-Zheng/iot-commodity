package com.iot.framework.web.service;

import com.iot.common.constant.CacheConstants;
import com.iot.common.constant.Constants;
import com.iot.common.constant.UserConstants;
import com.iot.common.core.domain.entity.SysUser;
import com.iot.common.core.domain.model.RegisterBody;
import com.iot.common.core.redis.RedisCache;
import com.iot.common.exception.ServiceException;
import com.iot.common.exception.user.CaptchaException;
import com.iot.common.exception.user.CaptchaExpireException;
import com.iot.common.utils.MessageUtils;
import com.iot.common.utils.SecurityUtils;
import com.iot.common.utils.StringUtils;
import com.iot.framework.manager.AsyncManager;
import com.iot.framework.manager.factory.AsyncFactory;
import com.iot.system.service.ISysConfigService;
import com.iot.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 注册校验方法
 * 
 * @author ruoyi
 */
@Component
public class SysRegisterService
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 注册
     */
    public String register(RegisterBody registerBody)
    {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);

        // 验证码开关
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled)
        {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }

        if (StringUtils.isEmpty(username))
        {
            msg = "用户名不能为空";
        }
        else if (StringUtils.isEmpty(password))
        {
            msg = "用户密码不能为空";
        }
        else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            msg = "账户长度必须在2到20个字符之间";
        }
        else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            msg = "密码长度必须在5到20个字符之间";
        }
        else if (!userService.checkUserNameUnique(sysUser))
        {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        }
        else
        {
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag)
            {
                msg = "注册失败,请联系系统管理人员";
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
        return msg;
    }

    /**
     * 校验验证码
     * 
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);

//        if (captcha == null)
//        {
//            throw new CaptchaExpireException();
//        }
        if (captcha == null) {
            // 获取分布式锁
            String lockKey = verifyKey + "_lock";
            boolean locked = redisCache.setIfAbsent(lockKey, "locked", 5); // 设置5秒锁
            try {
                if (locked) {
                    // 再次检查缓存
                    captcha = redisCache.getCacheObject(verifyKey);
                    if (captcha == null) {
                        // 记录验证码已失效日志信息
                        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                        // 抛出验证码已失效异常
                        throw new CaptchaExpireException();
                    }
                } else {
                    // 等待锁释放
                    Thread.sleep(100);
                    captcha = redisCache.getCacheObject(verifyKey);
                    if (captcha == null) {
                        throw new CaptchaExpireException();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new ServiceException("验证码校验中断");
            } finally {
                if (locked) {
                    redisCache.deleteObject(lockKey);
                }
            }
        }
        redisCache.deleteObject(verifyKey);
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }
    }
}
