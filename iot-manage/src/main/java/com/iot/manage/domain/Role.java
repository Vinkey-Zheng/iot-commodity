package com.iot.manage.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.iot.common.annotation.Excel;
import com.iot.common.core.domain.BaseEntity;

/**
 * 工单角色对象 tb_role
 *
 * @author zmq
 * @date 2025-03-20
 */
@Data
public class Role extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long roleId;

    /** 角色编码
 */
    @Excel(name = "角色编码")
    private String roleCode;

    /** 角色名称
 */
    @Excel(name = "角色名称")
    private String roleName;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("roleCode", getRoleCode())
            .append("roleName", getRoleName())
            .toString();
    }
}
