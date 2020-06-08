package com.stndorm.community.mapper;

import com.stndorm.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("insert into users(name,account_id,token,gmt_create,gmt_modified,avatar_url) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    public void insert(User user);

    @Select("select * from users where token = #{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from users where id = #{creator}")
    User findById(Integer creator);
}