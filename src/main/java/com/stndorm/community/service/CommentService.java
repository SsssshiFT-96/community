package com.stndorm.community.service;

import com.stndorm.community.dto.CommentDTOFromDB;
import com.stndorm.community.emus.CommentTypeEnum;
import com.stndorm.community.exception.CustomizeErrorCode;
import com.stndorm.community.exception.CustomizeException;
import com.stndorm.community.mapper.CommentMapper;
import com.stndorm.community.mapper.QuestionMapper;
import com.stndorm.community.mapper.UserMapper;
import com.stndorm.community.model.Comment;
import com.stndorm.community.model.Question;
import com.stndorm.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void insert(Comment comment) {
        //判断一些异常，如评论父类id是否存在，类型是否存在
        if(comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        //判断是回复评论还是回复问题
        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment = commentMapper.selectParentComment(comment.getParentId());
            if(dbComment == null){
                throw new CustomizeException((CustomizeErrorCode.COMMENT_NOT_FOUND));
            }
            commentMapper.insert(comment);
            //更新评论数
            dbComment.setCommentCount(1L);
            commentMapper.updateCommentCount(dbComment);
        }else{
            //回复问题
            Question question = questionMapper.getById(comment.getParentId());
            if(question == null){
                throw new CustomizeException((CustomizeErrorCode.QUESTION_NOT_FOUND));
            }
            commentMapper.insert(comment);
            //更新评论数
            question.setCommentCount(1);
            questionMapper.updateCommentCount(question);
        }
    }

    public List<CommentDTOFromDB> selectListByQuestionId(Integer id, CommentTypeEnum type) {
        List<Comment> comments = commentMapper.selectByParentIdAndType(id, type.getType());
        if(comments.size() == 0){
            return new ArrayList<>();
        }

        //获取去重的评论人id
        Set<Integer> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Integer> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        //获取评论人并转换为map
        List<User> users = new ArrayList<>();
        for(Integer userId : userIds){
            User user = userMapper.findById(userId);
            users.add(user);
        }
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //转换comment为CommentDTOFromDB
        List<CommentDTOFromDB> commentDTOFromDBs = comments.stream().map(comment -> {
            CommentDTOFromDB commentDTOFromDB = new CommentDTOFromDB();
            BeanUtils.copyProperties(comment, commentDTOFromDB);
            commentDTOFromDB.setUser(userMap.get(comment.getCommentator()));
            return commentDTOFromDB;
        }).collect(Collectors.toList());
        //将评论按时间最新排序
        Collections.sort(commentDTOFromDBs, new Comparator<CommentDTOFromDB>() {
            @Override
            public int compare(CommentDTOFromDB o1, CommentDTOFromDB o2) {
                return o1.getGmtCreate() >= o2.getGmtCreate() ? -1 : 1;
            }
        });
        return commentDTOFromDBs;
    }
}
