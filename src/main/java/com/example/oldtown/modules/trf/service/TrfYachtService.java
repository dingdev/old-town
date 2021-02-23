package com.example.oldtown.modules.trf.service;
import com.example.oldtown.modules.trf.model.TrfYacht;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;

import java.util.List;

/**
 * <p>
 * 交通接驳游船 服务类
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
public interface TrfYachtService extends IService<TrfYacht> {


    /**
    * 分页查询交通接驳游船
    */
    CommonResult getAll(Integer pageSize, Integer pageNum,String type, String keyword,Integer orderByCurrentStatus);

    /**
     * 根据id查询交通接驳游船
     */
    CommonResult getYachtById(Long id);

    /**
     * 增加交通接驳游船
     */
    CommonResult add(TrfYacht trfYacht);

    /**
     * 更新交通接驳游船
     */
    CommonResult update(TrfYacht trfYacht);
    /**
     * 删除交通接驳游船
     */
    CommonResult delete(Long id);

    /**
     * 批量删除交通接驳游船
     */
    CommonResult batchDelete(List<Long> ids);

    /**
     * 统计交通接驳游船数量
     */
    CommonResult countByType();

}
