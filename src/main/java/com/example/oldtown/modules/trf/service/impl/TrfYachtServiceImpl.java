package com.example.oldtown.modules.trf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.oldtown.dto.MinTrfVehicle;
import com.example.oldtown.dto.StringIntegerDTO;
import com.example.oldtown.modules.trf.mapper.TrfYachtStaffMapper;
import com.example.oldtown.modules.trf.model.TrfSweepStaff;
import com.example.oldtown.modules.trf.model.TrfYacht;
import com.example.oldtown.modules.trf.mapper.TrfYachtMapper;
import com.example.oldtown.modules.trf.model.TrfYachtStaff;
import com.example.oldtown.modules.trf.service.TrfYachtService;
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
 * 交通接驳游船 服务实现类
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
@Service
public class TrfYachtServiceImpl extends ServiceImpl<TrfYachtMapper, TrfYacht> implements TrfYachtService {
    private final Logger LOGGER = LoggerFactory.getLogger(TrfYachtServiceImpl.class);

    @Resource
    TrfYachtMapper trfYachtMapper;
    @Resource
    TrfYachtStaffMapper trfYachtStaffMapper;

    @Autowired
    RedisTemplate redisTemplate;
    /**
     * 分页查询交通接驳游船
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum,String type, String keyword,Integer orderByCurrentStatus) {

        QueryWrapper<TrfYacht> ew = new QueryWrapper<>();
        if(StrUtil.isNotBlank(type)){
            if (!type.contains(",")) {
                ew.eq("y.type", type);
            } else {
                ew.in("y.type", type.split(","));
            }

        }
        if (StrUtil.isNotBlank(keyword)) {
            ew.and(wrapper -> wrapper.like("y.name", keyword).or().like("y.serial", keyword)) ;
        }

        if (orderByCurrentStatus > 0) {
            ew.orderByDesc("y.current_status");
        }

        if (pageNum == null) {
            return CommonResult.success(trfYachtMapper.getAllList(ew));
        } else {
            Page<TrfYacht> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(trfYachtMapper.getPageList(page,ew));
        }
    }

    /**
     * 根据id查询交通接驳游船
     */
    @Override
    public CommonResult getYachtById(Long id) {
        TrfYacht trfYacht = trfYachtMapper.getById(id);
        return CommonResult.success(trfYacht);
    }


    /**
     * 增加交通接驳游船
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(TrfYacht trfYacht) {
        try {

            trfYacht.setCreateTime(new Date());
            trfYachtMapper.insert(trfYacht);
            Long yachtId = trfYacht.getId();

            // 船工信息更新
            Long staffId = trfYacht.getStaffId();
            if (staffId != null) {
                TrfYachtStaff trfYachtStaff = new TrfYachtStaff();
                trfYachtStaff.setId(staffId);
                trfYachtStaff.setYachtId(yachtId);
                trfYachtStaffMapper.updateById(trfYachtStaff);
            }

            // 缓存更新
            String gpsCode = trfYacht.getGpsCode();
            if (StrUtil.isNotBlank(gpsCode)) {
                HashOperations<String, String, MinTrfVehicle> hashOperations = redisTemplate.opsForHash();
                hashOperations.put("trfYachtGPS",gpsCode,
                        new MinTrfVehicle(yachtId,trfYacht.getName(),gpsCode,System.currentTimeMillis()/1000,null));
            }

            return CommonResult.success("成功增加交通接驳游船:"+trfYacht.getId(),trfYacht.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加交通接驳游船失败:"+e.getMessage());
        }
    }

    /**
     * 更新交通接驳游船
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(TrfYacht trfYacht) {
        try {

            Long yachtId = trfYacht.getId();
            if (trfYachtMapper.selectById(yachtId) == null) {
                return CommonResult.failed("该交通接驳游船不存在:"+yachtId);
            }

            trfYachtMapper.updateById(trfYacht);

            Long staffId = trfYacht.getStaffId();
            if (staffId != null) {
                TrfYachtStaff trfYachtStaff = new TrfYachtStaff();
                trfYachtStaff.setId(staffId);
                trfYachtStaff.setYachtId(yachtId);
                trfYachtStaffMapper.updateById(trfYachtStaff);
            }

            // 缓存更新
            String gpsCode = trfYacht.getGpsCode();
            if (StrUtil.isNotBlank(gpsCode)) {
                HashOperations<String, String, MinTrfVehicle> hashOperations = redisTemplate.opsForHash();
                hashOperations.put("trfYachtGPS",gpsCode,
                        new MinTrfVehicle(yachtId,trfYacht.getName(),gpsCode,System.currentTimeMillis()/1000,null));
            }

            return CommonResult.success("成功更新交通接驳游船:"+yachtId,yachtId+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新交通接驳游船失败:"+e.getMessage());
        }
    }

    /**
     * 删除交通接驳游船
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {
            TrfYacht trfYacht = trfYachtMapper.selectById(id);
            if (trfYacht == null) {
                return CommonResult.failed("该交通接驳游船不存在:"+id);
            }

            trfYachtMapper.deleteById(id);

            Long staffId = trfYacht.getStaffId();
            if (staffId != null) {
                UpdateWrapper<TrfYachtStaff> wrapper = new UpdateWrapper<>();
                wrapper.lambda().set(TrfYachtStaff::getYachtId,null).eq(TrfYachtStaff::getId,staffId);
                trfYachtStaffMapper.update(null, wrapper);
            }

            return CommonResult.success("成功删除交通接驳游船:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳游船失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除交通接驳游船
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = trfYachtMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除交通接驳游船"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳游船失败:"+e.getMessage());
        }
    }


    /**
     * 统计交通接驳游船数量
     */
    @Override
    public CommonResult countByType() {

        try {
            List<StringIntegerDTO> list = trfYachtMapper.countByType();
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
