package edu.mum.ea.socialnetwork.services.impl;

import edu.mum.ea.socialnetwork.domain.Profile;
import edu.mum.ea.socialnetwork.domain.User;
import edu.mum.ea.socialnetwork.domain.UserFollowing;
import edu.mum.ea.socialnetwork.dto.ProfileDto;
import edu.mum.ea.socialnetwork.dto.UserDto;
import edu.mum.ea.socialnetwork.repository.ProfileRepository;
import edu.mum.ea.socialnetwork.repository.UserFollowingRepository;
import edu.mum.ea.socialnetwork.repository.UserRepository;
import edu.mum.ea.socialnetwork.services.Mapper;
import edu.mum.ea.socialnetwork.services.ProfileService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    UserFollowingRepository userFollowingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    Mapper mapper;

    @Override
    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }

    @Override
    public ProfileDto findById(String id) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (!optionalProfile.isPresent()) {
            return null;
        }

        Profile profile = optionalProfile.get();

//        ProfileDto profileDto = new ProfileDto();
//        BeanUtils.copyProperties(profile, profileDto);
//
//        User user = userRepository.getOne(profile.getUserId());
//        UserDto userDto = new UserDto();
//        BeanUtils.copyProperties(user, userDto);
//        profileDto.setUser(userDto);

        ProfileDto profileDto = mapper.fromProfileToDto(profile);
        return profileDto;
    }

    @Override
    public List<Profile> findAll() {
        return profileRepository.findAll();
    }

    // this function used in Following Controller to search all the users by first name
    @Override
    public List<ProfileDto> searchProfiles(String id, String name) {
        List<UserFollowing> allByUserId = userFollowingRepository.findAllByUserId(id);
        List<String> followedUser = allByUserId.stream().map(UserFollowing::getFollowingId).collect(Collectors.toList());
        List<Profile> allByIdIn = profileRepository.findAllByIdIn(followedUser);
        return allByIdIn.stream().filter(profile -> profile.getEmail().contains(name)
                || profile.getFirstName().contains(name)
                || profile.getLastName().contains(name))
                .map(mapper::fromProfileToDto).collect(Collectors.toList());
    }

    @Override
    public List<ProfileDto> unfollowedUsers(String id) {
        List<String> followed = userFollowingRepository.findAllByUserId(id)
                .stream().map(UserFollowing::getFollowingId).collect(Collectors.toList());
        followed.add(id);
        List<Profile> allByUserIdIn = profileRepository.findAll();
        return allByUserIdIn.stream().filter(profile -> !followed.contains(profile.getUserId())).map(mapper::fromProfileToDto).collect(Collectors.toList());
    }

    @Override
    public List<ProfileDto> findTop5(String id) {
        List<String> followed = userFollowingRepository.findAllByUserId(id)
                .stream().map(UserFollowing::getFollowingId).collect(Collectors.toList());
        followed.add(id);
        List<Profile> allByUserIdIn = profileRepository.findAll();
        return allByUserIdIn.stream().filter(profile -> !followed.contains(profile.getUserId())).map(mapper::fromProfileToDto).collect(Collectors.toList());
    }


}
