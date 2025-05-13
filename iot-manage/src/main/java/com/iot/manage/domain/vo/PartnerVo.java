package com.iot.manage.domain.vo;

import com.iot.manage.domain.Partner;
import lombok.Data;

@Data
public class PartnerVo extends Partner {

    // 点位数量
    private Integer nodeCount;
}
