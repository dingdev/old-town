package com.example.oldtown.modules.trf.service;
import com.example.oldtown.modules.trf.model.TrfSecurityCar;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;

import java.util.List;

/**
 * <p>
 * 交通接驳安保车辆 服务类
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
public interface TrfSecurityCarService extends IService<TrfSecurityCar> {


    /**
    * 分页查询交通接驳安保车辆
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String keyword,Integer orderByCurrentStatus);

    /**
     * 根据id查询交通接驳安保车辆
     */
    CommonResult getSecurityCarById(Long id);

    /**
     * 增加交通接驳安保车辆
     */
    CommonResult add(TrfSecurityCar trfSecurityCar);

    /**
     * 更新交通接驳安保车辆
     */
    CommonResult update(TrfSecurityCar trfSecurityCar);
    /**
     * 删除交通接驳安保车辆
     */
    CommonResult delete(Long id);

    /**
     * 批量删除交通接驳安保车辆
     */
    CommonResult batchDelete(List<Long> ids);

    /**
     * 统计交通接驳安保车辆数量
     */
    CommonResult countByType();
}
