package com.iot.manage.service.impl;

import com.iot.manage.domain.ItemClass;
import com.iot.manage.mapper.ItemClassMapper;
import com.iot.manage.service.IitemClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品类型Service业务层处理
 * 
 * @author zmq
 * @date 2025-03-23
 */
@Service
public class ItemClassServiceImpl implements IitemClassService
{
    @Autowired
    private ItemClassMapper itemClassMapper;

    /**
     * 查询商品类型
     * 
     * @param classId 商品类型主键
     * @return 商品类型
     */
    @Override
    public ItemClass selectItemClassByClassId(Long classId)
    {
        return itemClassMapper.selectItemClassByClassId(classId);
    }

    /**
     * 查询商品类型列表
     * 
     * @param itemClass 商品类型
     * @return 商品类型
     */
    @Override
    public List<ItemClass> selectItemClassList(ItemClass itemClass)
    {
        return itemClassMapper.selectItemClassList(itemClass);
    }

    /**
     * 新增商品类型
     * 
     * @param itemClass 商品类型
     * @return 结果
     */
    @Override
    public int insertItemClass(ItemClass itemClass)
    {
        return itemClassMapper.insertItemClass(itemClass);
    }

    /**
     * 修改商品类型
     * 
     * @param itemClass 商品类型
     * @return 结果
     */
    @Override
    public int updateItemClass(ItemClass itemClass)
    {
        return itemClassMapper.updateItemClass(itemClass);
    }

    /**
     * 批量删除商品类型
     * 
     * @param classIds 需要删除的商品类型主键
     * @return 结果
     */
    @Override
    public int deleteItemClassByClassIds(Long[] classIds)
    {
        return itemClassMapper.deleteItemClassByClassIds(classIds);
    }

    /**
     * 删除商品类型信息
     * 
     * @param classId 商品类型主键
     * @return 结果
     */
    @Override
    public int deleteItemClassByClassId(Long classId)
    {
        return itemClassMapper.deleteItemClassByClassId(classId);
    }
}
