package com.example.oldtown.modules.com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.oldtown.component.GlobalData;
import com.example.oldtown.modules.com.mapper.ComPlaceMapper;
import com.example.oldtown.modules.com.model.ComPlace;
import com.example.oldtown.modules.com.model.ComRoute;
import com.example.oldtown.modules.com.mapper.ComRouteMapper;
import com.example.oldtown.modules.com.service.ComRouteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.modules.xcx.mapper.XcxFavoriteMapper;
import com.example.oldtown.modules.xcx.model.XcxFavorite;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.oldtown.common.api.CommonResult;

/**
 * <p>
 * 通用游线 服务实现类
 * </p>
 *
 * @author dyp
 * @since 2020-11-13
 */
@Service
public class ComRouteServiceImpl extends ServiceImpl<ComRouteMapper, ComRoute> implements ComRouteService {
    private final Logger LOGGER = LoggerFactory.getLogger(ComRouteServiceImpl.class);

    @Resource
    ComRouteMapper comRouteMapper;
    @Resource
    ComPlaceMapper comPlaceMapper;
    @Resource
    XcxFavoriteMapper xcxFavoriteMapper;

    /**
     * 分页查询通用游线
     *
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String keyword) {

        QueryWrapper<ComRoute> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<ComRoute> lambda = wrapper.lambda();

        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(ComRoute::getName, keyword);
        }
        if (pageNum == null) {
            return CommonResult.success(comRouteMapper.selectList(wrapper));
        } else {
            Page<ComRoute> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page, wrapper));
        }
    }

    /**
     * 根据id查询通用游线
     */
    @Override
    public CommonResult getRouteById(Long id, Long userId) {

        ComRoute comRoute = comRouteMapper.selectById(id);
        if (comRoute == null) {
            return CommonResult.success(comRoute);
        }
        try {
            // 小程序用户需要返回收藏与否
            if (userId != null) {
                QueryWrapper<XcxFavorite> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(XcxFavorite::getContentFrom, GlobalData.COM_ROUTE).eq(XcxFavorite::getContentId, id).eq(XcxFavorite::getUserId, userId);
                List<XcxFavorite> xcxFavoriteList = xcxFavoriteMapper.selectList(wrapper);
                if (xcxFavoriteList != null && xcxFavoriteList.size() > 0) {
                    comRoute.setIfFavorite(xcxFavoriteList.get(0).getId());
                }
            }

            return CommonResult.success(comRoute);
        } catch (Exception e) {
            LOGGER.warn("根据id查询通用场所失败:", e);
            return CommonResult.success(comRoute);
        }
    }

    /**
     * 增加通用游线
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(ComRoute comRoute) {
        try {

            comRoute.setCreateTime(new Date());
            comRouteMapper.insert(comRoute);

            return CommonResult.success("成功增加通用游线:" + comRoute.getId(), comRoute.getId() + "");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加通用游线失败:" + e.getMessage());
        }
    }

    /**
     * 更新通用游线
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(ComRoute comRoute) {
        try {

            Long id = comRoute.getId();
            if (comRouteMapper.selectById(id) == null) {
                return CommonResult.failed("该通用游线不存在:" + id);
            }

            comRouteMapper.updateById(comRoute);

            return CommonResult.success("成功更新通用游线:" + id, id + "");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新通用游线失败:" + e.getMessage());
        }
    }

    /**
     * 删除通用游线
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (comRouteMapper.selectById(id) == null) {
                return CommonResult.failed("该通用游线不存在:" + id);
            }

            comRouteMapper.deleteById(id);
            return CommonResult.success("成功删除通用游线:" + id, id + "");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除通用游线失败:" + e.getMessage());
        }
    }

    /**
     * 查询通用游线包含的通用场所
     */
    @Override
    public CommonResult getContent(Long id) {
        try {
            ComRoute comRoute = comRouteMapper.selectById(id);
            if (comRoute == null) {
                return CommonResult.failed("该通用游线不存在:" + id);
            }

            String contentIds = comRoute.getContentIds();
            if (StrUtil.isBlank(contentIds)) {
                return CommonResult.success(null);
            }

            String[] idArray = contentIds.split(",");
            if (idArray == null || idArray.length == 0) {
                return CommonResult.success(null);
            }
            List<Long> idList = Arrays.asList(idArray).stream().map(Long::parseLong).collect(Collectors.toList());
            List<ComPlace> comPlaceList = comPlaceMapper.selectBatchIds(idList);
            return CommonResult.success(comPlaceList);
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除通用游线失败:" + e.getMessage());
        }
    }


    /**
     * 批量删除通用游线
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = comRouteMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除通用游线" + deleteNum + "个", deleteNum + "");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除通用游线失败:" + e.getMessage());
        }
    }

    /**
     * 查询通用游线的标签
     *
     * @return
     */
    @Override
    public CommonResult getLabelList() {
        List<String> labelList = comRouteMapper.getLabelList();
        return CommonResult.success(labelList);
    }

    /**
     * 根据标签和游玩天数查询通用游线
     */
    @Override
    public ComRoute getByLabelAndDayNum(String label, Integer dayNum) {
        ComRoute comRoute = null;
        if (StrUtil.isBlank(label)) {
            return comRouteMapper.getRandom();
        }
        QueryWrapper<ComRoute> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<ComRoute> lambda = wrapper.lambda();
        String[] labelArray = label.split(",");
        label = labelArray[(int) (Math.random() * labelArray.length)];
        lambda.eq(ComRoute::getLabel, label);
        List<ComRoute> comRouteList = comRouteMapper.selectList(wrapper);
        if (comRouteList != null && comRouteList.size() > 0) {
            if (dayNum != null) {
                List<ComRoute> comRouteList1 = comRouteList.stream().filter(comRoute1 -> dayNum.equals(comRoute1.getDayNum())).collect(Collectors.toList());
                if (comRouteList1 != null && comRouteList1.size() > 0) {
                    comRoute = comRouteList1.get(0);
                }
            }
        }

        if (comRoute == null) {
            comRoute = comRouteList.get((int) (Math.random() * comRouteList.size()));
        }
        return comRoute;
    }

}
