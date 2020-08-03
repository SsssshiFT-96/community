package com.stndorm.community.controller;

import com.stndorm.community.cache.TagCache;
import com.stndorm.community.dto.QuestionDTO;
import com.stndorm.community.mapper.QuestionMapper;
import com.stndorm.community.model.Question;
import com.stndorm.community.model.User;
import com.stndorm.community.rabbitmq.ESMessage;
import com.stndorm.community.rabbitmq.MQSender;
import com.stndorm.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;

@Controller
public class publishController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private MQSender mqSender;

    //编辑功能
    @GetMapping("publish/{id}")
    public String edit(@PathVariable(name = "id")Integer id,
                       Model model){
        //通过id获得question信息将其回显至页面上
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", question.getId());
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    //使用get就渲染页面
    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    //使用post就执行请求
    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam("tag") String tag,
                            @RequestParam("id") Integer id,
                            HttpServletRequest request,
                            Model model){
        //保存之前写的内容
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", TagCache.get());
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
        //判断输入的标签是否合规
        String invalid = TagCache.filterInvalid(tag);
        if(StringUtils.isNotBlank(invalid)){
            model.addAttribute("error","输入非法标签：" + invalid);
            return "publish";
        }
        //获取user信息
        User user = (User)request.getSession().getAttribute("user");
        //如果user不存在，就传入错误信息
        if(user == null) {
            model.addAttribute("error","用户不存在");
            return "publish";
        }
        //创建question对象，将发布的问题信息传入，并保存至数据库中
        Question question = new Question();
        question.setCreator(user.getId());
        question.setDescription(description);
        question.setTitle(title);
        question.setTag(tag);
        //将id作为唯一标识判断该问题新建还是已有。若为新建，则id传入的是空值
        question.setId(id);

        //获得添加或者更新的问题的id
        Integer returnID = questionService.createOrUpdate(question);
//        questionMapper.create(question);
        //将更新信息通过mq异步通知去更新es
        ESMessage esMessage = new ESMessage(returnID, ESMessage.CREATE_OR_UPDATE);
        mqSender.sendUpdateESMsg(esMessage);

        //发布成功就返回首页
        return "redirect:/";
    }
}
