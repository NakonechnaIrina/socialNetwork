package edu.mum.ea.socialnetwork.controller;

import edu.mum.ea.socialnetwork.domain.Likes;
import edu.mum.ea.socialnetwork.domain.Post;
import edu.mum.ea.socialnetwork.domain.User;
import edu.mum.ea.socialnetwork.repository.PostRepository;
import edu.mum.ea.socialnetwork.services.LikeService;
import edu.mum.ea.socialnetwork.services.PostService;
import edu.mum.ea.socialnetwork.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Set;

@RestController
public class LikeController {

    @Autowired
    PostService postService;

    @Autowired
    LikeService likeService;

    @Autowired
    UserService userService;

    @Autowired
    PostRepository postRepository;

    @PostMapping(value = "/like")
    public Likes addLike(@RequestBody String id, Principal principal) {
        System.out.println("I am inside addLike method" + id);
        id = id.substring(1, id.length() - 1);
        System.out.println(principal.getName());
        User currentUser = userService.findUserByName(principal.getName());

        Post post = postService.findPostById(id);
        Likes likes = new Likes();

        likes.setUserId(currentUser.getId());
        likes.addPost(post);
//        post.addLike(likes);
        postRepository.save(post);
        return likeService.save(likes);

    }


    @DeleteMapping(value = "/like")
    public Likes removeLike(@RequestParam String likeId, @RequestParam String postId, Principal principal) {
        System.out.println("I am inside addLike method likeId: " + likeId);
        System.out.println("I am inside addLike method postId: " + postId);
        Likes like = likeService.find(likeId);

        Post post = postService.findPostById(postId);
        post.removeLike(like);

        likeService.remove(like);
        postRepository.save(post);

        return like;

    }


}
