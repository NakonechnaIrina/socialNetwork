package edu.mum.ea.socialnetwork.services.impl;

import edu.mum.ea.socialnetwork.domain.Profile;
import edu.mum.ea.socialnetwork.domain.User;
import edu.mum.ea.socialnetwork.dto.ProfileDto;
import edu.mum.ea.socialnetwork.dto.UserDto;
import edu.mum.ea.socialnetwork.repository.ProfileRepository;
import edu.mum.ea.socialnetwork.repository.UserRepository;
import edu.mum.ea.socialnetwork.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Username:" + username + " not found!"));


        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),getAuthorities(user));
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
//        String[] userRoles = user.getRoles().stream().map((role) -> role.getName()).toArray(String[]::new);
        String[] roles = new String[1];
        roles[0] = user.getRole().toString();
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roles);
        return authorities;
    }

    public UserDto save(UserDto userDto) {
        User user = new User();
        ProfileDto profile1 = userDto.getProfile();
        String pass = userDto.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(pass));
        user.setEnabled(userDto.isEnabled());
        user.setUsername(userDto.getUsername());
        user.setRole(userDto.getRole());


        Profile profile = new Profile();
        profile.setGender(profile1.getGender());
        profile.setEmail(profile1.getEmail());
        profile.setFirstName(profile1.getFirstName());
        profile.setLastName(profile1.getLastName());
        profile.setDateOfBirth(profile1.getDateOfBirth());
        profile.setJoinDate(LocalDate.now());
        profile.setNoOfDisapprovedPosts(profile1.getNoOfDisapprovedPosts());
        profile.setProfilePhoto(profile1.getProfilePhoto());
        profile.setOccupation(profile1.getOccupation());

        Profile savedProfile = profileRepository.save(profile);
        User savedUser = userRepository.save(user);

        savedUser.setProfileId(savedProfile.getId());
        savedProfile.setUserId(savedUser.getId());
        userRepository.save(user);
        profileRepository.save(savedProfile);
//        profile.setAddressId(profile1.); // todo

        userDto.getProfile().setId(savedProfile.getId());
        userDto.setId(savedUser.getId());
        return userDto;
    }


    @Override
    public User findUserById(String id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User findUserByName(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Username:" + username + " not found!"));
        return user;
    }

    @Override
    public User rawSave(User user){
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }


}