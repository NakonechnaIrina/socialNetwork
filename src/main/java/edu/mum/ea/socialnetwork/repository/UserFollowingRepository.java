package edu.mum.ea.socialnetwork.repository;

import edu.mum.ea.socialnetwork.domain.UserFollowing;
 
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

 // @Repository
public interface UserFollowingRepository extends MongoRepository<UserFollowing, String> {

    List<UserFollowing> findAllByUserId(String userId);

}
