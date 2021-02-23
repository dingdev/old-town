package com.example.oldtown.modules.xcx.controller;

import com.example.oldtown.modules.xcx.service.XcxNoticeService;
import com.example.oldtown.modules.xcx.model.XcxNotice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import  com.example.oldtown.common.api.CommonResult;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 小程序公告 控制器
 * </p>
 * @author dyp
 * @since 2020-12-01
 */
@RestController
@RequestMapping("/xcx/xcxNotice")
@Api(value = "XcxNoticeController", tags = "小程序公告相关")
@Validated
public class XcxNoticeController {


    @Autowired
    XcxNoticeService  xcxNoticeService;

    @ApiOperation("分页查询小程序公告")
    @GetMapping("/getAll")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "keyword",required = false) String keyword){
        return xcxNoticeService.getAll(pageSize,pageNum,keyword);
    }

    @ApiOperation("根据id查询小程序公告")
    @GetMapping("/getById")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult<XcxNotice> getById(@RequestParam("id") @NotNull Long id){
        return CommonResult.success(xcxNoticeService.getById(id));
    }

    @ApiOperation("增加小程序公告")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('小程序公告管理')")
    public CommonResult add(@Validated(XcxNotice.class) XcxNotice xcxNotice){

        return xcxNoticeService.add(xcxNotice);
    }

    @ApiOperation("更新小程序公告")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('小程序公告管理')")
    public CommonResult update(@Validated(XcxNotice.class) XcxNotice xcxNotice){

        if(xcxNotice.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return xcxNoticeService.update(xcxNotice);
    }

    @ApiOperation("删除小程序公告")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('小程序公告管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return xcxNoticeService.delete(id);
    }

    @ApiOperation("批量删除小程序公告")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('小程序公告管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return xcxNoticeService.batchDelete(ids);
    }

}

