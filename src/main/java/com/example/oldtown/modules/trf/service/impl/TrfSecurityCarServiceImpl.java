package com.example.oldtown.modules.trf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.oldtown.dto.MinTrfVehicle;
import com.example.oldtown.dto.StringIntegerDTO;
import com.example.oldtown.modules.trf.model.TrfSecurityCar;
import com.example.oldtown.modules.trf.mapper.TrfSecurityCarMapper;
import com.example.oldtown.modules.trf.model.TrfYacht;
import com.example.oldtown.modules.trf.service.TrfSecurityCarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
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
 * 交通接驳安保车辆 服务实现类
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
@Service
public class TrfSecurityCarServiceImpl extends ServiceImpl<TrfSecurityCarMapper, TrfSecurityCar> implements TrfSecurityCarService {
    private final Logger LOGGER = LoggerFactory.getLogger(TrfSecurityCarServiceImpl.class);

    @Resource
    TrfSecurityCarMapper trfSecurityCarMapper;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 分页查询交通接驳安保车辆
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum, String keyword, Integer orderByCurrentStatus) {
        QueryWrapper<TrfSecurityCar> ew = new QueryWrapper<>();

        if (StrUtil.isNotBlank(keyword)) {
            ew.like("c.name", keyword).or().like("c.serial",keyword);
        }

        if (orderByCurrentStatus > 0) {
            ew.orderByDesc("c.current_status");
        }

        if (pageNum == null) {
            return CommonResult.success(trfSecurityCarMapper.getAllList(ew));
        } else {
            Page<TrfSecurityCar> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(trfSecurityCarMapper.getPageList(page,ew));
        }
    }

    /**
     * 根据id查询交通接驳游船
     */
    @Override
    public CommonResult getSecurityCarById(Long id) {
        TrfSecurityCar trfSecurityCar = trfSecurityCarMapper.getById(id);
        return CommonResult.success(trfSecurityCar);
    }


    /**
     * 增加交通接驳安保车辆
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(TrfSecurityCar trfSecurityCar) {
        try {

            trfSecurityCar.setCreateTime(new Date());
            trfSecurityCarMapper.insert(trfSecurityCar);
            Long securityCarId = trfSecurityCar.getId();

            // 缓存更新
            String gpsCode = trfSecurityCar.getGpsCode();
            if (StrUtil.isNotBlank(gpsCode)) {
                HashOperations<String, String, MinTrfVehicle> hashOperations = redisTemplate.opsForHash();
                hashOperations.put("trfSecurityCarGPS",gpsCode,
                        new MinTrfVehicle(securityCarId,trfSecurityCar.getName(),gpsCode,System.currentTimeMillis()/1000,null));
            }

            return CommonResult.success("成功增加交通接驳安保车辆:"+securityCarId,securityCarId+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加交通接驳安保车辆失败:"+e.getMessage());
        }
    }

    /**
     * 更新交通接驳安保车辆
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(TrfSecurityCar trfSecurityCar) {
        try {

            Long securityCarId = trfSecurityCar.getId();
            if (trfSecurityCarMapper.selectById(securityCarId) == null) {
                return CommonResult.failed("该交通接驳安保车辆不存在:"+securityCarId);
            }

            trfSecurityCarMapper.updateById(trfSecurityCar);

            // 缓存更新
            String gpsCode = trfSecurityCar.getGpsCode();
            if (StrUtil.isNotBlank(gpsCode)) {
                HashOperations<String, String, MinTrfVehicle> hashOperations = redisTemplate.opsForHash();
                hashOperations.put("trfSecurityCarGPS",gpsCode,
                        new MinTrfVehicle(securityCarId,trfSecurityCar.getName(),gpsCode,System.currentTimeMillis()/1000,null));
            }

            return CommonResult.success("成功更新交通接驳安保车辆:"+securityCarId,securityCarId+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新交通接驳安保车辆失败:"+e.getMessage());
        }
    }

    /**
     * 删除交通接驳安保车辆
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (trfSecurityCarMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳安保车辆不存在:"+id);
            }

            trfSecurityCarMapper.deleteById(id);
            return CommonResult.success("成功删除交通接驳安保车辆:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳安保车辆失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除交通接驳安保车辆
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = trfSecurityCarMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除交通接驳安保车辆"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳安保车辆失败:"+e.getMessage());
        }
    }

    /**
     * 统计交通接驳游船数量
     */
    @Override
    public CommonResult countByType() {

        try {
            List<StringIntegerDTO> list = trfSecurityCarMapper.countByType();
            int sum = 0;
            if (list!=null) {
                sum = list.stream().mapToInt(StringIntegerDTO::getValue).sum();
            }
            return CommonResult.success(list,sum+"");
        } catch (Exception e) {
            LOGGER.error("统计交通接驳打捞船数量失败:", e);
            return CommonResult.failed("统计交通接驳打捞船数量失败:"+e.getMessage());
        }
    }

}
