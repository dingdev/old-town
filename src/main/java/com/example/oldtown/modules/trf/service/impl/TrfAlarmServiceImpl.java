package com.example.oldtown.modules.trf.service.impl;

import com.example.oldtown.modules.trf.model.TrfAlarm;
import com.example.oldtown.modules.trf.mapper.TrfAlarmMapper;
import com.example.oldtown.modules.trf.service.TrfAlarmService;
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
 * 交通接驳gps报警 服务实现类
 * </p>
 * @author dyp
 * @since 2021-01-15
 */
@Service
public class TrfAlarmServiceImpl extends ServiceImpl<TrfAlarmMapper, TrfAlarm> implements TrfAlarmService {
    private final Logger LOGGER = LoggerFactory.getLogger(TrfAlarmServiceImpl.class);

    @Resource
    TrfAlarmMapper trfAlarmMapper;

    /**
     * 分页查询交通接驳gps报警
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String keyword, String fromTime, String toTime) {

        QueryWrapper<TrfAlarm> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TrfAlarm> lambda = wrapper.lambda();
        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(TrfAlarm::getGpsCode, keyword);
        }
        if (StrUtil.isNotBlank(fromTime)) {
            lambda.ge(TrfAlarm::getAlarmTime, fromTime);
        }
        if (StrUtil.isNotBlank(toTime)) {
            lambda.le(TrfAlarm::getAlarmTime, toTime);
        }
        if (pageNum == null) {
            return CommonResult.success(trfAlarmMapper.selectList(wrapper));
        } else {
            Page<TrfAlarm> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page,wrapper));
        }
    }

    /**
     * 增加交通接驳gps报警
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(TrfAlarm trfAlarm) {
        try {
            trfAlarmMapper.insert(trfAlarm);

            return CommonResult.success("成功增加交通接驳gps报警:"+trfAlarm.getId(),trfAlarm.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加交通接驳gps报警失败:"+e.getMessage());
        }
    }

    /**
     * 更新交通接驳gps报警
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(TrfAlarm trfAlarm) {
        try {

            Long id = trfAlarm.getId();
            if (trfAlarmMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳gps报警不存在:"+id);
            }

            trfAlarmMapper.updateById(trfAlarm);

            return CommonResult.success("成功更新交通接驳gps报警:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新交通接驳gps报警失败:"+e.getMessage());
        }
    }

    /**
     * 删除交通接驳gps报警
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (trfAlarmMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳gps报警不存在:"+id);
            }

            trfAlarmMapper.deleteById(id);
            return CommonResult.success("成功删除交通接驳gps报警:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳gps报警失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除交通接驳gps报警
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = trfAlarmMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除交通接驳gps报警"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳gps报警失败:"+e.getMessage());
        }
    }

}
