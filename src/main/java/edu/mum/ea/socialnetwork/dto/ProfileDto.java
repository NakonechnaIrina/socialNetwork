package edu.mum.ea.socialnetwork.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ProfileDto {
    private String id;
    private String gender;
    private String email;
    private String firstName;
    private String lastName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDate;
    private Integer noOfDisapprovedPosts = 0;
    private UserDto user;
    private AddressDto address;
    private String profilePhoto;
    private String occupation;
    private String userId;

    public String getUserId(){
        return user.getId();
    }
}
