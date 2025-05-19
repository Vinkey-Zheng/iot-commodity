package com.iot.manage.domain;

import com.iot.common.annotation.Excel;
import com.iot.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 设备管理对象 tb_vending_machine
 *
 * @author zmq
 * @date 2025-03-23
 */
@Data
public class VendingMachine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Excel(name = "主键")
    private Long id;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String innerCode;

    /** 设备容量 */
    @Excel(name = "设备容量")
    private Long channelMaxCapacity;

    /** 点位Id */
    @Excel(name = "点位Id")
    private Long nodeId;

    /** 详细地址 */
    @Excel(name = "详细地址")
    private String addr;

    /** 上次补货时间 */
    @Excel(name = "上次补货时间")
    private Date lastSupplyTime;

    /** 商圈类型 */
    @Excel(name = "商圈类型")
    private Long businessType;

    /** 区域Id */
    @Excel(name = "区域Id")
    private Long regionId;

    /** 合作商Id */
    @Excel(name = "合作商Id")
    private Long partnerId;

    /** 设备型号 */
    @Excel(name = "设备型号")
    private Long vmTypeId;

    /** 设备状态，0:未投放;1-运营;3-撤机 */
    @Excel(name = "设备状态，0:未投放;1-运营;3-撤机")
    private Long vmStatus;

    /** 运行状态 */
    @Excel(name = "运行状态")
    private String runningStatus;

    /** 经度 */
    @Excel(name = "经度")
    private Long longitudes;

    /** 维度 */
    @Excel(name = "维度")
    private Long latitude;

    /** 客户端连接Id,做emq认证用 */
    @Excel(name = "客户端连接Id,做emq认证用")
    private String clientId;

    /** 策略id */
    @Excel(name = "策略id")
    private Long policyId;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("innerCode", getInnerCode())
            .append("channelMaxCapacity", getChannelMaxCapacity())
            .append("nodeId", getNodeId())
            .append("addr", getAddr())
            .append("lastSupplyTime", getLastSupplyTime())
            .append("businessType", getBusinessType())
            .append("regionId", getRegionId())
            .append("partnerId", getPartnerId())
            .append("vmTypeId", getVmTypeId())
            .append("vmStatus", getVmStatus())
            .append("runningStatus", getRunningStatus())
            .append("longitudes", getLongitudes())
            .append("latitude", getLatitude())
            .append("clientId", getClientId())
            .append("policyId", getPolicyId())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
