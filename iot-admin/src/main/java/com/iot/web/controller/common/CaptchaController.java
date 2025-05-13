package com.iot.web.controller.common;

import com.google.code.kaptcha.Producer;
import com.iot.common.config.RuoYiConfig;
import com.iot.common.constant.CacheConstants;
import com.iot.common.constant.Constants;
import com.iot.common.core.domain.AjaxResult;
import com.iot.common.core.redis.RedisCache;
import com.iot.common.utils.sign.Base64;
import com.iot.common.utils.uuid.IdUtils;
import com.iot.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 验证码操作处理
 *
 * @author ruoyi
 */
@RestController
public class CaptchaController
{
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysConfigService configService;
    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    public AjaxResult getCode(HttpServletResponse response) throws IOException
    {
        AjaxResult ajax = AjaxResult.success();
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        ajax.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled)
        {
            return ajax;
        }

        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        String captchaType = RuoYiConfig.getCaptchaType();
        if ("math".equals(captchaType))
        {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        }
        else if ("char".equals(captchaType))
        {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        // 防击穿：使用互斥锁
        String lockKey = verifyKey + "_lock";
        boolean locked = redisCache.setIfAbsent(lockKey, "locked", 5); // 设置5秒锁

        try {
            if (locked) {
                // 防雪崩：添加随机过期时间
                Integer randomExpire = Constants.CAPTCHA_EXPIRATION * 60 + (int)(Math.random() * 60); // 基础时间 + 随机60秒
                redisCache.setCacheObject(verifyKey, code, randomExpire, TimeUnit.SECONDS);
            } else {
                // 等待锁释放
                Thread.sleep(100);
                // 重试获取缓存
                code = (String) redisCache.getCacheObject(verifyKey);
                if (code == null) {
                    throw new RuntimeException("验证码生成失败");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("验证码生成中断", e);
        } finally {
            if (locked) {
                redisCache.deleteObject(lockKey);
            }
        }

        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e)
        {
            return AjaxResult.error(e.getMessage());
        }

        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));
        return ajax;
    }
}
