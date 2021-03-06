package com.stndorm.community.mapper;

import com.stndorm.community.dto.QuestionDTO;
import com.stndorm.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    //返回自增主键的值，即id的值
    @Insert("insert into questions (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void create(Question quesion);

//    @Select("select * from questions")
//    List<Question> selectQuestions();

    @Select("select * from questions ORDER BY gmt_create DESC limit #{offset}, #{size}")
    List<Question> selectQuestions(@Param(value = "offset") Integer offset,
                                   @Param(value = "size") Integer size);

    @Select("select * from questions where title regexp #{regexpSearch} ORDER BY gmt_create DESC limit #{offset}, #{size}")
    List<Question> selectQuestionsBySearch(@Param(value = "regexpSearch") String regexpSearch,
                                           @Param(value = "offset") Integer offset,
                                           @Param(value = "size") Integer size);

    @Select("select count(1) from questions")
    Integer count();

    @Select("select count(1) from questions where title regexp #{regexpSearch}")
    Integer countBySearch(@Param(value = "regexpSearch")String regexpSearch);
    
    @Select("select * from questions where creator = #{userId} ORDER BY gmt_create DESC limit #{offset}, #{size}")
    List<Question> selectQuestionsByUserId(@Param(value = "userId")Integer userId,
                                         @Param(value = "offset")Integer offset,
                                         @Param(value = "size")Integer size);


    @Select("select count(1) from questions where creator = #{userId}")
    Integer countByUserId(@Param(value = "userId") Integer userId);

    @Select("select * from questions where id = #{id}")
    Question getById(@Param(value = "id") Integer id);

    @Update("update questions set title=#{title},description=#{description},gmt_modified=#{gmtModified},tag=#{tag} where id=#{id}")
    void update(Question question);

    @Update("update questions set view_count=view_count + #{viewCount} where id = #{id}")
    void updateViewCount(Integer viewCount, Integer id);

    @Update("update questions set comment_count=comment_count + #{commentCount} where id = #{id}")
    void updateCommentCount(Question question);

    @Select("select * from questions where id != #{id} and tag regexp #{tag}")
    List<Question> selectRelatedQ(Question question);

    @Select("select * from questions where title regexp #{regexpSearch} ORDER BY gmt_create DESC")
    List<Question> selectQuestionsBySearch2(@Param(value = "regexpSearch")String regexpSearch);

    @Select("select * from questions ORDER BY gmt_create DESC")
    List<Question> selectQuestions2();

    @Select("select * from questions where tag regexp #{regexpTag} ORDER BY gmt_create DESC")
    List<Question> selectQuestionsByTag2(@Param(value = "regexpTag")String regexpTag);
}
