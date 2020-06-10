package com.stndorm.community.service;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.stndorm.community.dto.PaginationDTO;
import com.stndorm.community.dto.QuestionDTO;
import com.stndorm.community.exception.CustomizeErrorCode;
import com.stndorm.community.exception.CustomizeException;
import com.stndorm.community.mapper.QuestionMapper;
import com.stndorm.community.mapper.UserMapper;
import com.stndorm.community.model.Question;
import com.stndorm.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 起到一个组装的作用，当同时需要UserMapper和QuestionMapper时，就需要该层
 */
@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;

    public PaginationDTO<QuestionDTO> selectQuestionDTOs(Integer page, Integer size) {
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        Integer totalPage;
        //获取文章总数
        Integer totalCount = questionMapper.count();
        //判断总共有几页
        if(totalCount % size == 0){
            totalPage = totalCount / size;
        }else{
            totalPage = totalCount / size + 1;
        }

        //判断请求参数page是否合规，不合规就使它变为边界值
        if(page < 1) page = 1;
        if(page > totalPage)
            page = totalPage;
        //对paginationDTO进行赋值
        paginationDTO.setPagination(totalPage, page);

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

        paginationDTO.setData(questionDTOList);

        return paginationDTO;
    }

    public PaginationDTO<QuestionDTO> selectQuestionDTOsByUser(Integer userId, Integer page, Integer size) {
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        Integer totalPage;
        //获取文章总数
        Integer totalCount = questionMapper.countByUserId(userId);
        //判断总共有几页
        if(totalCount % size == 0){
            totalPage = totalCount / size;
        }else{
            totalPage = totalCount / size + 1;
        }

        //判断请求参数page是否合规，不合规就使它变为边界值
        if(page < 1) page = 1;
        if(page > totalPage)
            page = totalPage;
        //对paginationDTO进行赋值
        paginationDTO.setPagination(totalPage, page);

        //偏移量计算
        Integer offset = size * (page - 1);
        if(offset < 0) offset = 0;


        List<Question> questions = questionMapper.selectQuestionsByUserId(userId, offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();


        for(Question question : questions){
            User user = userMapper.findById(question.getCreator());
            //利用该方法可以将一个对象的属性复制到另一个对象的属性
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);

        return paginationDTO;
    }

    //获取指定question的信息
    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);

        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }

        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //新建问题，创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        }else{
            //已有问题，更新
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.update(question);
        }
    }

    public void incView(Integer id) {
        Question updateQuestion = new Question();
        updateQuestion.setViewCount(1);
        questionMapper.updateViewCount(updateQuestion.getViewCount(), id);
    }

    public List<QuestionDTO> selectRelatedQ(QuestionDTO questionDTO) {
        if(StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split((questionDTO.getTag()), ",");
        //将标签变成“xx|xx|xx”的形式，这样sql可以用正则来获取相关问题
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(regexpTag);
        //获取相关问题
        List<Question> questions = questionMapper.selectRelatedQ(question);
        //将获取的问题存到questionDTOS中
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO newQuestionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q,newQuestionDTO);
            return newQuestionDTO;
        }).collect(Collectors.toList());

        return questionDTOS;
    }
}
