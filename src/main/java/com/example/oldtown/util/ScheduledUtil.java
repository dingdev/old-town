package com.example.oldtown.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.math.MathUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.sql.visitor.functions.Char;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.oldtown.common.api.CommonResult;
import com.example.oldtown.common.service.MinioService;
import com.example.oldtown.component.GlobalData;
import com.example.oldtown.dto.MinTrfVehicle;
import com.example.oldtown.dto.StringIntegerDTO;
import com.example.oldtown.modules.trf.mapper.*;
import com.example.oldtown.modules.trf.model.*;
import com.example.oldtown.modules.xcx.model.XcxNews;
import com.example.oldtown.modules.xcx.service.XcxNewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ding.yp
 * @name
 * @info
 * @date 2020/12/03
 */

@Component
@Async
public class ScheduledUtil {
    private final Logger LOGGER = LoggerFactory.getLogger(ScheduledUtil.class);

    private static String TRF_GPS_TOKEN = "";
    private static final String CURRENT_STATUS = "行驶";
    private static final String TRF_YACHT = "trf_yacht";
    private static final String TRF_SWEEP = "trf_sweep";
    private static final String TRF_SECURITY_CAR = "trf_security_car";

    // 移通数据
    private static final String pcode = "3cc90005729003";
    private static final String pkey = "c093ccd4348a48468cf4a6d600887e7d";
    private static final String regionId = "57NX25DR";
    private static final Integer areaCode = 547;
    private Double RT_NUM= 0.0;
    private static final Double POINT_DIFF = 0.0003;

    // rtmp转发
    private static final String NMS_REDIS_GROUP = "nms:";
    private static final String NMS_IP = "192.168.1.104";
    private static final String NMS_USERNAME = "admin";
    private static final String NMS_PASSWORD = "admin";
    private static final List<Integer> NMS_STATUS_OK = Arrays.asList(1, 2);

    // 小程序
    @Value("${wechat.app-id}")
    private String APP_ID;
    @Value("${wechat.app-secret}")
    private String APP_SECRET;

    // 公众号
    @Value("${official.app-id}")
    private String OFFICIAL_APP_ID;
    @Value("${official.app-secret}")
    private String OFFICIAL_APP_SECRET;

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    MinioService minioService;
    @Autowired
    XcxNewsService xcxNewsService;
    @Resource
    TrfGpsMapper trfGpsMapper;
    @Resource
    TrfYachtMapper trfYachtMapper;
    @Resource
    TrfSweepMapper trfSweepMapper;
    @Resource
    TrfSecurityCarMapper trfSecurityCarMapper;
    @Resource
    TrfSweepRouteMapper trfSweepRouteMapper;
    @Resource
    TrfSweepPointMapper trfSweepPointMapper;
    @Resource
    TrfSweepCheckMapper trfSweepCheckMapper;
    @Resource
    TrfSweepStaffMapper trfSweepStaffMapper;
    @Resource
    TrfMileageMapper trfMileageMapper;
    @Resource
    TrfAlarmMapper trfAlarmMapper;
    @Resource
    TrfSecurityStaffMapper trfSecurityStaffMapper;


    /**
     * 程序启动后更新一次 缓存
     */
    @PostConstruct
    public void pullCacheOnStart() {
        pullAccessTokenAndJsapiTicket();
        pullOfficialAccessToken();
        pullNmsToken();
        pullTrfGpsToken();
        pullWeather();
        pullTrfGpsCode();
    }

    /**
     * 每天1点更新 打捞人员 是否完成了工作
     */
    @Scheduled(cron = "0 1 1 * * ? ")
    public void pushTrfSecurityCheck() {
        try {
            Long to = System.currentTimeMillis() / 1000 - 3660;
            Long from = to - 86400;
            Date date = new Date();
            // HashOperations<String, String, Integer> hashOperations = redisTemplate.opsForHash();
            // Set<String> gpsCodeSet = hashOperations.keys("trfSweepGPS");

            // 查出打捞船
            QueryWrapper<TrfSweep> trfSweepQueryWrapper = new QueryWrapper<>();
            trfSweepQueryWrapper.lambda().isNotNull(TrfSweep::getStaffId).isNotNull(TrfSweep::getRouteId).eq(TrfSweep::getDeleted, 0);
            List<TrfSweep> trfSweepList = trfSweepMapper.selectList(trfSweepQueryWrapper);


            for (TrfSweep trfSweep : trfSweepList) {
                // 判断打捞路线对应的打捞点是否存在
                Long routeId = trfSweep.getRouteId();
                QueryWrapper<TrfSweepPoint> trfSweepPointQueryWrapper = new QueryWrapper<>();
                trfSweepPointQueryWrapper.lambda().eq(TrfSweepPoint::getRouteId, routeId).eq(TrfSweepPoint::getDeleted, 0);
                List<TrfSweepPoint> trfSweepPointList = trfSweepPointMapper.selectList(trfSweepPointQueryWrapper);
                if (trfSweepPointList == null || trfSweepPointList.size() == 0) {
                    continue;
                }

                // 查询轨迹点
                String gpsCode = trfSweep.getGpsCode();
                QueryWrapper<TrfGps> trfGpsQueryWrapper = new QueryWrapper<>();
                trfGpsQueryWrapper.lambda().gt(TrfGps::getGpsTime, from).lt(TrfGps::getGpsTime, to).eq(TrfGps::getGpsCode, gpsCode);
                List<TrfGps> trfGpsList = trfGpsMapper.selectList(trfGpsQueryWrapper);

                Integer passNum = 0; // 合格打捞点的数量
                Long nowId = 0L; // 当前打捞点id

                for (TrfGps trfGps : trfGpsList) {
                    Double lng = trfGps.getLongitude();
                    Double lat = trfGps.getLatitude();
                    if (lng == null || lat == null) {
                        continue;
                    }

                    for (TrfSweepPoint trfSweepPoint : trfSweepPointList) {
                        Double lng0 = trfSweepPoint.getLongitude();
                        Double lat0 = trfSweepPoint.getLatitude();

                        if (!nowId.equals(trfSweepPoint.getId()) && lng < lng0 + POINT_DIFF && lng > lng0 - POINT_DIFF && lat < lat0 + POINT_DIFF && lat > lat0 - POINT_DIFF) {
                            trfSweepPoint.setDeleted(trfSweepPoint.getDeleted() + 1);
                            nowId = trfSweepPoint.getId();
                            break;
                        }
                    }
                }

                for (TrfSweepPoint trfSweepPoint2 : trfSweepPointList) {
                    if (trfSweepPoint2.getDeleted().compareTo(trfSweepPoint2.getDailyTimes()) > 0) {
                        passNum = passNum + 1;
                    }
                }

                // 增加一条不合格记录
                if (passNum < trfSweepPointList.size()) {

                    TrfSweepStaff trfSweepStaff = trfSweepStaffMapper.selectById(trfSweep.getStaffId());

                    TrfSweepCheck trfSweepCheck = new TrfSweepCheck();
                    trfSweepCheck.setDate(date);
                    trfSweepCheck.setStaffId(trfSweepStaff.getId());
                    trfSweepCheck.setUsername(trfSweepStaff.getUsername());
                    trfSweepCheck.setSerial(trfSweepStaff.getSerial());
                    trfSweepCheck.setTel(trfSweepStaff.getTel());
                    trfSweepCheck.setReason("总打捞点" + trfSweepPointList.size() + "个,已完成打捞点" + passNum + "个");
                    trfSweepCheck.setRouteId(routeId);
                    trfSweepCheck.setSweepId(trfSweep.getId());
                    trfSweepCheck.setGpsCode(gpsCode);

                    trfSweepCheckMapper.insert(trfSweepCheck);
                }
            }


        } catch (Exception e) {
            LOGGER.error("定时更新 打捞人员考核 失败:", e);
        }
    }


    /**
     * 每2小时更新 nmsToken
     */
    @Scheduled(cron = "10 0 0/2 * * ? ")
    // @Scheduled(initialDelay =3000,fixedRate = 7200000)
    public void pullNmsToken() {
        try {
            JSONObject body = new JSONObject();
            body.put("username", NMS_USERNAME);
            body.put("password", SecureUtil.md5(NMS_PASSWORD));
            String nmsToken = HttpUtil.post("http://" + NMS_IP + ":8000/api/login", body.toString());
            // HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
            if (StrUtil.isNotBlank(nmsToken) && nmsToken.startsWith("{")) {
                JSONObject object = JSONUtil.parseObj(nmsToken);
                if (object.getInt("code") == 200) {
                    GlobalData.setNmsToken(object.getJSONObject("data").getStr("token"));
                } else {
                    System.out.println("获取nmsToken失败:" + object.toString());
                }
            }

        } catch (Exception e) {
            LOGGER.error("定时更新 nmsToken 失败:", e);
        }
    }

    /**
     * 每2小时更新 南浔古镇天气 缓存
     */
    @Scheduled(cron = "0 0 4-23/2 * * ? ")
    // @Scheduled(initialDelay = 6000,cron = "0 0 4-23/2 * * ? ")
    public void pullWeather() {
        try {
            // 基本参数

            String url = "https://www.tianqiapi.com/api/?appid=73767761&appsecret=VE9uieYn&version=v6&cityid=101210206";

            // 请求外部数据
            String responseString = HttpUtil.get(url);
            if (StrUtil.isNotBlank(responseString)) {
                JSONObject weather = JSONUtil.parseObj(responseString);

                if (weather != null) {
                    HashOperations<String, String, JSONObject> hashOperations = redisTemplate.opsForHash();
                    // 缓存
                    hashOperations.put("weatherHashMap", "1", weather);
                }
            }
        } catch (Exception e) {
            LOGGER.error("定时更新 南浔古镇天气 缓存失败:", e);
        }
    }

    /**
     * 每天1点更新 gpsCode的缓存 和行驶状态
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void pullTrfGpsCode() {
        try {
            Long timestamp = System.currentTimeMillis() / 1000;
            HashOperations<String, String, MinTrfVehicle> hashOperations = redisTemplate.opsForHash();
            // 游船
            List<MinTrfVehicle> yachtList = trfYachtMapper.getAllGpsCode(timestamp);
            Map<String, MinTrfVehicle> yachtMap = yachtList.stream().collect(Collectors.toMap(MinTrfVehicle::getGpsCode, minTrfVehicle -> minTrfVehicle ));
            if (!hashOperations.keys("trfYachtGPS").isEmpty()) {
                hashOperations.delete("trfYachtGPS", hashOperations.keys("trfYachtGPS").toArray());
            }
            hashOperations.putAll("trfYachtGPS", yachtMap);
            // 行驶状态
            trfYachtMapper.nullCurrentStatus();

            // 打捞船
            List<MinTrfVehicle> sweepList = trfSweepMapper.getAllGpsCode(timestamp);
            Map<String, MinTrfVehicle> sweepMap = sweepList.stream().collect(Collectors.toMap(MinTrfVehicle::getGpsCode, minTrfVehicle -> minTrfVehicle));
            if (!hashOperations.keys("trfSweepGPS").isEmpty()) {
                hashOperations.delete("trfSweepGPS", hashOperations.keys("trfSweepGPS").toArray());
            }
            hashOperations.putAll("trfSweepGPS", sweepMap);
            // 行驶状态
            trfSweepMapper.nullCurrentStatus();

            // 安保车辆
            List<MinTrfVehicle> securityCarList = trfSecurityCarMapper.getAllGpsCode(timestamp);
            Map<String, MinTrfVehicle> securityCarMap =
                    securityCarList.stream().collect(Collectors.toMap(MinTrfVehicle::getGpsCode, minTrfVehicle -> minTrfVehicle));
            if (!hashOperations.keys("trfSecurityCarGPS").isEmpty()) {
                hashOperations.delete("trfSecurityCarGPS", hashOperations.keys("trfSecurityCarGPS").toArray());
            }
            hashOperations.putAll("trfSecurityCarGPS", securityCarMap);
            // 行驶状态
            trfSecurityCarMapper.nullCurrentStatus();

            // 安保人员的行驶状态
            trfSecurityStaffMapper.nullCurrentStatus();

        } catch (Exception e) {
            LOGGER.error("定时更新 gpsCode的缓存 失败:", e);
        }
    }

    /**
     * 每1小时更新 trfGpsToken
     */
    @Scheduled(cron = "1 0 0/1 * * ? ")
    // @Scheduled(initialDelay = 4000,fixedRate = 3600000)
    public void pullTrfGpsToken() {
        try {
            String appid = "中测新图";
            String key = "fa$2AX1M$Pbi#3TWW%7nS0jyRN!kgDbt";
            Long time = System.currentTimeMillis() / 1000;
            String md5 = SecureUtil.md5(key);
            String signature = SecureUtil.md5(md5 + time);
            Map<String, Object> req = new HashMap<>();
            req.put("appid", appid);
            req.put("time", time);
            req.put("signature", signature);
            String res = HttpUtil.post("http://open.gumigps.com/api/auth", JSONUtil.toJsonStr(req));
            if (StrUtil.isNotBlank(res)) {
                JSONObject json = JSONUtil.parseObj(res);
                if (json.getInt("code") == 0) {
                    if (json.getStr("accessToken") != null) {
                        TRF_GPS_TOKEN = json.getStr("accessToken");
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("定时更新 trfGpsToken 失败:", e);
        }
    }


    /**
     * 每天1点更新 游船里程
     */
    @Scheduled(cron = "10 0 1 * * ? ")
    public void pullTrfYachtMileage() {
        try {
            Long endTime = System.currentTimeMillis() / 1000 - 3610;
            Long startTime = endTime - 86400;
            Date date = new Date(startTime*1000);

            HashOperations<String, String, MinTrfVehicle> hashOperations = redisTemplate.opsForHash();
            Set<String> keySet = hashOperations.keys("trfYachtGPS");

            for (String imei : keySet) {
                String res = HttpUtil.get("http://open.gumigps.com/api/device/miles?accessToken=" + TRF_GPS_TOKEN
                        + "&startTime=" + startTime + "&endTime=" + endTime + "&imei=" + imei);
                if (StrUtil.isNotBlank(res) && res.startsWith("{")) {
                    JSONObject object0 = JSONUtil.parseObj(res);
                    if (object0.getInt("code") == 0) {
                        trfMileageMapper.insert(new TrfMileage(null,hashOperations.get("trfYachtGPS",imei).getName(),"游船", imei, object0.getDouble("miles"), date));
                    }
                } else {
                    LOGGER.info("每天1点更新 游船里程失败, res : " + res);
                }
            }
        } catch (Exception e) {
            LOGGER.error("每天1点更新 游船里程 失败:", e);
        }
    }

    /**
     * 每天1点更新 打捞船里程
     */
    @Scheduled(cron = "15 0 1 * * ? ")
    public void pullTrfSweepMileage() {
        try {
            Long endTime = System.currentTimeMillis() / 1000 - 3615;
            Long startTime = endTime - 86400;
            Date date = new Date(startTime*1000);

            HashOperations<String, String, MinTrfVehicle> hashOperations = redisTemplate.opsForHash();
            Set<String> keySet = hashOperations.keys("trfSweepGPS");

            for (String imei : keySet) {
                String res = HttpUtil.get("http://open.gumigps.com/api/device/miles?accessToken=" + TRF_GPS_TOKEN
                        + "&startTime=" + startTime + "&endTime=" + endTime + "&imei=" + imei);
                if (StrUtil.isNotBlank(res) && res.startsWith("{")) {
                    JSONObject object0 = JSONUtil.parseObj(res);
                    if (object0.getInt("code") == 0) {
                        trfMileageMapper.insert(new TrfMileage(null,hashOperations.get("trfSweepGPS",imei).getName(),"打捞船", imei, object0.getDouble("miles"), date));
                    }
                } else {
                    LOGGER.info("每天1点更新 打捞船里程失败, res : " + res);
                }
            }
        } catch (Exception e) {
            LOGGER.error("每天1点更新 打捞船里程 失败:", e);
        }
    }

    /**
     * 每天1点更新 安保车辆里程
     */
    @Scheduled(cron = "20 0 1 * * ? ")
    public void pullTrfSecurityCarMileage() {
        try {
            Long endTime = System.currentTimeMillis() / 1000 - 3620;
            Long startTime = endTime - 86400;
            Date date = new Date(startTime*1000);

            HashOperations<String, String, MinTrfVehicle> hashOperations = redisTemplate.opsForHash();
            Set<String> keySet = hashOperations.keys("trfSecurityCarGPS");

            for (String imei : keySet) {
                String res = HttpUtil.get("http://open.gumigps.com/api/device/miles?accessToken=" + TRF_GPS_TOKEN
                        + "&startTime=" + startTime + "&endTime=" + endTime + "&imei=" + imei);
                if (StrUtil.isNotBlank(res) && res.startsWith("{")) {
                    JSONObject object0 = JSONUtil.parseObj(res);
                    if (object0.getInt("code") == 0) {
                        trfMileageMapper.insert(new TrfMileage(null,hashOperations.get("trfSecurityCarGPS",imei).getName(),"安保车辆", imei, object0.getDouble("miles"), date));
                    }
                } else {
                    LOGGER.info("每天1点更新 安保车辆里程失败, res : " + res);
                }
            }
        } catch (Exception e) {
            LOGGER.error("每天1点更新 安保车辆里程 失败:", e);
        }
    }

    /**
     * 每天1点更新 游船报警信息
     */
    @Scheduled(cron = "25 0 1 * * ? ")
    public void pullTrfYachtAlarm() {
        try {
            Long endTime = System.currentTimeMillis() / 1000 - 3625;
            Long startTime = endTime - 86400;
            Date date = new Date(startTime*1000);

            HashOperations<String, String, MinTrfVehicle> hashOperations = redisTemplate.opsForHash();
            Set<String> keySet = hashOperations.keys("trfYachtGPS");

            for (String imei : keySet) {
                String res = HttpUtil.get("http://open.gumigps.com/api/device/alarm?accessToken=" + TRF_GPS_TOKEN
                        + "&startTime=" + startTime + "&endTime=" + endTime + "&imei=" + imei);
                if (StrUtil.isNotBlank(res) && res.startsWith("{")) {
                    JSONObject object0 = JSONUtil.parseObj(res);
                    if (object0.getInt("code") == 0) {
                        MinTrfVehicle minTrfVehicle = hashOperations.get("trfYachtGPS", imei);
                        JSONArray jsonArray = object0.getJSONArray("details");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            if (object1 != null) {
                                trfAlarmMapper.insert(new TrfAlarm(null,TRF_YACHT,minTrfVehicle.getId(),minTrfVehicle.getName(),
                                        imei,object1.getLong("alarmTime"),object1.getDouble("lng"),
                                        object1.getDouble("lat"),object1.getStr("alarmCode")));
                            }
                        }
                    }
                } else {
                    LOGGER.info("每天1点更新 游船报警信息失败, res : " + res);
                }
            }
        } catch (Exception e) {
            LOGGER.error("每天1点更新 游船报警信息 失败:", e);
        }
    }

    /**
     * 每天1点更新 打捞船报警信息
     */
    @Scheduled(cron = "30 0 1 * * ? ")
    public void pullTrfSweepAlarm() {
        try {
            Long endTime = System.currentTimeMillis() / 1000 - 3630;
            Long startTime = endTime - 86400;
            Date date = new Date(startTime*1000);

            HashOperations<String, String, MinTrfVehicle> hashOperations = redisTemplate.opsForHash();
            Set<String> keySet = hashOperations.keys("trfSweepGPS");

            for (String imei : keySet) {
                String res = HttpUtil.get("http://open.gumigps.com/api/device/alarm?accessToken=" + TRF_GPS_TOKEN
                        + "&startTime=" + startTime + "&endTime=" + endTime + "&imei=" + imei);
                if (StrUtil.isNotBlank(res) && res.startsWith("{")) {
                    JSONObject object0 = JSONUtil.parseObj(res);
                    if (object0.getInt("code") == 0) {
                        MinTrfVehicle minTrfVehicle = hashOperations.get("trfSweepGPS", imei);
                        JSONArray jsonArray = object0.getJSONArray("details");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            if (object1 != null) {
                                trfAlarmMapper.insert(new TrfAlarm(null,TRF_SWEEP,minTrfVehicle.getId(),minTrfVehicle.getName(),
                                        imei,object1.getLong("alarmTime"),object1.getDouble("lng"),
                                        object1.getDouble("lat"),object1.getStr("alarmCode")));
                            }
                        }
                    }
                } else {
                    LOGGER.info("每天1点更新 打捞船报警信息失败, res : " + res);
                }
            }
        } catch (Exception e) {
            LOGGER.error("每天1点更新 打捞船报警信息 失败:", e);
                      }
    }

    /**
     * 每天1点更新 安保车辆报警信息
     */
    @Scheduled(cron = "35 0 1 * * ? ")
    public void pullTrfSecurityCarAlarm() {
        try {
            Long endTime = System.currentTimeMillis() / 1000 - 3635;
            Long startTime = endTime - 86400;
            Date date = new Date(startTime*1000);

            HashOperations<String, String, MinTrfVehicle> hashOperations = redisTemplate.opsForHash();
            Set<String> keySet = hashOperations.keys("trfSecurityCarGPS");

            for (String imei : keySet) {
                String res = HttpUtil.get("http://open.gumigps.com/api/device/alarm?accessToken=" + TRF_GPS_TOKEN
                        + "&startTime=" + startTime + "&endTime=" + endTime + "&imei=" + imei);
                if (StrUtil.isNotBlank(res) && res.startsWith("{")) {
                    JSONObject object0 = JSONUtil.parseObj(res);
                    if (object0.getInt("code") == 0) {
                        MinTrfVehicle minTrfVehicle = hashOperations.get("trfSecurityCarGPS", imei);
                        JSONArray jsonArray = object0.getJSONArray("details");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            if (object1 != null) {
                                trfAlarmMapper.insert(new TrfAlarm(null,TRF_SECURITY_CAR,minTrfVehicle.getId(),minTrfVehicle.getName(),
                                        imei,object1.getLong("alarmTime"),object1.getDouble("lng"),
                                        object1.getDouble("lat"),object1.getStr("alarmCode")));
                            }
                        }
                    }
                } else {
                    LOGGER.info("每天1点更新 安保车辆报警信息失败, res : " + res);
                }
            }
        } catch (Exception e) {
            LOGGER.error("每天1点更新 安保车辆报警信息 失败:", e);
        }
    }

    /**
     * 每1分钟更新 游船实时位置
     */
    @Scheduled(cron = "5 0/3 4-23 * * ? ")
    public void pullTrfYachtReal() {
        try {

            // trfYachtMapper.nullCurrentStatus();

            HashOperations<String, String, MinTrfVehicle> hashOperations = redisTemplate.opsForHash();
            Set<String> keySet = hashOperations.keys("trfYachtGPS");
            if (keySet != null && keySet.size() > 0) {
                String imeiSet = keySet.toString();
                String imei = imeiSet.substring(imeiSet.indexOf("[") + 1, imeiSet.indexOf("]")).replaceAll(", ", ",");
                String res = HttpUtil.get("http://open.gumigps.com/api/device/status?accessToken=" + TRF_GPS_TOKEN + "&imei=" + imei);
                if (StrUtil.isNotBlank(res) && res.startsWith("{")) {
                    JSONObject object0 = JSONUtil.parseObj(res);
                    if (object0.getInt("code") == 0) {
                        JSONArray jsonArray = object0.getJSONArray("data");
                        if (jsonArray != null && jsonArray.size() > 0) {

                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                String gpsCode = object1.getStr("imei");
                                Long gpsTime = object1.getLong("gpsTime");
                                Double longitude = object1.getDouble("lng");
                                Double latitude = object1.getDouble("lat");
                                String status = object1.getStr("status");
                                Double speed = object1.getDouble("speed");
                                MinTrfVehicle minTrfVehicle = hashOperations.get("trfYachtGPS", gpsCode);

                                if (StrUtil.isNotBlank(gpsCode) && minTrfVehicle.getGpsTime() < gpsTime) {
                                    trfYachtMapper.updatePoint(longitude, latitude, gpsCode, status,gpsTime);
                                    if (!longitude.equals(minTrfVehicle.getLongitude())) {
                                        trfGpsMapper.insertTrfGps(gpsCode, gpsTime, longitude, latitude, speed);
                                        minTrfVehicle.setLongitude(longitude);
                                     }
                                    minTrfVehicle.setGpsTime(gpsTime);
                                    hashOperations.put("trfYachtGPS", gpsCode, minTrfVehicle);
                                }
                            }
                        }
                    }
                } else {
                    LOGGER.info("每1分钟更新 游船实时位置失败, res : " + res);
                }
            }

        } catch (Exception e) {
            LOGGER.error("定时更新 游船实时位置 失败:", e);
        }
    }

    /**
     * 每1分钟更新 打捞船实时位置
     */
    @Scheduled(cron = "10 1/3 4-23 * * ? ")
    public void pullTrfSweepReal() {
        try {

            // trfSweepMapper.nullCurrentStatus();

            HashOperations<String, String, MinTrfVehicle> hashOperations = redisTemplate.opsForHash();
            Set<String> keySet = hashOperations.keys("trfSweepGPS");
            if (keySet != null && keySet.size() > 0) {
                String imeiSet = keySet.toString();
                String imei = imeiSet.substring(imeiSet.indexOf("[") + 1, imeiSet.indexOf("]")).replaceAll(", ", ",");
                String res = HttpUtil.get("http://open.gumigps.com/api/device/status?accessToken=" + TRF_GPS_TOKEN + "&imei=" + imei);
                if (StrUtil.isNotBlank(res) && res.startsWith("{")) {
                    JSONObject object0 = JSONUtil.parseObj(res);
                    if (object0.getInt("code") == 0) {
                        JSONArray jsonArray = object0.getJSONArray("data");
                        if (jsonArray != null && jsonArray.size() > 0) {

                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                String gpsCode = object1.getStr("imei");
                                Long gpsTime = object1.getLong("gpsTime");
                                Double longitude = object1.getDouble("lng");
                                Double latitude = object1.getDouble("lat");
                                String status = object1.getStr("status");
                                Double speed = object1.getDouble("speed");
                                MinTrfVehicle minTrfVehicle = hashOperations.get("trfSweepGPS", gpsCode);

                                if (StrUtil.isNotBlank(gpsCode) && minTrfVehicle.getGpsTime() < gpsTime) {
                                    trfSweepMapper.updatePoint(longitude, latitude, gpsCode, status,gpsTime);
                                    if (!longitude.equals(minTrfVehicle.getLongitude())) {
                                        trfGpsMapper.insertTrfGps(gpsCode, gpsTime, longitude, latitude, speed);
                                        minTrfVehicle.setLongitude(longitude);
                                    }
                                    minTrfVehicle.setGpsTime(gpsTime);
                                    hashOperations.put("trfSweepGPS", gpsCode, minTrfVehicle);
                                }
                            }
                        }
                    }
                } else {
                    LOGGER.info("每1分钟更新 打捞船实时位置失败, res : " + res);
                }
            }
        } catch (Exception e) {
            LOGGER.error("定时更新 打捞船实时位置 失败:", e);
        }
    }

    /**
     * 每1分钟更新 安保车辆实时位置
     */
    @Scheduled(cron = "15 2/3 4-23 * * ? ")
    public void pullTrfSecurityCarReal() {
        try {

            // trfSecurityCarMapper.nullCurrentStatus();

            HashOperations<String, String, MinTrfVehicle> hashOperations = redisTemplate.opsForHash();
            Set<String> keySet = hashOperations.keys("trfSecurityCarGPS");
            if (keySet != null && keySet.size() > 0) {
                String imeiSet = keySet.toString();
                String imei = imeiSet.substring(imeiSet.indexOf("[") + 1, imeiSet.indexOf("]")).replaceAll(", ", ",");
                String res = HttpUtil.get("http://open.gumigps.com/api/device/status?accessToken=" + TRF_GPS_TOKEN + "&imei=" + imei);
                if (StrUtil.isNotBlank(res) && res.startsWith("{")) {
                    JSONObject object0 = JSONUtil.parseObj(res);
                    if (object0.getInt("code") == 0) {
                        JSONArray jsonArray = object0.getJSONArray("data");
                        if (jsonArray != null && jsonArray.size() > 0) {

                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                String gpsCode = object1.getStr("imei");
                                Long gpsTime = object1.getLong("gpsTime");
                                Double longitude = object1.getDouble("lng");
                                Double latitude = object1.getDouble("lat");
                                String status = object1.getStr("status");
                                Double speed = object1.getDouble("speed");
                                MinTrfVehicle minTrfVehicle = hashOperations.get("trfSecurityCarGPS", gpsCode);

                                if (StrUtil.isNotBlank(gpsCode) && minTrfVehicle.getGpsTime() < gpsTime) {
                                    trfSecurityCarMapper.updatePoint(longitude, latitude, gpsCode, status,gpsTime);
                                    if (!longitude.equals(minTrfVehicle.getLongitude())) {
                                        trfGpsMapper.insertTrfGps(gpsCode, gpsTime, longitude, latitude, speed);
                                        minTrfVehicle.setLongitude(longitude);
                                    }
                                    minTrfVehicle.setGpsTime(gpsTime);
                                    hashOperations.put("trfSecurityCarGPS", gpsCode, minTrfVehicle);
                                }
                            }
                        }
                    }
                } else {
                    LOGGER.info("每1分钟更新 安保车辆实时位置失败, res : " + res);
                }
            }

        } catch (Exception e) {
            LOGGER.error("定时更新 安保车辆实时位置 失败:", e);
        }
    }

    /**
     * 每10分钟更新 景区人数等移通数据
     */
    @Scheduled(cron = "20 0/10 4-23 * * ? ")
    public void pullYiTong() {
        try {

            Long timestamp = System.currentTimeMillis() / 1000;
            HashOperations<String, String, JSONObject> hashOperations = redisTemplate.opsForHash();

            // 当天 24 小时趋势
            String res2 = HttpUtil.get("https://efence.ytbig.cn/zoneaiapi/fetchPeopleFlow?regionId=" + regionId + "&pcode=" + pcode + "&pkey=" + pkey + "&timestamp=" + timestamp);
            if (StrUtil.isNotBlank(res2)) {
                JSONObject object2 = JSONUtil.parseObj(res2);
                if ("0".equals(object2.getStr("responseCode"))) {
                    hashOperations.put("yiTong", "fetchPeopleFlow", object2);
                }
            }

            // 七天趋势
            String res3 = HttpUtil.get("https://efence.ytbig.cn/zoneaiapi/fetchHistoryPeopleFlow?regionId=" + regionId + "&pcode=" + pcode + "&pkey=" + pkey + "&timestamp=" + timestamp);
            if (StrUtil.isNotBlank(res3)) {
                JSONObject object3 = JSONUtil.parseObj(res3);
                if ("0".equals(object3.getStr("responseCode"))) {
                    hashOperations.put("yiTong", "fetchHistoryPeopleFlow", object3);
                }
            }

            //驻留时长构成
            String res5 = HttpUtil.get("https://efence.ytbig.cn/zoneaiapi/fetchResidentDuration?regionId=" + regionId + "&pcode=" + pcode + "&pkey=" + pkey + "&timestamp=" + timestamp);
            if (StrUtil.isNotBlank(res5)) {
                JSONObject object5 = JSONUtil.parseObj(res5);
                if ("0".equals(object5.getStr("responseCode"))) {
                    hashOperations.put("yiTong", "fetchResidentDuration", object5);
                }
            }

            // 年龄构成
            String res6 = HttpUtil.get("https://efence.ytbig.cn/zoneaiapi/fetchAgeStructure?regionId=" + regionId + "&pcode=" + pcode + "&pkey=" + pkey + "&timestamp=" + timestamp);
            if (StrUtil.isNotBlank(res6)) {
                JSONObject object6 = JSONUtil.parseObj(res6);
                if ("0".equals(object6.getStr("responseCode"))) {
                    hashOperations.put("yiTong", "fetchAgeStructure", object6);
                }
            }


            // 性别构成
            String res7 = HttpUtil.get("https://efence.ytbig.cn/zoneaiapi/fetchGenderStructure?regionId=" + regionId + "&pcode=" + pcode + "&pkey=" + pkey + "&timestamp=" + timestamp);
            if (StrUtil.isNotBlank(res7)) {
                JSONObject object7 = JSONUtil.parseObj(res7);
                if ("0".equals(object7.getStr("responseCode"))) {
                    hashOperations.put("yiTong", "fetchGenderStructure", object7);
                }
            }

            // 来源省份构成
            String res8 = HttpUtil.get("https://efence.ytbig.cn/zoneaiapi/fetchOrginProvince?regionId=" + regionId + "&pcode=" + pcode + "&pkey=" + pkey + "&timestamp=" + timestamp);
            if (StrUtil.isNotBlank(res7)) {
                JSONObject object8 = JSONUtil.parseObj(res8);
                if ("0".equals(object8.getStr("responseCode"))) {
                    hashOperations.put("yiTong", "fetchOrginProvince", object8);
                }
            }

            // 省内来源地市构成
            String res9 = HttpUtil.get("https://efence.ytbig.cn/zoneaiapi/fetchOrginCity?regionId=" + regionId + "&pcode=" + pcode + "&pkey=" + pkey + "&timestamp=" + timestamp);
            if (StrUtil.isNotBlank(res9)) {
                JSONObject object9 = JSONUtil.parseObj(res9);
                if ("0".equals(object9.getStr("responseCode"))) {
                    JSONArray jsonArray = object9.getJSONArray("responseData");
                    RT_NUM = jsonArray.getJSONObject(0).getDouble("NUM");
                    hashOperations.put("yiTong", "fetchOrginCity", object9);
                }
            }

            // 来源国籍构成
            String res10 = HttpUtil.get("https://efence.ytbig.cn/zoneaiapi/fetchOrginCountry?regionId=" + regionId + "&pcode=" + pcode + "&pkey=" + pkey + "&timestamp=" + timestamp);
            if (StrUtil.isNotBlank(res10)) {
                JSONObject object10 = JSONUtil.parseObj(res10);
                if ("0".equals(object10.getStr("responseCode"))) {
                    hashOperations.put("yiTong", "fetchOrginCountry", object10);
                }
            }

            // 实时人数
            String res1 = HttpUtil.get("https://efence.ytbig.cn/zoneaiapi/fetchRTHeadCountData?regionId=" + regionId + "&pcode=" + pcode + "&pkey=" + pkey + "&timestamp=" + timestamp);
            if (StrUtil.isNotBlank(res1)) {
                JSONObject object1 = JSONUtil.parseObj(res1);
                if ("0".equals(object1.getStr("responseCode"))) {
                    JSONObject object11 = object1.getJSONObject("responseData");
                    Double NUM = object11.getDouble("NUM");
                    Double ACCUM_NUM = object11.getDouble("ACCUM_NUM");
                    object11.set("NUM",(int)(NUM - RT_NUM));
                    object11.set("ACCUM_NUM", (int)(ACCUM_NUM  - ACCUM_NUM *( RT_NUM / NUM)));
                    object1.set("responseData", object11);
                    hashOperations.put("yiTong", "fetchRTHeadCountData", object1);
                }
            }

        } catch (Exception e) {
            LOGGER.error("定时更新 景区人数等移通数据 失败:", e);
        }
    }


    /**
     * 定时更新公众号资讯
     */
    @Scheduled(cron = "20 10 9,18 * * ? ")
    public void pullXcxNews() {

        try {
            // String result1 = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" +
            //         appid + "&secret=" + secret);
            // Map map_ree = JSONUtil.parseObj(result1);
            // String access_token = (String) map_ree.get("access_token");

            String ip = "https://api.weixin.qq.com/cgi-bin/material/batchget_material";
            List<XcxNews> xcxNewsList = new ArrayList<>();
            Map<String, String> map = new HashMap();
            map.put("type", "news");
            map.put("offset", "0");
            map.put("count", "3");
            String json = JSONUtil.toJsonStr(map);
            String result = HttpUtil.post(ip + "?access_token=" + GlobalData.OFFICIAL_ACCESS_TOKEN, json);
            result = new String(result.getBytes("ISO-8859-1"), "utf-8");

            Map map1 = JSONUtil.parseObj(result);

            List list = (List) map1.get("item");
            if (list == null || list.size() < 1) {
                LOGGER.warn("定时更新 小程序资讯 失败, item list 为空!");
            } else {
                Date date = new Date();
                for (int k = 0; k < list.size(); k++) {
                    Map map2 = (Map) list.get(k);

                    Map map3 = (Map) map2.get("content");
                    List list1 = (List) map3.get("news_item");
                    for (int l = 0; l < list1.size(); l++) {
                        Map map4 = (Map) list1.get(l);

                        XcxNews xcxNews = new XcxNews();
                        if ("".equals((String) map4.get("url"))) {
                            continue;
                        } else if ("".equals((String) map4.get("title"))) {
                            continue;
                        } else if ("".equals((String) map4.get("thumb_url"))) {
                            continue;
                        }
                        xcxNews.setTitle((String) map4.get("title"));
                        xcxNews.setAuthor((String) map4.get("author"));
                        xcxNews.setDigest((String) map4.get("digest"));
                        xcxNews.setWebUrl((String) map4.get("url"));
                        xcxNews.setCoverUrl((String) map4.get("thumb_url"));
                        xcxNews.setCreateTime(date);
                        xcxNewsList.add(xcxNews);
                    }

                }
                if (xcxNewsList != null && xcxNewsList.size() > 0) {
                    // 去重
                    xcxNewsList = xcxNewsList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                            new TreeSet<>(Comparator.comparing(xcxNews -> xcxNews.getTitle()))), ArrayList::new));
                    xcxNewsList = xcxNewsList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                            new TreeSet<>(Comparator.comparing(xcxNews -> xcxNews.getCoverUrl()))), ArrayList::new));

                    xcxNewsService.saveBatch(xcxNewsList);
                }
                LOGGER.info("定时更新 小程序资讯 成功.");
            }
        } catch (Exception e) {
            LOGGER.warn("定时更新 小程序资讯 失败:", e);
        }
    }

    /**
     * 每1小时更新 小程序access_token和jsapi_ticket
     */
    @Scheduled(cron = "3 0 0/1 * * ? ")
    // @Scheduled(initialDelay = 1000,fixedRate = 3600000)
    public void pullAccessTokenAndJsapiTicket() {
        try {
            String res = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" +
                    APP_ID + "&secret=" + APP_SECRET);
            if (StrUtil.isNotBlank(res) && res.startsWith("{")) {
                JSONObject object = JSONUtil.parseObj(res);
                String access_token = object.getStr("access_token");
                if (StrUtil.isNotBlank(access_token)) {
                    // HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
                    // hashOperations.put("devInfo", "access_token", access_token);

                    String res2 = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
                            + access_token + "&type=jsapi");
                    if (StrUtil.isNotBlank(res2)) {
                        JSONObject object2 = JSONUtil.parseObj(res2);
                        String jsapi_ticket = object2.getStr("ticket");
                        if (StrUtil.isNotBlank(jsapi_ticket)) {
                            GlobalData.setJsapiTicket(jsapi_ticket);
                        }
                    }
                } else {
                    LOGGER.error("定时更新 小程序access_token和jsapi_ticket 失败,res:", res);
                }
            }else {
                LOGGER.error("定时更新 小程序access_token和jsapi_ticket 失败,res:", res);
            }
        } catch (Exception e) {
            LOGGER.error("定时更新 小程序access_token和jsapi_ticket 失败:", e);
        }
    }

    /**
     * 每1小时更新 公众号access_token
     */
    @Scheduled(cron = "4 0 0/1 * * ? ")
    // @Scheduled(initialDelay = 2000,fixedRate = 3600000)
    public void pullOfficialAccessToken() {
        try {
            String res = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" +
                    OFFICIAL_APP_ID + "&secret=" + OFFICIAL_APP_SECRET);
            if (StrUtil.isNotBlank(res) && res.startsWith("{")) {
                JSONObject object = JSONUtil.parseObj(res);
                String access_token = object.getStr("access_token");
                if (StrUtil.isNotBlank(access_token)) {
                    GlobalData.setOfficialAccessToken(access_token);
                }else {
                    LOGGER.error("定时更新 公众号access_token 失败,res:",res);
                }
            }else {
                LOGGER.error("定时更新 公众号access_token 失败,res:",res);
            }
        } catch (Exception e) {
            LOGGER.error("定时更新 公众号access_token 失败:", e);
        }
    }

    /**
     * 每30分钟更新 nms转流任务状态
     */
    @Scheduled(cron = "22 0/30 * * * ? ")
    public void pullNmsStatus() {
        try {
            String result1 = HttpRequest.get("http://" + NMS_IP + ":8000/api/relays")
                    .header(Header.AUTHORIZATION, GlobalData.NMS_TOKEN).execute().body();
            if (StrUtil.isNotBlank(result1) && result1.startsWith("{")) {
                JSONObject object1 = JSONUtil.parseObj(result1);
                if (object1.getInt("code").equals(200)) {
                    JSONObject object2 = object1.getJSONObject("data");
                    for (String nmsId : object2.keySet()) {
                        if (NMS_STATUS_OK.contains(object2.getJSONObject(nmsId).getInt("status"))) {
                            HttpRequest.delete("http://" + NMS_IP + ":8000/api/relay/" + nmsId)
                                    .header(Header.AUTHORIZATION, GlobalData.NMS_TOKEN).execute();
                            redisTemplate.delete(NMS_REDIS_GROUP + nmsId);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("定时更新 nms转流任务状态 失败:", e);
        }
    }

}
