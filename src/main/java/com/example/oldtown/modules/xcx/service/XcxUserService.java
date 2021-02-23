package com.example.oldtown.modules.xcx.service;
import com.example.oldtown.dto.WechatLoginRequest;
import com.example.oldtown.modules.xcx.model.XcxUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;

import java.util.List;

/**
 * <p>
 * 小程序用户 服务类
 * </p>
 * @author dyp
 * @since 2020-11-13
 */
public interface XcxUserService extends IService<XcxUser> {


    /**
    * 分页查询小程序用户
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String keyword);

    /**
     * 增加小程序用户
     */
    CommonResult add(XcxUser xcxUser);

    /**
     * 更新小程序用户
     */
    CommonResult update(XcxUser xcxUser);
    /**
     * 删除小程序用户
     */
    CommonResult delete(Long id);

    /**
     * 小程序用户登录
     */
    CommonResult login(WechatLoginRequest wechatLoginRequest);

    /**
     * 小程序用户登出
     */
    CommonResult logout(Long userId);

    /**
     * 批量删除小程序用户
     */
    CommonResult batchDelete(List<Long> ids);

}
