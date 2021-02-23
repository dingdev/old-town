package com.example.oldtown.modules.xcx.service;
import com.example.oldtown.modules.xcx.model.XcxFeedback;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;

/**
 * <p>
 * 小程序用户反馈 服务类
 * </p>
 * @author dyp
 * @since 2020-11-13
 */
public interface XcxFeedbackService extends IService<XcxFeedback> {


    /**
    * 小程序用户分页查询本人反馈
    */
    Page<XcxFeedback> getSelf(Integer pageSize, Integer pageNum, Long userId, Integer status, String type);
    /**
     * 小程序用户根据id查询本人反馈
     */
    XcxFeedback getFeedbackById(Long id,Long userId);
    /**
     * 增加小程序用户反馈
     */
    CommonResult add(XcxFeedback xcxFeedback);

    /**
     * 更新小程序用户反馈
     */
    CommonResult update(XcxFeedback xcxFeedback);
    /**
     * 删除小程序用户反馈
     */
    CommonResult delete(Long id,Long userId);

    /**
     * 管理员分页查询反馈
     */
    CommonResult getAll(Integer pageSize, Integer pageNum,Integer status, String type);

    /**
     * 管理员更新反馈状态
     */
    CommonResult updateStatus(Long id, Integer status);

}
