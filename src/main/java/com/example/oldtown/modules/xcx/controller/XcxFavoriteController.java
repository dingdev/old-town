package com.example.oldtown.modules.xcx.controller;

import cn.hutool.core.util.StrUtil;
import com.example.oldtown.modules.xcx.service.XcxFavoriteService;
import com.example.oldtown.modules.xcx.model.XcxFavorite;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import com.example.oldtown.common.api.CommonResult;

import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * 小程序用户收藏 控制器
 * </p>
 *
 * @author dyp
 * @since 2020-11-13
 */
@RestController
@RequestMapping("/xcx/xcxFavorite")
@Api(value = "XcxFavoriteController", tags = "小程序用户收藏相关")
@Validated
public class XcxFavoriteController {


    @Autowired
    XcxFavoriteService xcxFavoriteService;

    @ApiOperation("小程序用户分页查询本人收藏")
    @GetMapping("/getSelf")
    @PreAuthorize("hasAnyAuthority('小程序用户')")
    public CommonResult getSelf(@ApiIgnore Authentication authentication,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "type",required = false) String type,
                                @RequestParam(value = "keyword",required = false) String keyword) {

        if (authentication == null || StrUtil.isBlank(authentication.getName())) {
            return CommonResult.unauthorized(null);
        }
        String username = authentication.getName();
        Long userId = Long.parseLong(username.substring(username.lastIndexOf(",")+1));
        return CommonResult.success(xcxFavoriteService.getSelf(pageSize, pageNum, userId, type, keyword));
    }

    @ApiOperation("小程序用户根据id查询本人收藏")
    @GetMapping("/getById")
    @PreAuthorize("hasAnyAuthority('小程序用户')")
    public CommonResult<XcxFavorite> getById(@ApiIgnore Authentication authentication,
                                @RequestParam("id") @NotNull Long id) {
        if (authentication == null || StrUtil.isBlank(authentication.getName())) {
            return CommonResult.unauthorized(null);
        }
        String username = authentication.getName();
        Long userId = Long.parseLong(username.substring(username.lastIndexOf(",")+1));
        return CommonResult.success(xcxFavoriteService.getFavoriteById(id, userId));
    }

    @ApiOperation("小程序用户增加本人收藏")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('小程序用户')")
    public CommonResult add(@ApiIgnore Authentication authentication,
                            @Validated(XcxFavorite.class) XcxFavorite xcxFavorite) {
        if (authentication == null || StrUtil.isBlank(authentication.getName())) {
            return CommonResult.unauthorized(null);
        }
        String username = authentication.getName();
        Long userId = Long.parseLong(username.substring(username.lastIndexOf(",")+1));
        xcxFavorite.setUserId(userId);
        return xcxFavoriteService.add(xcxFavorite);
    }

    @ApiOperation("小程序用户更新本人收藏")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('小程序用户')")
    public CommonResult update(@ApiIgnore Authentication authentication,
                               @Validated(XcxFavorite.class) XcxFavorite xcxFavorite) {

        if (xcxFavorite.getId() == null) {
            return CommonResult.failed("请输入id");
        }
        if (authentication == null || StrUtil.isBlank(authentication.getName())) {
            return CommonResult.unauthorized(null);
        }
        String username = authentication.getName();
        Long userId = Long.parseLong(username.substring(username.lastIndexOf(",")+1));
        xcxFavorite.setUserId(userId);
        return xcxFavoriteService.update(xcxFavorite);
    }

    @ApiOperation("小程序用户删除本人收藏")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('小程序用户')")
    public CommonResult delete(@ApiIgnore Authentication authentication,
                               @RequestParam("id") @NotNull Long id) {
        if (authentication == null || StrUtil.isBlank(authentication.getName())) {
            return CommonResult.unauthorized(null);
        }
        String username = authentication.getName();
        Long userId = Long.parseLong(username.substring(username.lastIndexOf(",")+1));
        return xcxFavoriteService.delete(id, userId);
    }

}

