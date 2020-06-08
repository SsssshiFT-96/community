package com.stndorm.community.mapper;

import com.stndorm.community.dto.QuestionDTO;
import com.stndorm.community.model.Question;
import org.apache.ibatis.annotations.*;

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

    @Select("select * from questions where id = #{id}")
    Question getById(@Param(value = "id") Integer id);

    @Update("update questions set title=#{title},description=#{description},gmt_modified=#{gmtModified},tag=#{tag} where id=#{id}")
    void update(Question question);
}
