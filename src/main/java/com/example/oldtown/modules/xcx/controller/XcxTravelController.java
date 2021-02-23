package com.example.oldtown.modules.xcx.controller;

import cn.hutool.core.util.StrUtil;
import com.example.oldtown.dto.XcxTravelDTO;
import com.example.oldtown.modules.xcx.service.XcxTravelService;
import com.example.oldtown.modules.xcx.model.XcxTravel;
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
 * 小程序用户行程 控制器
 * </p>
 *
 * @author dyp
 * @since 2020-11-13
 */
@RestController
@RequestMapping("/xcx/xcxTravel")
@Api(value = "XcxTravelController", tags = "小程序用户行程相关")
@Validated
public class XcxTravelController {

    @Autowired
    XcxTravelService xcxTravelService;

    @ApiOperation("小程序用户分页查询本人行程")
    @GetMapping("/getSelf")
    @PreAuthorize("hasAnyAuthority('小程序用户')")
    public CommonResult getSelf(@ApiIgnore Authentication authentication,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "keyword",required = false) String keyword) {
        if (authentication == null || StrUtil.isBlank(authentication.getName())) {
            return CommonResult.unauthorized(null);
        }
        String username = authentication.getName();
        Long userId = Long.parseLong(username.substring(username.lastIndexOf(",")+1));
        return CommonResult.success(xcxTravelService.getSelf(pageSize, pageNum, userId, keyword));
    }

    @ApiOperation("小程序用户根据id查询本人行程")
    @GetMapping("/getById")
    @PreAuthorize("hasAnyAuthority('小程序用户')")
    public CommonResult<XcxTravelDTO> getById(@ApiIgnore Authentication authentication,
                                              @RequestParam("id") @NotNull Long id) {
        if (authentication == null || StrUtil.isBlank(authentication.getName())) {
            return CommonResult.unauthorized(null);
        }
        String username = authentication.getName();
        Long userId = Long.parseLong(username.substring(username.lastIndexOf(",")+1));

        return xcxTravelService.getTravelById(id, userId);
    }

    @ApiOperation("小程序用户增加本人行程")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('小程序用户')")
    public CommonResult add(@ApiIgnore Authentication authentication,
                            @Validated(XcxTravel.class) XcxTravel xcxTravel) {
        if (authentication == null || StrUtil.isBlank(authentication.getName())) {
            return CommonResult.unauthorized(null);
        }
        String username = authentication.getName();
        Long userId = Long.parseLong(username.substring(username.lastIndexOf(",")+1));
        xcxTravel.setUserId(userId);
        return xcxTravelService.add(xcxTravel);
    }

    @ApiOperation("小程序用户更新本人行程")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('小程序用户')")
    public CommonResult update(@ApiIgnore Authentication authentication,
                               @Validated(XcxTravel.class) XcxTravel xcxTravel) {
        if (xcxTravel.getId() == null) {
            return CommonResult.failed("请输入id");
        }
        if (authentication == null || StrUtil.isBlank(authentication.getName())) {
            return CommonResult.unauthorized(null);
        }
        String username = authentication.getName();
        Long userId = Long.parseLong(username.substring(username.lastIndexOf(",")+1));
        xcxTravel.setUserId(userId);
        return xcxTravelService.update(xcxTravel);
    }

    @ApiOperation("小程序用户删除本人行程")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('小程序用户')")
    public CommonResult delete(@ApiIgnore Authentication authentication,
                               @RequestParam("id") @NotNull Long id) {
        if (authentication == null || StrUtil.isBlank(authentication.getName())) {
            return CommonResult.unauthorized(null);
        }
        String username = authentication.getName();
        Long userId = Long.parseLong(username.substring(username.lastIndexOf(",")+1));
        return xcxTravelService.delete(id, userId);
    }



}

