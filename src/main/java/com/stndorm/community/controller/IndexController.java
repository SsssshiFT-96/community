package com.stndorm.community.controller;

import com.stndorm.community.dto.PageHelperDTO;
import com.stndorm.community.dto.PaginationDTO;
import com.stndorm.community.dto.QuestionDTO;
import com.stndorm.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;


@Controller
public class IndexController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/")
    public String hello(HttpServletRequest request,
                        Model model,
                        @RequestParam(name="page", defaultValue = "1")Integer page,
                        @RequestParam(name="size", defaultValue = "5")Integer size,
                        @RequestParam(name="search", required = false)String search){
        //获取cookie,并判断是否存在token，若存在则从数据库中查询用户，这里交给拦截器去做，因为很多controller都需要这一操作

//        PaginationDTO pagination = questionService.selectQuestionDTOs(search, page, size);
//        model.addAttribute("pagination",pagination);
        PageHelperDTO<QuestionDTO> pageHelperDTO = questionService.selectQuestionDTOs(search, page, size);
        model.addAttribute("pageHelperDTO", pageHelperDTO);
        model.addAttribute("search",search);
        return "index";
    }
}
