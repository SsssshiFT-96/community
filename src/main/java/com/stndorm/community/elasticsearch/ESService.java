package com.stndorm.community.elasticsearch;

import com.stndorm.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ESService {

    @Autowired
    ESMapper esMapper;

    public List<Question> findQuestionByESBySearch(String search){
        return esMapper.findQuestionByESBySearch(search);
    }


    public void createOrUpdate(Integer id) {
        esMapper.createOrUpdate(id);
    }

    public void remove(Integer id) { esMapper.remove(id); }
}
