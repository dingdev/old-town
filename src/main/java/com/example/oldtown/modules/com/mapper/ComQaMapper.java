package com.example.oldtown.modules.com.mapper;

import com.example.oldtown.modules.com.model.ComQa;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 通用问答 Mapper 接口
 * </p>
 *
 * @author dyp
 * @since 2020-11-13
 */
public interface ComQaMapper extends BaseMapper<ComQa> {
    @Select("SELECT\n" +
            "\tanswer \n" +
            "FROM\n" +
            "\t`com_qa` \n" +
            "WHERE\n" +
            "\tquestion = #{question} \n" +
            "\tAND deleted = 0 ;")
    String getAnswerByQuestion(String question);
}
