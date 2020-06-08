package com.stndorm.community.controller;

import com.stndorm.community.mapper.QuestionMapper;
import com.stndorm.community.model.Question;
import com.stndorm.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;

@Controller
public class publishController {

    @Autowired
    private QuestionMapper questionMapper;

    //使用get就渲染页面
    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    //使用post就执行请求
    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam("tag") String tag,
                            HttpServletRequest request,
                            Model model){
        //保存之前写的内容
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        //校验输入内容是否合规
        if(title == null || title == ""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(description == null || description == ""){
            model.addAttribute("error","内容不能为空");
            return "publish";
        }
        if(tag == null || tag == ""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
        //获取user信息
        User user = (User)request.getSession().getAttribute("user");
        //如果user不存在，就传入错误信息
        if(user == null) {
            model.addAttribute("error","用户不存在");
            return "publish";
        }
        //创建question对象，将发布的文章信息传入，并保存至数据库中
        Question question = new Question();
        question.setCreator(user.getId());
        question.setDescription(description);
        question.setTitle(title);
        question.setTag(tag);
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.create(question);

        //发布成功就返回首页
        return "redirect:/";
    }
}
