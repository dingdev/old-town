package com.example.oldtown.modules.xcx.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.oldtown.component.GlobalData;
import com.example.oldtown.component.JwtTokenUtil;
import com.example.oldtown.dto.RawDataDO;
import com.example.oldtown.dto.WechatLoginRequest;
import com.example.oldtown.modules.xcx.model.XcxUser;
import com.example.oldtown.modules.xcx.mapper.XcxUserMapper;
import com.example.oldtown.modules.xcx.service.XcxUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.oldtown.common.api.CommonResult;
import sun.security.rsa.RSASignature;

/**
 * <p>
 * 小程序用户 服务实现类
 * </p>
 * @author dyp
 * @since 2020-11-13
 */
@Service
public class XcxUserServiceImpl extends ServiceImpl<XcxUserMapper, XcxUser> implements XcxUserService {
    private final Logger LOGGER = LoggerFactory.getLogger(XcxUserServiceImpl.class);
    private static final String REQUEST_URL = "https://api.weixin.qq.com/sns/jscode2session";
    private final String GRANT_TYPE = "authorization_code";
    private final String WECHAT_PERMISSION = "[小程序用户]";

    @Value("${wechat.app-id}")
    private String APP_ID;
    @Value("${wechat.app-secret}")
    private String APP_SECRET;



    @Resource
    XcxUserMapper xcxUserMapper;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    /**
     * 分页查询小程序用户
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String keyword) {
        QueryWrapper<XcxUser> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<XcxUser> lambda = wrapper.lambda();
        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(XcxUser::getNickname, keyword).or().like(XcxUser::getOpenId, keyword);
        }
        if (pageNum == null) {
            return CommonResult.success(xcxUserMapper.selectList(wrapper));
        } else {
            Page<XcxUser> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page, wrapper));
        }
    }


    /**
     * 增加小程序用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(XcxUser xcxUser) {
        try {

            xcxUser.setCreateTime(new Date());
            xcxUserMapper.insert(xcxUser);

            return CommonResult.success("成功增加小程序用户:"+xcxUser.getId(),xcxUser.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加小程序用户失败:"+e.getMessage());
        }
    }

    /**
     * 更新小程序用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(XcxUser xcxUser) {
        try {

            Long id = xcxUser.getId();
            if (xcxUserMapper.selectById(id) == null) {
            return CommonResult.failed("该小程序用户不存在:"+id);
            }

            xcxUserMapper.updateById(xcxUser);

            return CommonResult.success("成功更新小程序用户:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新小程序用户失败:"+e.getMessage());
        }
    }

    /**
     * 删除小程序用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (xcxUserMapper.selectById(id) == null) {
            return CommonResult.failed("该小程序用户不存在:"+id);
            }

            xcxUserMapper.deleteById(id);
            return CommonResult.success("成功删除小程序用户:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除小程序用户失败:"+e.getMessage());
        }
    }


    /**
     * 批量删除小程序用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = xcxUserMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除小程序用户"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除小程序用户失败:"+e.getMessage());
        }
    }

    /**
     * 小程序用户登录
     * @param loginRequest
     * @return
     */
    @Override
    public CommonResult login(WechatLoginRequest loginRequest) {
        try {
            JSONObject sessionKeyOpenId = getSessionKeyAndOpenId(loginRequest.getCode());
            if (sessionKeyOpenId == null) {
                return CommonResult.failed("小程序用户登录失败,sessionKeyOpenId为空");
            }
            LOGGER.info("小程序登录code:"+loginRequest.getCode()+"\n sessionKeyOpenId:"+sessionKeyOpenId.toString());

            // 获取openId && sessionKey
            String openId = sessionKeyOpenId.getStr("openid");
            if (openId == null) {
                return CommonResult.failed("小程序用户登录失败,openId为空");
            }
            String sessionKey = sessionKeyOpenId.getStr("session_key");
            // sha1(rawDataDO+session_key)对比signature可校验,此处不必校验
            XcxUser xcxUser = buildXcxUser(loginRequest, sessionKey, openId);

            // // 根据code保存openId和sessionKey,不必缓存,code只能用一次
            // JSONObject sessionObj = new JSONObject();
            // sessionObj.put("openId", openId);
            // sessionObj.put("sessionKey", sessionKey);
            // // 这里的set方法，自行导入自己项目的Redis，key自行替换，这里10表示10天
            // stringJedisClientTem.set(WechatRedisPrefixConstant.USER_OPPEN_ID_AND_SESSION_KEY_PREFIX + loginRequest.getCode(),
            //         sessionObj.toJSONString(), 10, TimeUnit.DAYS);

            // 根据openid查询用户
            QueryWrapper<XcxUser> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(XcxUser::getOpenId, openId);
            Date date = new Date();
            if (xcxUserMapper.selectCount(wrapper) == 0) {
                // 用户不存在，insert用户，这里加了个分布式锁，防止insert重复用户，看自己的业务，决定要不要这段代码
                xcxUser.setCreateTime(date);
                xcxUser.setOnline(1);
                xcxUser.setDeleted(0);
                xcxUserMapper.insert(xcxUser);
            } else {
                xcxUserMapper.update(xcxUser, wrapper);
                xcxUser = xcxUserMapper.selectList(wrapper).get(0);
            }

            String token = jwtTokenUtil.generateToken(GlobalData.XCX_USER+ "," + xcxUser.getNickname()+","+ xcxUser.getId() , WECHAT_PERMISSION);
            return CommonResult.success(xcxUser,token);
        } catch (Exception e) {
            LOGGER.error("", e);
            return CommonResult.failed("小程序用户登录失败:" + e.getMessage());
        }

    }



    /**
     * 小程序用户登录所用
     */
    private JSONObject getSessionKeyAndOpenId(String code) throws Exception  {
        Map<String, Object> requestUrlParam = new HashMap<>();
        // 小程序appId，自己补充
        requestUrlParam.put("appid", APP_ID);
        // 小程序secret，自己补充
        requestUrlParam.put("secret", APP_SECRET);
        // 小程序端返回的code
        requestUrlParam.put("js_code", code);
        // 默认参数
        requestUrlParam.put("grant_type", GRANT_TYPE);

        // 发送post请求读取调用微信接口获取openid用户唯一标识
        String result = HttpUtil.post(REQUEST_URL, requestUrlParam);
        return JSONUtil.parseObj(result);
    }
    /**
     * 小程序用户登录所用
     */
    private XcxUser buildXcxUser(WechatLoginRequest loginRequest, String sessionKey, String openId){
        XcxUser xcxUser = new XcxUser();
        xcxUser.setOpenId(openId);
        xcxUser.setOnline(1);

        if (loginRequest.getRawData() != null) {
            RawDataDO rawDataDO = JSONUtil.toBean(loginRequest.getRawData(), RawDataDO.class);
            xcxUser.setNickname(rawDataDO.getNickName());
            xcxUser.setAvatarUrl(rawDataDO.getAvatarUrl());
            xcxUser.setGender(rawDataDO.getGender());
            xcxUser.setCountry(rawDataDO.getCountry());
            xcxUser.setProvince(rawDataDO.getProvince());
            xcxUser.setCountry(rawDataDO.getCity());
        }
        //
        // // 解密加密信息，获取unionID
        // if (loginRequest.getEncryptedData() != null){
        //     JSONObject encryptedData = getEncryptedData(loginRequest.getEncryptedData(), sessionKey, loginRequest.getIv());
        //     if (encryptedData != null){
        //         String unionId = encryptedData.getString("unionId");
        //         xcxUser.setUnionId(unionId);
        //     }
        // }
        return xcxUser;
    }

    /**
     * 小程序用户登出
     */
    @Override
    public CommonResult logout(Long userId) {

        if (xcxUserMapper.selectById(userId) == null) {
            return CommonResult.failed("该用户不存在:"+userId);
        }

        XcxUser xcxUser = new XcxUser();
        xcxUser.setOnline(0);
        xcxUserMapper.updateById(xcxUser);
        return CommonResult.success("登出成功:"+userId);
    }
}
