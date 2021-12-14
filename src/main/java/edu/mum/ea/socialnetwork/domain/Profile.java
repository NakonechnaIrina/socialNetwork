package edu.mum.ea.socialnetwork.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "profile")
public class Profile implements Serializable {

    @Id
    private String id;

    @NotNull
    private String gender;  //values: Male & Female

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @Past
    private LocalDate dateOfBirth;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDate;

    private Integer noOfDisapprovedPosts = 0;

    private String userId;

    private String addressId;


    private String profilePhoto;

    private String occupation;


    public Profile(@NotNull String gender, @NotBlank @Email String email, @NotBlank String firstName,
                   @NotBlank String lastName, @NotNull @Past LocalDate dateOfBirth, LocalDate joinDate) {
        this.gender = gender;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.joinDate = joinDate;
    }
}
