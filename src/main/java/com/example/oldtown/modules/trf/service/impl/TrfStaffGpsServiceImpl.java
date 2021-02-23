package com.example.oldtown.modules.trf.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.oldtown.dto.Gps;
import com.example.oldtown.modules.trf.mapper.TrfSecurityStaffMapper;
import com.example.oldtown.modules.trf.model.TrfGps;
import com.example.oldtown.modules.trf.model.TrfSecurityStaff;
import com.example.oldtown.modules.trf.model.TrfStaffGps;
import com.example.oldtown.modules.trf.mapper.TrfStaffGpsMapper;
import com.example.oldtown.modules.trf.service.TrfStaffGpsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.util.GPSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.oldtown.common.api.CommonResult;

/**
 * <p>
 * 交通接驳员工gps定位点 服务实现类
 * </p>
 * @author dyp
 * @since 2020-12-24
 */
@Service
public class TrfStaffGpsServiceImpl extends ServiceImpl<TrfStaffGpsMapper, TrfStaffGps> implements TrfStaffGpsService {
    private final Logger LOGGER = LoggerFactory.getLogger(TrfStaffGpsServiceImpl.class);

    @Resource
    TrfStaffGpsMapper trfStaffGpsMapper;
    @Resource
    TrfSecurityStaffMapper trfSecurityStaffMapper;
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 分页查询交通接驳员工gps定位点
     * @return
     */
    @Override
    public Page<TrfStaffGps> getAll(Integer pageSize, Integer pageNum, String keyword) {
        Page<TrfStaffGps> page = new Page<>(pageNum, pageSize);
        QueryWrapper<TrfStaffGps> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TrfStaffGps> lambda = wrapper.lambda();
        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(TrfStaffGps::getGpsCode, keyword);
        }
        return page(page,wrapper);
    }


    /**
     * 增加交通接驳员工gps定位点
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(TrfStaffGps trfStaffGps) {
        try {
            String gpsCode = trfStaffGps.getGpsCode();
            Long gpsTime = trfStaffGps.getGpsTime();
            if (StrUtil.isBlank(gpsCode) || gpsTime == null) {
                return CommonResult.failed("gpsCode和gpsTime不应为空");
            }

            // 限制频率 一分钟一次
            HashOperations<String, String, Double> hashOperations = redisTemplate.opsForHash();
            // Integer oldTime = hashOperations.get("trfStaffGPS", gpsCode);
            // if (oldTime != null && gpsTime.intValue() - oldTime < 60) {
            //     return CommonResult.success("一分钟之内已增加此gpsCode的位置:" + gpsCode);
            // }

            // 百度转为84
            Gps gps = GPSUtil.gcj_To_Gps84(trfStaffGps.getLatitude(),trfStaffGps.getLongitude());
            trfStaffGps.setLatitude(gps.getWgLat());
            trfStaffGps.setLongitude(gps.getWgLon());

            // staffGps表
            trfStaffGpsMapper.insert(trfStaffGps);

            // staff表
            UpdateWrapper<TrfSecurityStaff> wrapper = new UpdateWrapper<>();
            wrapper.lambda().eq(TrfSecurityStaff::getGpsCode, trfStaffGps.getGpsCode())
                    .set(TrfSecurityStaff::getLongitude, trfStaffGps.getLongitude())
                    .set(TrfSecurityStaff::getLatitude, trfStaffGps.getLatitude())
                    .set(TrfSecurityStaff::getCurrentStatus,trfStaffGps.getLongitude()
                            .equals(hashOperations.get("trfStaffGPS", gpsCode))?"静止":"行驶")
                    .set(TrfSecurityStaff::getGpsTime, gpsTime);

            trfSecurityStaffMapper.update(null, wrapper);

            hashOperations.put("trfStaffGPS", gpsCode,trfStaffGps.getLongitude());

            return CommonResult.success("成功增加交通接驳员工gps定位点:"+trfStaffGps.getId(),trfStaffGps.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加交通接驳员工gps定位点失败:"+e.getMessage());
        }
    }

    /**
     * 更新交通接驳员工gps定位点
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(TrfStaffGps trfStaffGps) {
        try {

            Long id = trfStaffGps.getId();
            if (trfStaffGpsMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳员工gps定位点不存在:"+id);
            }

            trfStaffGpsMapper.updateById(trfStaffGps);

            return CommonResult.success("成功更新交通接驳员工gps定位点:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新交通接驳员工gps定位点失败:"+e.getMessage());
        }
    }

    /**
     * 删除交通接驳员工gps定位点
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (trfStaffGpsMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳员工gps定位点不存在:"+id);
            }

            trfStaffGpsMapper.deleteById(id);
            return CommonResult.success("成功删除交通接驳员工gps定位点:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳员工gps定位点失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除交通接驳员工gps定位点
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = trfStaffGpsMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除交通接驳员工gps定位点"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳员工gps定位点失败:"+e.getMessage());
        }
    }

    /**
     * 根据gps设备编号查询某日定位点集
     */
    @Override
    public List<TrfStaffGps> getListByGpsCode(String gpsCode, String fromTime, String toTime) {
        Long from = DateUtil.parseDateTime(fromTime).getTime()/1000;
        Long to = DateUtil.parseDateTime(toTime).getTime()/1000;
        QueryWrapper<TrfStaffGps> wrapper = new QueryWrapper<>();
        wrapper.lambda().gt(TrfStaffGps::getGpsTime,from).lt(TrfStaffGps::getGpsTime,to).eq(TrfStaffGps::getGpsCode,gpsCode);
        return trfStaffGpsMapper.selectList(wrapper);
    }

}
