package com.iot.manage.controller;

import com.iot.common.annotation.Log;
import com.iot.common.core.controller.BaseController;
import com.iot.common.core.domain.AjaxResult;
import com.iot.common.core.page.TableDataInfo;
import com.iot.common.enums.BusinessType;
import com.iot.common.utils.poi.ExcelUtil;
import com.iot.manage.domain.ItemClass;
import com.iot.manage.service.IitemClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 商品类型Controller
 *
 * @author zmq
 * @date 2025-03-21
 */
@RestController
@RequestMapping("/manage/itemClass")
public class ItemClassController extends BaseController
{
    @Autowired
    private IitemClassService itemClassService;

    /**
     * 查询商品类型列表
     */
    @PreAuthorize("@ss.hasPermi('manage:itemClass:list')")
    @GetMapping("/list")
    public TableDataInfo list(ItemClass itemClass)
    {
        startPage();
        List<ItemClass> list = itemClassService.selectItemClassList(itemClass);
        return getDataTable(list);
    }

    /**
     * 导出商品类型列表
     */
    @PreAuthorize("@ss.hasPermi('manage:itemClass:export')")
    @Log(title = "商品类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ItemClass itemClass)
    {
        List<ItemClass> list = itemClassService.selectItemClassList(itemClass);
        ExcelUtil<ItemClass> util = new ExcelUtil<ItemClass>(ItemClass.class);
        util.exportExcel(response, list, "商品类型数据");
    }

    /**
     * 获取商品类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:itemClass:query')")
    @GetMapping(value = "/{classId}")
    public AjaxResult getInfo(@PathVariable("classId") Long classId)
    {
        return success(itemClassService.selectItemClassByClassId(classId));
    }

    /**
     * 新增商品类型
     */
    @PreAuthorize("@ss.hasPermi('manage:itemClass:add')")
    @Log(title = "商品类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ItemClass itemClass)
    {
        return toAjax(itemClassService.insertItemClass(itemClass));
    }

    /**
     * 修改商品类型
     */
    @PreAuthorize("@ss.hasPermi('manage:itemClass:edit')")
    @Log(title = "商品类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ItemClass itemClass)
    {
        return toAjax(itemClassService.updateItemClass(itemClass));
    }

    /**
     * 删除商品类型
     */
    @PreAuthorize("@ss.hasPermi('manage:itemClass:remove')")
    @Log(title = "商品类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{classIds}")
    public AjaxResult remove(@PathVariable Long[] classIds)
    {
        return toAjax(itemClassService.deleteItemClassByClassIds(classIds));
    }
}
