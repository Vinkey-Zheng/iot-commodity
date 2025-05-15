package com.iot.manage.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.iot.common.annotation.Excel;
import com.iot.common.core.domain.BaseEntity;

/**
 * 区域管理对象 tb_region
 * 
 * @author zmq
 * @date 2025-03-20
 */
@Data
public class Region extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 区域名称 */
    @Excel(name = "区域名称")
    private String regionName;

    /**
     * 由于toString方法中涉及部分父类(BaseEntity)的字段，
     * 因此，不采用data完全覆盖，保留toString方法
     */

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("regionName", getRegionName())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("createBy", getCreateBy())
                .append("updateBy", getUpdateBy())
                .append("remark", getRemark())
                .toString();
    }
}
