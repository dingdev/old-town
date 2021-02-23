package com.example.oldtown.modules.com.controller;

import cn.hutool.core.util.StrUtil;
import com.example.oldtown.modules.com.service.ComInfoService;
import com.example.oldtown.modules.com.model.ComInfo;
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

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 通用信息 控制器
 * </p>
 * @author dyp
 * @since 2020-11-06
 */
@RestController
@RequestMapping("/com/comInfo")
@Api(value = "ComInfoController", tags = "通用信息相关")
@Validated
public class ComInfoController {
    @Value("${minio.publicUrl}")
    private String MINIO_PUBLIC_URL;

    @Autowired
    ComInfoService  comInfoService;

    @ApiOperation("分页查询通用信息")
    @GetMapping("/getAll")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "keyword",required = false) String keyword){
        return comInfoService.getAll(pageSize,pageNum,keyword);
    }

    @ApiOperation("根据id查询通用信息")
    @GetMapping("/getById")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult<ComInfo> getById(@RequestParam("id") @NotNull Long id){
        return CommonResult.success(comInfoService.getById(id));
    }

    @ApiOperation("增加通用信息")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('通用信息管理')")
    public CommonResult add(@Validated(ComInfo.class) ComInfo comInfo){

        if (StrUtil.isBlank(comInfo.getName()) || StrUtil.isBlank(comInfo.getContent()) ) {
            return CommonResult.failed("请输入名称(name)和内容(content)");
        }
        return comInfoService.add(comInfo);
    }

    @ApiOperation("更新通用信息")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('通用信息管理')")
    public CommonResult update(@Validated(ComInfo.class) ComInfo comInfo){

        if(comInfo.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return comInfoService.update(comInfo);
    }

    @ApiOperation("删除通用信息")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('通用信息管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return comInfoService.delete(id);
    }

    @ApiOperation("批量删除通用信息")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('通用信息管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return comInfoService.batchDelete(ids);
    }

    @ApiOperation("根据名称查询通用信息")
    @GetMapping("/getInfoByName")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult getInfoByName(@RequestParam("name") @NotBlank String name) {
        return CommonResult.success(comInfoService.getByName(name));
    }

    @ApiOperation("查询Minio里文件地址的前缀,拼上其他返回值里文件的后半部分url,即可访问文件")
    @GetMapping("/getFilePrefix")
    public CommonResult getFilePrefix(){
        return CommonResult.success(MINIO_PUBLIC_URL);
    }

    @ApiOperation("查询南浔古镇今日天气")
    @GetMapping("/getTodayWeather")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult getTodayWeather(){
        return comInfoService.getTodayWeather();
    }

    @ApiOperation("获取小程序config接口部分参数")
    @GetMapping("/getXcxConfig")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult getXcxConfig(@RequestParam("url") @NotBlank String url,
                                     @RequestParam("secret") @NotBlank String secret){
        return comInfoService.getXcxConfig(url, secret);
    }

    @ApiOperation("获取公众号access_token参数")
    @GetMapping("/getOfficialAccessToken")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult getOfficialAccessToken(@RequestParam("secret") @NotBlank String secret){
        return comInfoService.getOfficialAccessToken(secret);
    }

}

