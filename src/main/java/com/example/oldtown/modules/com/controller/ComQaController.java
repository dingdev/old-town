package com.example.oldtown.modules.com.controller;

import com.example.oldtown.modules.com.service.ComQaService;
import com.example.oldtown.modules.com.model.ComQa;
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

import java.util.List;

/**
 * <p>
 * 通用问答 控制器
 * </p>
 * @author dyp
 * @since 2020-11-13
 */
@RestController
@RequestMapping("/com/comQa")
@Api(value = "ComQaController", tags = "通用问答相关")
@Validated
public class ComQaController {


    @Autowired
    ComQaService  comQaService;

    @ApiOperation("分页查询通用问答")
    @GetMapping("/getAll")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "type", required = false)String type,
                               @RequestParam(value = "keyword",required = false) String keyword){
        return comQaService.getAll(pageSize,pageNum,type,keyword);
    }

    @ApiOperation("根据id查询通用问答")
    @GetMapping("/getById")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult<ComQa> getById(@RequestParam("id") @NotNull Long id){
        return CommonResult.success(comQaService.getById(id));
    }

    @ApiOperation("增加通用问答")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('通用问答管理')")
    public CommonResult add(@Validated(ComQa.class) ComQa comQa){

        return comQaService.add(comQa);
    }

    @ApiOperation("更新通用问答(包括禁用启用)")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('通用问答管理')")
    public CommonResult update(@Validated(ComQa.class) ComQa comQa){

        if(comQa.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return comQaService.update(comQa);
    }

    @ApiOperation("删除通用问答")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('通用问答管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return comQaService.delete(id);
    }

    @ApiOperation("批量删除通用问答")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('通用问答管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return comQaService.batchDelete(ids);
    }

    @ApiOperation("根据问查询答")
    @GetMapping("/getAnswerByQuestion")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult getAnswerByQuestion(@RequestParam("question") @NotBlank String question){
        return comQaService.getAnswerByQuestion(question);
    }

}

