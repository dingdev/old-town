package com.example.oldtown.modules.trf.service;
import com.example.oldtown.modules.trf.model.TrfSecurityCheck;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;
import java.util.List;

/**
 * <p>
 * 交通接驳安保人员考核 服务类
 * </p>
 * @author dyp
 * @since 2020-12-28
 */
public interface TrfSecurityCheckService extends IService<TrfSecurityCheck> {


    /**
    * 分页查询交通接驳安保人员考核
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String keyword, String fromTime, String toTime);

    /**
     * 增加交通接驳安保人员考核
     */
    CommonResult add(TrfSecurityCheck trfSecurityCheck);

    /**
     * 更新交通接驳安保人员考核
     */
    CommonResult update(TrfSecurityCheck trfSecurityCheck);

    /**
     * 删除交通接驳安保人员考核
     */
    CommonResult delete(Long id);

    /**
     * 批量删除交通接驳安保人员考核
     */
    CommonResult batchDelete(List<Long> ids);
}
