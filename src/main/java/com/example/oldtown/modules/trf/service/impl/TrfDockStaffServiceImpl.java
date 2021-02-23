package com.example.oldtown.modules.trf.service.impl;

import com.example.oldtown.dto.StringIntegerDTO;
import com.example.oldtown.modules.trf.model.TrfDockStaff;
import com.example.oldtown.modules.trf.mapper.TrfDockStaffMapper;
import com.example.oldtown.modules.trf.service.TrfDockStaffService;
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
 * 交通接驳管理人员 服务实现类
 * </p>
 * @author dyp
 * @since 2020-12-07
 */
@Service
public class TrfDockStaffServiceImpl extends ServiceImpl<TrfDockStaffMapper, TrfDockStaff> implements TrfDockStaffService {
    private final Logger LOGGER = LoggerFactory.getLogger(TrfDockStaffServiceImpl.class);

    @Resource
    TrfDockStaffMapper trfDockStaffMapper;

    /**
     * 分页查询交通接驳管理人员
     */
    @Override
    public CommonResult getAll(Integer pageSize, Integer pageNum,String type, String keyword) {

        QueryWrapper<TrfDockStaff> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TrfDockStaff> lambda = wrapper.lambda();
        if (StrUtil.isNotBlank(type)) {
            if (!type.contains(",")) {
                lambda.eq(TrfDockStaff::getType, type);
            } else {
                lambda.in(TrfDockStaff::getType, type.split(","));
            }
        }
        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(TrfDockStaff::getUsername, keyword).or().like(TrfDockStaff::getSerial,keyword);
        }
        if (pageNum == null) {
            return CommonResult.success(trfDockStaffMapper.selectList(wrapper));
        } else {
            Page<TrfDockStaff> page = new Page<>(pageNum, pageSize);
            return CommonResult.success(page(page,wrapper));
        }
    }


    /**
     * 增加交通接驳管理人员
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult add(TrfDockStaff trfDockStaff) {
        try {

            trfDockStaff.setCreateTime(new Date());
            trfDockStaffMapper.insert(trfDockStaff);

            return CommonResult.success("成功增加交通接驳管理人员:"+trfDockStaff.getId(),trfDockStaff.getId()+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("增加交通接驳管理人员失败:"+e.getMessage());
        }
    }

    /**
     * 更新交通接驳管理人员
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult update(TrfDockStaff trfDockStaff) {
        try {

            Long id = trfDockStaff.getId();
            if (trfDockStaffMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳管理人员不存在:"+id);
            }

            trfDockStaffMapper.updateById(trfDockStaff);

            return CommonResult.success("成功更新交通接驳管理人员:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("更新交通接驳管理人员失败:"+e.getMessage());
        }
    }

    /**
     * 删除交通接驳管理人员
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delete(Long id) {
        try {

            if (trfDockStaffMapper.selectById(id) == null) {
                return CommonResult.failed("该交通接驳管理人员不存在:"+id);
            }

            trfDockStaffMapper.deleteById(id);
            return CommonResult.success("成功删除交通接驳管理人员:"+id,id+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳管理人员失败:"+e.getMessage());
        }
    }

    /**
     * 批量删除交通接驳管理人员
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult batchDelete(List<Long> ids) {
        try {
            int deleteNum = trfDockStaffMapper.deleteBatchIds(ids);
            return CommonResult.success("成功删除交通接驳管理人员"+deleteNum+"个",deleteNum+"");
        } catch (Exception e) {
            LOGGER.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return CommonResult.failed("删除交通接驳管理人员失败:"+e.getMessage());
        }
    }

    /**
     * 按类型统计交通接驳管理人员数量
     */
    @Override
    public CommonResult countByType() {
        try {
            List<StringIntegerDTO> list = trfDockStaffMapper.countByType();
            int sum = 0;
            if (list!=null) {
                sum = list.stream().mapToInt(StringIntegerDTO::getValue).sum();
            }
            return CommonResult.success(list,sum+"");
        } catch (Exception e) {
            LOGGER.error("按类型统计交通接驳管理人员数量失败:", e);
            return CommonResult.failed("按类型统计交通接驳管理人员数量失败:"+e.getMessage());
        }
    }

}
