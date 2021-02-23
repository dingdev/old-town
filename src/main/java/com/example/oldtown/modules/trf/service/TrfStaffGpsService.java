package com.example.oldtown.modules.trf.service;
import com.example.oldtown.modules.trf.model.TrfStaffGps;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;
import java.util.List;

/**
 * <p>
 * 交通接驳员工gps定位点 服务类
 * </p>
 * @author dyp
 * @since 2020-12-24
 */
public interface TrfStaffGpsService extends IService<TrfStaffGps> {


    /**
    * 分页查询交通接驳员工gps定位点
    */
    Page<TrfStaffGps> getAll(Integer pageSize, Integer pageNum, String keyword);

    /**
     * 增加交通接驳员工gps定位点
     */
    CommonResult add(TrfStaffGps trfStaffGps);

    /**
     * 更新交通接驳员工gps定位点
     */
    CommonResult update(TrfStaffGps trfStaffGps);

    /**
     * 删除交通接驳员工gps定位点
     */
    CommonResult delete(Long id);

    /**
     * 批量删除交通接驳员工gps定位点
     */
    CommonResult batchDelete(List<Long> ids);

    /**
     * 根据gps设备编号查询某日定位点集
     */
    List<TrfStaffGps> getListByGpsCode(String gpsCode, String fromTime, String toTime);

}
