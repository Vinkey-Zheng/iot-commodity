package com.iot.manage.domain.vo;

import com.iot.manage.domain.Node;
import com.iot.manage.domain.Partner;
import com.iot.manage.domain.Region;
import lombok.Data;

@Data
public class NodeVo extends Node {

    // 设备数量
    private Integer vmCount;

    /**
     * 这里用到了别的实体类，所以在xml文件中要用association标签来关联
     * 这里的column属性是指数据库中的字段名，property属性是指实体类中的属性名
     */
    // 区域信息
    private Region region;

    // 合作商信息
    private Partner partner;
}
