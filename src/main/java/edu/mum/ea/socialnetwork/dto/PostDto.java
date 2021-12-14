package edu.mum.ea.socialnetwork.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {
    private String id;
    private String text;
    private String photo;
    private String video;
    private Date creationDate = new Date();
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deletionDate;
    private Integer likeCount = 0;
    private Integer commentCount = 0;
    private boolean unhealthy = false;
    private boolean notifyAllFollowers = true;
    private boolean enabled = true;

    private UserDto user;

    private Set<LikesDto> likes = new HashSet<>();
    private List<CommentDto> comments = new ArrayList<>();

}
