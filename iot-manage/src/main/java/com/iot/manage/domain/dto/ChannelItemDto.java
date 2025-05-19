package com.iot.manage.domain.dto;

import lombok.Data;

@Data
public class ChannelItemDto {

    private String innerCode;// 售货机编号
    private String channelCode;// 货道编号
    private Long itemId;// 商品ID
}
