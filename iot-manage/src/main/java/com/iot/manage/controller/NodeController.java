package com.iot.manage.controller;

import com.iot.common.annotation.Log;
import com.iot.common.core.controller.BaseController;
import com.iot.common.core.domain.AjaxResult;
import com.iot.common.core.page.TableDataInfo;
import com.iot.common.enums.BusinessType;
import com.iot.common.utils.poi.ExcelUtil;
import com.iot.manage.domain.Node;
import com.iot.manage.domain.vo.NodeVo;
import com.iot.manage.service.INodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 点位管理Controller
 *
 * @author zmq
 * @date 2025-03-21
 */
@RestController
@RequestMapping("/manage/node")
public class NodeController extends BaseController
{
    @Autowired
    private INodeService nodeService;

    /**
     * 查询点位管理列表
     */
    @PreAuthorize("@ss.hasPermi('manage:node:list')")
    @GetMapping("/list")
    public TableDataInfo list(Node node)
    {
        startPage();
        List<NodeVo> voList = nodeService.selectNodeVoList(node);
        return getDataTable(voList);
    }

    /**
     * 导出点位管理列表
     */
    @PreAuthorize("@ss.hasPermi('manage:node:export')")
    @Log(title = "点位管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Node node)
    {
        List<NodeVo> voList = nodeService.selectNodeVoList(node);
        ExcelUtil<NodeVo> util = new ExcelUtil<NodeVo>(NodeVo.class);
        util.exportExcel(response, voList, "点位管理数据");
    }

    /**
     * 获取点位管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:node:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(nodeService.selectNodeById(id));
    }

    /**
     * 新增点位管理
     */
    @PreAuthorize("@ss.hasPermi('manage:node:add')")
    @Log(title = "点位管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Node node)
    {
        return toAjax(nodeService.insertNode(node));
    }

    /**
     * 修改点位管理
     */
    @PreAuthorize("@ss.hasPermi('manage:node:edit')")
    @Log(title = "点位管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Node node)
    {
        return toAjax(nodeService.updateNode(node));
    }

    /**
     * 删除点位管理
     */
    @PreAuthorize("@ss.hasPermi('manage:node:remove')")
    @Log(title = "点位管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(nodeService.deleteNodeByIds(ids));
    }
}
