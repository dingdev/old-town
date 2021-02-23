package com.example.oldtown.modules.trf.service;
import com.example.oldtown.modules.trf.model.TrfSweep;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;

import java.util.List;

/**
 * <p>
 * 交通接驳打捞船 服务类
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
public interface TrfSweepService extends IService<TrfSweep> {


    /**
    * 分页查询交通接驳打捞船
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String type, String keyword,Integer orderByCurrentStatus);

    /**
     * 根据id查询交通接驳打捞船
     */
    CommonResult getSweepById(Long id);

    /**
     * 增加交通接驳打捞船
     */
    CommonResult add(TrfSweep trfSweep);

    /**
     * 更新交通接驳打捞船
     */
    CommonResult update(TrfSweep trfSweep);
    /**
     * 删除交通接驳打捞船
     */
    CommonResult delete(Long id);

    /**
     * 批量删除交通接驳打捞船
     */
    CommonResult batchDelete(List<Long> ids);

    /**
     * 按类型统计交通接驳打捞船数量
     */
    CommonResult countByType();

    /**
     * 按当前状态统计交通接驳打捞船数量
     */
    CommonResult countByCurrentStatus();

}
