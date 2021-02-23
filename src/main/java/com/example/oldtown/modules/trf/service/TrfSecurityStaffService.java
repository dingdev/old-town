package com.example.oldtown.modules.trf.service;
import com.example.oldtown.modules.trf.model.TrfSecurityStaff;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;

import java.util.List;

/**
 * <p>
 * 交通接驳安保人员 服务类
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
public interface TrfSecurityStaffService extends IService<TrfSecurityStaff> {


    /**
    * 分页查询交通接驳安保人员
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String type, Integer ifCaptain, String keyword, Integer orderByCurrentStatus);

    /**
     * 根据id查询交通接驳安保人员
     */
    CommonResult getSecurityStaffById(Long id);

    /**
     * 增加交通接驳安保人员
     */
    CommonResult add(TrfSecurityStaff trfSecurityStaff);

    /**
     * 更新交通接驳安保人员
     */
    CommonResult update(TrfSecurityStaff trfSecurityStaff);
    /**
     * 删除交通接驳安保人员
     */
    CommonResult delete(Long id);

    /**
     * 批量删除交通接驳安保人员
     */
    CommonResult batchDelete(List<Long> ids);

    /**
     * 按类型统计交通接驳安保人员
     */
    CommonResult countByType();

}
