package com.example.oldtown.modules.com.service;
import com.example.oldtown.modules.com.model.ComSpecialty;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;

import java.util.List;

/**
 * <p>
 * 通用特产 服务类
 * </p>
 * @author dyp
 * @since 2020-12-01
 */
public interface ComSpecialtyService extends IService<ComSpecialty> {


    /**
    * 分页查询通用特产
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String type, String keyword);

    /**
     * 根据id查询通用特产
     */
    CommonResult getSpecialtyById(Long id);

    /**
     * 增加通用特产
     */
    CommonResult add(ComSpecialty comSpecialty);

    /**
     * 更新通用特产
     */
    CommonResult update(ComSpecialty comSpecialty);

    /**
     * 删除通用特产
     */
    CommonResult delete(Long id);

    /**
     * 批量删除通用特产
     */
    CommonResult batchDelete(List<Long> ids);
}
