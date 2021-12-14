package edu.mum.ea.socialnetwork.services;

import edu.mum.ea.socialnetwork.domain.Profile;
import edu.mum.ea.socialnetwork.dto.ProfileDto;

import java.util.List;

public interface ProfileService {
    Profile save(Profile profile);

    ProfileDto findById(String id);


    List<Profile> findAll();

    // this function used in Following Controller to search profiles according to their first name
    List<ProfileDto> searchProfiles(String id, String name);

    // this function return list of profiles that user doesn't follow exclude user's profile itself
    List<ProfileDto> unfollowedUsers(String id);

    List<ProfileDto> findTop5(String id);
}
