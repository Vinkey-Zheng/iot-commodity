package com.iot.manage.domain;

import com.iot.common.annotation.Excel;
import com.iot.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 人员列表对象 tb_emp
 *
 * @author zmq
 * @date 2025-03-20
 */
@Data
public class Emp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 人员名称 */
    @Excel(name = "人员名称")
    private String userName;

    /** 所属区域Id */
    private Long regionId;

    /** 归属区域 */
    @Excel(name = "归属区域")
    private String regionName;

    /** 角色id */
    private Long roleId;

    /** 角色编号 */
    private String roleCode;

    /** 角色名称 */
    @Excel(name = "角色名称")
    private String roleName;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String mobile;

    /** 员工头像 */
    private String image;

    /** 是否启用 */
    private Long status;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userName", getUserName())
            .append("regionId", getRegionId())
            .append("regionName", getRegionName())
            .append("roleId", getRoleId())
            .append("roleCode", getRoleCode())
            .append("roleName", getRoleName())
            .append("mobile", getMobile())
            .append("image", getImage())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
