package com.example.oldtown.modules.trf.controller;

import com.example.oldtown.modules.trf.service.TrfMileageService;
import com.example.oldtown.modules.trf.model.TrfMileage;
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
 * 交通接驳行驶里程 控制器
 * </p>
 * @author dyp
 * @since 2021-01-14
 */
@RestController
@RequestMapping("/trf/trfMileage")
@Api(value = "TrfMileageController", tags = "交通接驳行驶里程相关")
@Validated
public class TrfMileageController {


    @Autowired
    TrfMileageService  trfMileageService;

    @ApiOperation("分页查询交通接驳行驶里程")
    @GetMapping("/getAll")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "type",required = false) String type,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "fromTime", required = false) String fromTime,
                               @RequestParam(value = "toTime", required = false) String toTime){
        return trfMileageService.getAll(pageSize,pageNum,type,keyword,fromTime,toTime);
    }

    @ApiOperation("根据id查询交通接驳行驶里程")
    @GetMapping("/getById")
    @PreAuthorize("isAuthenticated()")
    public CommonResult<TrfMileage> getById(@RequestParam("id") @NotNull Long id){
        return CommonResult.success(trfMileageService.getById(id));
    }

    @ApiOperation("增加交通接驳行驶里程")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('交通接驳行驶里程管理')")
    public CommonResult add(@Validated(TrfMileage.class) TrfMileage trfMileage){

        return trfMileageService.add(trfMileage);
    }

    @ApiOperation("更新交通接驳行驶里程")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('交通接驳行驶里程管理')")
    public CommonResult update(@Validated(TrfMileage.class) TrfMileage trfMileage){

        if(trfMileage.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return trfMileageService.update(trfMileage);
    }

    @ApiOperation("删除交通接驳行驶里程")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('交通接驳行驶里程管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return trfMileageService.delete(id);
    }

    @ApiOperation("批量删除交通接驳行驶里程")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('交通接驳行驶里程管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return trfMileageService.batchDelete(ids);
    }

}

