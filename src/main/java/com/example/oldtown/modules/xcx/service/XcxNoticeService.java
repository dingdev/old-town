package com.example.oldtown.modules.xcx.service;
import com.example.oldtown.modules.xcx.model.XcxNotice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;
import java.util.List;

/**
 * <p>
 * 小程序公告 服务类
 * </p>
 * @author dyp
 * @since 2020-12-01
 */
public interface XcxNoticeService extends IService<XcxNotice> {


    /**
    * 分页查询小程序公告
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String keyword);

    /**
     * 增加小程序公告
     */
    CommonResult add(XcxNotice xcxNotice);

    /**
     * 更新小程序公告
     */
    CommonResult update(XcxNotice xcxNotice);

    /**
     * 删除小程序公告
     */
    CommonResult delete(Long id);

    /**
     * 批量删除小程序公告
     */
    CommonResult batchDelete(List<Long> ids);
}
