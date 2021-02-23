package com.example.oldtown.modules.com.mapper;

import com.example.oldtown.modules.com.model.ComFestival;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 通用节假日数据 Mapper 接口
 * </p>
 *
 * @author dyp
 * @since 2021-01-14
 */
public interface ComFestivalMapper extends BaseMapper<ComFestival> {

    @Select("SELECT\n" +
            "\t* \n" +
            "FROM\n" +
            "\t`com_festival` \n" +
            "WHERE\n" +
            "\t`year` = #{year} \n" +
            "ORDER BY\n" +
            "\tstart_date;")
    List<ComFestival> getFestivalByYear(@Param("year") Integer year);
}
