package com.example.oldtown.modules.com.service;
import com.example.oldtown.modules.com.model.ComQa;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;

import java.util.List;

/**
 * <p>
 * 通用问答 服务类
 * </p>
 * @author dyp
 * @since 2020-11-13
 */
public interface ComQaService extends IService<ComQa> {


    /**
    * 分页查询通用问答
    */
    CommonResult getAll(Integer pageSize, Integer pageNum,String type, String keyword);

    /**
     * 增加通用问答
     */
    CommonResult add(ComQa comQa);

    /**
     * 更新通用问答
     */
    CommonResult update(ComQa comQa);
    /**
     * 删除通用问答
     */
    CommonResult delete(Long id);

    /**
     * 批量删除通用问答
     */
    CommonResult batchDelete(List<Long> ids);

    /**
     * 根据问查询答
     */
    CommonResult getAnswerByQuestion(String question);
}
