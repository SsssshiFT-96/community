package com.stndorm.community.mapper;

import com.stndorm.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into questions (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    public void create(Question quesion);

//    @Select("select * from questions")
//    List<Question> selectQuestions();

    @Select("select * from questions limit #{offset}, #{size}")
    List<Question> selectQuestions(@Param(value = "offset") Integer offset,
                                   @Param(value = "size") Integer size);

    @Select("select count(1) from questions")
    Integer count();

    @Select("select * from questions where creator = #{userId} limit #{offset}, #{size}")
    List<Question> selectQuestionsByUserId(@Param(value = "userId")Integer userId,
                                         @Param(value = "offset")Integer offset,
                                         @Param(value = "size")Integer size);

    @Select("select count(1) from questions where creator = #{userId}")
    Integer countByUserId(@Param(value = "userId") Integer userId);
}
