package com.stndorm.community.elasticsearch;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Document(indexName = "community", type="question")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuestionEntity {
    @Id
    private Integer id;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String description;
    @Field(type = FieldType.Date)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date gmtCreate;
    @Field(type = FieldType.Integer)
    private Integer creator;
    @Field(type = FieldType.Keyword)
    private String tag;
    @Field(type = FieldType.Integer)
    private Integer viewCount;
    @Field(type = FieldType.Integer)
    private Integer commentCount;
}
