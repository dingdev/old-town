package com.example.oldtown.modules.trf.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.oldtown.dto.MinTrfVehicle;
import com.example.oldtown.dto.StringIntegerDTO;
import com.example.oldtown.modules.trf.mapper.TrfSweepStaffMapper;
import com.example.oldtown.modules.trf.model.TrfSweep;
import com.example.oldtown.modules.trf.mapper.TrfSweepMapper;
import com.example.oldtown.modules.trf.model.TrfSweepStaff;
import com.example.oldtown.modules.trf.service.TrfSweepService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.oldtown.common.api.CommonResult;

/**
 * <p>
 * 交通接驳打捞船 服务实现类
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
@Service
public class TrfSweepServiceImpl extends ServiceImpl<TrfSweepMapper, TrfSweep> implements TrfSweepService {
    private final Logger LOGGER = LoggerFactory.getLogger(TrfSweepServiceImpl.class);

    @Resource
    TrfSweepMapper trfSweepMapper;
    @Resource
    TrfSweepStaffMapper trfSweepStaffMapper;

    @Autowired
    RedisTemplate redisTemplate;


    /**
     * 分页查询交通接驳打捞船
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String type, String keyword,Integer orderByCurrentStatus) {
        QueryWrapper<TrfSweep> ew = new QueryWrapper<>();
        if(StrUtil.isNotBlank(type)){
            if (!type.contains(",")) {
                ew.eq("s.type", type);
            } else {
                ew.in("s.type", type.split(","));
            }
        }

        if (StrUtil.isNotBlank(keyword)) {
            ew.and(wrapper -> wrapper.like("s.name",keyword).or().like("s.serial",keyword));
        }

        if (orderByCurrentStatus > 0) {
            ew.orderByDesc("s.current_status");
        }

        if (pageNum == null) {
            return CommonResult.success(trfSweepMapper.getAllList(ew));
        } else {
            Page<TrfSweep> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(trfSweepMapper.getPageList(page,ew));
        }
    }

    /**
     * 根据id查询交通接驳打捞船
     */
    @Override
    public CommonResult getSweepById(Long id) {
        TrfSweep trfSweep = trfSweepMapper.getById(id);
        return CommonResult.success(trfSweep);
    }


    /**
     * 增加交通接驳打捞船
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(TrfSweep trfSweep) {
        try {

            trfSweep.setCreateTime(new Date());
            trfSweepMapper.insert(trfSweep);
            Long sweepId = trfSweep.getId();

            // 船工信息更新
            Long staffId = trfSweep.getStaffId();
            if (staffId != null) {
                TrfSweepStaff trfSweepStaff = new TrfSweepStaff();
                trfSweepStaff.setId(staffId);
                trfSweepStaff.setSweepId(sweepId);
                trfSweepStaffMapper.updateById(trfSweepStaff);
            }

            // 缓存更新
            String gpsCode = trfSweep.getGpsCode();
            if (StrUtil.isNotBlank(gpsCode)) {
                HashOperations<String, String, MinTrfVehicle> hashOperations = redisTemplate.opsForHash();
                hashOperations.put("trfSweepGPS",gpsCode,
                        new MinTrfVehicle(sweepId,trfSweep.getName(),gpsCode,System.currentTimeMillis()/1000,null));
            }

            return CommonResult.success("成功增加交通接驳打捞船:"+trfSweep.getId(),trfSweep.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加交通接驳打捞船失败:"+e.getMessage());
        }
    }

    /**
     * 更新交通接驳打捞船
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(TrfSweep trfSweep) {
        try {

            Long sweepId = trfSweep.getId();
            if (trfSweepMapper.selectById(sweepId) == null) {
                return CommonResult.failed("该交通接驳打捞船不存在:"+sweepId);
            }

            trfSweepMapper.updateById(trfSweep);

            // 船工信息更新
            Long staffId = trfSweep.getStaffId();
            if (staffId != null) {
                TrfSweepStaff trfSweepStaff = new TrfSweepStaff();
                trfSweepStaff.setId(staffId);
                trfSweepStaff.setSweepId(sweepId);
                trfSweepStaffMapper.updateById(trfSweepStaff);
            }

            // 缓存更新
            String gpsCode = trfSweep.getGpsCode();
            if (StrUtil.isNotBlank(gpsCode)) {
                HashOperations<String, String, MinTrfVehicle> hashOperations = redisTemplate.opsForHash();
                hashOperations.put("trfSweepGPS",gpsCode,
                        new MinTrfVehicle(sweepId,trfSweep.getName(),gpsCode,System.currentTimeMillis()/1000,null));
            }

            return CommonResult.success("成功更新交通接驳打捞船:"+sweepId,sweepId+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新交通接驳打捞船失败:"+e.getMessage());
        }
    }

    /**
     * 删除交通接驳打捞船
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {
            TrfSweep trfSweep = trfSweepMapper.selectById(id);
            if (trfSweep == null) {
                return CommonResult.failed("该交通接驳打捞船不存在:"+id);
            }

            trfSweepMapper.deleteById(id);

            Long staffId = trfSweep.getStaffId();
            if (staffId != null) {
                UpdateWrapper<TrfSweepStaff> wrapper = new UpdateWrapper<>();
                wrapper.lambda().set(TrfSweepStaff::getSweepId,null).eq(TrfSweepStaff::getId,staffId);
                trfSweepStaffMapper.update(null, wrapper);
            }

            return CommonResult.success("成功删除交通接驳打捞船:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳打捞船失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除交通接驳打捞船
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = trfSweepMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除交通接驳打捞船"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳打捞船失败:"+e.getMessage());
        }
    }

    /**
     * 按类型统计交通接驳打捞船数量
     */
    @Override
    public CommonResult countByType() {

        try {
            List<StringIntegerDTO> list = trfSweepMapper.countByType();
            int sum = 0;
            if (list!=null) {
                sum = list.stream().mapToInt(StringIntegerDTO::getValue).sum();
            }
            return CommonResult.success(list,sum+"");
        } catch (Exception e) {
            LOGGER.error("按类型统计交通接驳打捞船数量失败:", e);
            return CommonResult.failed("按类型统计交通接驳打捞船数量失败:"+e.getMessage());
        }
    }

    /**
     * 按当前状态统计交通接驳打捞船数量
     */
    @Override
    public CommonResult countByCurrentStatus() {

        try {
            List<StringIntegerDTO> list = trfSweepMapper.countByCurrentStatus();
            int sum = 0;
            if (list!=null) {
                sum = list.stream().mapToInt(StringIntegerDTO::getValue).sum();
            }
            return CommonResult.success(list,sum+"");
        } catch (Exception e) {
            LOGGER.error("按当前状态统计交通接驳打捞船数量失败:", e);
            return CommonResult.failed("按当前状态统计交通接驳打捞船数量失败:"+e.getMessage());
        }
    }


}
