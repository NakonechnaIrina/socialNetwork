package edu.mum.ea.socialnetwork.services;

import edu.mum.ea.socialnetwork.domain.*;
import edu.mum.ea.socialnetwork.dto.*;
import edu.mum.ea.socialnetwork.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class Mapper {


    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final AddressRepository addressRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    public Mapper(UserRepository userRepository, ProfileRepository profileRepository, AddressRepository addressRepository, PostRepository postRepository, LikeRepository likeRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.addressRepository = addressRepository;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
    }

    public UserDto fromUserToDto(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        Profile profile = profileRepository.getByUserId(user.getId());
        if (Objects.nonNull(profile)) {
            userDto.setProfile(fromProfileToDto(profile));
        }
        return userDto;
    }


    public AddressDto fromAddressToDto(Address address) {
        AddressDto addressDto = new AddressDto();
        addressDto.setId(address.getId());
        addressDto.setCity(address.getCity());
        addressDto.setState(address.getState());
        addressDto.setCountry(address.getCountry());
        return addressDto;
    }

    public ProfileDto fromProfileToDto(Profile profile) {
        ProfileDto profileDto = new ProfileDto();
        BeanUtils.copyProperties(profile, profileDto);
        User user = userRepository.findById(profile.getUserId()).get();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);

        if (Objects.nonNull(profile.getAddressId())) {
            Optional<Address> addressOptional = addressRepository.findById(profile.getAddressId());
            addressOptional.ifPresent(address -> profileDto.setAddress(fromAddressToDto(address)));
        }
        profileDto.setUser(userDto);
        return profileDto;
    }




    public PostDto toPostDto(Post post) {
        PostDto result = new PostDto();
        BeanUtils.copyProperties(post, result);
        User user = userRepository.findById(post.getUserId()).get();
        result.setUser(fromUserToDto(user));

        List<Likes> likes = likeRepository.findAllByPostId(post.getId());

        if (Objects.nonNull(likes) && !likes.isEmpty()) {
            result.setLikes(likes.stream().map(this::toLikeDto).collect(Collectors.toSet()));
        }

        List<Comments> comments = commentRepository.findAllByPostId(post.getId());

        if (Objects.nonNull(comments) && !comments.isEmpty()) {
            result.setComments(comments.stream().map(this::toCommentDto).collect(Collectors.toList()));
        }
        return result;
    }


    public CommentDto toCommentDto(Comments comments) {
        CommentDto commentDto = new CommentDto();
        BeanUtils.copyProperties(comments, commentDto);
        String userId = comments.getUserId();
        String postId = comments.getPostId();
        User user = userRepository.findById(userId).get();

        commentDto.setUser(fromUserToDto(user));
        return commentDto;
    }

    public LikesDto toLikeDto(Likes likes) {
        LikesDto likesDto = new LikesDto();
        likesDto.setId(likes.getId());
        String postId = likes.getPostId();
        String userId = likes.getUserId();
        User user = userRepository.findById(userId).get();

        likesDto.setUser(fromUserToDto(user));
        return likesDto;
    }

    public NotificationDto toNotificationDto(Notification notification) {
        NotificationDto notificationDto = new NotificationDto();
        BeanUtils.copyProperties(notification, notificationDto);
        String userId = notification.getUserId();
        String postId = notification.getPostId();
        Optional<User> user = userRepository.findById(userId);
        if (Objects.nonNull(postId)) {
            Post post = postRepository.findById(postId).get();
            notificationDto.setPost(toPostDto(post));
        }

        notificationDto.setUser(fromUserToDto(user.get()));

        return notificationDto;
    }

}
