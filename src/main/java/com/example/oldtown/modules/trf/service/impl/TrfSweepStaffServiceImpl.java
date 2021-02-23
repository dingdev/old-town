package com.example.oldtown.modules.trf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.oldtown.dto.StringIntegerDTO;
import com.example.oldtown.dto.TrfSweepStaffDTO;
import com.example.oldtown.modules.trf.mapper.TrfSweepMapper;
import com.example.oldtown.modules.trf.model.TrfSweep;
import com.example.oldtown.modules.trf.model.TrfSweepStaff;
import com.example.oldtown.modules.trf.mapper.TrfSweepStaffMapper;
import com.example.oldtown.modules.trf.model.TrfYachtStaff;
import com.example.oldtown.modules.trf.service.TrfSweepStaffService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
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
 * 交通接驳打捞船员工 服务实现类
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
@Service
public class TrfSweepStaffServiceImpl extends ServiceImpl<TrfSweepStaffMapper, TrfSweepStaff> implements TrfSweepStaffService {
    private final Logger LOGGER = LoggerFactory.getLogger(TrfSweepStaffServiceImpl.class);

    @Resource
    TrfSweepStaffMapper trfSweepStaffMapper;

    @Resource
    TrfSweepMapper trfSweepMapper;

    /**
     * 分页查询交通接驳打捞船员工
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum,String type, String keyword) {

        QueryWrapper<TrfSweepStaff> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TrfSweepStaff> lambda = wrapper.lambda();
        if(StrUtil.isNotBlank(type)){
            if (!type.contains(",")) {
                lambda.eq(TrfSweepStaff::getType, type);
            } else {
                lambda.in(TrfSweepStaff::getType, type);
            }
        }
        if (StrUtil.isNotBlank(keyword)) {
            lambda.and(lambda1 ->lambda1.like(TrfSweepStaff::getUsername, keyword).or().like(TrfSweepStaff::getNickname, keyword)
                                .or().like(TrfSweepStaff::getSerial,keyword)) ;
        }
        if (pageNum == null) {
            return CommonResult.success(trfSweepStaffMapper.selectList(wrapper));
        } else {
            Page<TrfSweepStaff> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page,wrapper));
        }
    }

    /**
     * 根据id查询交通接驳打捞船员工
     */
    @Override
    public CommonResult getSweepStaffById(Long id) {
        TrfSweepStaff trfSweepStaff = trfSweepStaffMapper.getById(id);
        return CommonResult.success(trfSweepStaff);
    }


    /**
     * 增加交通接驳打捞船员工
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(TrfSweepStaff trfSweepStaff) {
        try {

            trfSweepStaff.setCreateTime(new Date());
            trfSweepStaffMapper.insert(trfSweepStaff);

            return CommonResult.success("成功增加交通接驳打捞船员工:"+trfSweepStaff.getId(),trfSweepStaff.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加交通接驳打捞船员工失败:"+e.getMessage());
        }
    }

    /**
     * 更新交通接驳打捞船员工
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(TrfSweepStaff trfSweepStaff) {
        try {

            Long id = trfSweepStaff.getId();
            if (trfSweepStaffMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳打捞船员工不存在:"+id);
            }

            trfSweepStaffMapper.updateById(trfSweepStaff);

            return CommonResult.success("成功更新交通接驳打捞船员工:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新交通接驳打捞船员工失败:"+e.getMessage());
        }
    }

    /**
     * 删除交通接驳打捞船员工
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {
            TrfSweepStaff trfSweepStaff = trfSweepStaffMapper.selectById(id);
            if (trfSweepStaff == null) {
                return CommonResult.failed("该交通接驳打捞船员工不存在:"+id);
            }

            trfSweepStaffMapper.deleteById(id);

            Long sweepId = trfSweepStaff.getSweepId();
            if (sweepId != null) {
                UpdateWrapper<TrfSweep> wrapper = new UpdateWrapper<>();
                wrapper.lambda().set(TrfSweep::getStaffId,null).eq(TrfSweep::getId,sweepId);
                trfSweepMapper.update(null, wrapper);
            }

            return CommonResult.success("成功删除交通接驳打捞船员工:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳打捞船员工失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除交通接驳打捞船员工
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = trfSweepStaffMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除交通接驳打捞船员工"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳打捞船员工失败:"+e.getMessage());
        }
    }

    /**
     * 按类型统计交通接驳打捞船员工数量
     */
    @Override
    public CommonResult countByType() {
        try {
            List<StringIntegerDTO> list = trfSweepStaffMapper.countByType();
            int sum = 0;
            if (list!=null) {
                sum = list.stream().mapToInt(StringIntegerDTO::getValue).sum();
            }
            return CommonResult.success(list,sum+"");
        } catch (Exception e) {
            LOGGER.error("按类型统计交通接驳打捞船员工数量失败:", e);
            return CommonResult.failed("按类型统计交通接驳打捞船员工数量失败:"+e.getMessage());
        }
    }
}
