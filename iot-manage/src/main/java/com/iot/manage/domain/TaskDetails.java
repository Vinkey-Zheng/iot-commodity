package com.iot.manage.domain;

import com.iot.common.annotation.Excel;
import com.iot.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 工单详情对象 tb_task_details
 *
 * @author zmq
 * @date 2025-03-20
 */
@ApiModel(description = "工单详情对象")
@Data
public class TaskDetails extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 工单详情ID */
    @ApiModelProperty(value = "工单详情ID")
    private Long detailsId;

    /** 工单Id */
    @ApiModelProperty(value = "工单Id")
    @Excel(name = "工单Id")
    private Long taskId;

    /** 货道编号 */
    @ApiModelProperty(value = "货道编号")
    @Excel(name = "货道编号")
    private String channelCode;

    /** 补货期望容量 */
    @ApiModelProperty(value = "补货期望容量")
    @Excel(name = "补货期望容量")
    private Long expectCapacity;

    /** 商品Id */
    @ApiModelProperty(value = "商品Id")
    @Excel(name = "商品Id")
    private Long itemId;

    /** 商品名称 */
    @ApiModelProperty(value = "商品名称")
    @Excel(name = "商品名称", readConverterExp = "itemName=${comment}")
    private String itemName;

    /** 商品图片 */
    @ApiModelProperty(value = "商品图片")
    @Excel(name = "商品图片", readConverterExp = "itemImage=${comment}")
    private String itemImage;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("detailsId", getDetailsId())
            .append("taskId", getTaskId())
            .append("channelCode", getChannelCode())
            .append("expectCapacity", getExpectCapacity())
            .append("itemId", getItemId())
            .append("itemName", getItemName())
            .append("itemImage", getItemImage())
            .toString();
    }
}
