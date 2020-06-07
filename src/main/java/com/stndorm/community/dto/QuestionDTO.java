package com.stndorm.community.dto;

import com.stndorm.community.model.User;
import lombok.Data;

/**
 * QuestionDTO类只比Question多了一个User对象
 */
@Data
public class QuestionDTO {
    private Integer id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private String tag;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
