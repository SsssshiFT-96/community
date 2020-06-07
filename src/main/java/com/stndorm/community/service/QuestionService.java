package com.stndorm.community.service;

import com.stndorm.community.dto.QuestionDTO;
import com.stndorm.community.mapper.QuestionMapper;
import com.stndorm.community.mapper.UserMapper;
import com.stndorm.community.model.Question;
import com.stndorm.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 起到一个组装的作用，当同时需要UserMapper和QuestionMapper时，就需要该层
 */
@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;

    public List<QuestionDTO> selectQuestionDTOs() {
        List<Question> questions = questionMapper.selectQuestions();
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for(Question question : questions){
            User user = userMapper.findById(question.getCreator());
            //利用该方法可以将一个对象的属性复制到另一个对象的属性
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }
}
