package com.stndorm.community.dto;

import com.stndorm.community.model.User;
import lombok.Data;

@Data
public class CommentDTOFromDB {
    private Long id;
    private Integer parentId;
    private Integer type;
    private Integer commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;

    private String content;
    private User user;
}
