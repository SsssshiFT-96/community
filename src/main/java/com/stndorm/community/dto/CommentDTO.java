package com.stndorm.community.dto;

import lombok.Data;

//从页面获取的DTO
@Data
public class CommentDTO {
    private Integer parentId;
    private String content;
    private Integer type;
}
