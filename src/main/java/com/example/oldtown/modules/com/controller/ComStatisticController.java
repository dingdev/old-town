package com.example.oldtown.modules.com.controller;

import com.example.oldtown.common.api.CommonResult;
import com.example.oldtown.modules.com.model.ComRoute;
import com.example.oldtown.modules.com.service.ComFestivalService;
import com.example.oldtown.modules.com.service.ComStatisticService;
import com.example.oldtown.util.ShendaSchedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author ding.yp
 * @name
 * @info
 * @date 2020/12/28
 */

@RestController
@RequestMapping("/com/comStatistic")
@Api(value = "ComStatisticController", tags = "通用统计相关")
@Validated
public class ComStatisticController {

    private final Logger LOGGER = LoggerFactory.getLogger(ComStatisticController.class);

    @Autowired
    ComStatisticService comStatisticService;
    @Autowired
    ComFestivalService comFestivalService;

    @ApiOperation("查询游客实时人数和累计人数")
    @GetMapping("/fetchRTHeadCountData")

    // @PreAuthorize("isAuthenticated()")
    public CommonResult fetchRTHeadCountData(){
        return comStatisticService.fetchRTHeadCountData();
    }

    @ApiOperation("查询游客 当天 24 小时趋势")
    @GetMapping("/fetchPeopleFlow")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult fetchPeopleFlow(){
        return comStatisticService.fetchPeopleFlow();
    }

    @ApiOperation("查询游客七天趋势")
    @GetMapping("/fetchHistoryPeopleFlow")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult fetchHistoryPeopleFlow(){
        return comStatisticService.fetchHistoryPeopleFlow();
    }

    @ApiOperation("查询游客驻留时长构成")
    @GetMapping("/fetchResidentDuration")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult fetchResidentDuration(){
        return comStatisticService.fetchResidentDuration();
    }

    @ApiOperation("查询游客年龄构成")
    @GetMapping("/fetchAgeStructure")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult fetchAgeStructure(){
        return comStatisticService.fetchAgeStructure();
    }

    @ApiOperation("查询游客性别构成")
    @GetMapping("/fetchGenderStructure")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult fetchGenderStructure(){
        return comStatisticService.fetchGenderStructure();
    }

    @ApiOperation("查询游客来源省份构成")
    @GetMapping("/fetchOriginProvince")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult fetchOriginProvince(){
        return comStatisticService.fetchOriginProvince();
    }

    @ApiOperation("查询游客省内来源地市构成")
    @GetMapping("/fetchOriginCity")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult fetchOriginCity(){
        return comStatisticService.fetchOriginCity();
    }

    @ApiOperation("查询游客来源国籍构成")
    @GetMapping("/fetchOrginCountry")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult fetchOrginCountry(){
        return comStatisticService.fetchOriginCountry();
    }

    @ApiOperation("查询停车信息")
    @GetMapping("/fetchParkingStastics")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult fetchParkingStastics() {
        return comStatisticService.fetchParkingStastics();
    }

    @ApiOperation("查询游船销售信息")
    @GetMapping("/fetchShipSaleStastics")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult fetchShipSaleStastics(@RequestParam(value = "type", required = true) String type,
                                              @RequestParam(value = "startDate", required = true) Date startDate,
                                              @RequestParam(value = "endDate", required = true) Date endDate) {
        LOGGER.debug(type);
        if (!("Year".equals(type) || "Month".equals(type)  ||"Day".equals(type) )){
            return  CommonResult.failed("type必须是Year、Month或Day,大小写敏感");
        }

        return comStatisticService.fetchShipSaleStastics(type, startDate, endDate);
    }


    @ApiOperation("查询门票销售信息")
    @GetMapping("/fetchTicketSaleStastics")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult fetchTicketSaleStastics(@RequestParam(value = "startDate", required = true) Date startDate,
                                              @RequestParam(value = "endDate", required = true) Date endDate) {

        return comStatisticService.fetchTicketSaleStastics(startDate, endDate);
    }

    @ApiOperation("查询近两年节假日数据")
    @GetMapping("/fetchFestivalData")
    // @PreAuthorize("isAuthenticated()")
    public CommonResult fetchFestivalData(@RequestParam(value = "year",required = false) Integer year) {

        return comFestivalService.fetchFestivalData(year);
    }

}
