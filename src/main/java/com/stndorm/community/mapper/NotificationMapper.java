package com.stndorm.community.mapper;

import com.stndorm.community.model.Notification;
import com.stndorm.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotificationMapper {

    @Insert("insert into notifications (notifier,receiver,outerId,`type`,gmt_create,`status`,notifier_name,outer_title) values (#{notifier},#{receiver},#{outerId},#{type},#{gmtCreate},#{status},#{notifierName},#{outerTitle})")
    void insert(Notification notification);

    @Select("select count(1) from notifications where receiver = #{userId}")
    Integer countByReceiver(@Param(value = "userId") Integer userId);

    @Select("select count(1) from notifications where receiver = #{userId} and `status` = #{status}")
    Integer countByReceiverAndStatus(@Param(value = "userId") Integer userId,
                                     @Param(value = "status") Integer status);


    @Select("select * from notifications where receiver = #{userId} order by gmt_create desc limit #{offset}, #{size}")
    List<Notification> selectNotificationsByReceiver(@Param(value = "userId")Integer userId,
                                           @Param(value = "offset")Integer offset,
                                           @Param(value = "size")Integer size);
    @Select("select * from notifications where id = #{id}")
    Notification selectById(@Param(value = "id")Integer id);

    @Update("update notifications set `status` = #{status} where id = #{id}")
    void updateStatus(Notification notification);

}
