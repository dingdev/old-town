package com.example.oldtown.modules.trf.controller;

import com.example.oldtown.common.service.MinioService;
import com.example.oldtown.modules.trf.service.TrfYachtService;
import com.example.oldtown.modules.trf.model.TrfYacht;
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
 * 交通接驳游船 控制器
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
@RestController
@RequestMapping("/trf/trfYacht")
@Api(value = "TrfYachtController", tags = "交通接驳游船相关")
@Validated
public class TrfYachtController {
    private final String YACHT_FOLDER = "trf/yacht/";
    @Autowired
    MinioService minioService;


    @Autowired
    TrfYachtService  trfYachtService;

    @ApiOperation("分页查询交通接驳游船")
    @GetMapping("/getAll")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "type",required = false) String type,
                               @RequestParam(value = "keyword",required = false) String keyword,
                               @RequestParam(value = "orderByCurrentStatus",required = false,defaultValue = "0") Integer orderByCurrentStatus){
        return trfYachtService.getAll(pageSize,pageNum,type,keyword,orderByCurrentStatus);
    }

    @ApiOperation("根据id查询交通接驳游船")
    @GetMapping("/getById")
    @PreAuthorize("isAuthenticated()")
    public CommonResult<TrfYacht> getById(@RequestParam("id") @NotNull Long id){
        return trfYachtService.getYachtById(id);
    }

    @ApiOperation("增加交通接驳游船")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('交通接驳游船管理')")
    public CommonResult add(@Validated(TrfYacht.class) TrfYacht trfYacht){

        return trfYachtService.add(trfYacht);
    }

    @ApiOperation("更新交通接驳游船")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('交通接驳游船管理')")
    public CommonResult update(@Validated(TrfYacht.class) TrfYacht trfYacht){

        if(trfYacht.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return trfYachtService.update(trfYacht);
    }

    @ApiOperation("删除交通接驳游船")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('交通接驳游船管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return trfYachtService.delete(id);
    }

    @ApiOperation("批量删除交通接驳游船")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('交通接驳游船管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return trfYachtService.batchDelete(ids);
    }

    @ApiOperation("上传交通接驳游船的文件")
    @PostMapping("/uploadFile")
    @PreAuthorize("hasAnyAuthority('交通接驳游船管理')")
    public CommonResult uploadFile(@RequestParam("file") @NotNull MultipartFile file){

        return minioService.uploadFile(YACHT_FOLDER,file);
    }

    @ApiOperation("删除交通接驳游船的文件")
    @DeleteMapping("/deleteFile")
    @PreAuthorize("hasAnyAuthority('交通接驳游船管理')")
    public CommonResult deleteFile(@RequestParam("url") @NotBlank String url){

        return minioService.deleteFile(YACHT_FOLDER,url);
    }

    @ApiOperation("统计交通接驳游船数量")
    @GetMapping("/countByType")
    @PreAuthorize("isAuthenticated()")
    public CommonResult countByType(){
        return trfYachtService.countByType();
    }

}

