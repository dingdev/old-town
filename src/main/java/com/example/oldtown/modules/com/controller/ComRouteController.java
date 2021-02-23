package com.example.oldtown.modules.com.controller;

import cn.hutool.core.util.StrUtil;
import com.example.oldtown.common.service.MinioService;
import com.example.oldtown.component.GlobalData;
import com.example.oldtown.modules.com.service.ComRouteService;
import com.example.oldtown.modules.com.model.ComRoute;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import  com.example.oldtown.common.api.CommonResult;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * <p>
 * 通用游线 控制器
 * </p>
 * @author dyp
 * @since 2020-11-13
 */
@RestController
@RequestMapping("/com/comRoute")
@Api(value = "ComRouteController", tags = "通用游线相关")
@Validated
public class ComRouteController {

    private final String ROUTE_FOLDER = "com/route/";
    @Autowired
    MinioService minioService;
    @Autowired
    ComRouteService  comRouteService;


    @ApiOperation("分页查询通用游线")
    @GetMapping("/getAll")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "keyword",required = false) String keyword){
        return comRouteService.getAll(pageSize,pageNum,keyword);
    }

    @ApiOperation("根据id查询通用游线")
    @GetMapping("/getById")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult<ComRoute> getById(@ApiIgnore Authentication authentication,
                                          @RequestParam("id") @NotNull Long id){
        Long userId = null;
        if (authentication != null && StrUtil.isNotBlank(authentication.getName())) {
            String username = authentication.getName();

            if (GlobalData.XCX_USER.equalsIgnoreCase(username.substring(0, username.indexOf(",")))) {
                userId = Long.parseLong(username.substring(username.lastIndexOf(",")+1));
            }
        }
        return comRouteService.getRouteById(id,userId);
    }

    @ApiOperation("增加通用游线")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('通用游线管理')")
    public CommonResult add(@Validated(ComRoute.class) ComRoute comRoute){

        return comRouteService.add(comRoute);
    }

    @ApiOperation("更新通用游线(包括禁用启用)")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('通用游线管理')")
    public CommonResult update(@Validated(ComRoute.class) ComRoute comRoute){

        if(comRoute.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return comRouteService.update(comRoute);
    }

    @ApiOperation("删除通用游线")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('通用游线管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return comRouteService.delete(id);
    }

    @ApiOperation("批量删除通用游线")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('通用游线管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return comRouteService.batchDelete(ids);
    }

    @ApiOperation("查询通用游线包含的通用场所")
    @GetMapping("/getContent")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult getContent(@RequestParam("id") @NotNull Long id){
        return CommonResult.success(comRouteService.getContent(id));
    }

    @ApiOperation("上传通用游线的文件")
    @PostMapping("/uploadFile")
    @PreAuthorize("hasAnyAuthority('通用游线管理')")
    public CommonResult uploadFile(@RequestParam("file") @NotNull MultipartFile file){

        return minioService.uploadFile(ROUTE_FOLDER,file);
    }

    @ApiOperation("删除通用游线的文件")
    @DeleteMapping("/deleteFile")
    @PreAuthorize("hasAnyAuthority('通用游线管理')")
    public CommonResult deleteFile(@RequestParam("url") @NotBlank String url){

        return minioService.deleteFile(ROUTE_FOLDER,url);
    }

    @ApiOperation("查询通用游线的所有标签")
    @GetMapping("/getLabelList")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult getLabelList(){
        return CommonResult.success(comRouteService.getLabelList());
    }


}

