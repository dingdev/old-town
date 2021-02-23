package com.example.oldtown.modules.trf.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.dto.StringIntegerDTO;
import com.example.oldtown.modules.trf.model.TrfSecurityStaff;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 交通接驳安保人员 Mapper 接口
 * </p>
 *
 * @author dyp
 * @since 2020-11-25
 */
public interface TrfSecurityStaffMapper extends BaseMapper<TrfSecurityStaff> {


    @Select("SELECT\n" +
            "\tf.*, \n" +
            "\tr.name AS routeName,\n" +
            "\tr.post AS routePost\n" +
            "FROM\n" +
            "\ttrf_security_staff AS f\n" +
            "\tLEFT JOIN trf_security_route AS r ON f.route_id = r.id\n" +
            "${ew.customSqlSegment}" )
    List<TrfSecurityStaff> getAllList(@Param("ew") QueryWrapper<TrfSecurityStaff> ew);

    @Select("SELECT\n" +
            "\tf.*, \n" +
            "\tr.name AS routeName,\n" +
            "\tr.post AS routePost\n" +
            "FROM\n" +
            "\ttrf_security_staff AS f\n" +
            "\tLEFT JOIN trf_security_route AS r ON f.route_id = r.id\n" +
            "${ew.customSqlSegment}" )
    Page<TrfSecurityStaff> getPageList(@Param("page") Page<TrfSecurityStaff> page, @Param("ew") QueryWrapper<TrfSecurityStaff> ew);
    
    @Select("SELECT\n" +
            "\tf.*, \n" +
            "\tr.name AS routeName,\n" +
            "\tr.post AS routePost\n" +
            "FROM\n" +
            "\ttrf_security_staff AS f\n" +
            "\tLEFT JOIN trf_security_route AS r ON f.route_id = r.id\n" +
            "WHERE\n" +
            "\tf.id = #{id}")
    TrfSecurityStaff getById(@Param("id") Long id);

    @Select("SELECT\n" +
            "\ttype AS `key`,\n" +
            "\tCOUNT( id ) AS `value` \n" +
            "FROM\n" +
            "\t`trf_security_staff` \n" +
            "GROUP BY\n" +
            "\ttype \n" +
            "ORDER BY\n" +
            "\t`value` DESC;")
    List<StringIntegerDTO> countByType();

    @Update("UPDATE `trf_security_staff` \n" +
            "SET current_status = NULL")
    int nullCurrentStatus();

}