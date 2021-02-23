package com.example.oldtown.modules.xcx.controller;

import cn.hutool.core.util.StrUtil;
import com.example.oldtown.dto.WechatLoginRequest;
import com.example.oldtown.modules.xcx.service.XcxUserService;
import com.example.oldtown.modules.xcx.model.XcxUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.example.oldtown.common.api.CommonResult;

import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * <p>
 * 小程序用户 控制器
 * </p>
 *
 * @author dyp
 * @since 2020-11-13
 */
@RestController
@RequestMapping("/xcx/xcxUser")
@Api(value = "XcxUserController", tags = "小程序用户相关")
@Validated
public class XcxUserController {


    @Autowired
    XcxUserService xcxUserService;

    @ApiOperation("分页查询小程序用户")
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('小程序用户管理')")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "pageNum", required = false) Integer pageNum,
                               @RequestParam(value = "keyword",required = false) String keyword) {
        return xcxUserService.getAll(pageSize, pageNum,keyword);
    }

    @ApiOperation("根据id查询小程序用户")
    @GetMapping("/getById")
    @PreAuthorize("hasAnyAuthority('小程序用户管理')")
    public CommonResult<XcxUser> getById(@RequestParam("id") @NotNull Long id) {
        return CommonResult.success(xcxUserService.getById(id));
    }

    @ApiOperation("增加小程序用户")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('小程序用户管理')")
    public CommonResult add(@Validated(XcxUser.class) XcxUser xcxUser) {

        return xcxUserService.add(xcxUser);
    }

    @ApiOperation("更新小程序用户")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('小程序用户管理')")
    public CommonResult update(@Validated(XcxUser.class) XcxUser xcxUser) {

        if (xcxUser.getId() == null) {
            return CommonResult.failed("请输入id");
        }

        return xcxUserService.update(xcxUser);
    }

    @ApiOperation("删除小程序用户")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('小程序用户管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id) {

        return xcxUserService.delete(id);
    }

    @ApiOperation("批量删除小程序用户")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('小程序用户管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return xcxUserService.batchDelete(ids);
    }

    @ApiOperation("小程序用户登录")
    @PostMapping("/login")
    public CommonResult login(@Validated(WechatLoginRequest.class) WechatLoginRequest wechatLoginRequest) {
        return xcxUserService.login(wechatLoginRequest);
    }

    @ApiOperation("小程序用户登出")
    @PostMapping("/logout")
    public CommonResult logout(@ApiIgnore Authentication authentication) {
        if (authentication == null || StrUtil.isBlank(authentication.getName())) {
            return CommonResult.unauthorized(null);
        }
        String username = authentication.getName();
        Long userId = Long.parseLong(username.substring(username.lastIndexOf(",")+1));
        return xcxUserService.logout(userId);
    }
}

