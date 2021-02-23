package com.example.oldtown.modules.com.service;
import cn.hutool.json.JSONObject;
import com.example.oldtown.modules.com.model.ComPoi;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.common.api.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 通用设施 服务类
 * </p>
 * @author dyp
 * @since 2020-11-06
 */
public interface ComPoiService extends IService<ComPoi> {


    /**
    * 分页查询通用设施
    */
    CommonResult getAll(Integer pageSize, Integer pageNum, String type, String keyword);

    /**
     * 根据id查询通用设施
     */
    CommonResult getPoiById(Long id);

    /**
     * 增加通用设施
     */
    CommonResult add(ComPoi comPoi);

    /**
     * 更新通用设施
     */
    CommonResult update(ComPoi comPoi);
    /**
     * 删除通用设施
     */
    CommonResult delete(Long id);

    /**
     * 批量删除通用设施
     */
    CommonResult batchDelete(List<Long> ids);

    /**
     * 通过Excel批量增加通用设施
     */
    CommonResult batchAddWithExcel(MultipartFile file);

    /**
     * 根据监控点摄像头的编号查询rtmp视频地址
     */
    CommonResult rtmpByCameraCode(String code);

    /**
     * 查询当前通用设施的所有类型
     */
    CommonResult getCurrentTypes();

}
