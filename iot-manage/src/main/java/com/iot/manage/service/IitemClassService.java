package com.iot.manage.service;

import com.iot.manage.domain.ItemClass;

import java.util.List;

/**
 * 商品类型Service接口
 * 
 * @author itheima
 * @date 2024-07-15
 */
public interface IitemClassService
{
    /**
     * 查询商品类型
     * 
     * @param classId 商品类型主键
     * @return 商品类型
     */
    public ItemClass selectItemClassByClassId(Long classId);

    /**
     * 查询商品类型列表
     * 
     * @param itemClass 商品类型
     * @return 商品类型集合
     */
    public List<ItemClass> selectItemClassList(ItemClass itemClass);

    /**
     * 新增商品类型
     * 
     * @param itemClass 商品类型
     * @return 结果
     */
    public int insertItemClass(ItemClass itemClass);

    /**
     * 修改商品类型
     * 
     * @param itemClass 商品类型
     * @return 结果
     */
    public int updateItemClass(ItemClass itemClass);

    /**
     * 批量删除商品类型
     * 
     * @param classIds 需要删除的商品类型主键集合
     * @return 结果
     */
    public int deleteItemClassByClassIds(Long[] classIds);

    /**
     * 删除商品类型信息
     * 
     * @param classId 商品类型主键
     * @return 结果
     */
    public int deleteItemClassByClassId(Long classId);
}
