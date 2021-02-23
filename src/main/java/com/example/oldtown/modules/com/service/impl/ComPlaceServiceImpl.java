package com.example.oldtown.modules.com.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.oldtown.component.GlobalData;
import com.example.oldtown.dto.StringIntegerDTO;
import com.example.oldtown.modules.com.model.ComPlace;
import com.example.oldtown.modules.com.mapper.ComPlaceMapper;
import com.example.oldtown.modules.com.service.ComPlaceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.modules.xcx.mapper.XcxFavoriteMapper;
import com.example.oldtown.modules.xcx.model.XcxFavorite;
import com.example.oldtown.util.excel.UploadComPlaceListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.example.oldtown.common.api.CommonResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 通用场所 服务实现类
 * </p>
 * @author dyp
 * @since 2020-11-06
 */
@Service
public class ComPlaceServiceImpl extends ServiceImpl<ComPlaceMapper, ComPlace> implements ComPlaceService {
    private final Logger LOGGER = LoggerFactory.getLogger(ComPlaceServiceImpl.class);

    // private final String COM_PLACE_VIEW = "com:place:view";


    @Value("${minio.bucketSide}")
    private String BUCKET_SIDE;

    @Resource
    ComPlaceMapper comPlaceMapper;
    @Resource
    XcxFavoriteMapper xcxFavoriteMapper;
    @Autowired
    ComPlaceService comPlaceService;
    // @Autowired
    // RedisService redisService;
    @Autowired
    RedisTemplate redisTemplate;


    /**
     * 分页查询通用场所
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String type, String keyword) {


        QueryWrapper<ComPlace> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<ComPlace> lambda = wrapper.lambda();
        if (StrUtil.isNotBlank(type)) {
            if (type.contains(",")) {
                lambda.in(ComPlace::getType,type.split(","));
            } else {
                lambda.eq(ComPlace::getType,type);
            }
        }
        if (StrUtil.isNotBlank(keyword)) {
            lambda.and(lambda1 -> lambda1.like(ComPlace::getName,keyword).or().like(ComPlace::getCode,keyword)) ;
        }
        // 排序
        wrapper.orderByDesc("sorting_order").orderByAsc("id");
        if (pageNum == null) {
            return CommonResult.success(comPlaceMapper.selectList(wrapper));
        } else {
            Page<ComPlace> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page,wrapper));
        }
    }

    /**
     * 根据id查询通用场所
     */
    @Override
    public CommonResult getPlaceById(Long id, Long userId) {

        ComPlace comPlace = comPlaceMapper.selectById(id);
        if (comPlace == null) {
            return CommonResult.success(comPlace);
        }
        try {
            ComPlace comPlace1 = new ComPlace();
            comPlace1.setId(id);
            comPlace1.setViewNum(comPlace.getViewNum() + 1);
            comPlaceMapper.updateById(comPlace1);

            // 小程序用户需要返回收藏与否
            if (userId != null) {
                QueryWrapper<XcxFavorite> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(XcxFavorite::getContentFrom, GlobalData.COM_PLACE).eq(XcxFavorite::getContentId, id).eq(XcxFavorite::getUserId, userId);
                List<XcxFavorite> xcxFavoriteList = xcxFavoriteMapper.selectList(wrapper);
                if (xcxFavoriteList != null && xcxFavoriteList.size() >0) {
                    comPlace.setIfFavorite(xcxFavoriteList.get(0).getId());
                }
            }

            return CommonResult.success(comPlace);
        } catch (Exception e) {
            LOGGER.warn("根据id查询通用场所失败:", e);
            return CommonResult.success(comPlace);
        }
    }

    /**
     * 增加通用场所
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(ComPlace comPlace) {
        try {

            comPlace.setCreateTime(new Date());
            comPlaceMapper.insert(comPlace);

            return CommonResult.success("成功增加通用场所:"+comPlace.getId(),comPlace.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加通用场所失败:"+e.getMessage());
        }
    }

    /**
     * 更新通用场所
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(ComPlace comPlace) {
        try {

            Long id = comPlace.getId();
            if (comPlaceMapper.selectById(id) == null) {
            return CommonResult.failed("该通用场所不存在:"+id);
            }


            comPlaceMapper.updateById(comPlace);

            return CommonResult.success("成功更新通用场所:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新通用场所失败:"+e.getMessage());
        }
    }

    /**
     * 删除通用场所
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (comPlaceMapper.selectById(id) == null) {
            return CommonResult.failed("该通用场所不存在:"+id);
            }

            comPlaceMapper.deleteById(id);
            return CommonResult.success("成功删除通用场所:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除通用场所失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除通用场所
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = comPlaceMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除通用场所"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除通用场所失败:"+e.getMessage());
        }
    }

    /**
     * 通过Excel批量增加通用场所
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchAddWithExcel(MultipartFile file) {
        try {
            if (file == null || !GlobalData.EXCEL_TYPES.contains(file.getContentType())) {
                return CommonResult.failed("请传入后缀为xls或者xlsx的excel文件(file)");
            }
            EasyExcel.read(file.getInputStream(), ComPlace.class, new UploadComPlaceListener(comPlaceService)).sheet().doRead();
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("通过Excel批量增加通用场所失败:"+e.getMessage());
        }
        return CommonResult.success("成功通过Excel批量增加通用场所");
    }

    /**
     * 查询当前通用场所的所有类型
     */
    @Override
    public CommonResult getCurrentTypes() {
        // HashOperations<String,String,List<String>> hashOperations = redisTemplate.opsForHash();
        // List<String> typeList = hashOperations.get("currentTypes", "comPlace");
        // if (typeList != null && typeList.size() > 0) {
        //     return CommonResult.success(typeList);
        // } else {
        //     typeList = comPlaceMapper.getCurrentTypes();
        //     hashOperations.put("currentTypes", "comPlace",typeList);
        //     return CommonResult.success(typeList);
        // }
        return CommonResult.success(comPlaceMapper.getCurrentTypes());
    }

    /**
     * 查询热门搜索关键词
     */
    @Override
    public CommonResult getTopSearch() {
        HashOperations<String,String,List<String>> hashOperations = redisTemplate.opsForHash();
        List<String> searchList = hashOperations.get("topSearch", "comPlace");
        if (searchList != null && searchList.size() > 0) {
            return CommonResult.success(searchList);
        } else {
            searchList = comPlaceMapper.getTopSearch();
            hashOperations.put("currentTypes", "comPlace",searchList);
            return CommonResult.success(searchList);
        }
    }
}
