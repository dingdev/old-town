package com.example.oldtown.modules.xcx.service;
import com.example.oldtown.modules.xcx.model.XcxFavorite;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;

/**
 * <p>
 * 小程序用户收藏 服务类
 * </p>
 * @author dyp
 * @since 2020-11-13
 */
public interface XcxFavoriteService extends IService<XcxFavorite> {


    /**
    * 小程序用户分页查询本人收藏
    */
    Page<XcxFavorite> getSelf(Integer pageSize, Integer pageNum,Long userId, String type, String keyword);

    /**
     * 小程序用户根据id查询本人收藏
     */
    XcxFavorite getFavoriteById(Long id, Long userId);

    /**
     * 增加小程序用户收藏
     */
    CommonResult add(XcxFavorite xcxFavorite);

    /**
     * 更新小程序用户收藏
     */
    CommonResult update(XcxFavorite xcxFavorite);
    /**
     * 删除小程序用户收藏
     */
    CommonResult delete(Long id,Long userId);

}
