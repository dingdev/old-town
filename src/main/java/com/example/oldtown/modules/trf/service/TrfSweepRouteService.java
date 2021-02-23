package com.example.oldtown.modules.trf.service;
import com.example.oldtown.modules.trf.model.TrfSweepRoute;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 交通接驳打捞船标准路线 服务类
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
public interface TrfSweepRouteService extends IService<TrfSweepRoute> {


    /**
    * 分页查询交通接驳打捞船标准路线
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String keyword);

    /**
     * 增加交通接驳打捞船标准路线
     */
    CommonResult add(TrfSweepRoute trfSweepRoute, MultipartFile pointsExcel);

    /**
     * 更新交通接驳打捞船标准路线
     */
    CommonResult update(TrfSweepRoute trfSweepRoute);
    /**
     * 删除交通接驳打捞船标准路线
     */
    CommonResult delete(Long id);

    /**
     * 批量删除交通接驳打捞船标准路线
     */
    CommonResult batchDelete(List<Long> ids);

    /**
     * 获取增加打捞船标准路线时的pointExcel文件模板地址
     */
    CommonResult getPointExcelTemplate();
}
