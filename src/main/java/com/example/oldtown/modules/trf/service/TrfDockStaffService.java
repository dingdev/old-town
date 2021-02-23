package com.example.oldtown.modules.trf.service;
import com.example.oldtown.modules.trf.model.TrfDockStaff;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;
import java.util.List;

/**
 * <p>
 * 交通接驳管理人员 服务类
 * </p>
 * @author dyp
 * @since 2020-12-07
 */
public interface TrfDockStaffService extends IService<TrfDockStaff> {


    /**
    * 分页查询交通接驳管理人员
    */
    CommonResult getAll(Integer pageSize, Integer pageNum,String type, String keyword);

    /**
     * 增加交通接驳管理人员
     */
    CommonResult add(TrfDockStaff trfDockStaff);

    /**
     * 更新交通接驳管理人员
     */
    CommonResult update(TrfDockStaff trfDockStaff);

    /**
     * 删除交通接驳管理人员
     */
    CommonResult delete(Long id);

    /**
     * 批量删除交通接驳管理人员
     */
    CommonResult batchDelete(List<Long> ids);

    /**
     * 按类型统计交通接驳管理人员
     */
    CommonResult countByType();
}
