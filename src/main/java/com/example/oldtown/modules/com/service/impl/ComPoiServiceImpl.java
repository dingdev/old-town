package com.example.oldtown.modules.com.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.oldtown.common.service.RedisService;
import com.example.oldtown.component.GlobalData;
import com.example.oldtown.modules.com.model.ComPoi;
import com.example.oldtown.modules.com.mapper.ComPoiMapper;
import com.example.oldtown.modules.com.service.ComPoiService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.util.ScheduledUtil;
import com.example.oldtown.util.excel.UploadComPoiListener;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.example.oldtown.common.api.CommonResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 通用设施 服务实现类
 * </p>
 * @author dyp
 * @since 2020-11-06
 */
@Service
public class ComPoiServiceImpl extends ServiceImpl<ComPoiMapper, ComPoi> implements ComPoiService {
    private final Logger LOGGER = LoggerFactory.getLogger(ComPoiServiceImpl.class);
    private static final String NMS_REDIS_GROUP = "nms:";
    // private static final String NMS_IP = "192.168.0.222"; // linux服务器
    // private static final String NMS_IP2 = "61.153.180.66"; // linux服务器
    private static final String NMS_IP = "192.168.0.90"; // 本地服务器
    private static final String NMS_IP2 = "zjtoprs.f3322.net";// 本地服务器
    // private static final String NMS_PORT = "8001";
    private static final List<Integer> NMS_STATUS_OK = Arrays.asList(1,2);

    @Value("${minio.bucketSide}")
    private String BUCKET_SIDE;
    @Value("${spring.application.name}")
    private String APPLICATION_NAME;


    @Resource
    ComPoiMapper comPoiMapper;
    @Autowired
    ComPoiService comPoiService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    ScheduledUtil scheduledUtil;


    /**
     * 分页查询通用设施
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String type, String keyword) {

        QueryWrapper<ComPoi> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<ComPoi> lambda = wrapper.lambda();
        if (StrUtil.isNotBlank(type)) {
            if (type.contains(",")) {
                lambda.in(ComPoi::getType,type.split(","));
            } else {
                lambda.eq(ComPoi::getType,type);
            }
        }

        if (StrUtil.isNotBlank(keyword)) {
            lambda.and(lambda1 -> lambda1.like(ComPoi::getName,  keyword).or().like(ComPoi::getCode,keyword)) ;
        }
        if (pageNum == null) {
            return CommonResult.success(comPoiMapper.selectList(wrapper));
        } else {
            Page<ComPoi> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page,wrapper));
        }

    }

    /**
     * 根据id查询通用设施
     */
    @Override
    public CommonResult getPoiById(Long id) {
        ComPoi comPoi = comPoiMapper.selectById(id);
        return CommonResult.success(comPoi);
    }


    /**
     * 增加通用设施
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(ComPoi comPoi) {
        try {

            comPoi.setCreateTime(new Date());
            comPoiMapper.insert(comPoi);

            return CommonResult.success("成功增加通用设施:"+comPoi.getId(),comPoi.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加通用设施失败:"+e.getMessage());
        }
    }

    /**
     * 更新通用设施
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(ComPoi comPoi) {
        try {

            Long id = comPoi.getId();
            if (comPoiMapper.selectById(id) == null) {
            return CommonResult.failed("该通用设施不存在:"+id);
            }

            comPoiMapper.updateById(comPoi);

            return CommonResult.success("成功更新通用设施:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新通用设施失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除通用设施
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = comPoiMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除通用设施"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除通用设施失败:"+e.getMessage());
        }
    }

    /**
     * 删除通用设施
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (comPoiMapper.selectById(id) == null) {
            return CommonResult.failed("该通用设施不存在:"+id);
            }

            comPoiMapper.deleteById(id);
            return CommonResult.success("成功删除通用设施:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除通用设施失败:"+e.getMessage());
        }
    }

    /**
     * 通过Excel批量增加通用设施
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchAddWithExcel(MultipartFile file) {
        try {
            if (file == null || !GlobalData.EXCEL_TYPES.contains(file.getContentType())) {
                return CommonResult.failed("请传入后缀为xls或者xlsx的excel文件(file)");
            }
            EasyExcel.read(file.getInputStream(), ComPoi.class, new UploadComPoiListener(comPoiService)).sheet().doRead();
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("通过Excel批量增加通用设施失败:"+e.getMessage());
        }
        return CommonResult.success("成功通过Excel批量增加通用设施");
    }

    /**
     * 根据监控点摄像头的编号查询rtsp视频地址
     */
    @Override
    public CommonResult rtmpByCameraCode(String code) {
        try {
            // 先查询缓存
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String nmsRedisKey = NMS_REDIS_GROUP + code;
            String nmsId = valueOperations.get(nmsRedisKey);
            if (StrUtil.isNotBlank(nmsId)) {

                String result1 = HttpRequest.get("http://" + NMS_IP + ":8000/api/relay/" + nmsId)
                        .header(Header.AUTHORIZATION, GlobalData.NMS_TOKEN).execute().body();

                if (StrUtil.isNotBlank(result1) && result1.startsWith("{")) {
                    JSONObject object1 = JSONUtil.parseObj(result1);
                    if (object1.getInt("code").equals(200) && NMS_STATUS_OK.contains(object1.getJSONObject("data").getInt("status"))) {
                        return CommonResult.success(object1.getJSONObject("data").getStr("out_url").replace(NMS_IP, NMS_IP2),"缓存:"+nmsId);
                    } else {
                        HttpRequest.delete("http://" + NMS_IP + ":8000/api/relay/" + nmsId)
                                .header(Header.AUTHORIZATION, GlobalData.NMS_TOKEN).execute();
                        redisTemplate.delete(nmsRedisKey);
                    }
                } else {
                    redisTemplate.delete(nmsRedisKey);
                }
            }


            /**
             * STEP1：设置平台参数，根据实际情况,设置host appkey appsecret 三个参数.
             * openapi.ip=112.11.251.251
             * openapi.port=432
             * openapi.appKey=29298996
             * openapi.appSecret=Qhz3RYiXj3GbvXvFtmgl
             */
            ArtemisConfig.host = "112.11.251.251:432"; //Global.getConfig("openapi.ip")+":"+Global.getConfig("openapi.port"); // artemis网关服务器ip端口
            ArtemisConfig.appKey = "29298996";//Global.getConfig("openapi.appKey");  // 秘钥appkey
            ArtemisConfig.appSecret = "Qhz3RYiXj3GbvXvFtmgl";//Global.getConfig("openapi.appSecret");// 秘钥appSecret
            /**
             * STEP2：设置OpenAPI接口的上下文
             */
            final String ARTEMIS_PATH = "/artemis";
            /**
             * STEP3：设置接口的URI地址
             */
            final String previewURLsApi = ARTEMIS_PATH + "/api/video/v1/cameras/previewURLs";
            Map<String, String> path = new HashMap<String, String>(2) {
                {
                    put("https://", previewURLsApi);//根据现场环境部署确认是http还是https
                }
            };
            /**
             * STEP4：设置参数提交方式
             */
            String contentType = "application/json";
            /**
             * STEP5：组装请求参数
             */
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("cameraIndexCode", code);
            jsonBody.put("protocol", "rtsp");
            jsonBody.put("streamType", 0);
            // jsonBody.put("transmode",1);
            jsonBody.put("expand", "streamform=rtp");
            String body = jsonBody.toString();
            /**
             * STEP6：调用外部接口得rtsp
             */
            String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);// post请求application/json类型参数
            if (StrUtil.isNotBlank(result) && result.startsWith("{")) {
                JSONObject jsonObject0 = JSONUtil.parseObj(result);
                if (jsonObject0.getStr("code").equals("0")) {
                    JSONObject jsonObject1 = jsonObject0.getJSONObject("data");

                    if (jsonObject1 != null) {
                        /**
                         * STEP7：转为rtmp
                         */

                        JSONObject body2 = new JSONObject();
                        body2.put("mode", 3);
                        body2.put("in_url", jsonObject1.getStr("url"));
                        String out_url = "http://" + NMS_IP + ":8000/" + APPLICATION_NAME + "/" + code + ".flv";
                        body2.put("out_url", out_url);

                        String result2 = HttpRequest.post("http://" + NMS_IP + ":8000/api/relay")
                                .header(Header.AUTHORIZATION, GlobalData.NMS_TOKEN)
                                .body(body2.toString()).timeout(2000).execute().body();
                        if (StrUtil.isNotBlank(result2) && result2.startsWith("{")) {
                            JSONObject jsonObject2 = JSONUtil.parseObj(result2);
                            if (jsonObject2.getInt("code").equals(200)) {
                                String nmsId2 = jsonObject2.getJSONObject("data").getStr("id");
                                valueOperations.set(nmsRedisKey, nmsId2 );
                                return CommonResult.success(out_url.replace(NMS_IP, NMS_IP2),"新建:"+nmsId2);
                            } else {
                                System.out.println("jsonObject2:" + jsonObject2.toString());
                                return CommonResult.failed("转为rtmp失败" + jsonObject2.toString());
                            }
                        } else {
                            System.out.println("result2:" + result2);
                            return CommonResult.failed("转为rtmp失败" + result2);
                        }
                    } else {
                        return CommonResult.failed("查询rtsp失败,url为空");
                    }
                } else {
                    return CommonResult.failed("查询rtsp失败: " + jsonObject0.getStr("msg"));
                }
            } else {
                return CommonResult.failed("查询rtsp失败:" + result);
            }
        } catch (Exception e) {
            LOGGER.error("查询rtsp失败:", e);
            return CommonResult.failed("查询rtsp失败:"+e.getMessage());
        }

    }

    /**
     * 查询当前通用设施的所有类型
     */
    @Override
    public CommonResult getCurrentTypes() {
        // HashOperations<String,String,List<String>> hashOperations = redisTemplate.opsForHash();
        // List<String> typeList = hashOperations.get("currentTypes", "comPoi");
        // if (typeList != null && typeList.size() > 0) {
        //     return CommonResult.success(typeList);
        // } else {
        //     typeList = comPoiMapper.getCurrentTypes();
        //     hashOperations.put("currentTypes", "comPoi",typeList);
        //     return CommonResult.success(typeList);
        // }
        return CommonResult.success(comPoiMapper.getCurrentTypes());
    }
}
