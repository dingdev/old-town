package com.example.oldtown.modules.trf.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.example.oldtown.modules.trf.model.TrfGps;
import com.example.oldtown.modules.trf.mapper.TrfGpsMapper;
import com.example.oldtown.modules.trf.service.TrfGpsService;
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
 * 交通接驳gps定位点 服务实现类
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
@Service
public class TrfGpsServiceImpl extends ServiceImpl<TrfGpsMapper, TrfGps> implements TrfGpsService {
    private final Logger LOGGER = LoggerFactory.getLogger(TrfGpsServiceImpl.class);

    @Resource
    TrfGpsMapper trfGpsMapper;
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 分页查询交通接驳gps定位点
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum) {
        Page<TrfGps> page = new Page<>(pageNum, pageSize);
        QueryWrapper<TrfGps> wrapper = new QueryWrapper<>();


        return CommonResult.success(page(page,wrapper));
    }


    /**
     * 增加交通接驳gps定位点
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(TrfGps trfGps) {
        try {

            String gpsCode = trfGps.getGpsCode();
            if (StrUtil.isNotBlank(gpsCode)) {
                return CommonResult.failed("增加交通接驳gps定位点失败:gpsCode为空");
            }
            // trfGps.setCreateTime(new Date());
            trfGpsMapper.insert(trfGps);

            return CommonResult.success("成功增加交通接驳gps定位点:"+trfGps.getId(),trfGps.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加交通接驳gps定位点失败:"+e.getMessage());
        }
    }

    /**
     * 更新交通接驳gps定位点
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(TrfGps trfGps) {
        try {

            Long id = trfGps.getId();
            if (trfGpsMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳gps定位点不存在:"+id);
            }

            trfGpsMapper.updateById(trfGps);

            return CommonResult.success("成功更新交通接驳gps定位点:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新交通接驳gps定位点失败:"+e.getMessage());
        }
    }

    /**
     * 删除交通接驳gps定位点
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (trfGpsMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳gps定位点不存在:"+id);
            }

            trfGpsMapper.deleteById(id);
            return CommonResult.success("成功删除交通接驳gps定位点:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳gps定位点失败:"+e.getMessage());
        }
    }


    /**
     * 批量删除交通接驳gps定位点
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = trfGpsMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除交通接驳gps定位点"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳gps定位点失败:"+e.getMessage());
        }
    }

    // /**
    //  * 根据gps设备编号查询今日最新定位点
    //  */
    // @Override
    // public TrfGps getLatestByGpsCode(String gpsCode) {
    //
    //     // QueryWrapper<TrfGps> wrapper = new QueryWrapper<>();
    //     // wrapper.eq("DATE(receive_time)", "CURRENT_DATE").eq("gps_code",gpsCode);
    //     // wrapper.orderByDesc("id");
    //     HashOperations<String, String, TrfGps> hashOperations = redisTemplate.opsForHash();
    //     TrfGps trfGps = hashOperations.get("GpsHashMap", gpsCode);
    //     return trfGps;
    // }

    /**
     * 根据gps设备编号查询某日定位点集
     */
    @Override
    public List<TrfGps> getListByGpsCode(String gpsCode, String fromTime, String toTime) {
        Long from = DateUtil.parseDateTime(fromTime).getTime()/1000;
        Long to = DateUtil.parseDateTime(toTime).getTime()/1000;
        QueryWrapper<TrfGps> wrapper = new QueryWrapper<>();
        wrapper.lambda().gt(TrfGps::getGpsTime,from).lt(TrfGps::getGpsTime,to).eq(TrfGps::getGpsCode,gpsCode);
        return trfGpsMapper.selectList (wrapper);
    }

}
