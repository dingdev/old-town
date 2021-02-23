package com.example.oldtown.modules.trf.controller;

import com.example.oldtown.modules.trf.service.TrfAlarmService;
import com.example.oldtown.modules.trf.model.TrfAlarm;
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
 * 交通接驳gps报警 控制器
 * </p>
 * @author dyp
 * @since 2021-01-15
 */
@RestController
@RequestMapping("/trf/trfAlarm")
@Api(value = "TrfAlarmController", tags = "交通接驳gps报警相关")
@Validated
public class TrfAlarmController {


    @Autowired
    TrfAlarmService  trfAlarmService;

    @ApiOperation("分页查询交通接驳gps报警")
    @GetMapping("/getAll")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "keyword",required = false) String keyword,
                               @RequestParam(value = "fromTime", required = false) String fromTime,
                               @RequestParam(value = "toTime", required = false) String toTime){
        return trfAlarmService.getAll(pageSize,pageNum,keyword,fromTime,toTime);
    }

    @ApiOperation("根据id查询交通接驳gps报警")
    @GetMapping("/getById")
    @PreAuthorize("isAuthenticated()")
    public CommonResult<TrfAlarm> getById(@RequestParam("id") @NotNull Long id){
        return CommonResult.success(trfAlarmService.getById(id));
    }

    @ApiOperation("增加交通接驳gps报警")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('交通接驳gps报警管理')")
    public CommonResult add(@Validated(TrfAlarm.class) TrfAlarm trfAlarm){

        return trfAlarmService.add(trfAlarm);
    }

    @ApiOperation("更新交通接驳gps报警")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('交通接驳gps报警管理')")
    public CommonResult update(@Validated(TrfAlarm.class) TrfAlarm trfAlarm){

        if(trfAlarm.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return trfAlarmService.update(trfAlarm);
    }

    @ApiOperation("删除交通接驳gps报警")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('交通接驳gps报警管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return trfAlarmService.delete(id);
    }

    @ApiOperation("批量删除交通接驳gps报警")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('交通接驳gps报警管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return trfAlarmService.batchDelete(ids);
    }

}

