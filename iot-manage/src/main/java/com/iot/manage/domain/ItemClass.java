package com.iot.manage.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.iot.common.annotation.Excel;
import com.iot.common.core.domain.BaseEntity;

/**
 * 商品类型对象 tb_sku_class
 *
 * @author zmq
 * @date 2025-03-20
 */
@Data
public class ItemClass extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long classId;

    /** 商品类型 */
    // @Excel(name = "商品类型") 注解用于标记该字段在导出 Excel 表格时对应的列名，
    // 此处表示将 className 字段导出为 Excel 列，列标题为“商品类型”。
    @Excel(name = "商品类型")
    private String className;

    /** 上级id */
    private Long parentId;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("classId", getClassId())
            .append("className", getClassName())
            .append("parentId", getParentId())
            .toString();
    }
}
