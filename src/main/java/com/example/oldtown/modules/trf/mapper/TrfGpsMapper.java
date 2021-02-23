package com.example.oldtown.modules.trf.mapper;

import com.example.oldtown.modules.trf.model.TrfGps;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;

/**
 * <p>
 * 交通接驳gps定位点 Mapper 接口
 * </p>
 *
 * @author dyp
 * @since 2020-11-25
 */
public interface TrfGpsMapper extends BaseMapper<TrfGps> {


    @Insert("INSERT `trf_gps` \n" +
            "\t( gps_code, gps_time, longitude, latitude, speed )\n" +
            "VALUES\n" +
            "\t( #{gpsCode}, #{gpsTime}, #{longitude}, #{latitude},#{speed} );")
    Integer insertTrfGps (String gpsCode,Long gpsTime,Double longitude,Double latitude,Double speed);
}
