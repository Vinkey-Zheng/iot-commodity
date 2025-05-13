package com.iot.manage.domain.vo;

import com.iot.manage.domain.Channel;
import com.iot.manage.domain.Sku;
import lombok.Data;

@Data
public class ChannelVo extends Channel {

    // 商品对象
    private Sku sku;
}
