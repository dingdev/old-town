package com.example.oldtown.modules.com.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.example.oldtown.common.service.MinioService;
import com.example.oldtown.component.GlobalData;
import com.example.oldtown.modules.com.service.ComPlaceService;
import com.example.oldtown.modules.com.model.ComPlace;
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
 * 通用场所 控制器
 * </p>
 * @author dyp
 * @since 2020-11-06
 */
@RestController
@RequestMapping("/com/comPlace")
@Api(value = "ComPlaceController", tags = "通用场所相关")
@Validated
public class ComPlaceController {

    private final String PLACE_FOLDER = "com/place/";

    // 类型暂时包括: 景点 , 桥道 , 度假酒店 , 精品民宿 , 餐馆 , 酒吧 , 商铺 , 停车场 , 游客休息点 , 游客中心 , 码头

    @Autowired
    ComPlaceService  comPlaceService;
    @Autowired
    MinioService minioService;

    @ApiOperation("分页查询通用场所")
    @GetMapping("/getAll")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "type",required = false) String type,
                               @RequestParam(value = "keyword",required = false) String keyword){
        return comPlaceService.getAll(pageSize,pageNum,type,keyword);
    }

    @ApiOperation("根据id查询通用场所")
    @GetMapping("/getById")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult<ComPlace> getById(@ApiIgnore Authentication authentication,
                                             @RequestParam("id") @NotNull Long id){
        Long userId = null;
        if (authentication != null && StrUtil.isNotBlank(authentication.getName())) {
            String username = authentication.getName();

            if (GlobalData.XCX_USER.equalsIgnoreCase(username.substring(0, username.indexOf(",")))) {
                userId = Long.parseLong(username.substring(username.lastIndexOf(",")+1));
            }
        }
        return comPlaceService.getPlaceById(id,userId);
    }

    @ApiOperation("增加通用场所")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('通用场所管理')")
    public CommonResult add(@Validated(ComPlace.class) ComPlace comPlace){

        return comPlaceService.add(comPlace);
    }

    @ApiOperation("更新通用场所(包括禁用启用)")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('通用场所管理')")
    public CommonResult update(@Validated(ComPlace.class) ComPlace comPlace){

        if(comPlace.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return comPlaceService.update(comPlace);
    }

    @ApiOperation("删除通用场所")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('通用场所管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return comPlaceService.delete(id);
    }

    @ApiOperation("批量删除通用场所")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('通用场所管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return comPlaceService.batchDelete(ids);
    }

    @ApiOperation("上传通用场所的文件")
    @PostMapping("/uploadFile")
    @PreAuthorize("hasAnyAuthority('通用场所管理')")
    public CommonResult uploadFile(@RequestParam("file") @NotNull MultipartFile file){

        return minioService.uploadFile(PLACE_FOLDER,file);
    }

    @ApiOperation("删除通用场所的文件")
    @DeleteMapping("/deleteFile")
    @PreAuthorize("hasAnyAuthority('通用场所管理')")
    public CommonResult deleteFile(@RequestParam("url") @NotBlank String url){

        return minioService.deleteFile(PLACE_FOLDER,url);
    }

    @ApiOperation("通过Excel批量增加通用场所")
    @PostMapping("/batchAddWithExcel")
    @PreAuthorize("hasAnyAuthority('通用场所管理')")
    public CommonResult batchAddWithExcel(@RequestParam(value = "file") @NotNull MultipartFile file){
        return comPlaceService.batchAddWithExcel(file);
    }

    @ApiOperation("查询当前通用场所的所有类型")
    @GetMapping("/getCurrentTypes")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult getCurrentTypes(){
        return comPlaceService.getCurrentTypes();
    }

    @ApiOperation("查询热门搜索关键词")
    @GetMapping("/getTopSearch")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult getTopSearch(){
        return comPlaceService.getTopSearch();
    }


}

