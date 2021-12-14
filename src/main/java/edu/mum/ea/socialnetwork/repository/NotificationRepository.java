package edu.mum.ea.socialnetwork.repository;

import edu.mum.ea.socialnetwork.domain.Notification;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

 // @Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findAllByUserIdAndSeenIsFalse(String id);

    Notification findByUserIdAndPostId(String userId, String postId);

}
