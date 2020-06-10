package com.stndorm.community.service;

import com.stndorm.community.dto.NotificationDTO;
import com.stndorm.community.dto.PaginationDTO;
import com.stndorm.community.emus.NotificationStatusEnum;
import com.stndorm.community.emus.NotificationTypeEnum;
import com.stndorm.community.exception.CustomizeErrorCode;
import com.stndorm.community.exception.CustomizeException;
import com.stndorm.community.mapper.NotificationMapper;
import com.stndorm.community.model.Notification;
import com.stndorm.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;


    public PaginationDTO selectNotificationDTOsByUser(Integer userId, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        Integer totalPage;
        //获取文章总数
        Integer totalCount = notificationMapper.countByReceiver(userId);
        //判断总共有几页
        if(totalCount % size == 0){
            totalPage = totalCount / size;
        }else{
            totalPage = totalCount / size + 1;
        }

        //判断请求参数page是否合规，不合规就使它变为边界值
        if(page < 1) page = 1;
        if(page > totalPage)
            page = totalPage;
        //对paginationDTO进行赋值
        paginationDTO.setPagination(totalPage, page);

        //偏移量计算
        Integer offset = size * (page - 1);
        if(offset < 0) offset = 0;


        List<Notification> notifications = notificationMapper.selectNotificationsByReceiver(userId, offset, size);
        if(notifications.size() == 0 ){
            return paginationDTO;
        }
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for(Notification notification : notifications){
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOList.add(notificationDTO);
        }



        paginationDTO.setData(notificationDTOList);
        return paginationDTO;
    }

    public Integer unreadCount(Integer id) {
        Integer count = notificationMapper.countByReceiverAndStatus(id,NotificationStatusEnum.UNREAD.getStatus());
        return count;
    }

    public NotificationDTO read(Integer id, User user) {
        //根据通知的id得到该通知的信息。
        Notification notification = notificationMapper.selectById(id);
        //判断该通知的接收者是否是当前用户
        if(notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUNT);
        }
        if(notification.getReceiver()!= user.getId()){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAILED);
        }

        //将通知变为已读
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateStatus(notification);

        //若没有问题
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
