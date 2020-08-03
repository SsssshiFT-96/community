package com.stndorm.community.elasticsearch;

import com.stndorm.community.mapper.QuestionMapper;
import com.stndorm.community.model.Question;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ESMapper {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    QuestionMapper questionMapper;

    public List<Question> findQuestionByESBySearch(String search){
        List<Question> questions = new ArrayList<>();
        List<QuestionEntity> questionEntities = questionRepository.findByTitle(search);
//        System.out.println(questionEntities);
//        Iterable<QuestionEntity> all = questionRepository.findAll();
        for(QuestionEntity questionEntity : questionEntities){
            Question question = new Question();
            BeanUtils.copyProperties(questionEntity, question);
            long time = questionEntity.getGmtCreate().getTime();
            question.setGmtCreate(time);
            questions.add(question);
        }
        //通过比较id值实现按时间倒序排序
        Collections.sort(questions, new Comparator<Question>() {
            @Override
            public int compare(Question o1, Question o2) {
                return o2.getId() - o1.getId();
            }
        });
        return questions;
    }

    public void createOrUpdate(Integer id) {
        System.out.println("要获取的问题的id：" + id);
        Question question = questionMapper.getById(id);
//        System.out.println(question);
        QuestionEntity questionEntity = new QuestionEntity();
        BeanUtils.copyProperties(question, questionEntity);
        Date date = new Date();
        date.setTime(question.getGmtCreate());
        questionEntity.setGmtCreate(date);
//        System.out.println(questionEntity);
        questionRepository.save(questionEntity);
    }

    public void remove(Integer id) {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setId(id);
        questionRepository.delete(questionEntity);
    }
}
