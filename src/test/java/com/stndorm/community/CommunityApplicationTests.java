package com.stndorm.community;


import com.stndorm.community.elasticsearch.QuestionEntity;
import com.stndorm.community.elasticsearch.QuestionRepository;
import com.stndorm.community.elasticsearch.QuestionEntity;
import com.stndorm.community.elasticsearch.QuestionRepository;
import com.stndorm.community.mapper.QuestionMapper;
import com.stndorm.community.model.Question;
import com.stndorm.community.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class CommunityApplicationTests {

    @Test
    void contextLoads() {
    }

//
//    @Test
//    public void findQuestionByESBySearch(){
//        List<Question> questions = new ArrayList<>();
//        List<QuestionEntity> questionEntities = questionRepository.findByTitle("zzj");
////        Iterable<QuestionEntity> all = questionRepository.findAll();
//        for(QuestionEntity questionEntity : questionEntities){
//            Question question = new Question();
//            BeanUtils.copyProperties(questionEntity, question);
//            long time = questionEntity.getGmtCreate().getTime();
//            question.setGmtCreate(time);
//            questions.add(question);
//        }
//        System.out.println(questions);
//    }
//
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionRepository questionRepository;

    @Test
    public void insertQuestions(){
        List<Question> questions = questionMapper.selectQuestions2();
        for(Question question : questions){
            QuestionEntity questionEntity = new QuestionEntity();
            BeanUtils.copyProperties(question,questionEntity);
            Date date = new Date();
            date.setTime(question.getGmtCreate());
            questionEntity.setGmtCreate(date);
            questionRepository.save(questionEntity);
        }
    }
}
