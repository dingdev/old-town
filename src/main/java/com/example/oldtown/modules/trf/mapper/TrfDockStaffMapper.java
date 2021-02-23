package com.example.oldtown.modules.trf.mapper;

import com.example.oldtown.dto.StringIntegerDTO;
import com.example.oldtown.modules.trf.model.TrfDockStaff;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 交通接驳管理人员 Mapper 接口
 * </p>
 *
 * @author dyp
 * @since 2020-12-07
 */
public interface TrfDockStaffMapper extends BaseMapper<TrfDockStaff> {
    @Select("SELECT\n" +
            "\ttype AS `key`,\n" +
            "\tCOUNT( id ) AS `value` \n" +
            "FROM\n" +
            "\t`trf_dock_staff` \n" +
            "GROUP BY\n" +
            "\ttype \n" +
            "ORDER BY\n" +
            "\t`value` DESC;")
    List<StringIntegerDTO> countByType();
}
