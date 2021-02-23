package com.example.oldtown.modules.com.service;
import com.example.oldtown.modules.com.model.ComInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;

import java.util.List;

/**
 * <p>
 * 通用信息 服务类
 * </p>
 * @author dyp
 * @since 2020-11-06
 */
public interface ComInfoService extends IService<ComInfo> {


    /**
    * 分页查询通用信息
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String keyword);

    /**
     * 增加通用信息
     */
    CommonResult add(ComInfo comInfo);

    /**
     * 更新通用信息
     */
    CommonResult update(ComInfo comInfo);
    /**
     * 删除通用信息
     */
    CommonResult delete(Long id);
    /**
     * 根据名称查询信息
     */
    ComInfo getByName(String name);

    /**
     * 批量删除通用信息
     */
    CommonResult batchDelete(List<Long> ids);

    /**
     * 查询南浔古镇今日天气
     */
    CommonResult getTodayWeather();

    /**
     * 获取小程序config接口部分参数
     */
    CommonResult getXcxConfig(String url,String secret);

    /**
     * 获取公众号access_token参数
     */
    CommonResult getOfficialAccessToken(String secret);
}
