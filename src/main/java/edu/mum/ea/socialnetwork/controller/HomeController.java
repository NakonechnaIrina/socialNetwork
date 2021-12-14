package edu.mum.ea.socialnetwork.controller;

import edu.mum.ea.socialnetwork.domain.Notification;
import edu.mum.ea.socialnetwork.domain.Post;
import edu.mum.ea.socialnetwork.domain.Profile;
import edu.mum.ea.socialnetwork.domain.User;
import edu.mum.ea.socialnetwork.dto.NotificationDto;
import edu.mum.ea.socialnetwork.dto.PostDto;
import edu.mum.ea.socialnetwork.dto.ProfileDto;
import edu.mum.ea.socialnetwork.repository.ProfileRepository;
import edu.mum.ea.socialnetwork.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    ProfileService profileService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    AdvertisementService advertisementService;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    Mapper mapper;

    @ModelAttribute("currentUser")
    public Profile currentUser(Principal principal){
        User user = userService.findUserByName(principal.getName());
        Profile profile = profileRepository.getByUserId(user.getId());
        return profile;
    }

    @GetMapping("/")
    public String post(@ModelAttribute("addPost") PostDto post, Model model, Principal principal) {
        Page<PostDto> posts = postService.allFollowingsPostsPaged(currentUser(principal).getId(),0);
        if(posts.isEmpty()){
            model.addAttribute("nextPage", -1);
        }else if(posts.getContent().size()<5){
            model.addAttribute("nextPage",0);
        }else{
            model.addAttribute("nextPage", 1);
        }

        model.addAttribute("allPost", posts);
        Profile profile = currentUser(principal);
        List<ProfileDto> suggestions = profileService.findTop5(profile.getUserId());
        model.addAttribute("suggestions", suggestions);

        User user = userService.findUserByName(principal.getName());
        List<NotificationDto> notifications = notificationService.findNotificationByUserId(user.getId());
        System.out.println("All Notifications"+ notifications.size());
        model.addAttribute("notifications", notifications);


        model.addAttribute("ads", advertisementService.getAdList());

        return "index";
    }

    @GetMapping("/{pageNo}")
    public @ResponseBody Page<Post> getPostsPaged(@PathVariable("pageNo") Integer pageNo){
        System.out.println("-------------getPostPaged Called=---------------------");
        Page<Post> posts = postService.allPostsPaged(pageNo);
        return posts;
    }

    @GetMapping("/index")
    public String showIndex(){
        return "index";
    }

    @GetMapping("/denied")
    public String denied(){
        return "denied";
    }
}
