package com.example.oldtown.modules.com.mapper;

import com.example.oldtown.modules.com.model.ComRoute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 通用游线 Mapper 接口
 * </p>
 *
 * @author dyp
 * @since 2020-11-13
 */
public interface ComRouteMapper extends BaseMapper<ComRoute> {


    @Select("SELECT label FROM `com_route` WHERE deleted=0 GROUP BY label;")
    List<String> getLabelList();

    @Select("SELECT * FROM `com_route` WHERE id > (SELECT FLOOR(RAND() * (SELECT MAX(id) FROM `com_route`))) AND deleted=0 LIMIT 1;")
    ComRoute getRandom();
}
