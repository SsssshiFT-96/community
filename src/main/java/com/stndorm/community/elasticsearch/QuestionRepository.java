package com.stndorm.community.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface QuestionRepository extends ElasticsearchRepository<QuestionEntity, Integer> {
        List<QuestionEntity> findByTitle(String search);
}
