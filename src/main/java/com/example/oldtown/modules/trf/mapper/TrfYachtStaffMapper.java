package com.example.oldtown.modules.trf.mapper;

import com.example.oldtown.dto.StringIntegerDTO;
import com.example.oldtown.modules.trf.model.TrfSecurityStaff;
import com.example.oldtown.modules.trf.model.TrfYachtStaff;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 交通接驳游船员工 Mapper 接口
 * </p>
 *
 * @author dyp
 * @since 2020-11-25
 */
public interface TrfYachtStaffMapper extends BaseMapper<TrfYachtStaff> {

    @Select("SELECT\n" +
            "\tf.*, \n" +
            "\ty.name AS yachtName,\n" +
            "\ty.serial AS yachtSerial,\n" +
            "\ty.type AS yachtType\n" +
            "FROM\n" +
            "\ttrf_yacht_staff AS f\n" +
            "\tLEFT JOIN trf_yacht AS y ON f.yacht_id = y.id\n" +
            "WHERE\n" +
            "\tf.id = #{id}")
    TrfYachtStaff getById(@Param("id") Long id);

    @Select("SELECT\n" +
            "\ttype AS `key`,\n" +
            "\tCOUNT( id ) AS `value` \n" +
            "FROM\n" +
            "\t`trf_yacht_staff` \n" +
            "GROUP BY\n" +
            "\ttype \n" +
            "ORDER BY\n" +
            "\t`value` DESC;")
    List<StringIntegerDTO> countByType();
}
