package com.stndorm.community.controller;

import com.stndorm.community.dto.PaginationDTO;
import com.stndorm.community.mapper.UserMapper;
import com.stndorm.community.model.User;
import com.stndorm.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action")String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name="page", defaultValue = "1")Integer page,
                          @RequestParam(name="size", defaultValue = "2")Integer size){
        User user = null;
        //当直接访问我的问题页面时也有用户登录信息
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length != 0){
            for (Cookie cookie: cookies) {
                if(cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if(user != null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }
        //如果没有登录就返回首页
        if(user == null){
            return "redirect:/";
        }

        //设置标题显示，比如点到我的问题，就显示我的问题
        if("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
        }else if("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }
        //通过user获取user创建的文章数据
        PaginationDTO paginationDTO =
                questionService.selectQuestionDTOsByUser(user.getId(), page, size);
        model.addAttribute("pagination", paginationDTO);
        return "profile";
    }
}
