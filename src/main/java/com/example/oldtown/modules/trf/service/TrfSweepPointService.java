package com.example.oldtown.modules.trf.service;
import com.example.oldtown.modules.trf.model.TrfSweepPoint;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;
import java.util.List;

/**
 * <p>
 * 交通接驳打捞点 服务类
 * </p>
 * @author dyp
 * @since 2020-12-28
 */
public interface TrfSweepPointService extends IService<TrfSweepPoint> {


    /**
    * 分页查询交通接驳打捞点
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String keyword);

    /**
     * 增加交通接驳打捞点
     */
    CommonResult add(TrfSweepPoint trfSweepPoint);

    /**
     * 更新交通接驳打捞点
     */
    CommonResult update(TrfSweepPoint trfSweepPoint);

    /**
     * 删除交通接驳打捞点
     */
    CommonResult delete(Long id);

    /**
     * 批量删除交通接驳打捞点
     */
    CommonResult batchDelete(List<Long> ids);
}
