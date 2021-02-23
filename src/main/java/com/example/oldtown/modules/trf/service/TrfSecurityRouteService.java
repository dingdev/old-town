package com.example.oldtown.modules.trf.service;
import com.example.oldtown.modules.trf.model.TrfSecurityRoute;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;

import java.util.List;

/**
 * <p>
 * 交通接驳安保人员标准路线 服务类
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
public interface TrfSecurityRouteService extends IService<TrfSecurityRoute> {


    /**
    * 分页查询交通接驳安保人员标准路线
    */
    CommonResult getAll(Integer pageSize, Integer pageNum,String type, String keyword);

    /**
     * 增加交通接驳安保人员标准路线
     */
    CommonResult add(TrfSecurityRoute trfSecurityRoute);

    /**
     * 更新交通接驳安保人员标准路线
     */
    CommonResult update(TrfSecurityRoute trfSecurityRoute);
    /**
     * 删除交通接驳安保人员标准路线
     */
    CommonResult delete(Long id);

    /**
     * 批量删除交通接驳安保人员标准路线
     */
    CommonResult batchDelete(List<Long> ids);

}
