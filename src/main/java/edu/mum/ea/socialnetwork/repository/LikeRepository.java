package edu.mum.ea.socialnetwork.repository;

import edu.mum.ea.socialnetwork.domain.Likes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

 // @Repository
public interface LikeRepository extends MongoRepository<Likes, String> {
//    List<Post> findByEnabledOrderByCreationDateDesc(boolean enabled);


    List<Likes> findAllByPostId(String postId);
}
