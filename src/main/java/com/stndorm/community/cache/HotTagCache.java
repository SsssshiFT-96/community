package com.stndorm.community.cache;

import com.stndorm.community.dto.HotTagDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
public class HotTagCache {
    private List<String> hots = new ArrayList<>();

    //使用优先级队列
    public void updateTags(Map<String, Integer> tags){
        int max = 5;
        PriorityQueue<HotTagDTO> priorityQueue = new PriorityQueue<>(max);

        //将tag先放入有限大小的优先级队列，此队列为大根堆
        tags.forEach((name, priority) ->{
            HotTagDTO hotTagDTO= new HotTagDTO();
            hotTagDTO.setName(name);
            hotTagDTO.setPriority(priority);
            if(priorityQueue.size() < max){
                priorityQueue.add(hotTagDTO);
            }else{
                HotTagDTO minHot = priorityQueue.peek();
                if(hotTagDTO.compareTo(minHot) > 0){
                    priorityQueue.poll();
                    priorityQueue.add(hotTagDTO);
                }
            }
        });

        List<String> sortedTags = new ArrayList<>();
        //然后将队列中的标签取出放入list中。
        while(!priorityQueue.isEmpty()){
            HotTagDTO poll = priorityQueue.poll();
            sortedTags.add(0,poll.getName());
        }
        hots = sortedTags;
    }
}
