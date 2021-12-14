package edu.mum.ea.socialnetwork.services.impl;

import edu.mum.ea.socialnetwork.domain.Notification;
import edu.mum.ea.socialnetwork.domain.Post;
import edu.mum.ea.socialnetwork.domain.User;
import edu.mum.ea.socialnetwork.domain.UserFollowing;
import edu.mum.ea.socialnetwork.dto.PostDto;
import edu.mum.ea.socialnetwork.dto.UserDto;
import edu.mum.ea.socialnetwork.repository.*;
import edu.mum.ea.socialnetwork.services.Mapper;
import edu.mum.ea.socialnetwork.services.NotificationService;
import edu.mum.ea.socialnetwork.services.PostService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserFollowingRepository userFollowingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private Mapper mapper;
//    public PostController(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }

    public PostDto save(PostDto post) {

        // rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, order);

        Post toSave = new Post();

        BeanUtils.copyProperties(post, toSave);
        toSave.setUserId(post.getUser().getId());
        Post saved = postRepository.save(toSave);
        post.setId(saved.getId());
        if(post.isNotifyAllFollowers()){
            UserDto user1 = post.getUser();
            User user  = userRepository.findById(user1.getId()).get();
            List<UserFollowing> allByUserId = userFollowingRepository.findAllByUserId(user.getId());
            List<String> followingIds = allByUserId.stream().map(UserFollowing::getFollowingId).collect(Collectors.toList());
//            List<User> users = user.getFollowing();
//            System.out.println("Following count: " + users.size());
            userRepository.findAllById(followingIds).forEach((u) -> {
                System.out.println("User NAme is :" + u.getUsername());
                Notification notification = new Notification();
                notification.setPostId(saved.getId());
                notification.setUserId(u.getId());
                notificationService.save(notification);
                //   rabbitTemplate.convertAndSend(RabbitMQDirectConfig.EXCHANGE, RabbitMQDirectConfig.ROUTING_KEY, notification);
            });



        }
        return post;
    }

    public List<PostDto> findPost() {
        return postRepository.findByEnabledOrderByCreationDateDesc(true)
                .stream().map(post -> mapper.toPostDto(post)).collect(Collectors.toList());
    }

    public Post findPostById(String id) {
        Post byId = postRepository.findById(id).get();
        return byId;
    }

    @Override
    public List<PostDto> findAllPostForSpecificUser(String id) {
       return postRepository.findAllByUserId(id).stream().map(mapper::toPostDto).collect(Collectors.toList());
    }

    @Override
    public Page<Post> allPostsPaged(int pageNo){
        return postRepository.findAll(PageRequest.of(pageNo, 5, Sort.by("creationDate").descending()));
    }

    @Override
    public List<PostDto> searchPosts(String text){
        return postRepository.findPostsByTextContaining(text).stream().map(mapper::toPostDto).collect(Collectors.toList());
    }

    @Override
    public Page<PostDto> allFollowingsPostsPaged(String userId, int pageNo){
        List<UserFollowing> userFollowings = userFollowingRepository.findAllByUserId(userId);
        List<String> userIds = userFollowings.stream().map(UserFollowing::getFollowingId).collect(Collectors.toList());
        userIds.add(userId);
//        return postRepository.findAll(PageRequest.of(pageNo, 5, Sort.by("creationDate").descending()));
        Page<Post> creationDate = postRepository.findByUserIdNotIn(userIds, PageRequest.of(pageNo, 5, Sort.by("creationDate").descending()));
        return creationDate.map(post -> mapper.toPostDto(post));
    }

}
