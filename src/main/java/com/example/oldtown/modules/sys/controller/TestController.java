package com.example.oldtown.modules.sys.controller;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONString;
import cn.hutool.json.JSONUtil;
import com.example.oldtown.common.api.CommonResult;
import com.example.oldtown.component.GlobalData;
import com.example.oldtown.component.JwtTokenUtil;
import com.example.oldtown.modules.xcx.model.XcxNews;
import com.example.oldtown.util.ScheduledUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author ding.yp
 * @name
 * @info
 * @date 2020/11/05
 */

@RestController
@RequestMapping("/sys/test")
@Api(value = "TestController", tags = "测试相关")
@Validated
public class TestController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ScheduledUtil scheduledUtil;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    RedisTemplate redisTemplate;


    private final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @ApiOperation("Hello world")
    @GetMapping("/hello")
    public CommonResult hello() {
        return CommonResult.success("Hello world".split(",")[0]);
    }

    @ApiOperation("bcrypt")
    @GetMapping("/bcrypt")
    public CommonResult bcrypt(@RequestParam("original") String original) {
        return CommonResult.success(passwordEncoder.encode(original));
    }

    @ApiOperation("getToken")
    @GetMapping("/getToken")
    public CommonResult getToken(String key,String from) {
        if (!"zjtoprs".equalsIgnoreCase(key)) {
            return CommonResult.failed("key错误");
        }
        return CommonResult.success(jwtTokenUtil.generateToken(GlobalData.XCX_USER+","+null+","+from,"[小程序用户]")) ;

    }

    @ApiOperation("initData")
    @GetMapping("/initData")
    public CommonResult initData(String key) {
        if (!"zjtoprs".equalsIgnoreCase(key)) {
            return CommonResult.failed("key错误");
        }
        // scheduledUtil.pullNmsToken();
        // scheduledUtil.pullTrfGpsToken();
        // scheduledUtil.pullWeather();
        // scheduledUtil.pullYiTong();
        // scheduledUtil.pullTrfSecurityCarReal();
        // scheduledUtil.pullTrfSweepReal();
        // scheduledUtil.pullTrfYachtReal();
        // scheduledUtil.pullAccessTokenAndJsapiTicket();
        // return CommonResult.success(GlobalData.getNmsToken()) ;
        // scheduledUtil.pullTrfYachtReal();
        return CommonResult.success("ok");

    }

    @ApiOperation("getGPS")
    @GetMapping("/getGPS")
    public String getGPS() {
        //2a69f2c4403611ebab200242c0482194
        String appid = "中测新图";
        String key = "fa$2AX1M$Pbi#3TWW%7nS0jyRN!kgDbt";
        Long time = System.currentTimeMillis()/1000;
        String md5 = SecureUtil.md5(key);
        System.out.println("\nmd5:"+md5);
        System.out.println("\nmd5-2:"+SecureUtil.md5(key));
        String signature = SecureUtil.md5(md5 + time);
        System.out.println("\nsignature:"+signature);
        Map<String, Object> req = new HashMap<>();
        req.put("appid", appid);
        req.put("time", time);
        req.put("signature", signature);
        System.out.println(JSONUtil.toJsonStr(req));
        String res = HttpUtil.post("http://open.gumigps.com/api/auth", JSONUtil.toJsonStr(req));

        return res;

    }

    @ApiOperation("pullXcxNews")
    @GetMapping("/pullXcxNews")
    public CommonResult pullXcxNews(String key) {
        if (!"zjtoprs".equalsIgnoreCase(key)) {
            return CommonResult.failed("key错误");
        }
        try {
            String appid = "wxc4dcbdad305e618d";//"wxc95e37b673ba89ef";
            String secret = "7ab9cf1330c042ceef3ab5485e2eb4c5";//"92c6e5338395dd49c71ff79c29c467fb";
            String result1 = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" +
                    appid + "&secret=" + secret);
            Map map_ree = JSONUtil.parseObj(result1);
            String access_token = (String) map_ree.get("access_token");

            String ip = "https://api.weixin.qq.com/cgi-bin/material/batchget_material";
            List<XcxNews> xcxNewsList = new ArrayList<>();
            Map<String, String> map = new HashMap();
            map.put("type", "news");
            map.put("offset", 0 + "");
            map.put("count", "3");
            String json = JSONUtil.toJsonStr(map);
            String result = HttpUtil.post(ip + "?access_token=" + access_token, json);
            result = new String(result.getBytes("ISO-8859-1"), "utf-8");

            return CommonResult.success(result);
        } catch (Exception e) {
            LOGGER.error("", e);
            return CommonResult.failed("失败:"+e.getMessage());
        }
    }

    public static void main () {
        Stack<Integer> stack = new Stack<>();
        stack.push(3);
        stack.push(4);
        stack.push(5);
        System.out.println(stack.peek());
    }

    @ApiOperation("sort")
    @GetMapping("/sort")
    public CommonResult sort() {
        // try {
        //     Double d = 4.56;
        //     HashOperations<String, String, Double> hashOperations = redisTemplate.opsForHash();
        //     hashOperations.put("trfStaffGPS", "aa", d);
        //     Double s = hashOperations.get("trfStaffGPS", "aa");
        //     return CommonResult.success(s);
        // } catch (Exception e) {
        //     return CommonResult.failed(e.getMessage());
        // }

        return null;

        // // String[] stringArray, Integer[] integerArray
        // String[] m = {"b","d","a","c"};
        // Integer[] n = {2, 4, 1, 3};
        //
        // if (m == null || n == null || m.length != n.length) {
        //     System.out.println("应输入长度一致的两个非空数组");
        // }
        // int size = m.length;
        // Map<Integer, String> map = new HashMap<>();
        //
        // for (int i = 0; i < size; i++) {
        //     int big = 0;
        //
        // }
        // Arrays.sort(n);
        //
        // return map.get(n[0]);

    }
}
