package com.example.oldtown.modules.trf.controller;

import com.example.oldtown.common.service.MinioService;
import com.example.oldtown.modules.trf.service.TrfSecurityCarService;
import com.example.oldtown.modules.trf.model.TrfSecurityCar;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.example.oldtown.common.api.CommonResult;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 交通接驳安保车辆 控制器
 * </p>
 *
 * @author dyp
 * @since 2020-11-25
 */
@RestController
@RequestMapping("/trf/trfSecurityCar")
@Api(value = "TrfSecurityCarController", tags = "交通接驳安保车辆相关")
@Validated
public class TrfSecurityCarController {

    private final String SECURITY_CAR_FOLDER = "trf/securitycar/";
    @Autowired
    MinioService minioService;

    @Autowired
    TrfSecurityCarService trfSecurityCarService;

    @ApiOperation("分页查询交通接驳安保车辆")
    @GetMapping("/getAll")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "pageNum", required = false) Integer pageNum,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "orderByCurrentStatus",required = false,defaultValue = "0") Integer orderByCurrentStatus) {
        return trfSecurityCarService.getAll(pageSize, pageNum, keyword, orderByCurrentStatus);
    }

    @ApiOperation("根据id查询交通接驳安保车辆")
    @GetMapping("/getById")
    @PreAuthorize("isAuthenticated()")
    public CommonResult<TrfSecurityCar> getById(@RequestParam("id") @NotNull Long id) {
        return trfSecurityCarService.getSecurityCarById(id);
    }

    @ApiOperation("增加交通接驳安保车辆")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('交通接驳安保车辆管理')")
    public CommonResult add(@Validated(TrfSecurityCar.class) TrfSecurityCar trfSecurityCar) {

        return trfSecurityCarService.add(trfSecurityCar);
    }

    @ApiOperation("更新交通接驳安保车辆")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('交通接驳安保车辆管理')")
    public CommonResult update(@Validated(TrfSecurityCar.class) TrfSecurityCar trfSecurityCar) {

        if (trfSecurityCar.getId() == null) {
            return CommonResult.failed("请输入id");
        }
        return trfSecurityCarService.update(trfSecurityCar);
    }

    @ApiOperation("删除交通接驳安保车辆")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('交通接驳安保车辆管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id) {

        return trfSecurityCarService.delete(id);
    }

    @ApiOperation("批量删除交通接驳安保车辆")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('交通接驳安保车辆管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids) {

        return trfSecurityCarService.batchDelete(ids);
    }

    @ApiOperation("上传交通接驳安保车辆的文件")
    @PostMapping("/uploadFile")
    @PreAuthorize("hasAnyAuthority('交通接驳安保车辆管理')")
    public CommonResult uploadFile(@RequestParam("file") @NotNull MultipartFile file) {

        return minioService.uploadFile(SECURITY_CAR_FOLDER, file);
    }

    @ApiOperation("删除交通接驳安保车辆的文件")
    @DeleteMapping("/deleteFile")
    @PreAuthorize("hasAnyAuthority('交通接驳安保车辆管理')")
    public CommonResult deleteFile(@RequestParam("url") @NotBlank String url) {

        return minioService.deleteFile(SECURITY_CAR_FOLDER, url);
    }

    @ApiOperation("统计交通接驳安保车辆数量")
    @GetMapping("/countByType")
    @PreAuthorize("isAuthenticated()")
    public CommonResult countByType(){
        return trfSecurityCarService.countByType();
    }
}

