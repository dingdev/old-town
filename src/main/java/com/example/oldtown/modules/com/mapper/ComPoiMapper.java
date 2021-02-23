package com.example.oldtown.modules.com.mapper;

import com.example.oldtown.dto.StringIntegerDTO;
import com.example.oldtown.modules.com.model.ComPoi;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 通用设施 Mapper 接口
 * </p>
 *
 * @author dyp
 * @since 2020-11-06
 */
public interface ComPoiMapper extends BaseMapper<ComPoi> {
    @Select("SELECT type AS `key`,COUNT(id) AS `value` FROM `com_poi` WHERE type IS NOT NULL GROUP BY type ORDER BY `value` DESC")
    List<StringIntegerDTO> getCurrentTypes();
}
