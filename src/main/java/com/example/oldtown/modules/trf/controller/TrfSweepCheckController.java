package com.example.oldtown.modules.trf.controller;

import com.example.oldtown.modules.trf.service.TrfSweepCheckService;
import com.example.oldtown.modules.trf.model.TrfSweepCheck;
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
 * 交通接驳打捞船员工考核 控制器
 * </p>
 * @author dyp
 * @since 2020-12-28
 */
@RestController
@RequestMapping("/trf/trfSweepCheck")
@Api(value = "TrfSweepCheckController", tags = "交通接驳打捞船员工考核相关")
@Validated
public class TrfSweepCheckController {


    @Autowired
    TrfSweepCheckService  trfSweepCheckService;

    @ApiOperation("分页查询交通接驳打捞船员工考核")
    @GetMapping("/getAll")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "keyword",required = false) String keyword,
                               @RequestParam(value = "fromTime", required = false) String fromTime,
                               @RequestParam(value = "toTime", required = false) String toTime){
        return trfSweepCheckService.getAll(pageSize,pageNum,keyword,fromTime,toTime);
    }

    @ApiOperation("根据id查询交通接驳打捞船员工考核")
    @GetMapping("/getById")
    @PreAuthorize("isAuthenticated()")
    public CommonResult<TrfSweepCheck> getById(@RequestParam("id") @NotNull Long id){
        return CommonResult.success(trfSweepCheckService.getById(id));
    }

    @ApiOperation("增加交通接驳打捞船员工考核")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('交通接驳打捞船员工考核管理')")
    public CommonResult add(@Validated(TrfSweepCheck.class) TrfSweepCheck trfSweepCheck){

        return trfSweepCheckService.add(trfSweepCheck);
    }

    @ApiOperation("更新交通接驳打捞船员工考核")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('交通接驳打捞船员工考核管理')")
    public CommonResult update(@Validated(TrfSweepCheck.class) TrfSweepCheck trfSweepCheck){

        if(trfSweepCheck.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return trfSweepCheckService.update(trfSweepCheck);
    }

    @ApiOperation("删除交通接驳打捞船员工考核")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('交通接驳打捞船员工考核管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return trfSweepCheckService.delete(id);
    }

    @ApiOperation("批量删除交通接驳打捞船员工考核")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('交通接驳打捞船员工考核管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return trfSweepCheckService.batchDelete(ids);
    }

}

