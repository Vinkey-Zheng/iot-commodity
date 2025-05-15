package com.iot.manage.domain.vo;

import lombok.Data;
import com.iot.manage.domain.Region;

/**
 * RegionVo 是 Region 对象的扩展，用于表示带有额外视图数据（如点位数量）的区域信息。
 * 通常用于前端展示或接口响应中，避免直接暴露数据库实体对象。
 */

@Data
public class RegionVo extends Region {

    /* 点位数量 */
    private Integer nodeCount;

}
