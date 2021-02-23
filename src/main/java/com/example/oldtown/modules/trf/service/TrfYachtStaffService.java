package com.example.oldtown.modules.trf.service;
import com.example.oldtown.modules.trf.model.TrfYachtStaff;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;

import java.util.List;

/**
 * <p>
 * 交通接驳游船员工 服务类
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
public interface TrfYachtStaffService extends IService<TrfYachtStaff> {


    /**
    * 分页查询交通接驳游船员工
    */
    CommonResult getAll(Integer pageSize, Integer pageNum,String type, String keyword);

    /**
     * 根据id查询交通接驳游船员工
     */
    CommonResult getYachtStaffById(Long id);


    /**
     * 增加交通接驳游船员工
     */
    CommonResult add(TrfYachtStaff trfYachtStaff);

    /**
     * 更新交通接驳游船员工
     */
    CommonResult update(TrfYachtStaff trfYachtStaff);
    /**
     * 删除交通接驳游船员工
     */
    CommonResult delete(Long id);

    /**
     * 批量删除交通接驳游船员工
     */
    CommonResult batchDelete(List<Long> ids);

    /**
     * 按类型统计交通接驳游船员工
     */
    CommonResult countByType();

}
