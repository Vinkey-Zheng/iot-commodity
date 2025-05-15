package com.iot.common.core.page;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 * 
 * @author zmq
 */
@Data
public class TableDataInfo implements Serializable
{
    /** 总记录数 */
    private long total;

    /** 列表数据 */
    private List<?> rows;

    /** 消息状态码 */
    private int code;

    /** 消息内容 */
    private String msg;

    /**
     * 分页
     * 
     * @param list 列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<?> list, int total)
    {
        this.rows = list;
        this.total = total;
    }
}
