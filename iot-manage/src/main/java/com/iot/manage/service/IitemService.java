package com.iot.manage.service;

import com.iot.manage.domain.Item;

import java.util.List;

/**
 * 商品管理Service接口
 *
 * @author itheima
 * @date 2024-07-15
 */
public interface IitemService
{
    /**
     * 查询商品管理
     *
     * @param itemId 商品管理主键
     * @return 商品管理
     */
    public Item selectItemByItemId(Long itemId);

    /**
     * 查询商品管理列表
     *
     * @param item 商品管理
     * @return 商品管理集合
     */
    public List<Item> selectItemList(Item item);

    /**
     * 新增商品管理
     *
     * @param item 商品管理
     * @return 结果
     */
    public int insertItem(Item item);

    /**
     * 批量新增商品管理
     * @param itemList
     * @return 结果
     */
    public int insertItems(List<Item> itemList);

    /**
     * 修改商品管理
     *
     * @param item 商品管理
     * @return 结果
     */
    public int updateItem(Item item);

    /**
     * 批量删除商品管理
     *
     * @param itemIds 需要删除的商品管理主键集合
     * @return 结果
     */
    public int deleteItemByItemIds(Long[] itemIds);

    /**
     * 删除商品管理信息
     *
     * @param itemId 商品管理主键
     * @return 结果
     */
    public int deleteItemByItemId(Long itemId);


}
