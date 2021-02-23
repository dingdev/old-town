package com.example.oldtown.modules.trf.controller;

import com.example.oldtown.modules.trf.service.TrfGpsService;
import com.example.oldtown.modules.trf.model.TrfGps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.example.oldtown.common.api.CommonResult;

import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 交通接驳gps定位点 控制器
 * </p>
 *
 * @author dyp
 * @since 2020-11-25
 */
@RestController
@RequestMapping("/trf/trfGps")
@Api(value = "TrfGpsController", tags = "交通接驳gps定位点相关")
@Validated
public class TrfGpsController {


    @Autowired
    TrfGpsService trfGpsService;

    @ApiOperation("分页查询交通接驳gps定位点")
    @GetMapping("/getAll")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "pageNum", required = false,defaultValue = "1") Integer pageNum) {
        return trfGpsService.getAll(pageSize, pageNum);
    }

    @ApiOperation("根据id查询交通接驳gps定位点")
    @GetMapping("/getById")
    @PreAuthorize("isAuthenticated()")
    public CommonResult<TrfGps> getById(@RequestParam("id") @NotNull Long id) {
        return CommonResult.success(trfGpsService.getById(id));
    }

    @ApiOperation("增加交通接驳gps定位点")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('交通接驳gps定位点管理')")
    public CommonResult add(@Validated(TrfGps.class) TrfGps trfGps) {

        return trfGpsService.add(trfGps);
    }

    @ApiOperation("更新交通接驳gps定位点")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('交通接驳gps定位点管理')")
    public CommonResult update(@Validated(TrfGps.class) TrfGps trfGps) {

        if (trfGps.getId() == null) {
            return CommonResult.failed("请输入id");
        }
        return trfGpsService.update(trfGps);
    }

    @ApiOperation("删除交通接驳gps定位点")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('交通接驳gps定位点管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id) {

        return trfGpsService.delete(id);
    }

    @ApiOperation("批量删除交通接驳gps定位点")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('交通接驳gps定位点管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids) {

        return trfGpsService.batchDelete(ids);
    }

    // @ApiOperation("根据gps设备编号查询今日最新定位点")
    // @GetMapping("/getLatestByGpsCode")
    // @PreAuthorize("isAuthenticated()")
    // public CommonResult getLatestByGpsCode(@RequestParam("gpsCode") @NotNull String gpsCode) {
    //     return CommonResult.success(trfGpsService.getLatestByGpsCode(gpsCode));
    // }

    @ApiOperation("根据gps设备编号查询某日定位点集")
    @GetMapping("/getListByGpsCode")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getListByGpsCode(@RequestParam("gpsCode") @NotBlank String gpsCode,
                                          @RequestParam("fromTime") @NotBlank String fromTime,
                                          @RequestParam("toTime") @NotBlank String toTime) {
        return CommonResult.success(trfGpsService.getListByGpsCode(gpsCode, fromTime, toTime));
    }

}

