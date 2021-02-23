package com.example.oldtown.modules.com.controller;

import com.example.oldtown.common.service.MinioService;
import com.example.oldtown.modules.com.service.ComSpecialtyService;
import com.example.oldtown.modules.com.model.ComSpecialty;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 通用特产 控制器
 * </p>
 * @author dyp
 * @since 2020-12-01
 */
@RestController
@RequestMapping("/com/comSpecialty")
@Api(value = "ComSpecialtyController", tags = "通用特产相关")
@Validated
public class ComSpecialtyController {

    private final String SPECIALTY_FOLDER = "com/specialty/";
    @Autowired
    MinioService minioService;
    @Autowired
    ComSpecialtyService  comSpecialtyService;

    @ApiOperation("分页查询通用特产")
    @GetMapping("/getAll")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "type",required = false) String type,
                               @RequestParam(value = "keyword",required = false) String keyword){
        return comSpecialtyService.getAll(pageSize,pageNum,type,keyword);
    }

    @ApiOperation("根据id查询通用特产")
    @GetMapping("/getById")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult<ComSpecialty> getById(@RequestParam("id") @NotNull Long id){
        return comSpecialtyService.getSpecialtyById(id);
    }

    @ApiOperation("增加通用特产")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('通用特产管理')")
    public CommonResult add(@Validated(ComSpecialty.class) ComSpecialty comSpecialty){

        return comSpecialtyService.add(comSpecialty);
    }

    @ApiOperation("更新通用特产")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('通用特产管理')")
    public CommonResult update(@Validated(ComSpecialty.class) ComSpecialty comSpecialty){

        if(comSpecialty.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return comSpecialtyService.update(comSpecialty);
    }

    @ApiOperation("删除通用特产")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('通用特产管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return comSpecialtyService.delete(id);
    }

    @ApiOperation("批量删除通用特产")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('通用特产管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return comSpecialtyService.batchDelete(ids);
    }

    @ApiOperation("上传通用特产的文件")
    @PostMapping("/uploadFile")
    @PreAuthorize("hasAnyAuthority('通用特产管理')")
    public CommonResult uploadFile(@RequestParam("file") @NotNull MultipartFile file){

        return minioService.uploadFile(SPECIALTY_FOLDER,file);
    }

    @ApiOperation("删除通用特产的文件")
    @DeleteMapping("/deleteFile")
    @PreAuthorize("hasAnyAuthority('通用特产管理')")
    public CommonResult deleteFile(@RequestParam("url") @NotBlank String url){

        return minioService.deleteFile(SPECIALTY_FOLDER,url);
    }

}

