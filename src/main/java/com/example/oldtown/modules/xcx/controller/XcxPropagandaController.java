package com.example.oldtown.modules.xcx.controller;

import com.example.oldtown.common.service.MinioService;
import com.example.oldtown.modules.xcx.service.XcxPropagandaService;
import com.example.oldtown.modules.xcx.model.XcxPropaganda;
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
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 小程序宣传 控制器
 * </p>
 * @author dyp
 * @since 2020-12-02
 */
@RestController
@RequestMapping("/xcx/xcxPropaganda")
@Api(value = "XcxPropagandaController", tags = "小程序宣传相关")
@Validated
public class XcxPropagandaController {

    private final String PROPAGANDA_FOLDER = "xcx/propaganda/";
    @Autowired
    MinioService minioService;
    @Autowired
    XcxPropagandaService  xcxPropagandaService;

    @ApiOperation("分页查询小程序宣传")
    @GetMapping("/getAll")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "type",required = false) String type,
                               @RequestParam(value = "keyword",required = false) String keyword){
        return xcxPropagandaService.getAll(pageSize,pageNum,type,keyword);
    }

    @ApiOperation("根据id查询小程序宣传")
    @GetMapping("/getById")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult<XcxPropaganda> getById(@RequestParam("id") @NotNull Long id){
        return CommonResult.success(xcxPropagandaService.getById(id));
    }

    @ApiOperation("增加小程序宣传")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('小程序宣传管理')")
    public CommonResult add(@Validated(XcxPropaganda.class) XcxPropaganda xcxPropaganda){

        return xcxPropagandaService.add(xcxPropaganda);
    }

    @ApiOperation("更新小程序宣传")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('小程序宣传管理')")
    public CommonResult update(@Validated(XcxPropaganda.class) XcxPropaganda xcxPropaganda){

        if(xcxPropaganda.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return xcxPropagandaService.update(xcxPropaganda);
    }

    @ApiOperation("删除小程序宣传")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('小程序宣传管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return xcxPropagandaService.delete(id);
    }

    @ApiOperation("批量删除小程序宣传")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('小程序宣传管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return xcxPropagandaService.batchDelete(ids);
    }

    @ApiOperation("上传小程序宣传的文件")
    @PostMapping("/uploadFile")
    @PreAuthorize("hasAnyAuthority('小程序宣传管理')")
    public CommonResult uploadFile(@RequestParam("file") @NotNull MultipartFile file){

        return minioService.uploadFile(PROPAGANDA_FOLDER,file);
    }

    @ApiOperation("删除小程序宣传的文件")
    @DeleteMapping("/deleteFile")
    @PreAuthorize("hasAnyAuthority('小程序宣传管理')")
    public CommonResult deleteFile(@RequestParam("url") @NotBlank String url){

        return minioService.deleteFile(PROPAGANDA_FOLDER,url);
    }

}

