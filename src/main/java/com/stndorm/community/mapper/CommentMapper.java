package com.stndorm.community.mapper;

import com.stndorm.community.dto.CommentDTOFromDB;
import com.stndorm.community.model.Comment;
import com.stndorm.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("insert into comments (parent_id,`type`,commentator,gmt_create,gmt_modified,content) values (#{parentId},#{type},#{commentator},#{gmtCreate},#{gmtModified},#{content})")
    void insert(Comment comment);

    @Select("select * from comments where id = #{id}")
    Comment selectParentComment(@Param(value = "id") Integer id);

    @Select("select * from comments where parent_id = #{id} and `type` = #{type}")
    List<Comment> selectByParentIdAndType(Integer id, Integer type);

    @Update("update comments set comment_count=comment_count + #{commentCount} where id = #{id}")
    void updateCommentCount(Comment dbComment);
}
