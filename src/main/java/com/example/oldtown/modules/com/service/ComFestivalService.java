package com.example.oldtown.modules.com.service;
import com.example.oldtown.modules.com.model.ComFestival;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;
import java.util.List;

/**
 * <p>
 * 通用节假日数据 服务类
 * </p>
 * @author dyp
 * @since 2021-01-14
 */
public interface ComFestivalService extends IService<ComFestival> {


    /**
    * 分页查询通用节假日数据
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String keyword, Integer year);

    /**
     * 增加通用节假日数据
     */
    CommonResult add(ComFestival comFestival);

    /**
     * 更新通用节假日数据
     */
    CommonResult update(ComFestival comFestival);

    /**
     * 删除通用节假日数据
     */
    CommonResult delete(Long id);

    /**
     * 批量删除通用节假日数据
     */
    CommonResult batchDelete(List<Long> ids);

    /**
     * 查询近两年节假日数据
     */
    CommonResult fetchFestivalData(Integer year);
}
