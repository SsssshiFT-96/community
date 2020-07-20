package com.stndorm.community.dto;

import com.github.pagehelper.PageInfo;
import com.stndorm.community.model.Question;
import lombok.Data;

import java.util.List;

@Data
public class PageHelperDTO<T> {
    PageInfo<Question> info;
    List<T> data;
}
