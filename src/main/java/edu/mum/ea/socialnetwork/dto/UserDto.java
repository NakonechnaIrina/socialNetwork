package edu.mum.ea.socialnetwork.dto;

import edu.mum.ea.socialnetwork.domain.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private String id;
    private String username;
    private String password;
    private boolean enabled = true;
    private Role role;
    private ProfileDto profile;
}
