package com.iot.manage.service.impl;

import com.iot.manage.domain.Role;
import com.iot.manage.mapper.RoleMapper;
import com.iot.manage.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工单角色Service业务层处理
 * 
 * @author zmq
 * @date 2025-03-23
 */
@Service
public class RoleServiceImpl implements IRoleService 
{
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 查询工单角色
     * 
     * @param roleId 工单角色主键
     * @return 工单角色
     */
    @Override
    public Role selectRoleByRoleId(Long roleId)
    {
        return roleMapper.selectRoleByRoleId(roleId);
    }

    /**
     * 查询工单角色列表
     * 
     * @param role 工单角色
     * @return 工单角色
     */
    @Override
    public List<Role> selectRoleList(Role role)
    {
        return roleMapper.selectRoleList(role);
    }

    /**
     * 新增工单角色
     * 
     * @param role 工单角色
     * @return 结果
     */
    @Override
    public int insertRole(Role role)
    {
        return roleMapper.insertRole(role);
    }

    /**
     * 修改工单角色
     * 
     * @param role 工单角色
     * @return 结果
     */
    @Override
    public int updateRole(Role role)
    {
        return roleMapper.updateRole(role);
    }

    /**
     * 批量删除工单角色
     * 
     * @param roleIds 需要删除的工单角色主键
     * @return 结果
     */
    @Override
    public int deleteRoleByRoleIds(Long[] roleIds)
    {
        return roleMapper.deleteRoleByRoleIds(roleIds);
    }

    /**
     * 删除工单角色信息
     * 
     * @param roleId 工单角色主键
     * @return 结果
     */
    @Override
    public int deleteRoleByRoleId(Long roleId)
    {
        return roleMapper.deleteRoleByRoleId(roleId);
    }
}
