package com.example.oldtown.modules.trf.mapper;

import com.example.oldtown.dto.StringIntegerDTO;
import com.example.oldtown.modules.trf.model.TrfSecurityStaff;
import com.example.oldtown.modules.trf.model.TrfSweepStaff;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 交通接驳打捞船员工 Mapper 接口
 * </p>
 *
 * @author dyp
 * @since 2020-11-25
 */
public interface TrfSweepStaffMapper extends BaseMapper<TrfSweepStaff> {

    @Select("SELECT\n" +
            "\tf.*, \n" +
            "\ts.name AS sweepName,\n" +
            "\ts.serial AS sweepSerial,\n" +
            "\ts.type AS sweepType\n" +
            "FROM\n" +
            "\ttrf_sweep_staff AS f\n" +
            "\tLEFT JOIN trf_sweep AS s ON f.sweep_id = s.id\n" +
            "WHERE\n" +
            "\tf.id = #{id}")
    TrfSweepStaff getById(@Param("id") Long id);

    @Select("SELECT\n" +
            "\ttype AS `key`,\n" +
            "\tCOUNT( id ) AS `value` \n" +
            "FROM\n" +
            "\t`trf_sweep_staff` \n" +
            "GROUP BY\n" +
            "\ttype \n" +
            "ORDER BY\n" +
            "\t`value` DESC;")
    List<StringIntegerDTO> countByType();
}
