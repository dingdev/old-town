package com.example.oldtown.modules.xcx.service;
import com.example.oldtown.modules.xcx.model.XcxTravel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;

/**
 * <p>
 * 小程序用户行程 服务类
 * </p>
 * @author dyp
 * @since 2020-11-13
 */
public interface XcxTravelService extends IService<XcxTravel> {


    /**
    * 分页查询小程序用户行程
    */
    Page<XcxTravel> getSelf(Integer pageSize, Integer pageNum, Long userId, String keyword);

    /**
     * 根据id查询小程序用户行程
     */
    CommonResult getTravelById(Long id,Long userId);


    /**
     * 增加小程序用户行程
     */
    CommonResult add(XcxTravel xcxTravel);

    /**
     * 更新小程序用户行程
     */
    CommonResult update(XcxTravel xcxTravel);
    /**
     * 删除小程序用户行程
     */
    CommonResult delete(Long id,Long userId);

}
