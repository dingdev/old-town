package com.example.oldtown.modules.com.service;
import com.example.oldtown.modules.com.model.ComTel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;
import java.util.List;

/**
 * <p>
 * 通用电话 服务类
 * </p>
 * @author dyp
 * @since 2021-01-29
 */
public interface ComTelService extends IService<ComTel> {


    /**
    * 分页查询通用电话
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String keyword);

    /**
     * 增加通用电话
     */
    CommonResult add(ComTel comTel);

    /**
     * 更新通用电话
     */
    CommonResult update(ComTel comTel);

    /**
     * 删除通用电话
     */
    CommonResult delete(Long id);

    /**
     * 批量删除通用电话
     */
    CommonResult batchDelete(List<Long> ids);
}
