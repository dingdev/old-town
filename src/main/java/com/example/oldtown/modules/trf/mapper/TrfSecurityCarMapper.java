package com.example.oldtown.modules.trf.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oldtown.dto.MinTrfVehicle;
import com.example.oldtown.dto.StringIntegerDTO;
import com.example.oldtown.modules.trf.model.TrfSecurityCar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.oldtown.modules.trf.model.TrfSecurityCar;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 交通接驳安保车辆 Mapper 接口
 * </p>
 *
 * @author dyp
 * @since 2020-11-25
 */
public interface TrfSecurityCarMapper extends BaseMapper<TrfSecurityCar> {
    @Select("SELECT\n" +
            "\ttype AS `key`,\n" +
            "\tCOUNT( id ) AS `value` \n" +
            "FROM\n" +
            "\t`trf_security_car` \n" +
            "GROUP BY\n" +
            "\ttype \n" +
            "ORDER BY\n" +
            "\t`value` DESC;")
    List<StringIntegerDTO> countByType();

    @Update("UPDATE `trf_security_car` \n" +
            "SET current_status = NULL")
    int nullCurrentStatus();

    @Update("UPDATE `trf_security_car` \n" +
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
            "\t`trf_security_car` \n" +
            "WHERE\n" +
            "\tgps_code != ''\n" +
            "AND deleted = 0 \n" +
            "GROUP BY gps_code")
    List<MinTrfVehicle> getAllGpsCode(Long timeStamp);


    @Select("SELECT\n" +
            "\tc.*, \n" +
            "\tf.username AS staffName,\n" +
            "\tf.serial AS staffSerial,\n" +
            "\tf.tel AS staffTel\n" +
            "FROM\n" +
            "\ttrf_security_car AS c\n" +
            "\tLEFT JOIN trf_security_staff AS f ON c.staff_id = f.id\n" +
            "${ew.customSqlSegment}" )
    List<TrfSecurityCar> getAllList(@Param("ew") QueryWrapper<TrfSecurityCar> ew);

    @Select("SELECT\n" +
            "\tc.*, \n" +
            "\tf.username AS staffName,\n" +
            "\tf.serial AS staffSerial,\n" +
            "\tf.tel AS staffTel\n" +
            "FROM\n" +
            "\ttrf_security_car AS c\n" +
            "\tLEFT JOIN trf_security_staff AS f ON c.staff_id = f.id\n" +
            "${ew.customSqlSegment}" )
    Page<TrfSecurityCar> getPageList(@Param("page") Page<TrfSecurityCar> page, @Param("ew") QueryWrapper<TrfSecurityCar> ew);

    @Select("SELECT\n" +
            "\tc.*, \n" +
            "\tf.username AS staffName,\n" +
            "\tf.serial AS staffSerial,\n" +
            "\tf.tel AS staffTel\n" +
            "FROM\n" +
            "\ttrf_security_car AS c\n" +
            "\tLEFT JOIN trf_security_staff AS f ON c.staff_id = f.id\n" +
            "WHERE\n" +
            "\tc.id = #{id}")
    TrfSecurityCar getById(@Param("id") Long id);
}
