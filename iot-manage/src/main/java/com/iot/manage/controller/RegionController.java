package com.iot.manage.controller;

import com.iot.common.annotation.Log;
import com.iot.common.core.controller.BaseController;
import com.iot.common.core.domain.AjaxResult;
import com.iot.common.core.page.TableDataInfo;
import com.iot.common.enums.BusinessType;
import com.iot.common.utils.poi.ExcelUtil;
import com.iot.manage.domain.Region;
import com.iot.manage.domain.vo.RegionVo;
import com.iot.manage.service.IRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 区域管理Controller
 *
 * @author zmq
 * @date 2025-03-20
 */
@RestController
@RequestMapping("/manage/region")
public class RegionController extends BaseController
{
    @Autowired
    private IRegionService regionService;

    /**
     * 查询区域管理列表
     */
    @PreAuthorize("@ss.hasPermi('manage:region:list')")
    @GetMapping("/list")
    public TableDataInfo list(Region region)
    {
        startPage();
        List<RegionVo> voList = regionService.selectRegionVoList(region);
        return getDataTable(voList);
    }

    /**
     * 导出区域管理列表
     */
    @PreAuthorize("@ss.hasPermi('manage:region:export')")
    @Log(title = "区域管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Region region)
    {
        List<RegionVo> list = regionService.selectRegionVoList(region);
        ExcelUtil<RegionVo> util = new ExcelUtil<RegionVo>(RegionVo.class);
        util.exportExcel(response, list, "区域管理数据");
    }

    /**
     * 获取区域管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:region:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(regionService.selectRegionById(id));
    }

    /**
     * 新增区域管理
     */
    @PreAuthorize("@ss.hasPermi('manage:region:add')")
    @Log(title = "区域管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Region region)
    {
        return toAjax(regionService.insertRegion(region));
    }

    /**
     * 修改区域管理
     */
    @PreAuthorize("@ss.hasPermi('manage:region:edit')")
    @Log(title = "区域管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Region region)
    {
        return toAjax(regionService.updateRegion(region));
    }

    /**
     * 删除区域管理
     */
    @PreAuthorize("@ss.hasPermi('manage:region:remove')")
    @Log(title = "区域管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(regionService.deleteRegionByIds(ids));
    }
}
