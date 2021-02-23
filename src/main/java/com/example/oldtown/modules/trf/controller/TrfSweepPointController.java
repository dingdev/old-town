package com.example.oldtown.modules.trf.controller;

import com.example.oldtown.modules.trf.service.TrfSweepPointService;
import com.example.oldtown.modules.trf.model.TrfSweepPoint;
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
 * 交通接驳打捞点 控制器
 * </p>
 * @author dyp
 * @since 2020-12-28
 */
@RestController
@RequestMapping("/trf/trfSweepPoint")
@Api(value = "TrfSweepPointController", tags = "交通接驳打捞点相关")
@Validated
public class TrfSweepPointController {


    @Autowired
    TrfSweepPointService  trfSweepPointService;

    @ApiOperation("分页查询交通接驳打捞点")
    @GetMapping("/getAll")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "keyword",required = false) String keyword){
        return trfSweepPointService.getAll(pageSize,pageNum,keyword);
    }

    @ApiOperation("根据id查询交通接驳打捞点")
    @GetMapping("/getById")
    @PreAuthorize("isAuthenticated()")
    public CommonResult<TrfSweepPoint> getById(@RequestParam("id") @NotNull Long id){
        return CommonResult.success(trfSweepPointService.getById(id));
    }

    @ApiOperation("增加交通接驳打捞点")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('交通接驳打捞点管理')")
    public CommonResult add(@Validated(TrfSweepPoint.class) TrfSweepPoint trfSweepPoint){

        return trfSweepPointService.add(trfSweepPoint);
    }

    @ApiOperation("更新交通接驳打捞点")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('交通接驳打捞点管理')")
    public CommonResult update(@Validated(TrfSweepPoint.class) TrfSweepPoint trfSweepPoint){

        if(trfSweepPoint.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return trfSweepPointService.update(trfSweepPoint);
    }

    @ApiOperation("删除交通接驳打捞点")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('交通接驳打捞点管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return trfSweepPointService.delete(id);
    }

    @ApiOperation("批量删除交通接驳打捞点")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('交通接驳打捞点管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return trfSweepPointService.batchDelete(ids);
    }

}

