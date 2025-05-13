package com.iot.framework.web.service;

import com.iot.common.constant.CacheConstants;
import com.iot.common.constant.Constants;
import com.iot.common.constant.UserConstants;
import com.iot.common.core.domain.entity.SysUser;
import com.iot.common.core.domain.model.LoginUser;
import com.iot.common.core.redis.RedisCache;
import com.iot.common.exception.ServiceException;
import com.iot.common.exception.user.*;
import com.iot.common.utils.DateUtils;
import com.iot.common.utils.MessageUtils;
import com.iot.common.utils.StringUtils;
import com.iot.common.utils.ip.IpUtils;
import com.iot.framework.manager.AsyncManager;
import com.iot.framework.manager.factory.AsyncFactory;
import com.iot.framework.security.context.AuthenticationContextHolder;
import com.iot.system.service.ISysConfigService;
import com.iot.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class SysLoginService
{
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid)
    {
        // 验证码校验
        validateCaptcha(username, code, uuid);
        // 登录前置校验
        loginPreCheck(username, password);
        // 用户验证
        Authentication authentication = null;
        try
        {
            // 创建用户名密码对象
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            // 将用户名密码放入认证上下文
            AuthenticationContextHolder.setContext(authenticationToken);
            // 执行用户认证，该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        }
        catch (Exception e)
        {
            // 处理认证异常
            if (e instanceof BadCredentialsException)
            {
                // 记录用户不存在/密码错误日志信息
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                // 抛出用户名或密码不匹配异常
                throw new UserPasswordNotMatchException();
            }
            else
            {
                // 记录登录失败日志信息
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                // 抛出系统异常
                throw new ServiceException(e.getMessage());
            }
        }
        finally
        {
            // 清除认证上下文中的用户名密码
            AuthenticationContextHolder.clearContext();
        }
        // 记录登录成功日志信息
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        // 获取登录用户信息
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 更新用户登录信息
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
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
        // 检查是否启用了验证码功能
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled)
        {
            // 构建验证码的缓存键
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            // 获取redis缓存中的验证码
            String captcha = redisCache.getCacheObject(verifyKey);
            // 假设基础过期时间为300秒（5分钟），随机范围为60秒，防雪崩操作
            redisCache.setWithRandomExpire(verifyKey, captcha, 300, 60);

            // 双重检查防止缓存击穿
            // 如果验证码不存在，则尝试从缓存中获取
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
                    }
                    else {
                        // 等待锁释放
                        Thread.sleep(100);
                        captcha = redisCache.getCacheObject(verifyKey);
                        if (captcha == null) {
                            throw new CaptchaExpireException();
                        }
                    }
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new ServiceException("验证码校验中断");
                }finally {
                    if (locked) {
                        redisCache.deleteObject(lockKey);
                    }
                }
            }

            // 删除redis缓存中的验证码
            redisCache.deleteObject(verifyKey);

            // 验证用户提交的验证码与缓存中的验证码是否匹配（不区分大小写）
            if (!code.equalsIgnoreCase(captcha))
            {
                // 记录验证码不匹配日志信息
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                // 抛出验证码不匹配异常
                throw new CaptchaException();
            }
        }
    }

    /**
     * 登录前置校验
     * @param username 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String username, String password)
    {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        {
            // 记录用户密码不存在日志信息
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            // 抛出用户密码不存在异常
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内（5~20）错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            // 记录用户密码错误日志信息
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            // 抛出用户密码不正确或不符合规范异常
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内（2~20）错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            // 记录用户不存在日志信息
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            // 抛出用户密码不正确或不符合规范异常
            throw new UserPasswordNotMatchException();
        }
        // IP黑名单校验
        // 从配置服务中获取黑名单IP列表
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        // 判断当前用户IP是否在黑名单中
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr()))
        {
            // 记录访问IP已被列入系统黑名单日志信息
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
            // 抛出黑名单异常
            throw new BlackListException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());// 登录ip
        sysUser.setLoginDate(DateUtils.getNowDate());// 登录时间
        userService.updateUserProfile(sysUser);// 更新用户登录信息
    }
}
