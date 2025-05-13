package com.iot.manage.domain;

import com.iot.common.annotation.Excel;
import com.iot.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 设备类型管理对象 tb_vm_type
 * 
 * @author zmq
 * @date 2025-3-20
 */
@Data
public class VmType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 型号名称 */
    @Excel(name = "型号名称")
    private String name;

    /** 型号编码 */
    @Excel(name = "型号编码")
    private String model;

    /** 设备图片 */
    @Excel(name = "设备图片")
    private String image;

    /** 货道行 */
    @Excel(name = "货道行")
    private Long vmRow;

    /** 货道列 */
    @Excel(name = "货道列")
    private Long vmCol;

    /** 设备容量 */
    @Excel(name = "设备容量")
    private Long channelMaxCapacity;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("model", getModel())
            .append("image", getImage())
            .append("vmRow", getVmRow())
            .append("vmCol", getVmCol())
            .append("channelMaxCapacity", getChannelMaxCapacity())
            .toString();
    }
}
