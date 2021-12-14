package edu.mum.ea.socialnetwork.controller;

import edu.mum.ea.socialnetwork.domain.*;
import edu.mum.ea.socialnetwork.dto.*;
import edu.mum.ea.socialnetwork.repository.*;
import edu.mum.ea.socialnetwork.services.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class ProfileController {

    @Autowired
    ProfileService profileService;
    @Autowired
    UserService userService;
    @Autowired
    ProfileImageUploadService profileImageUploadService;
    @Autowired
    PostService postService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserFollowingRepository userFollowingRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    Mapper mapper;

    @Autowired
    NotificationRepository notificationRepository;

    @ModelAttribute("currentUser")
    public ProfileDto currentUser(Principal principal) {
        User user = userService.findUserByName(principal.getName());
        Profile profile = profileRepository.getByUserId(user.getId());
        ProfileDto profileDto = mapper.fromProfileToDto(profile);
        return profileDto;
    }

    @GetMapping("/profile/{id}")
    public String showProfile(@PathVariable("id") String id, Principal principal, Model model,
                              @RequestParam(name = "scrollTo", required = false) String postId) {
        User userById = userService.findUserById(id);
        ProfileDto profile = profileService.findById(userById.getProfileId());

        if (profile == null) {
            return "redirect:/";
        }
        model.addAttribute("profile", profile);

        //check if this user is followed or not  0->notFollwed  1=>followed  -1->his/her Profile
        int follow = 0;
        if (Objects.equals(id, currentUser(principal).getId())) follow = -1;

//        List<User> following = currentUser(principal).getUser().getFollowing();
        String userId = currentUser(principal).getUser().getId();

        if (Objects.nonNull(postId)) {
            Notification byUserIdAndPostId = notificationRepository.findByUserIdAndPostId(userId, postId);
            byUserIdAndPostId.setSeen(true);
            notificationRepository.save(byUserIdAndPostId);
        }

        List<UserFollowing> allByUserId = userFollowingRepository.findAllByUserId(userId);
        for (UserFollowing user : allByUserId) {
            if (Objects.equals(id, user.getFollowingId())) follow = 1;
        }
        if (userId.equals(id)) {
            follow = 1;
        }
        model.addAttribute("follow", follow);
        List<PostDto> posts = postService.findAllPostForSpecificUser(id);
        model.addAttribute("posts", posts);

        User user = userService.findUserByName(principal.getName());
        List<NotificationDto> notifications = notificationService.findNotificationByUserId(user.getId());
        System.out.println("All Notifications" + notifications.size());
        model.addAttribute("notifications", notifications);
        return "profile";
    }

    @GetMapping("/profile/editProfile")
    public String showEditProfile(@ModelAttribute("profile") ProfileDto profile, Principal principal) {


        return "editProfile";
    }

    @PostMapping("/profile/profilePhotoUpload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, Principal principal) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/profile/myProfile";
        }

        String filename = profileImageUploadService.uploadImage(file);
        User user = userService.findUserByName(principal.getName());
        Profile profile = profileRepository.getByUserId(user.getId());
        profile.setProfilePhoto(filename);
        profileRepository.save(profile);
        userService.update(user);

        return "redirect:/profile/myProfile";
    }


    @PostMapping("/profile")
    public String updateProfile( @ModelAttribute ProfileDto profile, BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            return "editProfile";
        }
        User user = userService.findUserByName(principal.getName());
        AddressDto address = profile.getAddress();
        Profile toUpdate = profileRepository.getByUserId(user.getId());

        Address address1 = new Address();
        BeanUtils.copyProperties(profile,toUpdate);
        BeanUtils.copyProperties(address,address1);

        if (StringUtils.isEmpty(address1.getId()))        {
            address1.setId(null);
        }
        Address save = addressRepository.save(address1);
        toUpdate.setAddressId(save.getId());
        profileRepository.save(toUpdate);

        return "redirect:/profile/myProfile";
    }


    @GetMapping("/profile/myProfile")
    public String showMyProfile(Model model, Principal principal) {
        User me = userService.findUserByName(principal.getName());
//        model.addAttribute("profile", me.getProfile());
        return "redirect:/profile/" + me.getId();
    }

    @PostMapping("/profile/changePassword")
    public String changePassword(@ModelAttribute Profile profile, @RequestParam("new-password") String password, @RequestParam("repeat-password") String repeat, Model model, Principal principal) {
        if (!password.equals(repeat)) {
            model.addAttribute("passwordError", "Password does not match!");
            return "editProfile";
        }
        if (password.equals("")) {
            model.addAttribute("passwordError", "Password can not be empty!");
            return "editProfile";
        }

        User currentUser = userService.findUserByName(principal.getName());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        currentUser.setPassword(encoder.encode(password));

        userRepository.save(currentUser);

        return "redirect:/profile/myProfile";
    }


    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping("/test")
    public String test3() {

        return "redirect:/index";
    }


    // following GetMapping functions are use for changing language because after changing the language we do a get
    // request and if we don't have this get mapping method it will throw 400 error
    @GetMapping("/profile")
    public String redirectToProfile(@Valid @ModelAttribute Profile profile, BindingResult bindingResult, Principal principal) {
        return "editProfile";
    }

    @GetMapping("/profile/profilePhotoUpload")
    public String redirectToPhotoUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, Principal principal) {
        return "editProfile";
    }

    @GetMapping("/profile/changePassword")
    public String redirectToChangePass(@ModelAttribute Profile profile, @RequestParam("new-password") String password, @RequestParam("repeat-password") String repeat, Model model, Principal principal) {
        return "editProfile";
    }
}
