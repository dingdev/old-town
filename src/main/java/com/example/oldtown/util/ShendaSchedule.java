package com.example.oldtown.util;


import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.oldtown.dto.ParkingStatisDTo;
import com.example.oldtown.dto.ShipSaleDto;
import com.example.oldtown.dto.TicketSaleDto;
import com.example.oldtown.modules.com.mapper.ComPlaceMapper;
import com.example.oldtown.modules.com.model.ComPlace;
import com.mysql.cj.xdevapi.JsonArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author wei.p
 * @name 深大数据获取类
 * @info 获取停车场闸机及门票数据
 * @date 2021/1/8
 */

@Component
@Async
public class ShendaSchedule {

    private final Logger LOGGER = LoggerFactory.getLogger(ShendaSchedule.class);

    private String shendaToken = "";
    private static final String baseUrl = "http://61.153.180.66:8082";
    private static final String username = "nanxun";
    private static final String password = "123456";

    @Resource
    ComPlaceMapper comPlaceMapper;

//    RedisTemplate redisTemplate;
//
//    @Autowired
//     ShendaSchedule(RedisTemplate redisTemplate){
//        this.redisTemplate = redisTemplate;
//
//        //init();
//    }

    @PostConstruct
    private void init() {
        pullShendaToken();//启动时调用一下请求token
        // pullParkingList();//请求一下车位数据
        // pullScenicDataList();//请求一下景点数据
//        HashOperations<String, String, Integer> hashOperations = redisTemplate.opsForHash();
//        Object val = hashOperations.keys("Parking");
//
//
//        LOGGER.debug("------");
//        LOGGER.debug(val.toString());
    }


    /**
     * 每1小时更新 Token
     */
    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void pullShendaToken() {
        try {
            // 深大登录页面有验证码，但接口验证码不传也可以，不确定以后是否会变
            Long timestamp = System.currentTimeMillis() / 1000;
            String url = String.format("%s/api/user/login.htm?username=%s&password=%s&_=%d", baseUrl, username, SecureUtil.md5(password), timestamp);
            String res = HttpUtil.get(url);
            if (StrUtil.isNotBlank(res)) {
                JSONObject json = JSONUtil.parseObj(res);
                if (json.getBool("success")) {
                    if (json.getStr("token") != null) {
                        this.shendaToken = json.getStr("token");
                        return;
                    }
                }
            }
            LOGGER.warn("获取 Token 失败:", new Exception(res));
        } catch (Exception e) {
            LOGGER.error("定时更新 trfGpsToken 失败:", e);
        }
    }


    /**
     * 获取停车场实时车位数据
     */
    @Scheduled(cron = "30 0/5 * * * ? ") //获取token后3秒再获取车位信息
    public void pullParkingList() {
        try {
            // 深大登录页面有验证码，但接口验证码不传也可以，不确定以后是否会变
            Long timestamp = System.currentTimeMillis() / 1000;
            Date day = new Date();
            String url = String.format("%s/api/parking/getParkingList.htm?scenicId=425&beginDate=%tF&endDate=%tF&token=%s&_=%d",
                    baseUrl, day, day, shendaToken, timestamp);
            String res = HttpUtil.get(url);
            if (StrUtil.isNotBlank(res)) {
                LOGGER.debug(res);
                JSONArray jsonArray = JSONUtil.parseArray(res);
                if (jsonArray.size() > 0) {
//                    HashOperations<String, String, Integer> hashOperations = redisTemplate.opsForHash();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        String code = json.getStr("parkingCode");
                        Integer totalNum = json.getInt("totalNum", -1);
                        Integer leftTotalNum = json.getInt("leftTotalNum", -1);

                        if (StrUtil.isNotBlank(code) && code != null && leftTotalNum > -1 && totalNum > -1) {
                            comPlaceMapper.updateCurrentNumByCode("停车场", code, totalNum - leftTotalNum);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("获取停车场列表失败:", e);
        }
    }

    /**
     * 获取景点实时数据
     */
    @Scheduled(cron = "35 0/5 * * * ? ") //获取token后5秒再获取景点信息
    public void pullScenicDataList() {
        try { // 深大登录页面有验证码，但接口验证码不传也可以，不确定以后是否会变
            Long timestamp = System.currentTimeMillis() / 1000;
            Date day = new Date();
            String url = String.format("%s/api/check/listScenicData.htm?scenicId=425&beginDate=%tF&endDate=%tF&scenicCode=&token=%s&_=%d",
                    baseUrl, day, day, shendaToken, timestamp);
            String res = HttpUtil.get(url);
            if (StrUtil.isNotBlank(res)) {
                LOGGER.debug("获取景点实时数据");
                LOGGER.debug(res);
                JSONArray jsonArray = JSONUtil.parseArray(res);
                if (jsonArray.size() > 0) {
                    HashMap<String, Integer> scenicNums = new HashMap<String, Integer>();

                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        String code = json.getStr("scenicCode");
                        Integer checkNum = json.getInt("checkNum", -1);

                        if (StrUtil.isNotBlank(code) && code != null && checkNum > -1) {
                            scenicNums.put(code, checkNum);
                        }
                    }

                    List<ComPlace> comPlaces = GetAllPlaceWithCode("景点");
                    for (ComPlace complace : comPlaces) {
                        String code = complace.getCode();
                        Integer num = 0; //没有拉到的景点的当前人数是0
                        if (scenicNums.containsKey(code)) {
                            num = scenicNums.get(code);
                        }
                        comPlaceMapper.updateCurrentNumByCode("景点", code, num);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("获取景点列表失败:", e);
        }
    }

    public ParkingStatisDTo pullParkingStatic() {
        HashMap<Integer, Integer> numByHoursMap = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> outNumByHoursMap = new HashMap<Integer, Integer>();

        Integer count = pullAccruedNum();
        pullParkingNumAsHour(numByHoursMap, outNumByHoursMap);
        HashMap<String, Integer> durationGroup = pullParkingDuration();
        float sum = 0f;
        int totalCount = 0;
        double duration = 0d;
        for (String key : durationGroup.keySet()) {
            int number = durationGroup.get(key);//当前时段的车辆数
            float timeDuration = TimeGroupStringToTime(key);//当前时段的平均时间

            sum += timeDuration * number;
            totalCount += number;
        }
        if (totalCount == 0) {
            duration = 0;
        } else {
            duration = sum / totalCount;
        }

        return new ParkingStatisDTo(count, duration, numByHoursMap, outNumByHoursMap);
    }

    public TicketSaleDto pullTicketsSaleStatic(String startDate, String endDate) {

        TicketSaleDto dto = new TicketSaleDto();
        try {
            Long timestamp = System.currentTimeMillis() / 1000;
            String url = String.format("%s/api/tkt/sale/getSaleMonth.htm?beginDate=%s&endDate=%s&scenicId=425&token=%s&_=%d",
                    baseUrl, startDate, endDate, shendaToken, timestamp);
            String res = HttpUtil.get(url);
            dto.setDateList(new ArrayList<String>());
            dto.setThisYearSaleList(new ArrayList<Double>());
            dto.setLastYearSaleList(new ArrayList<Double>());

            if (StrUtil.isNotBlank(res)) {
                JSONObject json = JSONUtil.parseObj(res);
                if(json == null||!json.getBool("success")){
                    return dto;
                }

                JSONObject dataJson = json.getJSONObject("data");
                JSONArray xList = dataJson.getJSONArray("xList");
                JSONArray yThisList = dataJson.getJSONArray("yList").getJSONArray(0);
                JSONArray yLastList = dataJson.getJSONArray("yList").getJSONArray(1);

                if (xList.size() != yThisList.size() || xList.size() != yLastList.size()) {
                    return  dto;
                }

                for (int j = 0; j < xList.size(); j++) {
                    dto.getDateList().add(xList.getStr(j));
                    dto.getThisYearSaleList().add(yThisList.getDouble(j));
                    dto.getLastYearSaleList().add(yLastList.getDouble(j));
                }
            }
        } catch (Exception e) {
            LOGGER.error("获取景点列表失败:", e);
        }
        return  dto;
    }

    public ShipSaleDto pullShipSaleStatic(String type, String startDate, String endDate) {

        ShipSaleDto dto = new ShipSaleDto();
        try {
            Long timestamp = System.currentTimeMillis() / 1000;
            String url = String.format("%s/tkt/ship/get%sNum.htm?beginDate=%s&endDate=%s&parkingCode=&token=%s&_=%d",
                    baseUrl, type, startDate, endDate, shendaToken, timestamp);
            String res = HttpUtil.get(url);
            dto.setType(type);
            dto.setDateList(new ArrayList<String>());
            dto.setSaleAmountList(new ArrayList<Double>());
            dto.setSaleNumList(new ArrayList<Integer>());
            LOGGER.debug(res);

            if (StrUtil.isNotBlank(res)) {
                JSONObject json = JSONUtil.parseObj(res);
                if(json == null||!json.getBool("success")){
                    return dto;
                }

                JSONObject dataJson = json.getJSONObject("data");
                JSONArray xList = dataJson.getJSONArray("xList");
                JSONArray yNumberList = dataJson.getJSONArray("yList").getJSONArray(0);
                JSONArray yAmountList = dataJson.getJSONArray("yList").getJSONArray(1);

                if (xList.size() != yNumberList.size() || xList.size() != yAmountList.size()) {
                    return  dto;
                }
                for (int j = 0; j < xList.size(); j++) {
                    dto.getDateList().add(xList.getStr(j));
                    dto.getSaleNumList().add(yNumberList.getInt(j));
                    if(!"Day".equals(type)){
                        dto.getSaleAmountList().add(yAmountList.getDouble(j));
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("获取景点列表失败:", e);
        }
        return  dto;
    }

    private List<ComPlace> GetAllPlaceWithCode(String type) {
        QueryWrapper<ComPlace> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<ComPlace> lambda = wrapper.lambda();
        lambda.eq(ComPlace::getType, type);
        lambda.and(lambda1 -> lambda1.isNotNull(ComPlace::getCode));

        return comPlaceMapper.selectList(wrapper);
    }

    /**
     * 获取当日累积停车数量
     */
    private Integer pullAccruedNum() {
        try {
            Long timestamp = System.currentTimeMillis() / 1000;
            Date day = new Date();
            String url = String.format("%s/api/parking/getCompareStatis.htm??scenicId=425&beginDate=%tF&endDate=%tF&parkingCode=&token=%s&_=%d",
                    baseUrl, day, day, shendaToken, timestamp);
            String res = HttpUtil.get(url);
            if (StrUtil.isNotBlank(res)) {
                JSONArray jsonArray = JSONUtil.parseArray(res);
                if (jsonArray.size() > 0) {
                    Integer count = 0;
                    for (int i = 0; i < jsonArray.size(); i++) {

                        JSONObject json = jsonArray.getJSONObject(i).getJSONArray("data").getJSONObject(0);
                        count += json.getInt("count");
                    }
                    return count;
                }
            }
        } catch (Exception e) {
            LOGGER.error("获取景点列表失败:", e);
        }
        return 0;
    }


    /**
     * 获取当日累积停车数量
     */
    private void pullParkingNumAsHour(HashMap<Integer, Integer> inMapRef, HashMap<Integer, Integer> outMapRef) {
        try {
            Long timestamp = System.currentTimeMillis() / 1000;
            Date day = new Date();
            String url = String.format("%s/api/parking/getNumAsHalfHourChart.htm??scenicId=425&beginDate=%tF&endDate=%tF&parkingCode=&token=%s&_=%d",
                    baseUrl, day, day, shendaToken, timestamp);
            String res = HttpUtil.get(url);
            if (StrUtil.isNotBlank(res)) {
                JSONArray jsonArray = JSONUtil.parseArray(res);
                if (jsonArray.size() > 0) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject dataJson = jsonArray.getJSONObject(i).getJSONObject("data");
                        JSONArray xList = dataJson.getJSONArray("xList");
                        JSONArray yListIn = dataJson.getJSONArray("yList").getJSONArray(0);
                        JSONArray yListOut = dataJson.getJSONArray("yList").getJSONArray(1);

                        if (xList.size() != yListIn.size() || xList.size() != yListOut.size()) {
                            return;
                        }
                        for (int j = 0; j < xList.size(); j++) {
                            Integer hour = Integer.valueOf(xList.getStr(j));
                            inMapRef.put(hour, yListIn.getInt(j));
                            outMapRef.put(hour, yListOut.getInt(j));
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("获取景点列表失败:", e);
        }
    }

    /**
     * 获取停车平均驻留时间
     */
    private HashMap<String, Integer> pullParkingDuration() {
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        try {
            Long timestamp = System.currentTimeMillis() / 1000;
            Date day = new Date();
            String url = String.format("%s/api/parking/getParkingTimeGroup.htm??scenicId=425&beginDate=%tF&endDate=%tF&parkingCode=&token=%s&_=%d",
                    baseUrl, day, day, shendaToken, timestamp);
            String res = HttpUtil.get(url);
            if (StrUtil.isNotBlank(res)) {
                JSONArray jsonArray = JSONUtil.parseArray(res);
                if (jsonArray.size() > 0) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject dataJson = jsonArray.getJSONObject(i).getJSONObject("data");
                        JSONArray xList = dataJson.getJSONArray("xList");
                        JSONArray yList = dataJson.getJSONArray("yList");

                        if (xList.size() != yList.size()) {
                            return result;
                        }

                        for (int j = 0; j < xList.size(); j++) {
                            String timeGroup = xList.getStr(j);
                            result.put(timeGroup, yList.getInt(j));
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("获取景点列表失败:", e);
        }
        return result;
    }

    private float TimeGroupStringToTime(String timeGroup) {
        switch (timeGroup) {
            case "1小时以内":
                return 0.5f;
            case "1-2小时":
                return 1.5f;
            case "2-3小时":
                return 2.5f;
            case "3-4小时":
                return 3.5f;
            case "4-8小时":
                return 6f;
            case "8小时以上":
                return 12f;
            default:
                return 0f;
        }
    }


}
