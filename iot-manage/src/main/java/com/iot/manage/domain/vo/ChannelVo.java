package com.iot.manage.domain.vo;

import com.iot.manage.domain.Channel;
import com.iot.manage.domain.Item;
import lombok.Data;

@Data
public class ChannelVo extends Channel {

    // 商品对象
    private Item item;
}
