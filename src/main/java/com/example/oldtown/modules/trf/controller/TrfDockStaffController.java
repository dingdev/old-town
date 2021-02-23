package com.example.oldtown.modules.trf.controller;

import com.example.oldtown.common.service.MinioService;
import com.example.oldtown.modules.trf.service.TrfDockStaffService;
import com.example.oldtown.modules.trf.model.TrfDockStaff;
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
 * 交通接驳管理人员 控制器
 * </p>
 * @author dyp
 * @since 2020-12-07
 */
@RestController
@RequestMapping("/trf/trfDockStaff")
@Api(value = "TrfDockStaffController", tags = "交通接驳管理人员相关")
@Validated
public class TrfDockStaffController {

    private final String DOCK_STAFF_FOLDER = "trf/dockstaff/";
    @Autowired
    MinioService minioService;
    @Autowired
    TrfDockStaffService  trfDockStaffService;

    @ApiOperation("分页查询交通接驳管理人员")
    @GetMapping("/getAll")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "type",required = false) String type,
                               @RequestParam(value = "keyword",required = false) String keyword){
        return trfDockStaffService.getAll(pageSize,pageNum,type,keyword);
    }

    @ApiOperation("根据id查询交通接驳管理人员")
    @GetMapping("/getById")
    @PreAuthorize("isAuthenticated()")
    public CommonResult<TrfDockStaff> getById(@RequestParam("id") @NotNull Long id){
        return CommonResult.success(trfDockStaffService.getById(id));
    }

    @ApiOperation("增加交通接驳管理人员")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('交通接驳管理人员管理')")
    public CommonResult add(@Validated(TrfDockStaff.class) TrfDockStaff trfDockStaff){

        return trfDockStaffService.add(trfDockStaff);
    }

    @ApiOperation("更新交通接驳管理人员")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('交通接驳管理人员管理')")
    public CommonResult update(@Validated(TrfDockStaff.class) TrfDockStaff trfDockStaff){

        if(trfDockStaff.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return trfDockStaffService.update(trfDockStaff);
    }

    @ApiOperation("删除交通接驳管理人员")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('交通接驳管理人员管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return trfDockStaffService.delete(id);
    }

    @ApiOperation("批量删除交通接驳管理人员")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('交通接驳管理人员管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return trfDockStaffService.batchDelete(ids);
    }

    @ApiOperation("上传交通接驳管理人员的文件")
    @PostMapping("/uploadFile")
    @PreAuthorize("hasAnyAuthority('交通接驳管理人员管理')")
    public CommonResult uploadFile(@RequestParam("file") @NotNull MultipartFile file){

        return minioService.uploadFile(DOCK_STAFF_FOLDER,file);
    }

    @ApiOperation("删除交通接驳管理人员的文件")
    @DeleteMapping("/deleteFile")
    @PreAuthorize("hasAnyAuthority('交通接驳管理人员管理')")
    public CommonResult deleteFile(@RequestParam("url") @NotBlank String url){

        return minioService.deleteFile(DOCK_STAFF_FOLDER,url);
    }

    @ApiOperation("按类型统计交通接驳管理人员")
    @GetMapping("/countByType")
    @PreAuthorize("isAuthenticated()")
    public CommonResult countByType(){
        return trfDockStaffService.countByType();
    }
}

