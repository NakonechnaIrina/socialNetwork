package edu.mum.ea.socialnetwork.controller;

import edu.mum.ea.socialnetwork.domain.Profile;
import edu.mum.ea.socialnetwork.domain.User;
import edu.mum.ea.socialnetwork.domain.UserFollowing;
import edu.mum.ea.socialnetwork.dto.ProfileDto;
import edu.mum.ea.socialnetwork.repository.ProfileRepository;
import edu.mum.ea.socialnetwork.repository.UserFollowingRepository;
import edu.mum.ea.socialnetwork.services.ProfileService;
import edu.mum.ea.socialnetwork.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
public class FollowingController {

    @Autowired
    UserService userService;

    @Autowired
    ProfileService profileService;

    @Autowired
    UserFollowingRepository userFollowingRepository;

    @Autowired
    ProfileRepository profileRepository;

    @ModelAttribute("currentUser")
    public Profile profilePic(Principal principal) {
        User user = userService.findUserByName(principal.getName());
        Profile byUserId = profileRepository.getByUserId(user.getId());
        return byUserId;
    }


    @PostMapping(value = "/follow")
    public User follow(@RequestBody String id, Principal principal) {
        id = id.substring(1, id.length() - 1);
//        User user = userService.findUserById(id); // whom
        ProfileDto byId = profileService.findById(id);
        User user = userService.findUserById(byId.getUser().getId()); // whom
        User currentUser = userService.findUserByName(principal.getName());
        List<UserFollowing> following = userFollowingRepository.findAllByUserId(currentUser.getId());
        for (UserFollowing user1 : following) {
            if (Objects.equals(user1.getFollowingId(), user.getId())) {
                return null;
            }
        }
        UserFollowing userFollowing = new UserFollowing();
        userFollowing.setUserId(currentUser.getId()); // who follow
        userFollowing.setFollowingId(user.getId()); // whom follow
        userFollowingRepository.save(userFollowing);
        return userService.rawSave(currentUser);
    }


    @PostMapping(value = "/unfollow")
    public User unfollow(@RequestBody String id, Principal principal) {
        id = id.substring(1, id.length() - 1);
        ProfileDto byId = profileService.findById(id);
        User user = userService.findUserById(byId.getUser().getId()); // whom
        User currentUser = userService.findUserByName(principal.getName()); //who

        List<UserFollowing> following = userFollowingRepository.findAllByUserId(currentUser.getId());
        for (UserFollowing user1 : following) {
            if (Objects.equals(user1.getFollowingId(), user.getId())) {
                userFollowingRepository.delete(user1);
//                following.remove(user1);
                return userService.rawSave(currentUser);
            }
        }
        return null;
    }


    // this function display all the profiles that user doesn't follow and we call it when we go to the list of
    // profile page
    @GetMapping("/profileList")
    public ModelAndView showProfile(Principal principal) {
        User currentUser = userService.findUserByName(principal.getName());
        List<ProfileDto> profiles = profileService.unfollowedUsers(currentUser.getId());
        System.out.println(currentUser.getUsername());
        System.out.println("list" + profiles);
        ModelAndView mav = new ModelAndView();
        mav.addObject("profiles", profiles);
        mav.setViewName("profileList");
        return mav;
    }


    @PostMapping(value = "/search")
    public List<ProfileDto> search(@RequestBody String username, Principal principal) {
        User userId = userService.findUserByName(principal.getName());
        List<ProfileDto> profiles = profileService.searchProfiles(userId.getId(), username); // this function comes form profile service
        return profiles;
    }
}
