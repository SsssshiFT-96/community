package com.stndorm.community.schedule;

import com.stndorm.community.cache.HotTagCache;
import com.stndorm.community.mapper.QuestionMapper;
import com.stndorm.community.model.Question;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class HotTagTasks {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private HotTagCache hotTagCache;

    //每6小时更新一次
    //这应该是异步更新
    @Scheduled(fixedRate = 1000 * 60 * 60 * 6)
    public void hotTagSchedule() {
        int offset = 0;
        int limit = 5;
        List<Question> list = new ArrayList<>();
        Map<String, Integer> priorities = new HashMap<>();
        //将问题放入优先级队列
        while(offset == 0 || list.size() == limit){
            list = questionMapper.selectQuestions(offset, limit);
            for(Question question : list){
                //建立map，键为标签名，值为自定义算法算出的优先值
                String tag = question.getTag();
                String[] tags = StringUtils.split(tag,",");
                for(String t : tags){
                    Integer priority = priorities.get(t);
                    if(priority != null){
                        priorities.put(t, priority + 5 + question.getCommentCount());
                    }else{
                        priorities.put(t, 5 + question.getCommentCount());
                    }
                }
            }
            offset += limit;
        }
        //根据优先级值取出指定个数标签放入hotTagCache的list中。
        hotTagCache.updateTags(priorities);
        log.info("The time is now {}", new Date());
    }
}
