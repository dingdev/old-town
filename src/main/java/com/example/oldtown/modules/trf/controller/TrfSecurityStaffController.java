package com.example.oldtown.modules.trf.controller;

import com.example.oldtown.common.service.MinioService;
import com.example.oldtown.modules.trf.service.TrfSecurityStaffService;
import com.example.oldtown.modules.trf.model.TrfSecurityStaff;
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
 * 交通接驳安保人员 控制器
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
@RestController
@RequestMapping("/trf/trfSecurityStaff")
@Api(value = "TrfSecurityStaffController", tags = "交通接驳安保人员相关")
@Validated
public class TrfSecurityStaffController {

    private final String SECURITY_STAFF_FOLDER = "trf/securitystaff/";
    @Autowired
    MinioService minioService;
    @Autowired
    TrfSecurityStaffService  trfSecurityStaffService;



    @ApiOperation("分页查询交通接驳安保人员")
    @GetMapping("/getAll")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "type",required = false) String type,
                               @RequestParam(value = "ifCaptain",required = false) Integer ifCaptain,
                               @RequestParam(value = "keyword",required = false) String keyword,
                               @RequestParam(value = "orderByCurrentStatus",required = false,defaultValue = "0") Integer orderByCurrentStatus){
        return trfSecurityStaffService.getAll(pageSize,pageNum,type,ifCaptain,keyword,orderByCurrentStatus);
    }

    @ApiOperation("根据id查询交通接驳安保人员")
    @GetMapping("/getById")
    @PreAuthorize("isAuthenticated()")
    public CommonResult<TrfSecurityStaff> getById(@RequestParam("id") @NotNull Long id){
        return trfSecurityStaffService.getSecurityStaffById(id);
    }

    @ApiOperation("增加交通接驳安保人员")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('交通接驳安保人员管理')")
    public CommonResult add(@Validated(TrfSecurityStaff.class) TrfSecurityStaff trfSecurityStaff){

        return trfSecurityStaffService.add(trfSecurityStaff);
    }

    @ApiOperation("更新交通接驳安保人员")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('交通接驳安保人员管理')")
    public CommonResult update(@Validated(TrfSecurityStaff.class) TrfSecurityStaff trfSecurityStaff){

        if(trfSecurityStaff.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return trfSecurityStaffService.update(trfSecurityStaff);
    }

    @ApiOperation("删除交通接驳安保人员")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('交通接驳安保人员管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return trfSecurityStaffService.delete(id);
    }

    @ApiOperation("批量删除交通接驳安保人员")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('交通接驳安保人员管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return trfSecurityStaffService.batchDelete(ids);
    }

    @ApiOperation("上传交通接驳安保人员的文件")
    @PostMapping("/uploadFile")
    @PreAuthorize("hasAnyAuthority('交通接驳安保人员管理')")
    public CommonResult uploadFile(@RequestParam("file") @NotNull MultipartFile file){

        return minioService.uploadFile(SECURITY_STAFF_FOLDER,file);
    }

    @ApiOperation("删除交通接驳安保人员的文件")
    @DeleteMapping("/deleteFile")
    @PreAuthorize("hasAnyAuthority('交通接驳安保人员管理')")
    public CommonResult deleteFile(@RequestParam("url") @NotBlank String url){

        return minioService.deleteFile(SECURITY_STAFF_FOLDER,url);
    }

    @ApiOperation("按类型统计交通接驳安保人员")
    @GetMapping("/countByType")
    @PreAuthorize("isAuthenticated()")
    public CommonResult countByType(){
        return trfSecurityStaffService.countByType();
    }


}

