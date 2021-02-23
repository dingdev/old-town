package com.example.oldtown.modules.com.mapper;

import com.example.oldtown.dto.IdNameDTO;
import com.example.oldtown.dto.MinPlaceDTO;
import com.example.oldtown.dto.MinPointDTO;
import com.example.oldtown.dto.StringIntegerDTO;
import com.example.oldtown.modules.com.model.ComPlace;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 通用场所 Mapper 接口
 * </p>
 *
 * @author dyp
 * @since 2020-11-06
 */
public interface ComPlaceMapper extends BaseMapper<ComPlace> {
    @Select("SELECT id AS id,`name` AS name,latitude AS latitude,longitude AS longitude,picture_url AS pictureUrl FROM `com_place` where id IN ${contentIds}")
    List<MinPointDTO> getMinPointList(String contentIds);

    @Select("SELECT id AS id,`name` AS name, picture_url AS pictureUrl, view_num AS viewNum FROM `com_place` where id IN ${contentIds}")
    List<MinPlaceDTO> getMinPlaceList(String placeIds);

    @Select("SELECT type AS `key`,COUNT(id) AS `value` FROM `com_place` WHERE type IS NOT NULL GROUP BY type ORDER BY `value` DESC")
    List<StringIntegerDTO> getCurrentTypes();

    @Select("SELECT name FROM `com_place` WHERE deleted = 0 ORDER BY view_num DESC LIMIT 10")
    List<String> getTopSearch();

    @Update("UPDATE `com_place` \n" +
            "SET current_num = #{current}\n" +
            "WHERE\n" +
            "\tcode = #{code} AND type=#{type};")
    Integer updateCurrentNumByCode(String type, String code,Integer current);
}
