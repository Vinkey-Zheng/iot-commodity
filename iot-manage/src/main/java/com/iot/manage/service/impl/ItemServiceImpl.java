package com.iot.manage.service.impl;

import com.iot.common.exception.ServiceException;
import com.iot.common.utils.DateUtils;
import com.iot.manage.domain.Item;
import com.iot.manage.mapper.ItemMapper;
import com.iot.manage.service.IChannelService;
import com.iot.manage.service.IitemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品管理Service业务层处理
 *
 * @author zmq
 * @date 2025-03-23
 */
@Service
public class ItemServiceImpl implements IitemService
{
    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private IChannelService channelService;

    /**
     * 查询商品管理
     *
     * @param itemId 商品管理主键
     * @return 商品管理
     */
    @Override
    public Item selectItemByItemId(Long itemId)
    {
        return itemMapper.selectItemByItemId(itemId);
    }

    /**
     * 查询商品管理列表
     *
     * @param item 商品管理
     * @return 商品管理
     */
    @Override
    public List<Item> selectItemList(Item item)
    {
        return itemMapper.selectItemList(item);
    }

    /**
     * 新增商品管理
     *
     * @param item 商品管理
     * @return 结果
     */
    @Override
    public int insertItem(Item item)
    {
        item.setCreateTime(DateUtils.getNowDate());
        return itemMapper.insertItem(item);
    }

    /**
     * 修改商品管理
     *
     * @param item 商品管理
     * @return 结果
     */
    @Override
    public int updateItem(Item item)
    {
        item.setUpdateTime(DateUtils.getNowDate());
        return itemMapper.updateItem(item);
    }

    /**
     * 批量删除商品管理
     *
     * @param itemIds 需要删除的商品管理主键
     * @return 结果
     */
    @Override
    public int deleteItemByItemIds(Long[] itemIds)
    {   //1. 判断商品的id集合是否有关联货道
        int count = channelService.countChannelByItemIds(itemIds);
        if(count>0){
            throw new ServiceException("此商品被货道关联，无法删除");
        }
        //2. 没有关联货道才能删除
        return itemMapper.deleteItemByItemIds(itemIds);
    }

    /**
     * 删除商品管理信息
     *
     * @param itemId 商品管理主键
     * @return 结果
     */
    @Override
    public int deleteItemByItemId(Long itemId)
    {
        return itemMapper.deleteItemByItemId(itemId);
    }

    /**
    * 批量新增商品管理
    * @param itemList
    * @return 结果
    */
    @Override
    public int insertItems(List<Item> itemList) {
    return itemMapper.insertItems(itemList);
}
}
