package com.iot.manage.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.iot.common.annotation.Excel;
import com.iot.common.core.domain.BaseEntity;

/**
 * 工单类型对象 tb_task_type
 *
 * @author zmq
 * @date 2025-03-20
 */
@Data
public class TaskType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 工单类型ID */
    private Long typeId;

    /** 类型名称 */
    @Excel(name = "类型名称")
    private String typeName;

    /** 工单类型。1:维修工单;2:运营工单 */
    @Excel(name = "工单类型。1:维修工单;2:运营工单")
    private Long type;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("typeId", getTypeId())
            .append("typeName", getTypeName())
            .append("type", getType())
            .toString();
    }
}
