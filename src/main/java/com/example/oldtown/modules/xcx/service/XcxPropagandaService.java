package com.example.oldtown.modules.xcx.service;
import com.example.oldtown.modules.xcx.model.XcxPropaganda;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;
import java.util.List;

/**
 * <p>
 * 小程序宣传 服务类
 * </p>
 * @author dyp
 * @since 2020-12-02
 */
public interface XcxPropagandaService extends IService<XcxPropaganda> {


    /**
    * 分页查询小程序宣传
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String type, String keyword);

    /**
     * 增加小程序宣传
     */
    CommonResult add(XcxPropaganda xcxPropaganda);

    /**
     * 更新小程序宣传
     */
    CommonResult update(XcxPropaganda xcxPropaganda);

    /**
     * 删除小程序宣传
     */
    CommonResult delete(Long id);

    /**
     * 批量删除小程序宣传
     */
    CommonResult batchDelete(List<Long> ids);
}
