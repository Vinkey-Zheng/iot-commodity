package com.iot.system.service.impl;

import com.iot.common.annotation.DataSource;
import com.iot.common.constant.CacheConstants;
import com.iot.common.constant.UserConstants;
import com.iot.common.core.redis.RedisCache;
import com.iot.common.core.text.Convert;
import com.iot.common.enums.DataSourceType;
import com.iot.common.exception.ServiceException;
import com.iot.common.utils.StringUtils;
import com.iot.system.domain.SysConfig;
import com.iot.system.mapper.SysConfigMapper;
import com.iot.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 参数配置 服务层实现
 *
 * @author ruoyi
 */
@Service
public class SysConfigServiceImpl implements ISysConfigService
{
    @Autowired
    private SysConfigMapper configMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init()
    {
        loadingConfigCache();
    }

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public SysConfig selectConfigById(Long configId)
    {
        SysConfig config = new SysConfig();
        config.setConfigId(configId);
        return configMapper.selectConfig(config);
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey)
    {
        // 防击穿：使用互斥锁
        String lockKey = getCacheKey(configKey) + "_lock";
        boolean locked = redisCache.setIfAbsent(lockKey, "locked", 5); // 设置5秒锁

        try {
            if (locked) {
                // 尝试从Redis缓存中获取配置值
                String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey(configKey)));
                if (StringUtils.isNotEmpty(configValue))
                {
                    // 如果缓存中存在该配置值，则直接返回
                    return configValue;
                }

                // 创建SysConfig对象，设置查询条件
                SysConfig config = new SysConfig();
                config.setConfigKey(configKey);

                // 从数据库中查询配置
                SysConfig retConfig = configMapper.selectConfig(config);
                if (StringUtils.isNotNull(retConfig))
                {
                    // 防雪崩：添加随机过期时间
                    Integer randomExpire = 300 + (int)(Math.random() * 60); // 基础300秒 + 随机60秒
                    // 如果数据库中存在该配置，则将其存入缓存中，并返回配置值
                    redisCache.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue(), randomExpire, TimeUnit.SECONDS);
                    return retConfig.getConfigValue();
                }
            } else {
                // 等待锁释放
                Thread.sleep(100);
                // 重试获取缓存
                String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey(configKey)));
                if (StringUtils.isNotEmpty(configValue)) {
                    return configValue;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServiceException("配置获取中断");
        } finally {
            if (locked) {
                redisCache.deleteObject(lockKey);
            }
        }
        // 如果数据库中也不存在该配置键，则返回空字符串
        return StringUtils.EMPTY;
    }

    /**
     * 获取验证码开关
     *
     * @return true开启，false关闭
     */
    @Override
    public boolean selectCaptchaEnabled()
    {
        // 从参数配置中获取验证码启用状态
        String captchaEnabled = selectConfigByKey("sys.account.captchaEnabled");

        // 如果获取的配置值为空或未设置，则默认启用验证码
        if (StringUtils.isEmpty(captchaEnabled))
        {
            return true;
        }

        // 将配置值转换为布尔类型并返回
        return Convert.toBool(captchaEnabled);
    }

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<SysConfig> selectConfigList(SysConfig config)
    {
        return configMapper.selectConfigList(config);
    }

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(SysConfig config)
    {
        int row = configMapper.insertConfig(config);
        if (row > 0)
        {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int updateConfig(SysConfig config)
    {
        SysConfig temp = configMapper.selectConfigById(config.getConfigId());
        if (!StringUtils.equals(temp.getConfigKey(), config.getConfigKey()))
        {
            redisCache.deleteObject(getCacheKey(temp.getConfigKey()));
        }

        int row = configMapper.updateConfig(config);
        if (row > 0)
        {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    @Override
    public void deleteConfigByIds(Long[] configIds)
    {
        for (Long configId : configIds)
        {
            SysConfig config = selectConfigById(configId);
            if (StringUtils.equals(UserConstants.YES, config.getConfigType()))
            {
                throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
            configMapper.deleteConfigById(configId);
            redisCache.deleteObject(getCacheKey(config.getConfigKey()));
        }
    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingConfigCache()
    {
        List<SysConfig> configsList = configMapper.selectConfigList(new SysConfig());
        for (SysConfig config : configsList)
        {
            // 防雪崩：添加随机过期时间
            Integer randomExpire = 300 + (int)(Math.random() * 60); // 基础300秒 + 随机60秒
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue(), randomExpire, TimeUnit.SECONDS);
        }
    }

    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearConfigCache()
    {
        Collection<String> keys = redisCache.keys(CacheConstants.SYS_CONFIG_KEY + "*");
        redisCache.deleteObject(keys);
    }

    /**
     * 重置参数缓存数据
     */
    @Override
    public void resetConfigCache()
    {
        clearConfigCache();
        loadingConfigCache();
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public boolean checkConfigKeyUnique(SysConfig config)
    {
        Long configId = StringUtils.isNull(config.getConfigId()) ? -1L : config.getConfigId();
        SysConfig info = configMapper.checkConfigKeyUnique(config.getConfigKey());
        if (StringUtils.isNotNull(info) && info.getConfigId().longValue() != configId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey)
    {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }
}
