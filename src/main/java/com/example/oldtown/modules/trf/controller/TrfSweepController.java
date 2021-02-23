package com.example.oldtown.modules.trf.controller;

import com.example.oldtown.common.service.MinioService;
import com.example.oldtown.modules.trf.service.TrfSweepService;
import com.example.oldtown.modules.trf.model.TrfSweep;
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
 * 交通接驳打捞船 控制器
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
@RestController
@RequestMapping("/trf/trfSweep")
@Api(value = "TrfSweepController", tags = "交通接驳打捞船相关")
@Validated
public class TrfSweepController {
    private final String SWEEP_FOLDER = "trf/sweep/";
    @Autowired
    MinioService minioService;

    @Autowired
    TrfSweepService  trfSweepService;

    @ApiOperation("分页查询交通接驳打捞船")
    @GetMapping("/getAll")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "type",required = false) String type,
                               @RequestParam(value = "keyword",required = false) String keyword,
                               @RequestParam(value = "orderByCurrentStatus",required = false,defaultValue = "0") Integer orderByCurrentStatus){
        return trfSweepService.getAll(pageSize,pageNum,type,keyword,orderByCurrentStatus);
    }

    @ApiOperation("根据id查询交通接驳打捞船")
    @GetMapping("/getById")
    @PreAuthorize("isAuthenticated()")
    public CommonResult<TrfSweep> getById(@RequestParam("id") @NotNull Long id){
        return trfSweepService.getSweepById(id);
    }

    @ApiOperation("增加交通接驳打捞船")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('交通接驳打捞船管理')")
    public CommonResult add(@Validated(TrfSweep.class) TrfSweep trfSweep){

        return trfSweepService.add(trfSweep);
    }

    @ApiOperation("更新交通接驳打捞船")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('交通接驳打捞船管理')")
    public CommonResult update(@Validated(TrfSweep.class) TrfSweep trfSweep){

        if(trfSweep.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return trfSweepService.update(trfSweep);
    }

    @ApiOperation("删除交通接驳打捞船")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('交通接驳打捞船管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return trfSweepService.delete(id);
    }

    @ApiOperation("批量删除交通接驳打捞船")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('交通接驳打捞船管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return trfSweepService.batchDelete(ids);
    }

    @ApiOperation("上传交通接驳打捞船的文件")
    @PostMapping("/uploadFile")
    @PreAuthorize("hasAnyAuthority('交通接驳打捞船管理')")
    public CommonResult uploadFile(@RequestParam("file") @NotNull MultipartFile file){

        return minioService.uploadFile(SWEEP_FOLDER,file);
    }

    @ApiOperation("删除交通接驳打捞船的文件")
    @DeleteMapping("/deleteFile")
    @PreAuthorize("hasAnyAuthority('交通接驳打捞船管理')")
    public CommonResult deleteFile(@RequestParam("url") @NotBlank String url){

        return minioService.deleteFile(SWEEP_FOLDER,url);
    }

    @ApiOperation("按类型统计交通接驳打捞船数量")
    @GetMapping("/countByType")
    @PreAuthorize("isAuthenticated()")
    public CommonResult countByType(){
        return trfSweepService.countByType();
    }

    @ApiOperation("按当前状态统计交通接驳打捞船数量")
    @GetMapping("/countByCurrentStatus")
    @PreAuthorize("isAuthenticated()")
    public CommonResult countByCurrentStatus(){
        return trfSweepService.countByCurrentStatus();
    }


}

