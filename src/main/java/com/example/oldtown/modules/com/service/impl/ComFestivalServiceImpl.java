package com.example.oldtown.modules.com.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.example.oldtown.modules.com.model.ComFestival;
import com.example.oldtown.modules.com.mapper.ComFestivalMapper;
import com.example.oldtown.modules.com.service.ComFestivalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import com.example.oldtown.common.api.CommonResult;

/**
 * <p>
 * 通用节假日数据 服务实现类
 * </p>
 * @author dyp
 * @since 2021-01-14
 */
@Service
public class ComFestivalServiceImpl extends ServiceImpl<ComFestivalMapper, ComFestival> implements ComFestivalService {
    private final Logger LOGGER = LoggerFactory.getLogger(ComFestivalServiceImpl.class);

    @Resource
    ComFestivalMapper comFestivalMapper;

    /**
     * 分页查询通用节假日数据
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String keyword, Integer year) {

        QueryWrapper<ComFestival> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<ComFestival> lambda = wrapper.lambda();
        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(ComFestival::getName, keyword);
        }
        if (year != null) {
            lambda.eq(ComFestival::getYear, year);
        }
        if (pageNum == null) {
            lambda.orderByAsc(ComFestival::getStartTime);
            return CommonResult.success(comFestivalMapper.selectList(wrapper));
        } else {
            Page<ComFestival> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page,wrapper));
        }
    }


    /**
     * 增加通用节假日数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(ComFestival comFestival) {
        try {

            comFestival.setCreateTime(new Date());
            comFestivalMapper.insert(comFestival);

            return CommonResult.success("成功增加通用节假日数据:"+comFestival.getId(),comFestival.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加通用节假日数据失败:"+e.getMessage());
        }
    }

    /**
     * 更新通用节假日数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(ComFestival comFestival) {
        try {

            Long id = comFestival.getId();
            if (comFestivalMapper.selectById(id) == null) {
                return CommonResult.failed("该通用节假日数据不存在:"+id);
            }

            comFestivalMapper.updateById(comFestival);

            return CommonResult.success("成功更新通用节假日数据:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新通用节假日数据失败:"+e.getMessage());
        }
    }

    /**
     * 删除通用节假日数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (comFestivalMapper.selectById(id) == null) {
                return CommonResult.failed("该通用节假日数据不存在:"+id);
            }

            comFestivalMapper.deleteById(id);
            return CommonResult.success("成功删除通用节假日数据:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除通用节假日数据失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除通用节假日数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = comFestivalMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除通用节假日数据"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除通用节假日数据失败:"+e.getMessage());
        }
    }

    /**
     * 查询近两年节假日数据
     */
    @Override
    public CommonResult fetchFestivalData(Integer year) {

        JSONObject result = new JSONObject();

        if (year == null) {
            year = DateUtil.thisYear();
        }
        Integer previousYear = year - 1;

        result.put(String.valueOf(year),comFestivalMapper.getFestivalByYear(year)) ;
        result.put(String.valueOf(previousYear),comFestivalMapper.getFestivalByYear(previousYear));

        return CommonResult.success(result,previousYear+","+year);

    }

}
