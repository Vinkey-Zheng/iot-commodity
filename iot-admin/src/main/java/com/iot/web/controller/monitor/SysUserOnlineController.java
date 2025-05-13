package com.iot.web.controller.monitor;

import com.iot.common.annotation.Log;
import com.iot.common.constant.CacheConstants;
import com.iot.common.core.controller.BaseController;
import com.iot.common.core.domain.AjaxResult;
import com.iot.common.core.domain.model.LoginUser;
import com.iot.common.core.page.TableDataInfo;
import com.iot.common.core.redis.RedisCache;
import com.iot.common.enums.BusinessType;
import com.iot.common.utils.StringUtils;
import com.iot.system.domain.SysUserOnline;
import com.iot.system.service.ISysUserOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 在线用户监控
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController
{
    @Autowired
    private ISysUserOnlineService userOnlineService;

    @Autowired
    private RedisCache redisCache;

    @PreAuthorize("@ss.hasPermi('monitor:online:list')")
    @GetMapping("/list")
    public TableDataInfo list(String ipaddr, String userName)
    {
        Collection<String> keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        List<SysUserOnline> userOnlineList = new ArrayList<SysUserOnline>();

        // 防击穿：使用互斥锁
        String lockKey = CacheConstants.LOGIN_TOKEN_KEY + "_lock";
        boolean locked = redisCache.setIfAbsent(lockKey, "locked", 5); // 设置5秒锁

        try {
            if (locked) {
                for (String key : keys)
                {

                    LoginUser user = redisCache.getCacheObject(key);
                    // 防雪崩：添加随机过期时间，基础300秒，随机60秒
                    redisCache.setWithRandomExpire(key, user, 300, 60);
                    if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName))
                    {
                        userOnlineList.add(userOnlineService.selectOnlineByInfo(ipaddr, userName, user));
                    }
                    else if (StringUtils.isNotEmpty(ipaddr))
                    {
                        userOnlineList.add(userOnlineService.selectOnlineByIpaddr(ipaddr, user));
                    }
                    else if (StringUtils.isNotEmpty(userName) && StringUtils.isNotNull(user.getUser()))
                    {
                        userOnlineList.add(userOnlineService.selectOnlineByUserName(userName, user));
                    }
                    else
                    {
                        userOnlineList.add(userOnlineService.loginUserToUserOnline(user));
                    }
                }
            }
            else {
                // 等待锁释放
                Thread.sleep(100);
                // 重试获取缓存
                keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
                for (String key : keys) {
                    LoginUser user = redisCache.getCacheObject(key);
                    userOnlineList.add(userOnlineService.loginUserToUserOnline(user));
                }
            }} catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("获取在线用户信息中断", e);
        } finally {
            if (locked) {
                forceLogout(lockKey);
            }
        }

        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));
        return getDataTable(userOnlineList);
    }

    /**
     * 强退用户
     */
    @PreAuthorize("@ss.hasPermi('monitor:online:forceLogout')")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenId}")
    public AjaxResult forceLogout(@PathVariable String tokenId)
    {
        redisCache.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + tokenId);
        return success();
    }
}
