package com.iot.manage.controller;

import com.iot.common.annotation.Log;
import com.iot.common.core.controller.BaseController;
import com.iot.common.core.domain.AjaxResult;
import com.iot.common.core.page.TableDataInfo;
import com.iot.common.enums.BusinessType;
import com.iot.common.utils.poi.ExcelUtil;
import com.iot.manage.domain.Role;
import com.iot.manage.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 工单角色Controller
 *
 * @author zmq
 * @date 2025-03-21
 */
@RestController
@RequestMapping("/manage/role")
public class RoleController extends BaseController
{
    @Autowired
    private IRoleService roleService;

    /**
     * 查询工单角色列表
     */
    @PreAuthorize("@ss.hasPermi('manage:role:list')")
    @GetMapping("/list")
    public TableDataInfo list(Role role)
    {
        startPage();
        List<Role> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    /**
     * 导出工单角色列表
     */
    @PreAuthorize("@ss.hasPermi('manage:role:export')")
    @Log(title = "工单角色", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Role role)
    {
        List<Role> list = roleService.selectRoleList(role);
        ExcelUtil<Role> util = new ExcelUtil<Role>(Role.class);
        util.exportExcel(response, list, "工单角色数据");
    }

    /**
     * 获取工单角色详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:role:query')")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable("roleId") Long roleId)
    {
        return success(roleService.selectRoleByRoleId(roleId));
    }

    /**
     * 新增工单角色
     */
    @PreAuthorize("@ss.hasPermi('manage:role:add')")
    @Log(title = "工单角色", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Role role)
    {
        return toAjax(roleService.insertRole(role));
    }

    /**
     * 修改工单角色
     */
    @PreAuthorize("@ss.hasPermi('manage:role:edit')")
    @Log(title = "工单角色", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Role role)
    {
        return toAjax(roleService.updateRole(role));
    }

    /**
     * 删除工单角色
     */
    @PreAuthorize("@ss.hasPermi('manage:role:remove')")
    @Log(title = "工单角色", businessType = BusinessType.DELETE)
	@DeleteMapping("/{roleIds}")
    public AjaxResult remove(@PathVariable Long[] roleIds)
    {
        return toAjax(roleService.deleteRoleByRoleIds(roleIds));
    }
}
