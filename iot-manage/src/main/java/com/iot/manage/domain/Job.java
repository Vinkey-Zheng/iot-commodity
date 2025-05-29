package com.iot.manage.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.iot.common.annotation.Excel;
import com.iot.common.core.domain.BaseEntity;

/**
 * 自动补货任务对象 tb_job
 *
 * @author zmq
 * @date 2025-03-20
 */
@Data
public class Job extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 警戒值百分比 */
    @Excel(name = "警戒值百分比")
    private Long alertValue;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("alertValue", getAlertValue())
            .toString();
    }
}
