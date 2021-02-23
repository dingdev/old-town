package com.example.oldtown.modules.trf.controller;

import cn.hutool.core.util.StrUtil;
import com.example.oldtown.modules.trf.service.TrfSweepRouteService;
import com.example.oldtown.modules.trf.model.TrfSweepRoute;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import  com.example.oldtown.common.api.CommonResult;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 交通接驳打捞船标准路线 控制器
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
@RestController
@RequestMapping("/trf/trfSweepRoute")
@Api(value = "TrfSweepRouteController", tags = "交通接驳打捞船标准路线相关")
@Validated
public class TrfSweepRouteController {


    @Autowired
    TrfSweepRouteService  trfSweepRouteService;

    @ApiOperation("分页查询交通接驳打捞船标准路线")
    @GetMapping("/getAll")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "keyword",required = false) String keyword){
        return trfSweepRouteService.getAll(pageSize,pageNum,keyword);
    }

    @ApiOperation("根据id查询交通接驳打捞船标准路线")
    @GetMapping("/getById")
    @PreAuthorize("isAuthenticated()")
    public CommonResult<TrfSweepRoute> getById(@RequestParam("id") @NotNull Long id){
        return CommonResult.success(trfSweepRouteService.getById(id));
    }

    @ApiOperation("增加交通接驳打捞船标准路线")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('交通接驳打捞船标准路线管理')")
    public CommonResult add(@Validated(TrfSweepRoute.class) TrfSweepRoute trfSweepRoute,
                            @RequestParam(value = "pointsExcel", required = false) MultipartFile pointsExcel){

        return trfSweepRouteService.add(trfSweepRoute,pointsExcel);
    }

    @ApiOperation("更新交通接驳打捞船标准路线")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('交通接驳打捞船标准路线管理')")
    public CommonResult update(@Validated(TrfSweepRoute.class) TrfSweepRoute trfSweepRoute){

        if(trfSweepRoute.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return trfSweepRouteService.update(trfSweepRoute);
    }

    @ApiOperation("删除交通接驳打捞船标准路线")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('交通接驳打捞船标准路线管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return trfSweepRouteService.delete(id);
    }

    @ApiOperation("批量删除交通接驳打捞船标准路线")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('交通接驳打捞船标准路线管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return trfSweepRouteService.batchDelete(ids);
    }

    @ApiOperation("获取增加打捞船标准路线时的pointExcel文件模板地址")
    @PostMapping("/getPointExcelTemplate")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getPointExcelTemplate(){
        return trfSweepRouteService.getPointExcelTemplate();
    }

}

