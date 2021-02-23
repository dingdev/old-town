package com.example.oldtown.modules.xcx.controller;

import cn.hutool.core.util.StrUtil;
import com.example.oldtown.modules.xcx.service.XcxFeedbackService;
import com.example.oldtown.modules.xcx.model.XcxFeedback;
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
 * 小程序用户反馈 控制器
 * </p>
 *
 * @author dyp
 * @since 2020-11-13
 */
@RestController
@RequestMapping("/xcx/xcxFeedback")
@Api(value = "XcxFeedbackController", tags = "小程序用户反馈相关")
@Validated
public class XcxFeedbackController {


    @Autowired
    XcxFeedbackService xcxFeedbackService;

    @ApiOperation("小程序用户分页查询本人反馈")
    @GetMapping("/getSelf")
    @PreAuthorize("hasAnyAuthority('小程序用户')")
    public CommonResult getSelf(@ApiIgnore Authentication authentication,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "status", required = false) Integer status,
                                @RequestParam(value = "type", required = false) String type) {
        if (authentication == null || StrUtil.isBlank(authentication.getName())) {
            return CommonResult.unauthorized(null);
        }
        String username = authentication.getName();
        Long userId = Long.parseLong(username.substring(username.lastIndexOf(",")+1));
        return CommonResult.success(xcxFeedbackService.getSelf(pageSize, pageNum, userId, status, type));
    }

    @ApiOperation("小程序用户根据id查询本人反馈")
    @GetMapping("/getById")
    @PreAuthorize("hasAnyAuthority('小程序用户')")
    public CommonResult<XcxFeedback> getById(@ApiIgnore Authentication authentication,
                                @RequestParam("id") @NotNull Long id) {
        if (authentication == null || StrUtil.isBlank(authentication.getName())) {
            return CommonResult.unauthorized(null);
        }
        String username = authentication.getName();
        Long userId = Long.parseLong(username.substring(username.lastIndexOf(",")+1));
        return CommonResult.success(xcxFeedbackService.getFeedbackById(id,userId));
    }

    @ApiOperation("小程序用户增加本人反馈")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('小程序用户')")
    public CommonResult add(@ApiIgnore Authentication authentication,
                            @Validated(XcxFeedback.class) XcxFeedback xcxFeedback) {
        if (authentication == null || StrUtil.isBlank(authentication.getName())) {
            return CommonResult.unauthorized(null);
        }
        String username = authentication.getName();
        Long userId = Long.parseLong(username.substring(username.lastIndexOf(",")+1));
        xcxFeedback.setUserId(userId);
        return xcxFeedbackService.add(xcxFeedback);
    }

    @ApiOperation("小程序用户更新本人反馈")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('小程序用户')")
    public CommonResult update(@ApiIgnore Authentication authentication,
                               @Validated(XcxFeedback.class) XcxFeedback xcxFeedback) {
        if (authentication == null || StrUtil.isBlank(authentication.getName())) {
            return CommonResult.unauthorized(null);
        }
        String username = authentication.getName();
        Long userId = Long.parseLong(username.substring(username.lastIndexOf(",")+1));
        if (xcxFeedback.getId() == null) {
            return CommonResult.failed("请输入id");
        }
        xcxFeedback.setUserId(userId);
        return xcxFeedbackService.update(xcxFeedback);
    }

    @ApiOperation("小程序用户删除本人反馈")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('小程序用户')")
    public CommonResult delete(@ApiIgnore Authentication authentication,
                               @RequestParam("id") @NotNull Long id) {
        if (authentication == null || StrUtil.isBlank(authentication.getName())) {
            return CommonResult.unauthorized(null);
        }
        String username = authentication.getName();
        Long userId = Long.parseLong(username.substring(username.lastIndexOf(",")+1));
        return xcxFeedbackService.delete(id,userId);
    }

    @ApiOperation("管理员分页查询反馈")
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('小程序用户反馈管理')")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "pageNum", required = false) Integer pageNum,
                               @RequestParam(value = "status", required = false) Integer status,
                               @RequestParam(value = "type", required = false) String type) {

        return xcxFeedbackService.getAll(pageSize, pageNum,status,type);
    }

    @ApiOperation("管理员更新反馈状态")
    @PostMapping("/updateStatus")
    @PreAuthorize("hasAnyAuthority('小程序用户反馈管理')")
    public CommonResult updateStatus(@RequestParam(value = "id") @NotNull Long id,
                                     @RequestParam(value = "status") @NotNull Integer status) {

        return xcxFeedbackService.updateStatus(id,status);
    }
}

