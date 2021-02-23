package com.example.oldtown.modules.com.service;
import com.example.oldtown.modules.com.model.ComRoute;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;

import java.util.List;

/**
 * <p>
 * 通用游线 服务类
 * </p>
 * @author dyp
 * @since 2020-11-13
 */
public interface ComRouteService extends IService<ComRoute> {


    /**
    * 分页查询通用游线
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String keyword);

    /**
     * 根据id查询通用场所
     */
    CommonResult getRouteById(Long id, Long userId);

    /**
     * 增加通用游线
     */
    CommonResult add(ComRoute comRoute);

    /**
     * 更新通用游线
     */
    CommonResult update(ComRoute comRoute);
    /**
     * 删除通用游线
     */
    CommonResult delete(Long id);
    /**
     * 查询通用游线包含的通用场所
     */
    CommonResult getContent(Long id);

    /**
     * 批量删除通用游线
     */
    CommonResult batchDelete(List<Long> ids);

    /**
     * 查询通用游线的标签
     */
    CommonResult getLabelList();

    /**
     * 根据标签和游玩天数查询通用游线
     */
    ComRoute getByLabelAndDayNum(String label,Integer dayNum);

}
