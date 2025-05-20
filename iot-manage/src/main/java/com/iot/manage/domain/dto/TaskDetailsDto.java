package com.iot.manage.domain.dto;

import lombok.Data;

@Data
public class TaskDetailsDto {

    private String channelCode;// 货道编号
    private Long expectCapacity;// 补货数量
    private Long itemId;// 商品id
    private String itemName;// 商品名称
    private String itemImage;// 商品图片
}
