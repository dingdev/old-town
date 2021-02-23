package com.example.oldtown.modules.trf.controller;

import com.example.oldtown.modules.trf.service.TrfStaffGpsService;
import com.example.oldtown.modules.trf.model.TrfStaffGps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * 交通接驳员工gps定位点 控制器
 * </p>
 * @author dyp
 * @since 2020-12-24
 */
@RestController
@RequestMapping("/trf/trfStaffGps")
@Api(value = "TrfStaffGpsController", tags = "交通接驳员工gps定位点相关")
@Validated
public class TrfStaffGpsController {

    private static final String INTERCOM_SECRET = "zjtoprs";

    @Autowired
    TrfStaffGpsService  trfStaffGpsService;

    @ApiOperation("分页查询交通接驳员工gps定位点")
    @GetMapping("/getAll")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false, defaultValue = "1")Integer pageNum,
                               @RequestParam(value = "keyword",required = false) String keyword){
        return CommonResult.success(trfStaffGpsService.getAll(pageSize,pageNum,keyword));
    }

    @ApiOperation("根据id查询交通接驳员工gps定位点")
    @GetMapping("/getById")
    @PreAuthorize("isAuthenticated()")
    public CommonResult<TrfStaffGps> getById(@RequestParam("id") @NotNull Long id){
        return CommonResult.success(trfStaffGpsService.getById(id));
    }

    @ApiOperation("增加交通接驳员工gps定位点")
    @PostMapping("/add")
    // @PreAuthorize("hasAnyAuthority('交通接驳员工gps定位点管理')")
    public CommonResult add(@Validated(TrfStaffGps.class) TrfStaffGps trfStaffGps,
                            @RequestParam(value = "secret",required = false) @NotBlank String secret){
        Long gpsTime = trfStaffGps.getGpsTime();
        if (gpsTime == null || gpsTime % 60 != 0) {
            return null;
        }
        if (!INTERCOM_SECRET.equals(secret)) {
            return CommonResult.failed("秘钥(secret)有误");
        }
        return trfStaffGpsService.add(trfStaffGps);
    }

    @ApiOperation("更新交通接驳员工gps定位点")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('交通接驳员工gps定位点管理')")
    public CommonResult update(@Validated(TrfStaffGps.class) TrfStaffGps trfStaffGps){

        if(trfStaffGps.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return trfStaffGpsService.update(trfStaffGps);
    }

    @ApiOperation("删除交通接驳员工gps定位点")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('交通接驳员工gps定位点管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return trfStaffGpsService.delete(id);
    }

    @ApiOperation("批量删除交通接驳员工gps定位点")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('交通接驳员工gps定位点管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return trfStaffGpsService.batchDelete(ids);
    }

    @ApiOperation("根据gps设备编号查询某日定位点集")
    @GetMapping("/getListByGpsCode")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getListByGpsCode(@RequestParam("gpsCode") @NotBlank String gpsCode,
                                          @RequestParam("fromTime") @NotBlank String fromTime,
                                          @RequestParam("toTime") @NotBlank String toTime) {
        return CommonResult.success(trfStaffGpsService.getListByGpsCode(gpsCode, fromTime, toTime));
    }

}

