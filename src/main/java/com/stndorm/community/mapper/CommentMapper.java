package com.stndorm.community.mapper;

import com.stndorm.community.dto.CommentDTOFromDB;
import com.stndorm.community.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("insert into comments (parent_id,`type`,commentator,gmt_create,gmt_modified,content) values (#{parentId},#{type},#{commentator},#{gmtCreate},#{gmtModified},#{content})")
    void insert(Comment comment);

    @Select("select * from comments where id = #{id}")
    Comment selectParentComment(@Param(value = "id") Integer id);

    @Select("select * from comments where parent_id = #{id} and `type` = #{type}")
    List<Comment> selectByParentIdAndType(Integer id, Integer type);
}
