package com.example.oldtown.modules.com.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.oldtown.common.api.CommonResult;
import com.example.oldtown.dto.ParkingStatisDTo;
import com.example.oldtown.dto.ShipSaleDto;
import com.example.oldtown.dto.TicketSaleDto;
import com.example.oldtown.modules.com.service.ComStatisticService;
import com.example.oldtown.util.ShendaSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author ding.yp
 * @name
 * @info
 * @date 2020/12/29
 */

@Service
public class ComStatisticServiceImpl implements ComStatisticService {

    private final Logger LOGGER = LoggerFactory.getLogger(ComStatisticServiceImpl.class);
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ShendaSchedule shendaSchedule;

    /**
     * 查询游客实时人数和累计人数
     */
    @Override
    public CommonResult fetchRTHeadCountData() {
        HashOperations<String, String, JSONObject> hashOperations = redisTemplate.opsForHash();
        JSONObject jsonObject = hashOperations.get("yiTong", "fetchRTHeadCountData");
        if (jsonObject != null) {
            return CommonResult.success(jsonObject);
        } else {
            return CommonResult.failed("查询游客实时人数和累计人数失败");
        }
    }

    /**
     * 查询游客当天 24 小时趋势
     */
    @Override
    public CommonResult fetchPeopleFlow() {
        HashOperations<String, String, JSONObject> hashOperations = redisTemplate.opsForHash();
        JSONObject jsonObject = hashOperations.get("yiTong", "fetchPeopleFlow");
        if (jsonObject != null) {
            return CommonResult.success(jsonObject);
        } else {
            return CommonResult.failed("查询游客当天 24 小时趋势失败");
        }
    }

    /**
     * 查询游客七天趋势
     */
    @Override
    public CommonResult fetchHistoryPeopleFlow() {
        HashOperations<String, String, JSONObject> hashOperations = redisTemplate.opsForHash();
        JSONObject jsonObject = hashOperations.get("yiTong", "fetchHistoryPeopleFlow");
        if (jsonObject != null) {
            return CommonResult.success(jsonObject);
        } else {
            return CommonResult.failed("查询游客七天趋势失败");
        }
    }

    /**
     * 查询游客驻留时长构成
     */
    @Override
    public CommonResult fetchResidentDuration() {
        HashOperations<String, String, JSONObject> hashOperations = redisTemplate.opsForHash();
        JSONObject jsonObject = hashOperations.get("yiTong", "fetchResidentDuration");
        if (jsonObject != null) {
            return CommonResult.success(jsonObject);
        } else {
            return CommonResult.failed("查询游客驻留时长构成");
        }
    }

    /**
     * 查询游客年龄构成
     */
    @Override
    public CommonResult fetchAgeStructure() {
        HashOperations<String, String, JSONObject> hashOperations = redisTemplate.opsForHash();
        JSONObject jsonObject = hashOperations.get("yiTong", "fetchAgeStructure");
        if (jsonObject != null) {
            return CommonResult.success(jsonObject);
        } else {
            return CommonResult.failed("查询游客年龄构成");
        }
    }

    /**
     * 查询游客性别构成
     */
    @Override
    public CommonResult fetchGenderStructure() {
        HashOperations<String, String, JSONObject> hashOperations = redisTemplate.opsForHash();
        JSONObject jsonObject = hashOperations.get("yiTong", "fetchGenderStructure");
        if (jsonObject != null) {
            return CommonResult.success(jsonObject);
        } else {
            return CommonResult.failed("查询游客性别构成");
        }
    }

    /**
     * 查询游客来源省份构成
     */
    @Override
    public CommonResult fetchOriginProvince() {
        HashOperations<String, String, JSONObject> hashOperations = redisTemplate.opsForHash();
        JSONObject jsonObject = hashOperations.get("yiTong", "fetchOrginProvince");
        if (jsonObject != null) {
            return CommonResult.success(jsonObject);
        } else {
            return CommonResult.failed("查询游客来源省份构成");
        }
    }

    /**
     * 查询游客省内来源地市构成
     */
    @Override
    public CommonResult fetchOriginCity() {
        HashOperations<String, String, JSONObject> hashOperations = redisTemplate.opsForHash();
        JSONObject jsonObject = hashOperations.get("yiTong", "fetchOrginCity");
        if (jsonObject != null) {
            return CommonResult.success(jsonObject);
        } else {
            return CommonResult.failed("查询游客省内来源地市构成");
        }
    }

    /**
     * 查询游客来源国籍构成
     */
    @Override
    public CommonResult fetchOriginCountry() {
        HashOperations<String, String, JSONObject> hashOperations = redisTemplate.opsForHash();
        JSONObject jsonObject = hashOperations.get("yiTong", "fetchOrginCountry");
        if (jsonObject != null) {
            return CommonResult.success(jsonObject);
        } else {
            return CommonResult.failed("查询游客来源国籍构成");
        }
    }


    /**
     * 查询停车统计信息
     */
    @Override
    public CommonResult fetchParkingStastics(){

        ValueOperations<String, JSONObject> valueOperations = redisTemplate.opsForValue();
        JSONObject jsonObject = valueOperations.get("shenda:fetchParkingStastics");
        if (jsonObject != null) {
            return CommonResult.success(jsonObject);
        } else {
            ParkingStatisDTo dto = shendaSchedule.pullParkingStatic();
            if(dto != null){
                valueOperations.set("shenda:fetchParkingStastics", JSONUtil.parseObj(dto));
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MINUTE,3);
                redisTemplate.expireAt("shenda:fetchParkingStastics",  c.getTime());
                return CommonResult.success(dto);

            }else {
                return CommonResult.failed("查询停车统计信息");
            }
        }

    }

    /**
     * 查询游船销售信息
     */
    @Override
    public CommonResult fetchShipSaleStastics(String type, Date startDate, Date endDate){
        ValueOperations<String, JSONObject> valueOperations = redisTemplate.opsForValue();

        String start = GetKeyFromDate(type,startDate);
        String end = GetKeyFromDate(type,endDate);
        String key = String.format("shenda:shipSaleType%s_%s",start,end);
        JSONObject jsonObject = valueOperations.get(key);
        if(jsonObject!=null){
            return CommonResult.success(jsonObject);
        } else {
            ShipSaleDto dto = shendaSchedule.pullShipSaleStatic(type,start,end);
            if(dto != null){
                valueOperations.set(key, JSONUtil.parseObj(dto));

                Calendar c = Calendar.getInstance();
                c.add(Calendar.HOUR,2);
                redisTemplate.expireAt(key,  c.getTime());
                return CommonResult.success(dto);

            }else {
                return CommonResult.failed("查询停车统计信息");
            }
        }

    }

    /**
     * 查询门票销售信息
     */
    @Override
    public CommonResult fetchTicketSaleStastics(Date startDate, Date endDate){

        TicketSaleDto dto = shendaSchedule.pullTicketsSaleStatic(DateUtil.format(startDate,"yyyy-MM-dd")
                ,DateUtil.format(endDate,"yyyy-MM-dd"));
        if(dto != null){
            return CommonResult.success(dto);
        }else {
            return CommonResult.failed("查询停车统计信息");
        }
    }

    private String GetKeyFromDate(String type,Date date){
        SimpleDateFormat myFmt = null;
        switch (type){
            case "Year":
                myFmt = new SimpleDateFormat("yyyy");
                break;
            case "Month":
            case "Day":
                myFmt = new SimpleDateFormat("yyyy-MM-dd");
                break;
        }
        return  myFmt.format(date);
    }
}
