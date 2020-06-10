package com.stndorm.community.controller;

import com.stndorm.community.dto.CommentDTO;
import com.stndorm.community.dto.CommentDTOFromDB;
import com.stndorm.community.dto.QuestionDTO;
import com.stndorm.community.emus.CommentTypeEnum;
import com.stndorm.community.mapper.QuestionMapper;
import com.stndorm.community.model.Question;
import com.stndorm.community.service.CommentService;
import com.stndorm.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id,
                           Model model){
        //到数据库中查询该question的id是否存在
        QuestionDTO questionDTO = questionService.getById(id);

        //通过标签获取相关问题
        List<QuestionDTO> relatedQuestions =
                questionService.selectRelatedQ(questionDTO);

        //通过问题id获取评论
        List<CommentDTOFromDB> CommentDTOFromDBs =
                commentService.selectListByQuestionId(id, CommentTypeEnum.QUESTION);


        //点击就累加阅读数
        questionService.incView(id);

        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", CommentDTOFromDBs);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }
}
