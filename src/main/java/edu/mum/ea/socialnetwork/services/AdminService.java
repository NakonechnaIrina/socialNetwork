package edu.mum.ea.socialnetwork.services;

import edu.mum.ea.socialnetwork.domain.Post;
import edu.mum.ea.socialnetwork.domain.Role;
import edu.mum.ea.socialnetwork.domain.User;
import edu.mum.ea.socialnetwork.dto.UserDto;

import java.util.List;

public interface AdminService {

    List<UserDto> getUsers();

    List<UserDto> getDeactivatedUsers();

    void toggleUser(String userId);

    void setUserRole(String userId, Role role);

    void setNumberOfDisapprovedPosts(String userId, Integer noOfDisapprovedPosts);

    void setUserEnabled(String userId, boolean isEnabled);

    boolean userIsEnabled(String userId);

    void setPostUnhealthy(String postId, boolean isUnhealthy);

    List<Post> getUnhealthyPosts();

    void setPostEnabled(String postId, boolean isEnabled);
}
