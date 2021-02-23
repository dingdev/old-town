package com.example.oldtown.modules.xcx.service;
import com.example.oldtown.modules.xcx.model.XcxNews;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;
import java.util.List;

/**
 * <p>
 * 小程序资讯 服务类
 * </p>
 * @author dyp
 * @since 2020-12-03
 */
public interface XcxNewsService extends IService<XcxNews> {


    /**
    * 分页查询小程序资讯
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String keyword);

    /**
     * 增加小程序资讯
     */
    CommonResult add(XcxNews xcxNews);

    /**
     * 更新小程序资讯
     */
    CommonResult update(XcxNews xcxNews);

    /**
     * 删除小程序资讯
     */
    CommonResult delete(Long id);

    /**
     * 批量删除小程序资讯
     */
    CommonResult batchDelete(List<Long> ids);
}
