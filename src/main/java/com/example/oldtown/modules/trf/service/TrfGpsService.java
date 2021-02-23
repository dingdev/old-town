package com.example.oldtown.modules.trf.service;
import com.example.oldtown.modules.trf.model.TrfGps;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 交通接驳gps定位点 服务类
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
public interface TrfGpsService extends IService<TrfGps> {


    /**
    * 分页查询交通接驳gps定位点
    */
    CommonResult getAll(Integer pageSize, Integer pageNum);

    /**
     * 增加交通接驳gps定位点
     */
    CommonResult add(TrfGps trfGps);

    /**
     * 更新交通接驳gps定位点
     */
    CommonResult update(TrfGps trfGps);
    /**
     * 删除交通接驳gps定位点
     */
    CommonResult delete(Long id);

    /**
     * 批量删除交通接驳gps定位点
     */
    CommonResult batchDelete(List<Long> ids);

    // /**
    //  * 根据gps设备编号查询最新定位点
    //  */
    // TrfGps getLatestByGpsCode(String gpsCode);

    /**
     * 根据gps设备编号查询某日定位点集
     */
    List<TrfGps> getListByGpsCode(String gpsCode, String fromTime, String toTime);

}
