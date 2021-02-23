package com.example.oldtown.modules.com.controller;

import com.example.oldtown.modules.com.service.ComTelService;
import com.example.oldtown.modules.com.model.ComTel;
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
 * 通用电话 控制器
 * </p>
 * @author dyp
 * @since 2021-01-29
 */
@RestController
@RequestMapping("/com/comTel")
@Api(value = "ComTelController", tags = "通用电话相关")
@Validated
public class ComTelController {


    @Autowired
    ComTelService  comTelService;

    @ApiOperation("分页查询通用电话")
    @GetMapping("/getAll")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "keyword",required = false) String keyword){
        return comTelService.getAll(pageSize,pageNum,keyword);
    }

    @ApiOperation("根据id查询通用电话")
    @GetMapping("/getById")
    @PreAuthorize("isAuthenticated()")
    public CommonResult<ComTel> getById(@RequestParam("id") @NotNull Long id){
        return CommonResult.success(comTelService.getById(id));
    }

    @ApiOperation("增加通用电话")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('通用电话管理')")
    public CommonResult add(@Validated(ComTel.class) ComTel comTel){

        return comTelService.add(comTel);
    }

    @ApiOperation("更新通用电话")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('通用电话管理')")
    public CommonResult update(@Validated(ComTel.class) ComTel comTel){

        if(comTel.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return comTelService.update(comTel);
    }

    @ApiOperation("删除通用电话")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('通用电话管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return comTelService.delete(id);
    }

    @ApiOperation("批量删除通用电话")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('通用电话管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return comTelService.batchDelete(ids);
    }

}

