package com.iot.manage.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.iot.common.annotation.Excel;
import com.iot.common.core.domain.BaseEntity;

/**
 * 商品管理对象 tb_sku
 *
 * @author zmq
 * @date 2025-03-20
 */
@ExcelIgnoreUnannotated // 注解表示在导出excel时，忽略没有被任何注解标注的字段
@ColumnWidth(16)// 注解用于设置列的宽度
@HeadRowHeight(14)// 注解用于设置表头行的高度
@HeadFontStyle(fontHeightInPoints = 11)// 注解用于设置表头行的字体样式
@Data
public class Item extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long itemId;

    /** 商品名称 */
    @Excel(name = "商品名称")
    @ExcelProperty("商品名称")
    private String itemName;

    /** 商品图片 */
    @Excel(name = "商品图片")
    @ExcelProperty("商品图片")
    private String itemImage;

    /** 品牌 */
    @Excel(name = "品牌")
    @ExcelProperty("品牌")
    private String brandName;

    /** 规格(净含量) */
    @Excel(name = "规格(净含量)")
    @ExcelProperty("规格(净含量)")
    private String unit;

    /** 商品价格，单位分 */
    @Excel(name = "商品价格，单位分")
    @ExcelProperty("商品价格，单位分")
    private Long price;

    /** 商品类型Id */
    @Excel(name = "商品类型Id")
    @ExcelProperty("商品类型Id")
    private Long classId;

    /** 是否打折促销 */
    private Integer isDiscount;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("itemId", getItemId())
            .append("itemName", getItemName())
            .append("itemImage", getItemImage())
            .append("brandName", getBrandName())
            .append("unit", getUnit())
            .append("price", getPrice())
            .append("classId", getClassId())
            .append("isDiscount", getIsDiscount())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
