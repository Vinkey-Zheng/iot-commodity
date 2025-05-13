package com.iot.common.annotation;

import com.iot.common.enums.BusinessType;
import com.iot.common.enums.OperatorType;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 * 
 * @author ruoyi
 *
 */
// 指定该注解可以应用在方法参数和方法上
@Target({ ElementType.PARAMETER, ElementType.METHOD })
// 指定该注解在运行时保留，可以通过反射读取
@Retention(RetentionPolicy.RUNTIME)
// 指定该注解包含在JavaDoc中
@Documented
public @interface Log
{
    /**
     * 模块
     */
    public String title() default "";

    /**
     * 功能
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    public OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    public boolean isSaveResponseData() default true;

    /**
     * 排除指定的请求参数
     */
    public String[] excludeParamNames() default {};
}
