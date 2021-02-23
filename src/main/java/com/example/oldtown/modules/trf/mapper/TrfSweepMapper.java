package com.example.oldtown.modules.trf.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.dto.MinTrfVehicle;
import com.example.oldtown.dto.StringIntegerDTO;
import com.example.oldtown.modules.trf.model.TrfSweep;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 交通接驳打捞船 Mapper 接口
 * </p>
 *
 * @author dyp
 * @since 2020-11-25
 */
public interface TrfSweepMapper extends BaseMapper<TrfSweep> {
    @Select("SELECT\n" +
            "\ttype AS `key`,\n" +
            "\tCOUNT( id ) AS `value` \n" +
            "FROM\n" +
            "\t`trf_sweep` \n" +
            "GROUP BY\n" +
            "\ttype \n" +
            "ORDER BY\n" +
            "\t`value` DESC;")
    List<StringIntegerDTO> countByType();

    @Select("SELECT\n" +
            "\tcurrent_status AS `key`,\n" +
            "\tCOUNT( id ) AS `value` \n" +
            "FROM\n" +
            "\t`trf_sweep` \n" +
            "GROUP BY\n" +
            "\tcurrent_status\n" +
            "ORDER BY\n" +
            "\t`value` DESC;")
    List<StringIntegerDTO> countByCurrentStatus();

    @Update("UPDATE `trf_sweep` \n" +
            "SET current_status = NULL")
    int nullCurrentStatus();

    @Update("UPDATE `trf_sweep` \n" +
            "SET longitude = #{longitude},\n" +
            "latitude = #{latitude},\n" +
            "current_status = #{currentStatus},\n" +
            "gps_time = #{gpsTime}\n" +
            "WHERE\n" +
            "\tgps_code = #{gpsCode};")
    Integer updatePoint(Double longitude,Double latitude,String gpsCode,String currentStatus,Long gpsTime);

    @Select("SELECT\n" +
            "\tid,\n" +
            "\tname,\n" +
            "\tgps_code AS gpsCode,\n" +
            "\t#{timeStamp} AS gpsTime\n" +
            "FROM\n" +
            "\t`trf_sweep` \n" +
            "WHERE\n" +
            "\tgps_code != ''\n" +
            "AND deleted = 0 \n" +
            "GROUP BY gps_code")
    List<MinTrfVehicle> getAllGpsCode(Long timeStamp);

    @Select("SELECT\n" +
            "\ts.*, \n" +
            "\tf.username AS staffName,\n" +
            "\tf.serial AS staffSerial,\n" +
            "\tf.tel AS staffTel\n" +
            "FROM\n" +
            "\ttrf_sweep AS s\n" +
            "\tLEFT JOIN trf_sweep_staff AS f ON s.staff_id = f.id\n" +
           "${ew.customSqlSegment}" )
    List<TrfSweep> getAllList(@Param("ew") QueryWrapper<TrfSweep> ew);

    @Select("SELECT\n" +
            "\ts.*, \n" +
            "\tf.username AS staffName,\n" +
            "\tf.serial AS staffSerial,\n" +
            "\tf.tel AS staffTel\n" +
            "FROM\n" +
            "\ttrf_sweep AS s\n" +
            "\tLEFT JOIN trf_sweep_staff AS f ON s.staff_id = f.id\n" +
            "${ew.customSqlSegment}" )
    Page<TrfSweep> getPageList(@Param("page") Page<TrfSweep> page, @Param("ew") QueryWrapper<TrfSweep> ew);

    @Select("SELECT\n" +
            "\ts.*, \n" +
            "\tf.username AS staffName,\n" +
            "\tf.serial AS staffSerial,\n" +
            "\tf.tel AS staffTel\n" +
            "FROM\n" +
            "\ttrf_sweep AS s\n" +
            "\tLEFT JOIN trf_sweep_staff AS f ON s.staff_id = f.id\n" +
            "WHERE\n" +
            "\ts.id = #{id}")
    TrfSweep getById(@Param("id") Long id);
}
