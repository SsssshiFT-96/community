package com.stndorm.community.controller;

import com.stndorm.community.dto.CommentDTO;
import com.stndorm.community.dto.CommentDTOFromDB;
import com.stndorm.community.dto.ResultDTO;
import com.stndorm.community.emus.CommentTypeEnum;
import com.stndorm.community.exception.CustomizeErrorCode;
import com.stndorm.community.mapper.CommentMapper;
import com.stndorm.community.model.Comment;
import com.stndorm.community.model.User;
import com.stndorm.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value="/comment", method= RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        //StringUtils.isBlank使用到了commons.lang包
        if(commentDTO == null || StringUtils.isBlank(commentDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(user.getId());
        commentService.insert(comment);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value="/comment/{id}", method= RequestMethod.GET)
    public ResultDTO<List<CommentDTOFromDB>> comments(@PathVariable(name="id")Integer id){
        List<CommentDTOFromDB> commentDTOFromDBS =
                commentService.selectListByQuestionId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOFromDBS);
    }
}
