package com.example.oldtown.modules.com.service;

import com.example.oldtown.common.api.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

/**
 * @author ding.yp
 * @name
 * @info
 * @date 2020/12/29
 */


public interface ComStatisticService {

    /**
     * 查询游客实时人数和累计人数
     */
    public CommonResult fetchRTHeadCountData();

    /**
     * 查询游客当天 24 小时趋势
     */
    public CommonResult fetchPeopleFlow();

    /**
     * 查询游客七天趋势
     */
    public CommonResult fetchHistoryPeopleFlow();

    /**
     * 查询游客驻留时长构成
     */
    public CommonResult fetchResidentDuration();

    /**
     * 查询游客年龄构成
     */
    public CommonResult fetchAgeStructure();

    /**
     * 查询游客性别构成
     */
    public CommonResult fetchGenderStructure();

    /**
     * 查询游客来源省份构成
     */
    public CommonResult fetchOriginProvince();

    /**
     * 查询游客省内来源地市构成
     */
    public CommonResult fetchOriginCity();

    /**
     * 查询游客来源国籍构成
     */
    public CommonResult fetchOriginCountry();


    /**
     * 查询停车统计信息
     */
    public CommonResult fetchParkingStastics();

    /**
     * 查询游船销售信息
     */
    public CommonResult fetchShipSaleStastics(String type, Date startDate, Date endDate);

    /**
     * 查询门票销售信息
     */
    public CommonResult fetchTicketSaleStastics(Date startDate, Date endDate);
}
