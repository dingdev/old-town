package com.example.oldtown.modules.com.service;
import com.example.oldtown.modules.com.model.ComActivity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;

import java.util.List;

/**
 * <p>
 * 通用活动演出 服务类
 * </p>
 * @author dyp
 * @since 2020-11-24
 */
public interface ComActivityService extends IService<ComActivity> {


    /**
    * 分页查询通用活动演出
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String keyword);

    /**
     * 增加通用活动演出
     */
    CommonResult add(ComActivity comActivity);

    /**
     * 更新通用活动演出
     */
    CommonResult update(ComActivity comActivity);
    /**
     * 删除通用活动演出
     */
    CommonResult delete(Long id);

    /**
     * 批量删除通用活动演出
     */
    CommonResult batchDelete(List<Long> ids);

}
