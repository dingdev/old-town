package com.example.oldtown.modules.trf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.oldtown.dto.StringIntegerDTO;
import com.example.oldtown.modules.trf.mapper.TrfYachtMapper;
import com.example.oldtown.modules.trf.model.TrfSweep;
import com.example.oldtown.modules.trf.model.TrfYacht;
import com.example.oldtown.modules.trf.model.TrfYachtStaff;
import com.example.oldtown.modules.trf.model.TrfYachtStaff;
import com.example.oldtown.modules.trf.mapper.TrfYachtStaffMapper;
import com.example.oldtown.modules.trf.service.TrfYachtStaffService;
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
 * 交通接驳游船员工 服务实现类
 * </p>
 * @author dyp
 * @since 2020-11-25
 */
@Service
public class TrfYachtStaffServiceImpl extends ServiceImpl<TrfYachtStaffMapper, TrfYachtStaff> implements TrfYachtStaffService {
    private final Logger LOGGER = LoggerFactory.getLogger(TrfYachtStaffServiceImpl.class);

    @Resource
    TrfYachtStaffMapper trfYachtStaffMapper;

    @Resource
    TrfYachtMapper trfYachtMapper;

    /**
     * 分页查询交通接驳游船员工
     * @return
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum,String type, String keyword) {

        QueryWrapper<TrfYachtStaff> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TrfYachtStaff> lambda = wrapper.lambda();
        if(StrUtil.isNotBlank(type)){
            if (!type.contains(",")) {
                lambda.eq(TrfYachtStaff::getType, type);
            } else {
                lambda.in(TrfYachtStaff::getType, type);
            }
        }
        if (StrUtil.isNotBlank(keyword)) {
            lambda.and(lambda1 -> lambda1.like(TrfYachtStaff::getUsername, keyword).or().like(TrfYachtStaff::getNickname, keyword)
                                .or().like(TrfYachtStaff::getSerial,keyword)) ;
        }
        if (pageNum == null) {
            return CommonResult.success(trfYachtStaffMapper.selectList(wrapper));
        } else {
            Page<TrfYachtStaff> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page,wrapper));
        }
    }

    /**
     * 根据id查询交通接驳游船员工
     */
    @Override
    public CommonResult getYachtStaffById(Long id) {
        TrfYachtStaff trfYachtStaff = trfYachtStaffMapper.getById(id);
        return CommonResult.success(trfYachtStaff);
    }


    /**
     * 增加交通接驳游船员工
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(TrfYachtStaff trfYachtStaff) {
        try {

            trfYachtStaff.setCreateTime(new Date());
            trfYachtStaffMapper.insert(trfYachtStaff);

            return CommonResult.success("成功增加交通接驳游船员工:"+trfYachtStaff.getId(),trfYachtStaff.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加交通接驳游船员工失败:"+e.getMessage());
        }
    }

    /**
     * 更新交通接驳游船员工
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(TrfYachtStaff trfYachtStaff) {
        try {

            Long id = trfYachtStaff.getId();
            if (trfYachtStaffMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳游船员工不存在:"+id);
            }

            trfYachtStaffMapper.updateById(trfYachtStaff);

            return CommonResult.success("成功更新交通接驳游船员工:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新交通接驳游船员工失败:"+e.getMessage());
        }
    }

    /**
     * 删除交通接驳游船员工
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {
            TrfYachtStaff trfYachtStaff = trfYachtStaffMapper.selectById(id);
            if (trfYachtStaff == null) {
                return CommonResult.failed("该交通接驳游船员工不存在:"+id);
            }

            trfYachtStaffMapper.deleteById(id);

            Long yachtId = trfYachtStaff.getYachtId();
            if (yachtId != null) {
                UpdateWrapper<TrfYacht> wrapper = new UpdateWrapper<>();
                wrapper.lambda().set(TrfYacht::getStaffId,null).eq(TrfYacht::getId,yachtId);
                trfYachtMapper.update(null, wrapper);
            }

            return CommonResult.success("成功删除交通接驳游船员工:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳游船员工失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除交通接驳游船员工
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = trfYachtStaffMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除交通接驳游船员工"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳游船员工失败:"+e.getMessage());
        }
    }

    /**
     * 按类型统计交通接驳游船员工数量
     */
    @Override
    public CommonResult countByType() {
        try {
            List<StringIntegerDTO> list = trfYachtStaffMapper.countByType();
            int sum = 0;
            if (list!=null) {
                sum = list.stream().mapToInt(StringIntegerDTO::getValue).sum();
            }
            return CommonResult.success(list,sum+"");
        } catch (Exception e) {
            LOGGER.error("按类型统计交通接驳打游船员工数量失败:", e);
            return CommonResult.failed("按类型统计交通接驳游船员工数量失败:"+e.getMessage());
        }
    }
}
