package com.example.oldtown.modules.trf.service;
import com.example.oldtown.modules.trf.model.TrfMileage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;
import java.util.List;

/**
 * <p>
 * 交通接驳行驶里程 服务类
 * </p>
 * @author dyp
 * @since 2021-01-14
 */
public interface TrfMileageService extends IService<TrfMileage> {


    /**
    * 分页查询交通接驳行驶里程
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String type, String keyword, String fromTime, String toTime);

    /**
     * 增加交通接驳行驶里程
     */
    CommonResult add(TrfMileage trfMileage);

    /**
     * 更新交通接驳行驶里程
     */
    CommonResult update(TrfMileage trfMileage);

    /**
     * 删除交通接驳行驶里程
     */
    CommonResult delete(Long id);

    /**
     * 批量删除交通接驳行驶里程
     */
    CommonResult batchDelete(List<Long> ids);
}
