package com.example.oldtown.modules.com.controller;

import com.example.oldtown.common.service.MinioService;
import com.example.oldtown.modules.com.service.ComPoiService;
import com.example.oldtown.modules.com.model.ComPoi;
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
 * 通用设施 控制器
 * </p>
 * @author dyp
 * @since 2020-11-06
 */
@RestController
@RequestMapping("/com/comPoi")
@Api(value = "ComPoiController", tags = "通用设施相关")
@Validated
public class ComPoiController {
    private final String POI_FOLDER = "com/poi/";
    // 类型暂时包括: 演出点 , 厕所 , 志愿服务站 , 报警柱 , AED急救箱 , 语音导览服务站 , 监控点 , 广播 , WIFI

    @Autowired
    MinioService minioService;
    @Autowired
    ComPoiService  comPoiService;



    @ApiOperation("分页查询通用设施")
    @GetMapping("/getAll")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
                               @RequestParam(value = "pageNum", required = false)Integer pageNum,
                               @RequestParam(value = "type",required = false) String type,
                               @RequestParam(value = "keyword",required = false) String keyword){
        return comPoiService.getAll(pageSize,pageNum,type,keyword);
    }

    @ApiOperation("根据id查询通用设施")
    @GetMapping("/getById")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult<ComPoi> getById(@RequestParam("id") @NotNull Long id){
        return comPoiService.getPoiById(id);
    }

    @ApiOperation("增加通用设施")
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('通用设施管理')")
    public CommonResult add(@Validated(ComPoi.class) ComPoi comPoi){

        return comPoiService.add(comPoi);
    }

    @ApiOperation("更新通用设施(包括禁用启用)")
    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('通用设施管理')")
    public CommonResult update(@Validated(ComPoi.class) ComPoi comPoi){

        if(comPoi.getId()==null){
            return CommonResult.failed("请输入id");
        }
        return comPoiService.update(comPoi);
    }

    @ApiOperation("删除通用设施")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('通用设施管理')")
    public CommonResult delete(@RequestParam("id") @NotNull Long id){

        return comPoiService.delete(id);
    }

    @ApiOperation("批量删除通用设施")
    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasAnyAuthority('通用设施管理')")
    public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){

        return comPoiService.batchDelete(ids);
    }

    @ApiOperation("上传通用设施的文件")
    @PostMapping("/uploadFile")
    @PreAuthorize("hasAnyAuthority('通用设施管理')")
    public CommonResult uploadFile(@RequestParam("file") @NotNull MultipartFile file){


        return minioService.uploadFile(POI_FOLDER,file);
    }

    @ApiOperation("删除通用设施的文件")
    @DeleteMapping("/deleteFile")
    @PreAuthorize("hasAnyAuthority('通用设施管理')")
    public CommonResult deleteFile(@RequestParam("url") @NotBlank String url){

        return minioService.deleteFile(POI_FOLDER,url);
    }

    @ApiOperation("通过Excel批量增加通用设施")
    @PostMapping("/batchAddWithExcel")
    @PreAuthorize("hasAnyAuthority('通用设施管理')")
    public CommonResult batchAddWithExcel(@RequestParam(value = "file") @NotNull MultipartFile file){
        return comPoiService.batchAddWithExcel(file);
    }

    @ApiOperation("根据监控点摄像头的编号查询rtmp视频地址")
    @PostMapping("/rtmpByCameraCode")
    @PreAuthorize("hasAnyAuthority('监控点监控视频查看')")
    public CommonResult rtmpByCameraCode(@RequestParam(value = "code") @NotNull String code){
        return comPoiService.rtmpByCameraCode(code);
    }

    @ApiOperation("查询当前通用设施的所有类型")
    @GetMapping("/getCurrentTypes")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult getCurrentTypes(){
        return comPoiService.getCurrentTypes();
    }




}

