package com.iot.manage.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.iot.common.annotation.Excel;
import com.iot.common.core.domain.BaseEntity;

/**
 * 工单对象 tb_task
 *
 * @author zmq
 * @date 2025-03-20
 */
@Data
public class Task extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 工单ID */
    private Long taskId;

    /** 工单编号 */
    @Excel(name = "工单编号")
    private String taskCode;

    /** 工单状态 */
    @Excel(name = "工单状态")
    private Long taskStatus;

    /** 创建类型 0：自动 1：手动 */
    @Excel(name = "创建类型 0：自动 1：手动")
    private Long createType;

    /** 售货机编码 */
    @Excel(name = "售货机编码")
    private String innerCode;

    /** 执行人id */
    @Excel(name = "执行人id")
    private Long userId;

    /** 执行人名称 */
    @Excel(name = "执行人名称")
    private String userName;

    /** 所属区域Id */
    @Excel(name = "所属区域Id")
    private Long regionId;

    /** 备注 */
    @Excel(name = "备注")
    private String desc;

    /** 工单类型id */
    @Excel(name = "工单类型id")
    private Long productTypeId;

    /** 指派人Id */
    @Excel(name = "指派人Id")
    private Long assignorId;

    /** 地址 */
    @Excel(name = "地址")
    private String addr;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("taskId", getTaskId())
            .append("taskCode", getTaskCode())
            .append("taskStatus", getTaskStatus())
            .append("createType", getCreateType())
            .append("innerCode", getInnerCode())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("regionId", getRegionId())
            .append("desc", getDesc())
            .append("productTypeId", getProductTypeId())
            .append("assignorId", getAssignorId())
            .append("addr", getAddr())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
