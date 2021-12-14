package edu.mum.ea.socialnetwork.services;

import edu.mum.ea.socialnetwork.domain.Notification;
import edu.mum.ea.socialnetwork.domain.Post;
import edu.mum.ea.socialnetwork.dto.NotificationDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NotificationService {
    Notification save(Notification notification);

    List<NotificationDto>  findNotificationByUserId(String id);

}
