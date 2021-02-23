package com.example.oldtown.modules.com.controller;

import com.example.oldtown.common.service.MinioService;
import com.example.oldtown.modules.com.service.ComActivityService;
import com.example.oldtown.modules.com.model.ComActivity;
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

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 通用活动演出 控制器
 * </p>
 * @author dyp
 * @since 2020-11-24
 */
@RestController
@RequestMapping("/com/comActivity")
@Api(value = "ComActivityController", tags = "通用活动演出相关")
@Validated
public class ComActivityController {

    private final String ACTIVITY_FOLDER = "com/activity/";
    @Autowired
    ComActivityService  comActivityService;
    @Autowired
    MinioService minioService;

    @ApiOperation("分页查询通用活动演出")
    @GetMapping("/getAll")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "keyword", required = false) String keyword){
        return comActivityService.getAll(pageSize,pageNum,keyword);
    }

    @ApiOperation("根据id查询通用活动演出")
    @GetMapping("/getById")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult<ComActivity> getById(@RequestParam("id") @NotNull Long id){
        return CommonResult.success(comActivityService.getById(id));
    }

    @ApiOperation("增加通用活动演出")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('通用活动演出管理')")
    public CommonResult add(@Validated(ComActivity.class) ComActivity comActivity){

        return comActivityService.add(comActivity);
    }

    @ApiOperation("更新通用活动演出")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('通用活动演出管理')")
    public CommonResult update(@Validated(ComActivity.class) ComActivity comActivity){

        if(comActivity.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return comActivityService.update(comActivity);
    }

    @ApiOperation("删除通用活动演出")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('通用活动演出管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return comActivityService.delete(id);
    }

    @ApiOperation("批量删除通用活动演出")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('通用活动演出管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return comActivityService.batchDelete(ids);
    }

    @ApiOperation("上传通用活动演出的文件")
    @PostMapping("/uploadFile")
    @PreAuthorize("hasAnyAuthority('通用活动演出管理')")
    public CommonResult uploadFile(@RequestParam("file") @NotNull MultipartFile file){

        return minioService.uploadFile(ACTIVITY_FOLDER,file);
    }

    @ApiOperation("删除通用活动演出的文件")
    @DeleteMapping("/deleteFile")
    @PreAuthorize("hasAnyAuthority('通用活动演出管理')")
    public CommonResult deleteFile(@RequestParam("url") @NotBlank String url){

        return minioService.deleteFile(ACTIVITY_FOLDER,url);
    }

}

