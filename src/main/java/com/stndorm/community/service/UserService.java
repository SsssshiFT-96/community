package com.stndorm.community.service;

import com.stndorm.community.mapper.UserMapper;
import com.stndorm.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public void createOrUpdate(User user) {
        //从数据库中拿到指定的user
        User dbUser = userMapper.findByAccountId(user.getAccountId());
        if(dbUser == null){
            //如果不存在，就插入。因为是新的用户所以才有创建时间，之前在Controller中就转到这里
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else{
            //如果存在，就更新
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setName(user.getName());
            dbUser.setToken(user.getToken());
            userMapper.update(dbUser);
        }
    }
}
