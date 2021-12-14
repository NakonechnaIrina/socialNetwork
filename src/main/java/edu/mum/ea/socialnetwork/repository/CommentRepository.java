package edu.mum.ea.socialnetwork.repository;

import edu.mum.ea.socialnetwork.domain.Comments;
 
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

 // @Repository


public interface CommentRepository extends MongoRepository<Comments, String> {
//    List<Post> findByEnabledOrderByCreationDateDesc(boolean enabled);

    List<Comments> findAllByPostId(String postId);
}
