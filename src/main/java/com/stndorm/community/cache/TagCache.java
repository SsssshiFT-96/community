package com.stndorm.community.cache;

import com.stndorm.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagCache {
    public static List<TagDTO> get(){
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("js","php","css","html","java","node","python","c++","ruby","golang"));
        tagDTOS.add(program);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("spring","laravel","express","flask","yii"));
        tagDTOS.add(framework);

        TagDTO life = new TagDTO();
        life.setCategoryName("日常生活");
        life.setTags(Arrays.asList("生活","情感","工作","饮食","住行"));
        tagDTOS.add(life);

        return tagDTOS;
    }

    public static String filterInvalid(String tags){
        String[] split = StringUtils.split(tags, ",");
        List<TagDTO> tagDTOS = get();
        //flatMap()将二维数组内容循环出来
        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().
                stream()).collect(Collectors.toList());
        //将非法的字符串提炼出来
        String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).
                collect(Collectors.joining(","));
        return invalid;
    }
}
