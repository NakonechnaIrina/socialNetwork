package edu.mum.ea.socialnetwork.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LikesDto {

    private String id;

    private UserDto user;

    private PostDto post;

}
