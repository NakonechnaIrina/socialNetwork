package edu.mum.ea.socialnetwork.services;

import edu.mum.ea.socialnetwork.domain.Post;
import edu.mum.ea.socialnetwork.dto.PostDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    PostDto save(PostDto post);

    List<PostDto> findPost();

    Post findPostById(String id);

    List<PostDto> findAllPostForSpecificUser(String id);

    Page<Post> allPostsPaged(int pageNo);

    List<PostDto> searchPosts(String text);

    Page<PostDto> allFollowingsPostsPaged(String userId,int pageNo);

}
