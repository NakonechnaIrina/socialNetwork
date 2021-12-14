package edu.mum.ea.socialnetwork.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.mum.ea.socialnetwork.domain.Post;
import edu.mum.ea.socialnetwork.domain.Profile;
import edu.mum.ea.socialnetwork.domain.Role;
import edu.mum.ea.socialnetwork.domain.User;
import edu.mum.ea.socialnetwork.dto.UserDto;
import edu.mum.ea.socialnetwork.repository.PostRepository;
import edu.mum.ea.socialnetwork.repository.ProfileRepository;
import edu.mum.ea.socialnetwork.repository.UserRepository;
import edu.mum.ea.socialnetwork.services.AdminService;
import edu.mum.ea.socialnetwork.services.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private UserRepository userRepository;
    private ProfileRepository profileRepository;
    private PostRepository postRepository;
    private Mapper mapper;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, ProfileRepository profileRepository, PostRepository postRepository, Mapper mapper) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> all = userRepository.findAll();
        List<UserDto> userDtos  = all.stream().map(user -> mapper.fromUserToDto(user)).collect(Collectors.toList());


        return userDtos;
    }

    @Override
    public List<UserDto> getDeactivatedUsers() {
        List<User> usersByEnabledIs = userRepository.findUsersByEnabledIs(false);
        List<UserDto> userDtos = usersByEnabledIs.stream().map(user -> mapper.fromUserToDto(user)).collect(Collectors.toList());

        return userDtos;
    }

    @Override
    public void toggleUser(String userId) {
        User user = userRepository.findById(userId).get();
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
//        userRepository.setUserEnabled(userId, !user.isEnabled());
    }

    @Override
    public void setUserRole(String userId, Role role) {
        User user = userRepository.findById(userId).get();
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public void setNumberOfDisapprovedPosts(String userId, Integer noOfDisapprovedPosts) {
        Profile profile = profileRepository.getByUserId(userId);
        profile.setNoOfDisapprovedPosts(noOfDisapprovedPosts);
        profileRepository.save(profile);
    }

    @Override
    public void setUserEnabled(String userId, boolean isEnabled) {
        User user = userRepository.findById(userId).get();
        user.setEnabled(isEnabled);
        userRepository.save(user);
//        userRepository.setUserEnabled(userId, isEnabled);
    }

    @Override
    public boolean userIsEnabled(String userId) {
        return userRepository.findById(userId).get().isEnabled();
    }

    @Override
    public void setPostUnhealthy(String postId, boolean isUnhealthy) {
        Post one = postRepository.findById(postId).get();
        one.setUnhealthy(isUnhealthy);
        postRepository.save(one);
//        postRepository.setPostUnhealthy(postId, isUnhealthy);
    }

    @Override
    public List<Post> getUnhealthyPosts() {
        return postRepository.findAllByUnhealthy(true);
    }

    @Override
    public void setPostEnabled(String postId, boolean isEnabled) {
        Post one = postRepository.findById(postId).get();
        one.setEnabled(isEnabled);
        postRepository.save(one);
//        postRepository.setPostEnabled(postId, isEnabled);
    }

}