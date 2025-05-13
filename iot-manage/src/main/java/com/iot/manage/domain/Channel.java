package com.iot.manage.domain;

import com.iot.common.annotation.Excel;
import com.iot.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 售货机货道对象 tb_channel
 *
 * @author zmq
 * @date 2025-03-20
 */
@Data
public class Channel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 货道编号 */
    @Excel(name = "货道编号")
    private String channelCode;

    /** 商品Id */
    @Excel(name = "商品Id")
    private Long skuId;

    /** 售货机Id */
    @Excel(name = "售货机Id")
    private Long vmId;

    /** 售货机软编号 */
    @Excel(name = "售货机软编号")
    private String innerCode;

    /** 货道最大容量 */
    @Excel(name = "货道最大容量")
    private Long maxCapacity;

    /** 货道当前容量 */
    @Excel(name = "货道当前容量")
    private Long currentCapacity;

    /** 上次补货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "上次补货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastSupplyTime;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("channelCode", getChannelCode())
            .append("skuId", getSkuId())
            .append("vmId", getVmId())
            .append("innerCode", getInnerCode())
            .append("maxCapacity", getMaxCapacity())
            .append("currentCapacity", getCurrentCapacity())
            .append("lastSupplyTime", getLastSupplyTime())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
