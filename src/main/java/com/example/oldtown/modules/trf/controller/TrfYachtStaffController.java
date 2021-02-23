package com.example.oldtown.modules.trf.controller;

import com.example.oldtown.common.service.MinioService;
import com.example.oldtown.modules.trf.service.TrfYachtStaffService;
import com.example.oldtown.modules.trf.model.TrfYachtStaff;
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
 * 交通接驳游船员工 控制器
 * </p>
 *
 * @author dyp
 * @since 2020-11-25
 */
@RestController
@RequestMapping("/trf/trfYachtStaff")
@Api(value = "TrfYachtStaffController", tags = "交通接驳游船员工相关")
@Validated
public class TrfYachtStaffController {

    private final String YACHT_STAFF_FOLDER = "trf/yachtstaff/";
    @Autowired
    MinioService minioService;
    @Autowired
    TrfYachtStaffService trfYachtStaffService;

    @ApiOperation("分页查询交通接驳游船员工")
    @GetMapping("/getAll")
    @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "pageNum", required = false) Integer pageNum,
                               @RequestParam(value = "type", required = false) String type,
                               @RequestParam(value = "keyword", required = false) String keyword) {
        return trfYachtStaffService.getAll(pageSize, pageNum, type, keyword);
    }

    @ApiOperation("根据id查询交通接驳游船员工")
    @GetMapping("/getById")
    @PreAuthorize("isAuthenticated()")
    public CommonResult<TrfYachtStaff> getById(@RequestParam("id") @NotNull Long id) {
        return trfYachtStaffService.getYachtStaffById(id);
    }

    @ApiOperation("增加交通接驳游船员工")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('交通接驳游船员工管理')")
    public CommonResult add(@Validated(TrfYachtStaff.class) TrfYachtStaff trfYachtStaff) {

        return trfYachtStaffService.add(trfYachtStaff);
    }

    @ApiOperation("更新交通接驳游船员工")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('交通接驳游船员工管理')")
    public CommonResult update(@Validated(TrfYachtStaff.class) TrfYachtStaff trfYachtStaff) {

        if (trfYachtStaff.getId() == null) {
            return CommonResult.failed("请输入id");
        }
        return trfYachtStaffService.update(trfYachtStaff);
    }

    @ApiOperation("删除交通接驳游船员工")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('交通接驳游船员工管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id) {

        return trfYachtStaffService.delete(id);
    }

    @ApiOperation("批量删除交通接驳游船员工")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('交通接驳游船员工管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids) {

        return trfYachtStaffService.batchDelete(ids);
    }

    @ApiOperation("上传交通接驳游船员工的文件")
    @PostMapping("/uploadFile")
    @PreAuthorize("hasAnyAuthority('交通接驳游船员工管理')")
    public CommonResult uploadFile(@RequestParam("file") @NotNull MultipartFile file) {

        return minioService.uploadFile(YACHT_STAFF_FOLDER, file);
    }

    @ApiOperation("删除交通接驳游船员工的文件")
    @DeleteMapping("/deleteFile")
    @PreAuthorize("hasAnyAuthority('交通接驳游船员工管理')")
    public CommonResult deleteFile(@RequestParam("url") @NotBlank String url) {

        return minioService.deleteFile(YACHT_STAFF_FOLDER, url);
    }

    @ApiOperation("按类型统计交通接驳游船员工")
    @GetMapping("/countByType")
    @PreAuthorize("isAuthenticated()")
    public CommonResult countByType(){
        return trfYachtStaffService.countByType();
    }


}

