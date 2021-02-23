package com.example.oldtown.modules.trf.service;
import com.example.oldtown.modules.trf.model.TrfSweepCheck;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;
import java.util.List;

/**
 * <p>
 * 交通接驳打捞船员工考核 服务类
 * </p>
 * @author dyp
 * @since 2020-12-28
 */
public interface TrfSweepCheckService extends IService<TrfSweepCheck> {


    /**
    * 分页查询交通接驳打捞船员工考核
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String keyword, String fromTime, String toTime);

    /**
     * 增加交通接驳打捞船员工考核
     */
    CommonResult add(TrfSweepCheck trfSweepCheck);

    /**
     * 更新交通接驳打捞船员工考核
     */
    CommonResult update(TrfSweepCheck trfSweepCheck);

    /**
     * 删除交通接驳打捞船员工考核
     */
    CommonResult delete(Long id);

    /**
     * 批量删除交通接驳打捞船员工考核
     */
    CommonResult batchDelete(List<Long> ids);
}
