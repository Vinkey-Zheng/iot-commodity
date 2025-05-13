package com.iot.manage.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.iot.common.annotation.Excel;
import com.iot.common.core.domain.BaseEntity;

/**
 * 策略管理对象 tb_policy
 *
 * @author zmq
 * @date 2025-03-20
 */
@Data
public class Policy extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 策略id */
    private Long policyId;

    /** 策略名称 */
    @Excel(name = "策略名称")
    private String policyName;

    /** 策略方案，如：80代表8折 */
    @Excel(name = "策略方案，如：80代表8折")
    private Long discount;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("policyId", getPolicyId())
            .append("policyName", getPolicyName())
            .append("discount", getDiscount())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
