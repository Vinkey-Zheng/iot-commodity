package com.iot.manage.domain.vo;

import com.iot.manage.domain.Region;
import lombok.Data;

@Data
public class RegionVo extends Region {

    // 点位数量
    private Integer nodeCount;

}
