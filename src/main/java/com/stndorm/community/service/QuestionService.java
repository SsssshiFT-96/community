package com.stndorm.community.service;

import com.stndorm.community.dto.PaginationDTO;
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

    public PaginationDTO selectQuestionDTOs(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        //获取文章总数
        Integer totalCount = questionMapper.count();
        //对paginationDTO进行赋值
        paginationDTO.setPagination(totalCount, page, size);
        //判断请求参数page是否合规，不合规就使它变为边界值
        if(page < 1) page = 1;
        if(page > paginationDTO.getTotalPage())
            page = paginationDTO.getTotalPage();

        //偏移量计算
        Integer offset = size * (page - 1);

        List<Question> questions = questionMapper.selectQuestions(offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();


        for(Question question : questions){
            User user = userMapper.findById(question.getCreator());
            //利用该方法可以将一个对象的属性复制到另一个对象的属性
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }
}
