package com.stndorm.community.controller;

import com.stndorm.community.dto.NotificationDTO;
import com.stndorm.community.dto.PaginationDTO;
import com.stndorm.community.emus.NotificationTypeEnum;
import com.stndorm.community.model.User;
import com.stndorm.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(@PathVariable(name = "id")Integer id,
                          HttpServletRequest request){
        //当直接访问我的问题页面时也有用户登录信息，交给拦截器去做
        User user = (User)request.getSession().getAttribute("user");
        //如果没有登录就返回首页
        if(user == null){
            return "redirect:/";
        }

        //校验点击的问题是否是当前用户发起，根据notificationDTO做跳转
        NotificationDTO notificationDTO =
                notificationService.read(id ,user);

        if(NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType() ||
                NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()){

            return "redirect:/question/" + notificationDTO.getOuterId();
        }else{
            return "redirect:/";
        }

    }
}

