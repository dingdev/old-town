package com.example.oldtown.modules.xcx.controller;

import com.example.oldtown.modules.xcx.service.XcxNewsService;
import com.example.oldtown.modules.xcx.model.XcxNews;
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
 * 小程序资讯 控制器
 * </p>
 * @author dyp
 * @since 2020-12-03
 */
@RestController
@RequestMapping("/xcx/xcxNews")
@Api(value = "XcxNewsController", tags = "小程序资讯相关")
@Validated
public class XcxNewsController {


    @Autowired
    XcxNewsService  xcxNewsService;

    @ApiOperation("分页查询小程序资讯")
    @GetMapping("/getAll")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "keyword",required = false) String keyword){
        return xcxNewsService.getAll(pageSize,pageNum,keyword);
    }

    @ApiOperation("根据id查询小程序资讯")
    @GetMapping("/getById")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult<XcxNews> getById(@RequestParam("id") @NotNull Long id){
        return CommonResult.success(xcxNewsService.getById(id));
    }

    @ApiOperation("增加小程序资讯")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('小程序资讯管理')")
    public CommonResult add(@Validated(XcxNews.class) XcxNews xcxNews){

        return xcxNewsService.add(xcxNews);
    }

    @ApiOperation("更新小程序资讯")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('小程序资讯管理')")
    public CommonResult update(@Validated(XcxNews.class) XcxNews xcxNews){

        if(xcxNews.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return xcxNewsService.update(xcxNews);
    }

    @ApiOperation("删除小程序资讯")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('小程序资讯管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return xcxNewsService.delete(id);
    }

    @ApiOperation("批量删除小程序资讯")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('小程序资讯管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return xcxNewsService.batchDelete(ids);
    }

}

