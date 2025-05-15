package com.iot.manage.service.impl;

import com.iot.common.utils.DateUtils;
import com.iot.manage.domain.Region;
import com.iot.manage.domain.vo.RegionVo;
import com.iot.manage.mapper.EmpMapper;
import com.iot.manage.mapper.RegionMapper;
import com.iot.manage.service.IRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 区域管理Service业务层处理
 *
 * @author zmq
 * @date 2025-03-21
 */
@Service
public class RegionServiceImpl implements IRegionService
{
    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private EmpMapper empMapper;

    /**
     * 查询区域管理
     *
     * @param id 区域管理主键
     * @return 区域管理
     */
    @Override
    public Region selectRegionById(Long id)
    {
        return regionMapper.selectRegionById(id);
    }

    /**
     * 查询区域管理列表
     *
     * @param region 区域管理
     * @return 区域管理
     */
    @Override
    public List<Region> selectRegionList(Region region)
    {
        return regionMapper.selectRegionList(region);
    }

    /**
     * 新增区域管理
     *
     * @param region 区域管理
     * @return 结果
     */
    /**
     * 开启事务管理，在出现指定异常时回滚（此处配置为所有Exception及其子类异常触发回滚）
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertRegion(Region region)
    {
        region.setCreateTime(DateUtils.getNowDate());
        return regionMapper.insertRegion(region);
    }

    /**
     * 修改区域管理
     *
     * @param region 区域管理
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateRegion(Region region)
    {
        // 先更新区域信息
        region.setUpdateTime(DateUtils.getNowDate());
        int result = regionMapper.updateRegion(region);

        // 同步更新员工表区域名称
        empMapper.updateByRegionId(region.getRegionName(),region.getId());
        return result;
    }

    /**
     * 批量删除区域管理
     *
     * @param ids 需要删除的区域管理主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteRegionByIds(Long[] ids)
    {
        return regionMapper.deleteRegionByIds(ids);
    }

    /**
     * 删除区域管理信息
     *
     * @param id 区域管理主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteRegionById(Long id)
    {
        return regionMapper.deleteRegionById(id);
    }

    /**
     * 查询区域列表
     * @param region
     * @return RegionVo集合
     */

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<RegionVo> selectRegionVoList(Region region){return regionMapper.selectRegionVoList(region);}
}
