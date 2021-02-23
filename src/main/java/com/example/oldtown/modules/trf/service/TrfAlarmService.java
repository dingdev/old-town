package com.example.oldtown.modules.trf.service;
import com.example.oldtown.modules.trf.model.TrfAlarm;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;
import java.util.List;

/**
 * <p>
 * 交通接驳gps报警 服务类
 * </p>
 * @author dyp
 * @since 2021-01-15
 */
public interface TrfAlarmService extends IService<TrfAlarm> {


    /**
    * 分页查询交通接驳gps报警
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String keyword, String fromTime, String toTime);

    /**
     * 增加交通接驳gps报警
     */
    CommonResult add(TrfAlarm trfAlarm);

    /**
     * 更新交通接驳gps报警
     */
    CommonResult update(TrfAlarm trfAlarm);

    /**
     * 删除交通接驳gps报警
     */
    CommonResult delete(Long id);

    /**
     * 批量删除交通接驳gps报警
     */
    CommonResult batchDelete(List<Long> ids);
}
