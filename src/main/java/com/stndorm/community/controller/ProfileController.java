package com.stndorm.community.controller;

import com.stndorm.community.dto.NotificationDTO;
import com.stndorm.community.dto.PaginationDTO;

import com.stndorm.community.model.Notification;
import com.stndorm.community.model.User;
import com.stndorm.community.service.NotificationService;
import com.stndorm.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action")String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name="page", defaultValue = "1")Integer page,
                          @RequestParam(name="size", defaultValue = "5")Integer size){
        //当直接访问我的问题页面时也有用户登录信息，交给拦截器去做
        User user = (User)request.getSession().getAttribute("user");
        //如果没有登录就返回首页
        if(user == null){
            return "redirect:/";
        }

        //设置标题显示，比如点到我的问题，就显示我的问题
        if("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
            //通过user获取user创建的文章数据
            PaginationDTO paginationDTO =
                    questionService.selectQuestionDTOsByUser(user.getId(), page, size);
            model.addAttribute("pagination", paginationDTO);
        }else if("replies".equals(action)){
            PaginationDTO paginationDTO =
                    notificationService.selectNotificationDTOsByUser(user.getId(), page, size);

            Integer unreadCount = notificationService.unreadCount(user.getId());
            model.addAttribute("section","replies");
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("unreadCount", unreadCount);
            model.addAttribute("sectionName","最新回复");

        }

        return "profile";
    }
}
