// package com.example.oldtown.modules.com.controller;
//
// import com.example.oldtown.modules.com.service.ComFestivalService;
// import com.example.oldtown.modules.com.model.ComFestival;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.*;
//
// import javax.validation.constraints.NotBlank;
// import javax.validation.constraints.NotEmpty;
// import javax.validation.constraints.NotNull;
// import  com.example.oldtown.common.api.CommonResult;
// import java.util.List;
//
// import org.springframework.web.bind.annotation.RestController;
//
// /**
//  * <p>
//  * 通用节假日数据 控制器
//  * </p>
//  * @author dyp
//  * @since 2021-01-14
//  */
// @RestController
// @RequestMapping("/com/comFestival")
// @Api(value = "ComFestivalController", tags = "通用节假日数据相关")
// @Validated
// public class ComFestivalController {
//
//
//     @Autowired
//     ComFestivalService  comFestivalService;
//
//     @ApiOperation("分页查询通用节假日数据")
//     @GetMapping("/getAll")
//     @PreAuthorize("isAuthenticated()")
//     public CommonResult getAll(@RequestParam(value = "pageSize", required = false, defaultValue = "10")Integer pageSize,
//                                @RequestParam(value = "pageNum", required = false, defaultValue = "1")Integer pageNum,
//                                @RequestParam(value = "keyword",required = false) String keyword){
//         return CommonResult.success(comFestivalService.getAll(pageSize,pageNum,keyword));
//     }
//
//     @ApiOperation("根据id查询通用节假日数据")
//     @GetMapping("/getById")
//     @PreAuthorize("isAuthenticated()")
//     public CommonResult<ComFestival> getById(@RequestParam("id") @NotNull Long id){
//         return CommonResult.success(comFestivalService.getById(id));
//     }
//
//     @ApiOperation("增加通用节假日数据")
//     @PostMapping("/add")
//     @PreAuthorize("hasAnyAuthority('通用节假日数据管理')")
//     public CommonResult add(@Validated(ComFestival.class) ComFestival comFestival){
//
//         return comFestivalService.add(comFestival);
//     }
//
//     @ApiOperation("更新通用节假日数据")
//     @PostMapping("/update")
//     @PreAuthorize("hasAnyAuthority('通用节假日数据管理')")
//     public CommonResult update(@Validated(ComFestival.class) ComFestival comFestival){
//
//         if(comFestival.getId()==null){
//             return CommonResult.failed("请输入id");
//         }
//         return comFestivalService.update(comFestival);
//     }
//
//     @ApiOperation("删除通用节假日数据")
//     @DeleteMapping("/delete")
//     @PreAuthorize("hasAnyAuthority('通用节假日数据管理')")
//     public CommonResult delete(@RequestParam("id") @NotNull Long id){
//
//         return comFestivalService.delete(id);
//     }
//
//     @ApiOperation("批量删除通用节假日数据")
//     @DeleteMapping("/batchDelete")
//     @PreAuthorize("hasAnyAuthority('通用节假日数据管理')")
//     public CommonResult batchDelete(@RequestParam("ids") @NotEmpty List<Long> ids){
//
//         return comFestivalService.batchDelete(ids);
//     }
//
// }
//
