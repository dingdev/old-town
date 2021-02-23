package com.example.oldtown.modules.trf.controller;

import com.example.oldtown.modules.trf.service.TrfSecurityRouteService;
import com.example.oldtown.modules.trf.model.TrfSecurityRoute;
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

import java.util.List;

/**
 * <p>
 * 交通接驳安保人员标准路线 控制器
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
@RestController
@RequestMapping("/trf/trfSecurityRoute")
@Api(value = "TrfSecurityRouteController", tags = "交通接驳安保人员标准路线相关")
@Validated
public class TrfSecurityRouteController {


    @Autowired
    TrfSecurityRouteService  trfSecurityRouteService;

    @ApiOperation("分页查询交通接驳安保人员标准路线")
    @GetMapping("/getAll")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "type",required = false) String type,
                               @RequestParam(value = "keyword",required = false) String keyword){
        return trfSecurityRouteService.getAll(pageSize,pageNum,type,keyword);
    }

    @ApiOperation("根据id查询交通接驳安保人员标准路线")
    @GetMapping("/getById")
    @PreAuthorize("isAuthenticated()")
    public CommonResult<TrfSecurityRoute> getById(@RequestParam("id") @NotNull Long id){
        return CommonResult.success(trfSecurityRouteService.getById(id));
    }

    @ApiOperation("增加交通接驳安保人员标准路线")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('交通接驳安保人员标准路线管理')")
    public CommonResult add(@Validated(TrfSecurityRoute.class) TrfSecurityRoute trfSecurityRoute){

        return trfSecurityRouteService.add(trfSecurityRoute);
    }

    @ApiOperation("更新交通接驳安保人员标准路线")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('交通接驳安保人员标准路线管理')")
    public CommonResult update(@Validated(TrfSecurityRoute.class) TrfSecurityRoute trfSecurityRoute){

        if(trfSecurityRoute.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return trfSecurityRouteService.update(trfSecurityRoute);
    }

    @ApiOperation("删除交通接驳安保人员标准路线")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('交通接驳安保人员标准路线管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return trfSecurityRouteService.delete(id);
    }

    @ApiOperation("批量删除交通接驳安保人员标准路线")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('交通接驳安保人员标准路线管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return trfSecurityRouteService.batchDelete(ids);
    }

}

