package com.iot.manage.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChannelConfigDto {

    private String innerCode;// 售货机编号
    private List<ChannelItemDto> channelList;// 货道Dto集合
}
