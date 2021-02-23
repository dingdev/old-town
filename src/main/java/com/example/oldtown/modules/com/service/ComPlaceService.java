package com.example.oldtown.modules.com.service;
import com.example.oldtown.modules.com.model.ComPlace;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 通用场所 服务类
 * </p>
 * @author dyp
 * @since 2020-11-06
 */
public interface ComPlaceService extends IService<ComPlace> {


    /**
    * 分页查询通用场所
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String type, String keyword);

    /**
     * 根据id查询通用场所
     */
    CommonResult getPlaceById(Long id, Long userId);

    // /**
    //  * 查询全部通用场所(小程序地图用)
    //  */
    // CommonResult getAllByType(String type);

    /**
     * 增加通用场所
     */
    CommonResult add(ComPlace comPlace);

    /**
     * 更新通用场所
     */
    CommonResult update(ComPlace comPlace);
    /**
     * 删除通用场所
     */
    CommonResult delete(Long id);

    /**
     * 批量删除通用场所
     */
    CommonResult batchDelete(List<Long> ids);

    /**
     * 通过Excel批量增加通用场所
     */
    CommonResult batchAddWithExcel(MultipartFile file);

    /**
     * 查询当前通用场所的所有类型
     */
    CommonResult getCurrentTypes();

    /**
     * 查询热门搜索
     */
    CommonResult getTopSearch();
}
