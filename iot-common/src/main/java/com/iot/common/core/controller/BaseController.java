package com.iot.common.core.controller;

import com.github.pagehelper.PageInfo;
import com.iot.common.constant.HttpStatus;
import com.iot.common.core.domain.AjaxResult;
import com.iot.common.core.domain.model.LoginUser;
import com.iot.common.core.page.TableDataInfo;
import com.iot.common.utils.DateUtils;
import com.iot.common.utils.PageUtils;
import com.iot.common.utils.SecurityUtils;
import com.iot.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

/**
 * web层通用数据处理
 * 
 * @author ruoyi
 */
public class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
        {
            @Override
            public void setAsText(String text)
            {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage()
    {
        PageUtils.startPage();
    }

    /**
     * 清理分页的线程变量
     */
    protected void clearPage()
    {
        PageUtils.clearPage();
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo(list, 0);
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 返回成功消息
     * @param message 消息内容
     */
    public AjaxResult success(String message)
    {
        return AjaxResult.success(message);
    }

    public static AjaxResult success(Object data){
        return new AjaxResult(HttpStatus.SUCCESS, "操作成功", data);
    }
    
    /**
     * 返回成功消息
     * @param message 消息内容
     * @param data 数据
     */
    public AjaxResult success(String message, Object data)
    {
        return AjaxResult.success(message, data);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message)
    {
        return AjaxResult.error(message);
    }

    /**
     * 返回警告消息
     */
    public AjaxResult warn(String message)
    {
        return AjaxResult.warn(message);
    }

    /**
     *
     * @param data 返回的数据对象
     * @return AjaxResult 包含操作结果的封装对象
     */
    public AjaxResult toAjax(Object data){
        return AjaxResult.success("操作成功！",data);
    }

    /**
     * 页面跳转
     */
    public String redirect(String url)
    {
        return StringUtils.format("redirect:{}", url);
    }

    /**
     * 获取用户缓存信息
     */
    public LoginUser getLoginUser()
    {
        return SecurityUtils.getLoginUser();
    }

    /**
     * 获取登录用户id
     */
    public Long getUserId()
    {
        return getLoginUser().getUserId();
    }

    /**
     * 获取登录部门id
     */
    public Long getDeptId()
    {
        return getLoginUser().getDeptId();
    }

    /**
     * 获取登录用户名
     */
    public String getUsername()
    {
        return getLoginUser().getUsername();
    }
}
