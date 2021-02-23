package com.example.oldtown.modules.com.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.oldtown.component.GlobalData;
import com.example.oldtown.modules.com.model.ComInfo;
import com.example.oldtown.modules.com.mapper.ComInfoMapper;
import com.example.oldtown.modules.com.service.ComInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.util.ScheduledUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import com.example.oldtown.common.api.CommonResult;

/**
 * <p>
 * 通用信息 服务实现类
 * </p>
 * @author dyp
 * @since 2020-11-06
 */
@Service
public class ComInfoServiceImpl extends ServiceImpl<ComInfoMapper, ComInfo> implements ComInfoService {
    private final Logger LOGGER = LoggerFactory.getLogger(ComInfoServiceImpl.class);

    // 小程序
    @Value("${wechat.app-id}")
    private String appId;
    @Value("${wechat.app-secret}")
    private String appSecret;
    @Value("${wechat.wx-secret}")
    private String wxSecret;


    @Resource
    ComInfoMapper comInfoMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    ScheduledUtil scheduledUtil;

    /**
     * 分页查询通用信息
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String keyword) {

        QueryWrapper<ComInfo> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<ComInfo> lambda = wrapper.lambda();
        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(ComInfo::getName, keyword);
        }
        if (pageNum == null) {
            return CommonResult.success(comInfoMapper.selectList(wrapper));
        } else {
            Page<ComInfo> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page,wrapper));
        }
    }


    /**
     * 增加通用信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(ComInfo comInfo) {
        try {
            String name = comInfo.getName();
            QueryWrapper<ComInfo> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(ComInfo::getName, name);
            if (comInfoMapper.selectCount(wrapper) > 0) {
                return CommonResult.failed("该信息名称已存在");
            }
            comInfo.setCreateTime(new Date());
            comInfoMapper.insert(comInfo);

            return CommonResult.success("成功增加通用信息:"+comInfo.getId(),comInfo.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加通用信息失败:"+e.getMessage());
        }
    }

    /**
     * 更新通用信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(ComInfo comInfo) {
        try {

            Long id = comInfo.getId();
            if (comInfoMapper.selectById(id) == null) {
            return CommonResult.failed("该通用信息不存在:"+id);
            }

            comInfoMapper.updateById(comInfo);

            return CommonResult.success("成功更新通用信息:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新通用信息失败:"+e.getMessage());
        }
    }

    /**
     * 删除通用信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (comInfoMapper.selectById(id) == null) {
            return CommonResult.failed("该通用信息不存在:"+id);
            }

            comInfoMapper.deleteById(id);
            return CommonResult.success("成功删除通用信息:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除通用信息失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除通用信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = comInfoMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除通用信息"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除通用信息失败:"+e.getMessage());
        }
    }



    /**
     * 根据名称查询信息
     */
    @Override
    public ComInfo getByName(String name) {
        QueryWrapper<ComInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ComInfo::getName, name);
        List<ComInfo> comInfoList = comInfoMapper.selectList(wrapper);
        if (comInfoList != null && comInfoList.size() > 0) {
            return comInfoList.get(0);
        }
        return null;
    }

    /**
     * 查询南浔古镇今日天气
     */
    @Override
    public CommonResult getTodayWeather() {
        try {
            HashOperations<String, String, JSONObject> hashOperations = redisTemplate.opsForHash();
            JSONObject weather = hashOperations.get("weatherHashMap", "1");

            if (weather == null) {
                scheduledUtil.pullWeather();
                weather = hashOperations.get("weatherHashMap", "1");
            }
            return CommonResult.success(weather);
        } catch (Exception e) {
            LOGGER.error("查询南浔古镇今日天气失败:", e);
            return CommonResult.failed("查询柯桥天气失败: " + e.getMessage());
        }
    }

    /**
     * 获取小程序config接口部分参数
     */
    @Override
    public CommonResult getXcxConfig(String url,String secret) {
        try {
            if (!wxSecret.equals(secret)) {
                return CommonResult.failed("秘钥secret有误!");
            }
            String noncestr = RandomUtil.randomString(16);
            Long timestamp = System.currentTimeMillis() / 1000;
            // HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
            // String jsapi_ticket = hashOperations.get("devInfo", "jsapi_ticket");
            System.out.println(GlobalData.JSAPI_TICKET);
            String signature = SecureUtil.sha1("jsapi_ticket=" + GlobalData.JSAPI_TICKET + "&noncestr=" + noncestr
                    + "&timestamp=" + timestamp + "&url=" + url);
            JSONObject result = new JSONObject();
            result.put("appId", appId);
            result.put("timestamp", timestamp);
            result.put("noncestr", noncestr);
            result.put("signature", signature);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.failed("获取小程序config接口部分参数失败:"+e.getMessage());
        }
    }


    /**
     * 获取公众号access_token参数
     */
    @Override
    public CommonResult getOfficialAccessToken(String secret) {
        if (!wxSecret.equals(secret)) {
            return CommonResult.failed("秘钥secret有误!");
        }
        if (StrUtil.isNotBlank(GlobalData.OFFICIAL_ACCESS_TOKEN)) {
            return CommonResult.success(GlobalData.OFFICIAL_ACCESS_TOKEN);
        } else {
            return CommonResult.failed("获取公众号access_token参数失败,其值为空");
        }

    }

}
