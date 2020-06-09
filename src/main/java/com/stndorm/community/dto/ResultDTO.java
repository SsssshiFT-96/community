package com.stndorm.community.dto;

import com.stndorm.community.exception.CustomizeErrorCode;
import com.stndorm.community.exception.CustomizeException;
import lombok.Data;

//用来封装返回结果信息，比如返回的code是xxx代表回复失败等
@Data
public class ResultDTO {
    private Integer code;
    private String message;

    public static ResultDTO errorOf(Integer code, String message){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;

    }

    public static ResultDTO
    errorOf(CustomizeErrorCode errorCode) {
        return new ResultDTO().errorOf(errorCode.getCode(),errorCode.getMessage());
    }

    public static ResultDTO okOf() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeException e) {
        return errorOf(e.getCode(),e.getMessage());
    }
}
